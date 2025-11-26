@file:Suppress("SetTextI18n")

package io.github.lazulikao.remotemate.ui.activity

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import io.github.lazulikao.remotemate.R
import io.github.lazulikao.remotemate.hook.modules.HookKeyboard
import com.highcapable.betterandroid.ui.extension.component.base.getThemeAttrsDrawable
import com.highcapable.betterandroid.ui.extension.view.textColor
import com.highcapable.betterandroid.ui.extension.view.updateTypeface
import com.highcapable.yukihookapi.hook.factory.prefs
import kotlinx.coroutines.*
import android.R as Android_R

data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
    val isRemoteDesktopApp: Boolean,
    var isSelected: Boolean = false,
    val isUninstalled: Boolean = false
)

class AppSelectorActivity : BaseActivity() {

    private val hookKeyboardPrefs by lazy { prefs("hook_keyboard") }
    private val selectedPackages = mutableSetOf<String>()
    private var appListContainer: LinearLayout? = null
    private var loadingView: View? = null
    private var selectedCountLabel: TextView? = null

    // 远程桌面相关的关键词，用于识别和排序
    private val remoteDesktopKeywords = listOf(
        "remote", "desktop", "rdp", "vnc", "teamviewer", "anydesk",
        "parsec", "moonlight", "steam link", "splashtop", "chrome remote",
        "rustdesk", "nomachine", "jump desktop", "microsoft remote",
        "远程", "桌面", "串流"
    )

    // 已知的远程桌面应用包名
    private val knownRemoteDesktopPackages = setOf(
        "com.microsoft.rdc.androidx",
        "com.microsoft.rdc.android",
        "com.realvnc.viewer.android",
        "com.teamviewer.teamviewer.market.mobile",
        "com.anydesk.anydeskandroid",
        "com.parsec.app",
        "com.limelight",
        "com.valvesoftware.steamlink",
        "com.splashtop.remote.pad.v2",
        "com.google.chromeremotedesktop",
        "com.carriez.flutter_hbb",
        "com.nomachine.nxplayer",
        "com.p5sys.android.jump",
        "com.yourcompany.yourapp"
    )

    private fun getEnabledTargetPackages(): Set<String> {
        val saved = hookKeyboardPrefs.getString("target_packages", "")
        return if (saved.isBlank()) HookKeyboard.DEFAULT_TARGET_PACKAGES
        else saved.split(",").filter { it.isNotBlank() }.toSet()
    }

    private fun saveEnabledTargetPackages(packages: Set<String>) {
        hookKeyboardPrefs.edit { putString("target_packages", packages.joinToString(",")) }
    }

    private fun isRemoteDesktopApp(packageName: String, appName: String): Boolean {
        if (packageName in knownRemoteDesktopPackages) return true
        val lowerName = appName.lowercase()
        val lowerPackage = packageName.lowercase()
        return remoteDesktopKeywords.any { keyword ->
            lowerName.contains(keyword) || lowerPackage.contains(keyword)
        }
    }

    private fun updateSelectedCount() {
        selectedCountLabel?.text = getString(R.string.selected_apps_count, selectedPackages.size)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedPackages.addAll(getEnabledTargetPackages())

        findViewById<View>(Android_R.id.content).setBackgroundResource(R.color.colorThemeBackground)

        // 创建主布局
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Title bar
        val titleBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            updatePadding(left = 16.dp, right = 16.dp, top = 16.dp, bottom = 8.dp)
        }

