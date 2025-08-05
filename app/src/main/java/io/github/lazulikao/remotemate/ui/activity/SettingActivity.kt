@file:Suppress("SetTextI18n")

package io.github.lazulikao.remotemate.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import io.github.lazulikao.remotemate.R
import com.highcapable.betterandroid.ui.component.activity.AppViewsActivity
import com.highcapable.betterandroid.ui.extension.component.base.getThemeAttrsDrawable
import com.highcapable.betterandroid.ui.extension.view.textColor
import com.highcapable.hikage.extension.setContentView
import com.highcapable.hikage.widget.android.widget.ImageView
import com.highcapable.hikage.widget.android.widget.LinearLayout
import com.highcapable.hikage.widget.android.widget.Space
import com.highcapable.hikage.widget.android.widget.TextView
import com.highcapable.hikage.widget.androidx.core.widget.NestedScrollView
import com.highcapable.hikage.widget.io.github.lazulikao.remotemate.ui.view.MaterialSwitch
import android.R as Android_R

class SettingActivity : AppViewsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Base activity background
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
                        gravity = Gravity.CENTER or Gravity.START
                        updatePadding( 15.dp)
                        updatePadding(top = 13.dp, bottom = 5.dp)
                    }
                ) {
                    ImageView(
                        lparams = LayoutParams(27.dp, 27.dp) {
                            marginEnd = 10.dp
                        }
                    ) {
                        background = getThemeAttrsDrawable(Android_R.attr.selectableItemBackgroundBorderless)
                        alpha = 0.85f
                        setImageResource(Android_R.drawable.ic_menu_revert)
                        imageTintList = stateColorResource(R.color.colorTextGray)
                        setOnClickListener {
                            finish()
                        }
                    }
                    TextView(
                        lparams = LayoutParams {
                            weight = 1f
                        }
                    ) {
                        isSingleLine = true
                        text = getString(R.string.settings)
                        textColor = colorResource(R.color.colorTextGray)
                        textSize = 25f
                    }
                }

                NestedScrollView(
                    lparams = LayoutParams(matchParent = true) {
                        updateMargins(10.dp)
                    },
                    init = {
                        isFillViewport = true
                        isVerticalFadingEdgeEnabled = true
                    }
                ) {
                    LinearLayout(
                        lparams = LayoutParams(widthMatchParent = true),
                        init = {
                            orientation = LinearLayout.VERTICAL
                        }
                    ) {
                        // Remote control settings
                        LinearLayout(
                            lparams = LayoutParams(widthMatchParent = true) {
                                updateMargins( 15.dp)
                            },
                            init = {
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER or Gravity.START
                                setBackgroundResource(R.drawable.bg_permotion_round)
                                updatePadding(left = 15.dp, top = 15.dp, right = 15.dp)
                            }
                        ) {
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    gravity = Gravity.CENTER or Gravity.START
                                }
                            ) {
                                ImageView(
                                    lparams = LayoutParams(15.dp, 15.dp) {
                                        marginEnd = 10.dp
                                    }
                                ) {
                                    setImageResource(Android_R.drawable.ic_menu_manage)
                                    imageTintList = stateColorResource(R.color.colorTextGray)
                                }
                                TextView(
                                    lparams = LayoutParams(widthMatchParent = true)
                                ) {
                                    alpha = 0.85f
                                    isSingleLine = true
                                    text = "Remote Control Settings"
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 12f
                                }
                            }
                            
                            MaterialSwitch(
                                lparams = LayoutParams(widthMatchParent = true)
                            ) {
                                text = "Enable Remote Control"
                                isAllCaps = false
                                textColor = colorResource(R.color.colorTextGray)
                                textSize = 15f
                                isChecked = true // Default checked
                                setOnCheckedChangeListener { button, isChecked ->
                                    if (button.isPressed) {
                                        // Handle remote control toggle
                                    }
                                }
                            }
                            
                            TextView(
                                lparams = LayoutParams(widthMatchParent = true) {
                                    bottomMargin = 10.dp
                                }
                            ) {
                                alpha = 0.6f
                                setLineSpacing(6f, 1f)
                                text = "Enable or disable remote control functionality"
                                textColor = colorResource(R.color.colorTextDark)
                                textSize = 12f
                            }
                        }

                        Space(lparams = LayoutParams(height = 15.dp))

                        // Keyboard settings
                        LinearLayout(
                            lparams = LayoutParams(widthMatchParent = true) {
                                updateMargins( 15.dp)
                            },
                            init = {
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER or Gravity.START
                                setBackgroundResource(R.drawable.bg_permotion_round)
                                updatePadding(left = 15.dp, top = 15.dp, right = 15.dp)
                            }
                        ) {
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    gravity = Gravity.CENTER or Gravity.START
                                }
                            ) {
                                ImageView(
                                    lparams = LayoutParams(15.dp, 15.dp) {
                                        marginEnd = 10.dp
                                    }
                                ) {
                                    setImageResource(Android_R.drawable.ic_dialog_info)
                                    imageTintList = stateColorResource(R.color.colorTextGray)
                                }
                                TextView(
                                    lparams = LayoutParams(widthMatchParent = true)
                                ) {
                                    alpha = 0.85f
                                    isSingleLine = true
                                    text = "Keyboard Settings"
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 12f
                                }
                            }
                            
                            MaterialSwitch(
                                lparams = LayoutParams(widthMatchParent = true)
                            ) {
                                text = "Hook Keyboard Input"
                                isAllCaps = false
                                textColor = colorResource(R.color.colorTextGray)
                                textSize = 15f
                                isChecked = true
                                setOnCheckedChangeListener { button, isChecked ->
                                    if (button.isPressed) {
                                        // Handle keyboard hook toggle
                                    }
                                }
                            }
                            
                            MaterialSwitch(
                                lparams = LayoutParams(widthMatchParent = true)
                            ) {
                                text = "Enable Special Keys"
                                isAllCaps = false
                                textColor = colorResource(R.color.colorTextGray)
                                textSize = 15f
                                isChecked = false
                                setOnCheckedChangeListener { button, isChecked ->
                                    if (button.isPressed) {
                                        // Handle special keys toggle
                                    }
                                }
                            }
                            
                            TextView(
                                lparams = LayoutParams(widthMatchParent = true) {
                                    bottomMargin = 10.dp
                                }
                            ) {
                                alpha = 0.6f
                                setLineSpacing(6f, 1f)
                                text = "Configure keyboard input hooking and special key support"
                                textColor = colorResource(R.color.colorTextDark)
                                textSize = 12f
                            }
                        }

                        Space(lparams = LayoutParams(height = 15.dp))

                        // Advanced settings
                        LinearLayout(
                            lparams = LayoutParams(widthMatchParent = true) {
                                updateMargins(15.dp)
                            },
                            init = {
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER or Gravity.START
                                setBackgroundResource(R.drawable.bg_permotion_round)
                                updatePadding(left = 15.dp, top = 15.dp, right = 15.dp)
                            }
                        ) {
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    gravity = Gravity.CENTER or Gravity.START
                                }
                            ) {
                                ImageView(
                                    lparams = LayoutParams(15.dp, 15.dp) {
                                        marginEnd = 10.dp
                                    }
                                ) {
                                    setImageResource(Android_R.drawable.ic_menu_preferences)
                                    imageTintList = stateColorResource(R.color.colorTextGray)
                                }
                                TextView(
                                    lparams = LayoutParams(widthMatchParent = true)
                                ) {
                                    alpha = 0.85f
                                    isSingleLine = true
                                    text = "Advanced Settings"
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 12f
                                }
                            }
                            
                            MaterialSwitch(
                                lparams = LayoutParams(widthMatchParent = true)
                            ) {
                                text = "Debug Mode"
                                isAllCaps = false
                                textColor = colorResource(R.color.colorTextGray)
                                textSize = 15f
                                isChecked = false
                                setOnCheckedChangeListener { button, isChecked ->
                                    if (button.isPressed) {
                                        // Handle debug mode toggle
                                    }
                                }
                            }
                            
                            MaterialSwitch(
                                lparams = LayoutParams(widthMatchParent = true)
                            ) {
                                text = "Auto Start Service"
                                isAllCaps = false
                                textColor = colorResource(R.color.colorTextGray)
                                textSize = 15f
                                isChecked = true
                                setOnCheckedChangeListener { button, isChecked ->
                                    if (button.isPressed) {
                                        // Handle auto start toggle
                                    }
                                }
                            }
                            
                            TextView(
                                lparams = LayoutParams(widthMatchParent = true) {
                                    bottomMargin = 10.dp
                                }
                            ) {
                                alpha = 0.6f
                                setLineSpacing(6f, 1f)
                                text = "Advanced options for power users"
                                textColor = colorResource(R.color.colorTextDark)
                                textSize = 12f
                            }
                        }

                        Space(lparams = LayoutParams(height = 20.dp))
                    }
                }
            }
        }
    }
}
