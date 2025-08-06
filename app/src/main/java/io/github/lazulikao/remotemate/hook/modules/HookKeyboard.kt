package io.github.lazulikao.remotemate.hook.modules

import android.annotation.SuppressLint
import android.view.KeyEvent
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.hook.param.PackageParam

class HookKeyboard : IHookModule {
    override val enableLog: Boolean
        get() = true

    override fun onLoad(env: PackageParam) {
        env.apply {
            loadSystem {
                // https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/services/core/java/com/android/server/policy/PhoneWindowManager.java#3430
                "com.android.server.policy.PhoneWindowManager".toClass().resolve()
                    // public long interceptKeyBeforeDispatching(IBinder focusedToken, KeyEvent event,int policyFlags) {
                    .firstMethod { name = "interceptKeyBeforeDispatching" }.hook {
                        before {
                            val e = args[1] as KeyEvent
                            if (enableLog) {
                                logDebug("HookKeyboard: interceptKeyBeforeDispatching called with event: $e , device: ${e.device}. type: ${e.device.keyboardType}, external: ${e.device.isExternal}")
                            }
                            if (e.device.isExternal)
                                result = 0L // Return 0 to indicate that the event has been handled
                        }
                    }
            }
        }
    }
}