        val backButton = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(28.dp, 28.dp).apply {
                marginEnd = 12.dp
            }
            background = getThemeAttrsDrawable(Android_R.attr.selectableItemBackgroundBorderless)
            setImageResource(Android_R.drawable.ic_menu_close_clear_cancel)
            imageTintList = getColorStateList(R.color.colorTextGray)
            setOnClickListener { finish() }
        }
        titleBar.addView(backButton)

        val titleText = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text = getString(R.string.select_target_apps_title)
            textColor = getColor(R.color.colorTextGray)
            textSize = 22f
            updateTypeface(Typeface.BOLD)
        }
        titleBar.addView(titleText)

        val saveButton = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = getString(R.string.save)
            textColor = getColor(R.color.colorTextGray)
            textSize = 16f
            updateTypeface(Typeface.BOLD)
            background = getThemeAttrsDrawable(Android_R.attr.selectableItemBackgroundBorderless)
            updatePadding(left = 16.dp, top = 8.dp, right = 16.dp, bottom = 8.dp)
            setOnClickListener {
                saveEnabledTargetPackages(selectedPackages)
                setResult(RESULT_OK)
                finish()
            }
        }
        titleBar.addView(saveButton)

        mainLayout.addView(titleBar)

        // Subtitle with selected count
        val subtitleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            updatePadding(left = 16.dp, right = 16.dp, bottom = 12.dp)
        }

        val subtitleText = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text = getString(R.string.select_target_apps_summary)
            textColor = getColor(R.color.colorTextDark)
            textSize = 13f
            alpha = 0.75f
        }
        subtitleLayout.addView(subtitleText)

        selectedCountLabel = TextView(this).apply {
            text = getString(R.string.selected_apps_count, selectedPackages.size)
            textColor = getColor(R.color.colorTextGray)
            textSize = 13f
            updateTypeface(Typeface.BOLD)
            background = GradientDrawable().apply {
                cornerRadius = 999f
                setColor(ColorUtils.setAlphaComponent(getColor(R.color.colorTextDark), 35))
            }
            updatePadding(left = 12.dp, top = 4.dp, right = 12.dp, bottom = 4.dp)
        }
        subtitleLayout.addView(selectedCountLabel)

        mainLayout.addView(subtitleLayout)

        // Loading view
        loadingView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = getString(R.string.loading_apps)
            textColor = getColor(R.color.colorTextDark)
            textSize = 14f
            gravity = Gravity.CENTER
            updatePadding(top = 48.dp, bottom = 48.dp)
        }
        mainLayout.addView(loadingView)

        // ScrollView with app list
        val scrollView = NestedScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            isFillViewport = true
            clipToPadding = false
            updatePadding(left = 16.dp, right = 16.dp, bottom = 24.dp)
        }

        appListContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        scrollView.addView(appListContainer!!)
        mainLayout.addView(scrollView)

        setContentView(mainLayout)

        // 异步加载应用列表
        GlobalScope.launch(Dispatchers.Main) {
            val apps = withContext(Dispatchers.IO) { loadInstalledApps() }
            loadingView?.visibility = View.GONE
            displayApps(apps)
        }
    }

    private fun loadInstalledApps(): List<AppInfo> {
        val pm = packageManager
        val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val selfPackageName = packageName // 排除模块自身
        
        val installedPackages = installedApps.map { it.packageName }.toSet()
        val appList = mutableListOf<AppInfo>()

        // 首先添加已选中但已卸载的应用
        for (packageName in selectedPackages) {
            if (packageName !in installedPackages && packageName != selfPackageName) {
                appList.add(
                    AppInfo(
                        packageName = packageName,
                        appName = packageName, // 已卸载的应用只显示包名
                        icon = null,
                        isRemoteDesktopApp = false,
                        isSelected = true,
                        isUninstalled = true
                    )
                )
            }
        }

        // 然后添加已安装的应用
        appList.addAll(
            installedApps
                .filter { app ->
                    // 排除模块自身
                    if (app.packageName == selfPackageName) return@filter false
                    // 过滤掉系统应用（除非是已知的远程桌面应用）
                    val isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    val isKnownRemote = app.packageName in knownRemoteDesktopPackages
                    val hasLauncher = pm.getLaunchIntentForPackage(app.packageName) != null
                    (hasLauncher && !isSystemApp) || isKnownRemote
                }
                .map { app ->
                    val appName = pm.getApplicationLabel(app).toString()
                    val icon = try {
                        pm.getApplicationIcon(app.packageName)
                    } catch (e: Exception) {
                        null
                    }
                    val isRemote = isRemoteDesktopApp(app.packageName, appName)
                    AppInfo(
                        packageName = app.packageName,
                        appName = appName,
                        icon = icon,
                        isRemoteDesktopApp = isRemote,
                        isSelected = app.packageName in selectedPackages,
                        isUninstalled = false
                    )
                }
        )

        return appList.sortedWith(
            compareBy(
                { !it.isUninstalled },        // 已卸载的排在最前面
                { !it.isSelected },           // 已选择的次之
                { !it.isRemoteDesktopApp },   // 远程桌面应用再次之
                { it.appName.lowercase() }    // 按名称字母排序
            ))
    }

    private fun displayApps(apps: List<AppInfo>) {
        appListContainer?.removeAllViews()

        // 简化显示：已选中的优先，不再使用分组标题
        // 远程桌面应用会通过标签标识
        for (app in apps) {
            addAppItem(app)
        }
    }

    private fun addSectionHeader(title: String) {
        val header = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16.dp
                bottomMargin = 8.dp
            }
            text = title
            textColor = getColor(R.color.colorTextGray)
            textSize = 14f
            updateTypeface(Typeface.BOLD)
            alpha = 0.8f
        }
        appListContainer?.addView(header)
    }

    private fun addAppItem(app: AppInfo) {
        val itemLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 4.dp
                bottomMargin = 4.dp
            }
            background = GradientDrawable().apply {
                cornerRadius = 12.dp.toFloat()
                // 已卸载的应用使用不同的背景色
                val alpha = if (app.isUninstalled) 10 else 15
                setColor(ColorUtils.setAlphaComponent(getColor(R.color.colorTextDark), alpha))
            }
            updatePadding(left = 12.dp, top = 12.dp, right = 12.dp, bottom = 12.dp)
        }

        // App icon
        val iconView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(44.dp, 44.dp).apply {
                marginEnd = 12.dp
            }
            if (app.isUninstalled) {
                // 已卸载的应用显示默认图标并半透明
                setImageResource(Android_R.drawable.sym_def_app_icon)
                alpha = 0.4f
            } else if (app.icon != null) {
                setImageDrawable(app.icon)
            } else {
                setImageResource(Android_R.drawable.sym_def_app_icon)
            }
        }
        itemLayout.addView(iconView)

        // App info
        val infoLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val nameText = TextView(this).apply {
            text = app.appName
            textColor = getColor(R.color.colorTextGray)
            textSize = 15f
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
        }
        infoLayout.addView(nameText)

        val packageText = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 2.dp
            }
            text = app.packageName
            textColor = getColor(R.color.colorTextDark)
            textSize = 11f
            alpha = 0.6f
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
        }
        infoLayout.addView(packageText)

        // 标签容器（可能包含多个标签）
        val badgeLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 4.dp
            }
        }

        if (app.isUninstalled) {
            val uninstalledBadge = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = getString(R.string.uninstalled_badge)
                textColor = getColor(R.color.colorTextGray)
                textSize = 10f
                background = GradientDrawable().apply {
                    cornerRadius = 4.dp.toFloat()
                    setColor(ColorUtils.setAlphaComponent(getColor(Android_R.color.holo_red_light), 40))
                }
                updatePadding(left = 6.dp, top = 2.dp, right = 6.dp, bottom = 2.dp)
            }
            badgeLayout.addView(uninstalledBadge)
        }

        if (app.isRemoteDesktopApp) {
            val remoteBadge = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    if (app.isUninstalled) marginStart = 6.dp
                }
                text = getString(R.string.remote_desktop_badge)
                textColor = getColor(R.color.colorTextGray)
                textSize = 10f
                background = GradientDrawable().apply {
                    cornerRadius = 4.dp.toFloat()
                    setColor(ColorUtils.setAlphaComponent(getColor(R.color.colorPrimaryAccent), 40))
                }
                updatePadding(left = 6.dp, top = 2.dp, right = 6.dp, bottom = 2.dp)
            }
            badgeLayout.addView(remoteBadge)
        }

        if (badgeLayout.childCount > 0) {
            infoLayout.addView(badgeLayout)
        }

        itemLayout.addView(infoLayout)

        // Checkbox
        val checkBox = CheckBox(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            isChecked = app.isSelected
            setOnCheckedChangeListener { _, isChecked ->
                app.isSelected = isChecked
                if (isChecked) {
                    selectedPackages.add(app.packageName)
                } else {
                    selectedPackages.remove(app.packageName)
                }
                updateSelectedCount()
            }
        }
        itemLayout.addView(checkBox)

        // 点击整行也可以切换选择
        itemLayout.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
        }

        appListContainer?.addView(itemLayout)
    }

    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}
