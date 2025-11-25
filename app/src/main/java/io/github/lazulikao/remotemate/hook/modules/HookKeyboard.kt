package io.github.lazulikao.remotemate.hook.modules

import android.app.ActivityManager
import android.content.Context
import android.view.KeyEvent
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam

class HookKeyboard : IHookModule {
    override val enableLog: Boolean
        get() = true

    companion object {
        private const val TARGET_PACKAGE = "com.microsoft.rdc.androidx"
        private const val CACHE_EXPIRE_MS = 500L // 缓存过期时间（毫秒）
        
        @Volatile
        private var cachedForegroundPackage: String? = null
        @Volatile
        private var lastCheckTime: Long = 0L
    }

    override fun onLoad(env: PackageParam) {
        env.apply {
            val hookKeyboard = prefs("hook_keyboard")
            fun isHookKeyboardEnabled() = hookKeyboard.getBoolean("enable_hook_keyboard", false)
            
            var contextRef: Context? = null
            var amRef: ActivityManager? = null
            
            fun getForegroundPackage(instance: Any): String? {
                val now = System.currentTimeMillis()
                // 如果缓存未过期，直接返回缓存的包名
                if (now - lastCheckTime < CACHE_EXPIRE_MS) {
                    return cachedForegroundPackage
                }
                
                // 懒加载 Context 和 ActivityManager
                if (contextRef == null) {
                    contextRef = instance.javaClass.getDeclaredField("mContext").apply {
                        isAccessible = true
                    }.get(instance) as Context
                    amRef = contextRef!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                }
                
                // 更新缓存
                cachedForegroundPackage = amRef?.runningAppProcesses?.firstOrNull {
                    it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                }?.processName
                lastCheckTime = now
                
                return cachedForegroundPackage
            }
            
            loadSystem {
                // https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/policy/PhoneWindowManager.java#3430
                "com.android.server.policy.PhoneWindowManager".toClass().resolve()
                    // public long interceptKeyBeforeDispatching(IBinder focusedToken, KeyEvent event,int policyFlags) {
                    .firstMethod { name = "interceptKeyBeforeDispatching" }.hook {
                        before {
                            if (!isHookKeyboardEnabled()) return@before
                            val e = args[1] as KeyEvent
                            
                            // 非外接键盘直接跳过，避免不必要的检查
                            if (!e.device.isExternal) return@before
                            
                            val foregroundPackage = getForegroundPackage(instance)
                            
                            if (enableLog) {
                                logDebug("HookKeyboard: interceptKeyBeforeDispatching called with event: $e , device: ${e.device}. type: ${e.device.keyboardType}, external: ${e.device.isExternal}, foreground: $foregroundPackage")
                            }
                            
                            // 仅在前台应用为目标应用时拦截外接键盘事件
                            if (foregroundPackage == TARGET_PACKAGE)
                                result = 0L // Return 0 to indicate that the event has been handled
                        }
                    }
            }
        }
    }
}