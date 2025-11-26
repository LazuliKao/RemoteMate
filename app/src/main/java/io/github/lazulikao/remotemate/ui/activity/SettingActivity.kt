@file:Suppress("SetTextI18n")

package io.github.lazulikao.remotemate.ui.activity

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.ColorUtils
import androidx.core.view.updatePadding
import io.github.lazulikao.remotemate.R
import io.github.lazulikao.remotemate.hook.modules.HookKeyboard
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
import io.github.lazulikao.remotemate.utils.LanguageManager
import android.app.AlertDialog
import android.R as Android_R

class SettingActivity : BaseActivity() {

    private val hookKeyboardPrefs by lazy { prefs("hook_keyboard") }
    private val hookWindowsAppPrefs by lazy { prefs("hook_windows_app") }
    private var hookStatusLabel: TextView? = null
    private var windowsAppStatusLabel: TextView? = null
    private var targetAppsLabel: TextView? = null
    private var targetAppsDivider: View? = null
    private var targetAppsContainer: LinearLayout? = null

    private val appSelectorLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            updateTargetAppsLabel()
        }
    }

    private fun getEnabledTargetPackages(): Set<String> {
        val saved = hookKeyboardPrefs.getString("target_packages", "")
        return if (saved.isBlank()) HookKeyboard.DEFAULT_TARGET_PACKAGES
        else saved.split(",").filter { it.isNotBlank() }.toSet()
    }

    private fun updateTargetAppsLabel() {
        val count = getEnabledTargetPackages().size
        targetAppsLabel?.text = getString(R.string.selected_apps_count, count)
    }

    private fun openAppSelector() {
        val intent = Intent(this, AppSelectorActivity::class.java)
        appSelectorLauncher.launch(intent)
    }

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
                    text = getString(R.string.settings_description)
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
                                    text = getString(R.string.keyboard_hook)
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 14f
                                    updateTypeface(Typeface.BOLD)
                                    alpha = 0.9f
                                }
                                TextView(
                                    lparams = LayoutParams()
                                ) {
                                    hookStatusLabel = this
                                    text = if (isHookEnabledInitially) getString(R.string.status_enabled) else getString(R.string.status_disabled)
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
                                        text = getString(R.string.enable_keyboard_hook)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.enable_keyboard_hook_summary)
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
                                        hookStatusLabel?.text = if (checked) getString(R.string.status_enabled) else getString(R.string.status_disabled)
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
                                        // 显示/隐藏 Target Apps Selection
                                        val visibility = if (checked) View.VISIBLE else View.GONE
                                        targetAppsDivider?.visibility = visibility
                                        targetAppsContainer?.visibility = visibility
                                    }
                                }
                            }

                            // Divider (for Target Apps Selection)
                            View(
                                lparams = LayoutParams(widthMatchParent = true, height = 1.dp) {
                                    topMargin = 16.dp
                                    bottomMargin = 16.dp
                                }
                            ) {
                                targetAppsDivider = this
                                visibility = if (isTargetAppOnlyInitially) View.VISIBLE else View.GONE
                                setBackgroundColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 24))
                            }

                            // Target Apps Selection
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    targetAppsContainer = this
                                    visibility = if (isTargetAppOnlyInitially) View.VISIBLE else View.GONE
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_VERTICAL
                                    background = getThemeAttrsDrawable(Android_R.attr.selectableItemBackground)
                                    setOnClickListener { openAppSelector() }
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
                                        text = getString(R.string.select_target_apps_title)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.select_target_apps_summary)
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                TextView(
                                    lparams = LayoutParams()
                                ) {
                                    targetAppsLabel = this
                                    val count = getEnabledTargetPackages().size
                                    text = getString(R.string.selected_apps_count, count)
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 12f
                                    alpha = 0.85f
                                }
                                ImageView(
                                    lparams = LayoutParams(20.dp, 20.dp) {
                                        marginStart = 8.dp
                                    }
                                ) {
                                    setImageResource(R.drawable.ic_chevron_right)
                                    imageTintList = stateColorResource(R.color.colorTextGray)
                                    alpha = 0.6f
                                }
                            }
                        }

                        Space(lparams = LayoutParams(height = 16.dp))

                        // Windows App Hook Settings Card
                        val isHideSoftKeyboardInitially = hookWindowsAppPrefs.getBoolean("hide_soft_keyboard", true)
                        val isForceScancodeInitially = hookWindowsAppPrefs.getBoolean("force_scancode", true)
                        val isWindowsAppHookEnabled = isHideSoftKeyboardInitially || isForceScancodeInitially
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
                                        setImageResource(Android_R.drawable.ic_menu_manage)
                                        imageTintList = stateColorResource(R.color.colorTextGray)
                                    }
                                }
                                TextView(
                                    lparams = LayoutParams {
                                        weight = 1f
                                    }
                                ) {
                                    text = getString(R.string.windows_app_hook)
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 14f
                                    updateTypeface(Typeface.BOLD)
                                    alpha = 0.9f
                                }
                                TextView(
                                    lparams = LayoutParams()
                                ) {
                                    windowsAppStatusLabel = this
                                    text = if (isWindowsAppHookEnabled) getString(R.string.status_enabled) else getString(R.string.status_disabled)
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

                            // Description
                            TextView(
                                lparams = LayoutParams(widthMatchParent = true) {
                                    bottomMargin = 12.dp
                                }
                            ) {
                                text = getString(R.string.windows_app_hook_desc)
                                textColor = colorResource(R.color.colorTextDark)
                                textSize = 12f
                                alpha = 0.7f
                            }

                            View(
                                lparams = LayoutParams(widthMatchParent = true, height = 1.dp) {
                                    bottomMargin = 16.dp
                                }
                            ) {
                                setBackgroundColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 24))
                            }

                            // Hide Soft Keyboard Switch
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
                                        text = getString(R.string.hide_soft_keyboard_title)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.hide_soft_keyboard_summary)
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                MaterialSwitch(
                                    lparams = LayoutParams()
                                ) {
                                    isChecked = isHideSoftKeyboardInitially
                                    setOnCheckedChangeListener { button, checked ->
                                        if (button.isPressed) {
                                            hookWindowsAppPrefs.edit { putBoolean("hide_soft_keyboard", checked) }
                                            updateWindowsAppStatus()
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

                            // Force Scancode Switch
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
                                        text = getString(R.string.force_scancode_title)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.force_scancode_summary)
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                MaterialSwitch(
                                    lparams = LayoutParams()
                                ) {
                                    isChecked = isForceScancodeInitially
                                    setOnCheckedChangeListener { button, checked ->
                                        if (button.isPressed) {
                                            hookWindowsAppPrefs.edit { putBoolean("force_scancode", checked) }
                                            updateWindowsAppStatus()
                                        }
                                    }
                                }
                            }
                        }

                        Space(lparams = LayoutParams(height = 16.dp))

                        // Language Settings Card
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
                                        setImageResource(Android_R.drawable.ic_menu_sort_by_size)
                                        imageTintList = stateColorResource(R.color.colorTextGray)
                                    }
                                }
                                TextView(
                                    lparams = LayoutParams {
                                        weight = 1f
                                    }
                                ) {
                                    text = getString(R.string.language_settings)
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 14f
                                    updateTypeface(Typeface.BOLD)
                                    alpha = 0.9f
                                }
                            }

                            View(
                                lparams = LayoutParams(widthMatchParent = true, height = 1.dp) {
                                    bottomMargin = 16.dp
                                }
                            ) {
                                setBackgroundColor(ColorUtils.setAlphaComponent(colorResource(R.color.colorTextDark), 24))
                            }

                            // Language Selection
                            LinearLayout(
                                lparams = LayoutParams(widthMatchParent = true),
                                init = {
                                    orientation = LinearLayout.HORIZONTAL
                                    gravity = Gravity.CENTER_VERTICAL
                                    background = getThemeAttrsDrawable(Android_R.attr.selectableItemBackground)
                                    setOnClickListener { showLanguageDialog() }
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
                                        text = getString(R.string.language_title)
                                        textColor = colorResource(R.color.colorTextGray)
                                        textSize = 16f
                                    }
                                    TextView(
                                        lparams = LayoutParams {
                                            topMargin = 4.dp
                                        }
                                    ) {
                                        text = getString(R.string.language_summary)
                                        textColor = colorResource(R.color.colorTextDark)
                                        textSize = 12f
                                        alpha = 0.7f
                                        setLineSpacing(4f, 1f)
                                    }
                                }
                                TextView(
                                    lparams = LayoutParams()
                                ) {
                                    val currentLanguage = LanguageManager.getLanguage(this@SettingActivity)
                                    text = LanguageManager.getLanguageName(this@SettingActivity, currentLanguage)
                                    textColor = colorResource(R.color.colorTextGray)
                                    textSize = 12f
                                    alpha = 0.85f
                                }
                                ImageView(
                                    lparams = LayoutParams(20.dp, 20.dp) {
                                        marginStart = 8.dp
                                    }
                                ) {
                                    setImageResource(R.drawable.ic_chevron_right)
                                    imageTintList = stateColorResource(R.color.colorTextGray)
                                    alpha = 0.6f
                                }
                            }
                        }

                        Space(lparams = LayoutParams(height = 24.dp))
                    }
                }
            }
        }
    }

    private fun showLanguageDialog() {
        val languages = listOf(
            LanguageManager.LANGUAGE_FOLLOW_SYSTEM,
            LanguageManager.LANGUAGE_ENGLISH,
            LanguageManager.LANGUAGE_CHINESE,
            LanguageManager.LANGUAGE_ARABIC,
            LanguageManager.LANGUAGE_PERSIAN
        )
        
        val languageNames = languages.map { 
            LanguageManager.getLanguageName(this, it)
        }.toTypedArray()
        
        val currentLanguage = LanguageManager.getLanguage(this)
        val currentIndex = languages.indexOf(currentLanguage)
        
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.language_dialog_title))
            .setSingleChoiceItems(languageNames, currentIndex) { dialog, which ->
                val selectedLanguage = languages[which]
                if (selectedLanguage != currentLanguage) {
                    LanguageManager.setLanguage(this, selectedLanguage)
                    dialog.dismiss()
                    recreate()
                } else {
                    dialog.dismiss()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun updateWindowsAppStatus() {
        val hideSoftKeyboard = hookWindowsAppPrefs.getBoolean("hide_soft_keyboard", true)
        val forceScancode = hookWindowsAppPrefs.getBoolean("force_scancode", true)
        val isEnabled = hideSoftKeyboard || forceScancode
        windowsAppStatusLabel?.text = if (isEnabled) getString(R.string.status_enabled) else getString(R.string.status_disabled)
    }
}
