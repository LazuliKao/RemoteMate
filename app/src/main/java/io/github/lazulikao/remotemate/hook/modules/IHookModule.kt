package io.github.lazulikao.remotemate.hook.modules

import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.param.PackageParam

interface IHookModule {
    fun onLoad(env: PackageParam)
    val enableLog: Boolean
        get() = true

    fun logDebug(message: String) {
        if (enableLog) YLog.debug(message)
    }
}