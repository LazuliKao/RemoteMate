package io.github.lazulikao.remotemate.hook.modules

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam


class HookWindowsApp : IHookModule {
    override val enableLog: Boolean
        get() = true

    companion object {
        const val PREFS_NAME = "hook_windows_app"
    }

    override fun onLoad(env: PackageParam) {

        env.loadApp("com.microsoft.rdc.androidx") {
            val prefs = prefs(PREFS_NAME)
            val hideSoftKeyboard = prefs.getBoolean("hide_soft_keyboard", true)
            val forceScancode = prefs.getBoolean("force_scancode", true)
//            "com.microsoft.a3rdc.rdp.NativeRdpConnection".toClassOrNull()?.resolve()?.apply {
//                firstMethodOrNull {
//                    name = "sendScanCodeKey"
//                }?.hook {
//                    after {
//                        if (enableLog) {
//                            logDebug("HookWindowsApp: sendScanCodeKey called with args: ${args.joinToString()}")
//                        }
//                    }
//                }
//                firstMethodOrNull {
//                    name = "sendSmartKey"
//                }?.hook {
//                    after {
//                        if (enableLog) {
//                            logDebug("HookWindowsApp: sendSmartKey called with args: ${args.joinToString()}")
//                        }
//                    }
//                }
//                firstMethodOrNull {
//                    name = "sendVirtualKey"
//                }?.hook {
//                    after {
//                        if (enableLog) {
//                            logDebug("HookWindowsApp: sendVirtualKey called with args: ${args.joinToString()}")
//                        }
//                    }
//                }
//            }

            // 隐藏软键盘
            if (hideSoftKeyboard) {
                "android.view.inputmethod.InputMethodManager".toClassOrNull()?.resolve()?.apply {
                    firstMethodOrNull {
                        name = "showSoftInput"
                    }
                        ?.hook {
                            replaceUnit {
                                if (enableLog) {
                                    logDebug("HookWindowsApp: showSoftInput blocked")
                                }
                            }
                        }
                }
            }

            // 强制启用 Scancode 模式
            if (forceScancode) {
                "com.microsoft.a3rdc.AppSettings".toClassOrNull()?.resolve()?.apply {
                    firstMethodOrNull {
                        name = "getScancodeEnabled"
                    }
                        ?.hook {
                            before {
                                if (enableLog) {
                                    logDebug("HookWindowsApp: getScancodeEnabled -> true")
                                }
                                result = true
                            }
                        }
                } ?: run {
                    "android.content.SharedPreferences".toClassOrNull()?.resolve()?.apply {
                        firstMethodOrNull {
                            name = "getBoolean"
                            parameters(String::class.java, Boolean::class.java)
                        }?.hook {
                            after {
                                val key = args[0] as String
                                if (key == "scancode_enabled_v2") {
                                    result = true
                                    if (enableLog) {
                                        logDebug("HookWindowsApp: scancode_enabled_v2 -> true")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}