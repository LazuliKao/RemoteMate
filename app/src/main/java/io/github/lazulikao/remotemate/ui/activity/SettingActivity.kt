@file:Suppress("SetTextI18n")

package io.github.lazulikao.remotemate.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<View>(Android_R.id.content).setBackgroundResource(R.color.colorThemeBackground)

        setContentView {
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
                        setImageResource(Android_R.drawable.ic_menu_revert)
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

                NestedScrollView(
                    lparams = LayoutParams(matchParent = true),
                    init = {
                        isFillViewport = true
                        clipToPadding = false
                        updatePadding(left = 16.dp, right = 16.dp, bottom = 24.dp)
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
                        LinearLayout(
                            lparams = LayoutParams(widthMatchParent = true),
                            init = {
                                orientation = LinearLayout.VERTICAL
                                setBackgroundResource(R.drawable.bg_permotion_round)
                                updatePadding(16.dp)
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
                                ImageView(
                                    lparams = LayoutParams(18.dp, 18.dp) {
                                        marginEnd = 10.dp
                                    }
                                ) {
                                    setImageResource(Android_R.drawable.ic_dialog_dialer)
                                    imageTintList = stateColorResource(R.color.colorTextGray)
                                    alpha = 0.8f
                                }
                                TextView {
                                    text = "Keyboard Hook"
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 14f
                                    updateTypeface(Typeface.BOLD)
                                    alpha = 0.9f
                                }
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
                                    isChecked = hookKeyboardPrefs.getBoolean("enable_hook_keyboard", false)
                                    setOnCheckedChangeListener { button, checked ->
                                        if (button.isPressed) {
                                            hookKeyboardPrefs.edit { putBoolean("enable_hook_keyboard", checked) }
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
