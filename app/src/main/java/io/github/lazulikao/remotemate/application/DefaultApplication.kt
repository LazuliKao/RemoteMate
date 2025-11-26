package io.github.lazulikao.remotemate.application

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication
import io.github.lazulikao.remotemate.utils.LanguageManager

class DefaultApplication : ModuleApplication() {

    override fun onCreate() {
        super.onCreate()
        /**
         * 跟随系统夜间模式
         * Follow system night mode
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        
        // Apply language settings
        LanguageManager.applyLanguage(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageManager.applyLanguage(base))
    }
}