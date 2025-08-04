package io.github.lazulikao.remotemate.hook.modules

import com.highcapable.kavaref.KavaRef
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.kavaref.condition.base.MemberCondition
import com.highcapable.yukihookapi.hook.param.PackageParam
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import link.lk233.oneuiceiler.hook.IHookModule
import kotlin.apply
import kotlin.collections.joinToString


class HookWindowsApp : IHookModule {
    override val enableLog: Boolean
        get() = true

    override fun onLoad(env: PackageParam) {

        env.loadApp("com.microsoft.rdc.androidx") {
            "com.microsoft.a3rdc.rdp.NativeRdpConnection".toClassOrNull()?.resolve()?.apply {
                firstMethodOrNull {
                    name = "sendScanCodeKey"
                }?.hook {
                    after {
                        if (enableLog) {
                            logDebug("HookWindowsApp: sendScanCodeKey called with args: ${args.joinToString()}")
                        }
                    }
                }
                firstMethodOrNull {
                    name = "sendSmartKey"
                }?.hook {
                    after {
                        if (enableLog) {
                            logDebug("HookWindowsApp: sendSmartKey called with args: ${args.joinToString()}")
                        }
                    }
                }
                firstMethodOrNull {
                    name = "sendVirtualKey"
                }?.hook {
                    after {
                        if (enableLog) {
                            logDebug("HookWindowsApp: sendVirtualKey called with args: ${args.joinToString()}")
                        }
                    }
                }
            }
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
            }
        }
    }
}