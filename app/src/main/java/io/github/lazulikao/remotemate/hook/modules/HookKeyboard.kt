package io.github.lazulikao.remotemate.hook.modules

import android.view.KeyEvent
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam

class HookKeyboard : IHookModule {
    override val enableLog: Boolean
        get() = true

    companion object {
        // 默认支持的目标应用列表
        val DEFAULT_TARGET_PACKAGES = setOf(
            "com.microsoft.rdc.androidx",      // Microsoft Remote Desktop
            "com.microsoft.rdc.android",       // Microsoft Remote Desktop (旧版)
            "com.realvnc.viewer.android",      // RealVNC Viewer
            "com.teamviewer.teamviewer.market.mobile", // TeamViewer
        )

        @Volatile
        private var currentForegroundPackage: String? = null
    }

    override fun onLoad(env: PackageParam) {
        env.apply {
            val hookKeyboard = prefs("hook_keyboard")
            fun isHookKeyboardEnabled() = hookKeyboard.getBoolean("enable_hook_keyboard", true)
            fun isExternalOnlyEnabled() = hookKeyboard.getBoolean("external_only", true)
            fun isTargetAppOnlyEnabled() = hookKeyboard.getBoolean("target_app_only", false)
            fun getEnabledTargetPackages(): Set<String> {
                val saved = hookKeyboard.getString("target_packages", "")
                return if (saved.isBlank()) DEFAULT_TARGET_PACKAGES
                else saved.split(",").filter { it.isNotBlank() }.toSet()
            }

            loadSystem {
                // Hook ActivityRecord 的 scheduleTopResumedActivityChanged 来监听前台应用切换
                // https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/wm/ActivityRecord.java
                "com.android.server.wm.ActivityRecord".toClass().resolve().apply {
                    (firstMethodOrNull { name = "scheduleTopResumedActivityChanged" }
                        ?: firstMethodOrNull { name = "onTopResumedActivityChanged" })?.hook {
                        before {
                            val onTop = args[0] as Boolean
                            if (onTop) {
                                // 获取 packageName 字段 (继承自 WindowToken)
                                val packageName = instance.javaClass.getField("packageName")
                                    .get(instance) as? String
                                currentForegroundPackage = packageName
                                if (enableLog) {
                                    logDebug("HookKeyboard: Top resumed activity changed to: $packageName")
                                }
                            }
                        }
                    } ?: run {
                        if (enableLog) {
                            logDebug("HookKeyboard: Failed to hook scheduleTopResumedActivityChanged or onTopResumedActivityChanged")
                        }
                    }
                }
                // https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/policy/PhoneWindowManager.java#3430
                "com.android.server.policy.PhoneWindowManager".toClass().resolve()
                    // public long interceptKeyBeforeDispatching(IBinder focusedToken, KeyEvent event,int policyFlags) {
                    .firstMethod { name = "interceptKeyBeforeDispatching" }.hook {
                        before {
                            if (!isHookKeyboardEnabled()) return@before
                            val e = args[1] as KeyEvent

                            // 如果启用了仅拦截外接键盘，则非外接键盘直接跳过
                            if (isExternalOnlyEnabled() && !e.device.isExternal) return@before

                            // 如果启用了仅在目标应用前台时拦截，则检查前台应用
                            if (isTargetAppOnlyEnabled() && currentForegroundPackage !in getEnabledTargetPackages()) return@before

                            if (enableLog) {
                                logDebug("HookKeyboard: interceptKeyBeforeDispatching called with event: $e , device: ${e.device}. type: ${e.device.keyboardType}, external: ${e.device.isExternal}, foreground: $currentForegroundPackage")
                            }

                            result = 0L // Return 0 to indicate that the event has been handled
                        }
                    }
            }
        }
    }
}