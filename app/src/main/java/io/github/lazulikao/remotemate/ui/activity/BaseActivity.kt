package io.github.lazulikao.remotemate.ui.activity

import android.content.Context
import android.os.Bundle
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import io.github.lazulikao.remotemate.utils.LanguageManager

abstract class BaseActivity : AppViewsActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.applyLanguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.applyLanguage(this)
    }
}
