package io.github.lazulikao.remotemate.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import io.github.lazulikao.remotemate.hook.modules.HookKeyboard
import io.github.lazulikao.remotemate.hook.modules.HookWindowsApp

@InjectYukiHookWithXposed(isUsingResourcesHook = false)
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "RemoteMate"
        }
    }

    override fun onHook() = encase {
        HookKeyboard().onLoad(this)
        HookWindowsApp().onLoad(this)
    }
}