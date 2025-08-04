package io.github.lazulikao.remotemate.hook.modules

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam


class HookWindowsApp : IHookModule {
    override val enableLog: Boolean
        get() = true

    override fun onLoad(env: PackageParam) {

        env.loadApp("com.microsoft.rdc.androidx") {
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
            "android.view.inputmethod.InputMethodManager".toClassOrNull()?.resolve()?.apply {
                firstMethodOrNull {
                    name = "showSoftInput"
                }
                    ?.hook {
                        replaceUnit {
                            if (enableLog) {
                                logDebug("HookWindowsApp: showSoftInput called")
                            }
                        }
                    }
            }
            // todo: 添加到设置，仅在开启后生效
            "com.microsoft.a3rdc.AppSettings".toClassOrNull()?.resolve()?.apply {
                firstMethodOrNull {
                    name = "getScancodeEnabled"
                }
                    ?.hook {
                        before {
                            if (enableLog) {
                                logDebug("HookWindowsApp: getScancodeEnabled called")
                            }
                            // Return true to enable scancode
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
                            val defaultValue = args[1] as Boolean
                            val resultBool = result as Boolean
                            if (enableLog) {
                                logDebug("HookWindowsApp: getBoolean called with key: $key, defaultValue: $defaultValue, result: $resultBool")
                            }
                            // If the key is "scancode_enabled", return true to enable scancode
                            if (key == "scancode_enabled_v2") {
                                result = true
                                if (enableLog) {
                                    logDebug("HookWindowsApp: Overriding getBoolean for key 'scancode_enabled' to return true")
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}