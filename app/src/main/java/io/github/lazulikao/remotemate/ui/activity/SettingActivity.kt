@file:Suppress("SetTextI18n")

package io.github.lazulikao.remotemate.ui.activity

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.view.updatePadding
import io.github.lazulikao.remotemate.R
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.extension.component.base.getThemeAttrsDrawable
import com.highcapable.betterandroid.ui.extension.view.textColor
import com.highcapable.betterandroid.ui.extension.view.updateTypeface
import com.highcapable.hikage.extension.setContentView
import com.highcapable.hikage.widget.android.widget.ImageView
import com.highcapable.hikage.widget.android.widget.LinearLayout
import com.highcapable.hikage.widget.android.widget.Space
import com.highcapable.hikage.widget.android.widget.TextView
import com.highcapable.hikage.widget.androidx.core.widget.NestedScrollView
import com.highcapable.hikage.widget.io.github.lazulikao.remotemate.ui.view.MaterialSwitch
import com.highcapable.yukihookapi.hook.factory.prefs
import android.R as Android_R

class SettingActivity : AppViewsActivity() {

    private val hookKeyboardPrefs by lazy { prefs("hook_keyboard") }
    private var hookStatusLabel: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<View>(Android_R.id.content).setBackgroundResource(R.color.colorThemeBackground)

        setContentView {
            val isHookEnabledInitially = hookKeyboardPrefs.getBoolean("enable_hook_keyboard", false)
            LinearLayout(
                lparams = LayoutParams(matchParent = true),
                init = {
                    orientation = LinearLayout.VERTICAL
                }
            ) {
                // Title bar
                LinearLayout(
                    lparams = LayoutParams(widthMatchParent = true),
                    init = {
                        gravity = Gravity.CENTER_VERTICAL
                        updatePadding(left = 16.dp, right = 16.dp, top = 16.dp, bottom = 8.dp)
                    }
                ) {
                    ImageView(
                        lparams = LayoutParams(28.dp, 28.dp) {
                            marginEnd = 12.dp
                        }
                    ) {
                        background = getThemeAttrsDrawable(Android_R.attr.selectableItemBackgroundBorderless)
                        setImageResource(Android_R.drawable.ic_menu_close_clear_cancel)
                        imageTintList = stateColorResource(R.color.colorTextGray)
                        setOnClickListener { finish() }
                    }
                    TextView {
                        text = getString(R.string.settings)
                        textColor = colorResource(R.color.colorTextGray)
                        textSize = 22f
                        updateTypeface(Typeface.BOLD)
                    }
                }
                TextView(
                    lparams = LayoutParams(widthMatchParent = true) {
                        leftMargin = 16.dp
                        rightMargin = 16.dp
                        bottomMargin = 12.dp
                    }
                ) {
                    text = "Tune how RemoteMate handles hardware keyboard input on this device"
                    textColor = colorResource(R.color.colorTextDark)
                    textSize = 13f
                    alpha = 0.75f
                    setLineSpacing(4f, 1f)
                }

                NestedScrollView(
                    lparams = LayoutParams(matchParent = true),
                    init = {
                        isFillViewport = true
                        clipToPadding = false
                        updatePadding(left = 16.dp, top = 4.dp, right = 16.dp, bottom = 24.dp)
                    }
                ) {
                    LinearLayout(
                        lparams = LayoutParams(widthMatchParent = true),
                        init = {
                            orientation = LinearLayout.VERTICAL
                        }
                    ) {
                        Space(lparams = LayoutParams(height = 8.dp))

                        // Keyboard Hook Settings Card
                        val isExternalOnlyInitially = hookKeyboardPrefs.getBoolean("external_only", true)
                        val isTargetAppOnlyInitially = hookKeyboardPrefs.getBoolean("target_app_only", true)
                        LinearLayout(
                            lparams = LayoutParams(widthMatchParent = true),
                            init = {
                                orientation = LinearLayout.VERTICAL
                                setBackgroundResource(R.drawable.bg_permotion_round)
                                updatePadding(left = 20.dp, top = 20.dp, right = 20.dp, bottom = 20.dp)
                                elevation = 6f
                            }
                        ) {
                            // Card header
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true) {
                                    bottomMargin = 12.dp
                                },
                                init = {
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            ) {
                                LinearLayout(
                                    lparams = LayoutParams(40.dp, 40.dp) {
                                        marginEnd = 12.dp
                                    },
                                    init = {
                                        gravity = Gravity.CENTER
                                        background = GradientDrawable().apply {
                                            cornerRadius = 20.dp.toFloat()
                                            setColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 30))
                                        }
                                    }
                                ) {
                                    ImageView(
                                        lparams = LayoutParams(18.dp, 18.dp)
                                    ) {
                                        setImageResource(Android_R.drawable.ic_dialog_dialer)
                                        imageTintList = stateColorResource(R.color.colorTextGray)
                                    }
                                }
                                TextView(
                                    lparams = LayoutParams {
                                        weight = 1f
                                    }
                                ) {
                                    text = "Keyboard Hook"
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 14f
                                    updateTypeface(Typeface.BOLD)
                                    alpha = 0.9f
                                }
                                TextView(
                                    lparams = LayoutParams()
                                ) {
                                    hookStatusLabel = this
                                    text = if (isHookEnabledInitially) "Enabled" else "Disabled"
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 12f
                                    alpha = 0.85f
                                    updateTypeface(Typeface.BOLD)
                                    background = GradientDrawable().apply {
                                        cornerRadius = 999f
                                        setColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 35))
                                    }
                                    updatePadding(left = 14.dp, top = 6.dp, right = 14.dp, bottom = 6.dp)
                                }
                            }
                            View(
                                lparams = LayoutParams(widthMatchParent = true, height = 1.dp) {
                                    bottomMargin = 16.dp
                                }
                            ) {
                                setBackgroundColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 24))
                            }
                            // Switch item
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            ) {
                                LinearLayout(
                                    lparams = LayoutParams(widthMatchParent = true) {
                                        weight = 1f
                                        marginEnd = 12.dp
                                    },
                                    init = {
                                        orientation = LinearLayout.VERTICAL
                                    }
                                ) {
                                    TextView {
                                        text = "Enable Keyboard Hook"
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = "Intercept external keyboard input to prevent system shortcuts from being triggered"
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                MaterialSwitch(
                                    lparams = LayoutParams()
                                ) {
                                    isChecked = isHookEnabledInitially
                                    setOnCheckedChangeListener { button, checked ->
                                        if (button.isPressed) {
                                            hookKeyboardPrefs.edit { putBoolean("enable_hook_keyboard", checked) }
                                        }
                                        hookStatusLabel?.text = if (checked) "Enabled" else "Disabled"
                                    }
                                }
                            }

                            // Divider
                            View(
                                lparams = LayoutParams(widthMatchParent = true, height = 1.dp) {
                                    topMargin = 16.dp
                                    bottomMargin = 16.dp
                                }
                            ) {
                                setBackgroundColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 24))
                            }

                            // External Only Switch
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            ) {
                                LinearLayout(
                                    lparams = LayoutParams(widthMatchParent = true) {
                                        weight = 1f
                                        marginEnd = 12.dp
                                    },
                                    init = {
                                        orientation = LinearLayout.VERTICAL
                                    }
                                ) {
                                    TextView {
                                        text = getString(R.string.external_only_title)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.external_only_summary)
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                MaterialSwitch(
                                    lparams = LayoutParams()
                                ) {
                                    isChecked = isExternalOnlyInitially
                                    setOnCheckedChangeListener { button, checked ->
                                        if (button.isPressed) {
                                            hookKeyboardPrefs.edit { putBoolean("external_only", checked) }
                                        }
                                    }
                                }
                            }

                            // Divider
                            View(
                                lparams = LayoutParams(widthMatchParent = true, height = 1.dp) {
                                    topMargin = 16.dp
                                    bottomMargin = 16.dp
                                }
                            ) {
                                setBackgroundColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 24))
                            }

                            // Target App Only Switch
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_VERTICAL
                                }
                            ) {
                                LinearLayout(
                                    lparams = LayoutParams(widthMatchParent = true) {
                                        weight = 1f
                                        marginEnd = 12.dp
                                    },
                                    init = {
                                        orientation = LinearLayout.VERTICAL
                                    }
                                ) {
                                    TextView {
                                        text = getString(R.string.target_app_only_title)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.target_app_only_summary)
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                MaterialSwitch(
                                    lparams = LayoutParams()
                                ) {
                                    isChecked = isTargetAppOnlyInitially
                                    setOnCheckedChangeListener { button, checked ->
                                        if (button.isPressed) {
                                            hookKeyboardPrefs.edit { putBoolean("target_app_only", checked) }
                                        }
                                    }
                                }
                            }
                        }

                        Space(lparams = LayoutParams(height = 24.dp))
                    }
                }
            }
        }
    }
}
