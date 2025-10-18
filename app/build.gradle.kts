plugins {
    autowire(libs.plugins.android.application)
    autowire(libs.plugins.kotlin.android)
    autowire(libs.plugins.kotlin.ksp)
}

android {
    namespace = property.project.app.packageName
    compileSdk = property.project.android.compileSdk

    defaultConfig {
        applicationId = property.project.app.packageName
        minSdk = property.project.android.minSdk
        targetSdk = property.project.android.targetSdk
        versionName = property.project.app.versionName
        versionCode = property.project.app.versionCode
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    signingConfigs {
        create("release") {
            // For CI/CD: These values are provided via environment variables
            storeFile = System.getenv("KEYSTORE_FILE")?.let { file(it) }
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
            
            // For local builds: Load from local.properties file
            if (storeFile == null) {
                val keystorePropertiesFile = rootProject.file("keystore.properties")
                if (keystorePropertiesFile.exists()) {
                    val keystoreProperties = java.util.Properties()
                    keystoreProperties.load(keystorePropertiesFile.inputStream())
                    
                    keystoreProperties.getProperty("storeFile")?.let {
                        storeFile = rootProject.file(it)
                    }
                    storePassword = keystoreProperties.getProperty("storePassword")
                    keyAlias = keystoreProperties.getProperty("keyAlias")
                    keyPassword = keystoreProperties.getProperty("keyPassword")
                }
            }
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            
            // Apply signing config if available
            if (signingConfigs.getByName("release").storeFile != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xno-param-assertions",
            "-Xno-call-assertions",
            "-Xno-receiver-assertions"
        )
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    lint { checkReleaseBuilds = false }

    // TODO Please visit https://highcapable.github.io/YukiHookAPI/en/api/special-features/host-inject
    // TODO 请参考 https://highcapable.github.io/YukiHookAPI/zh-cn/api/special-features/host-inject
    // androidResources.additionalParameters += listOf("--allow-reserved-package-id", "--package-id", "0x64")
}

dependencies {
    compileOnly(de.robv.android.xposed.api)
    ksp(com.highcapable.yukihookapi.ksp.xposed)
    implementation(com.highcapable.yukihookapi.api)

    // Optional: KavaRef (https://github.com/HighCapable/KavaRef)
    implementation(com.highcapable.kavaref.kavaref.core)
    implementation(com.highcapable.kavaref.kavaref.extension)

    // Optional: Hikage (https://github.com/BetterAndroid/Hikage)
    ksp(com.highcapable.hikage.hikage.compiler)
    implementation(com.highcapable.hikage.hikage.core)
    implementation(com.highcapable.hikage.hikage.extension)
    implementation(com.highcapable.hikage.hikage.widget.androidx)
    implementation(com.highcapable.hikage.hikage.widget.material)

    // Optional: BetterAndroid (https://github.com/BetterAndroid/BetterAndroid)
    implementation(com.highcapable.betterandroid.ui.component)
    implementation(com.highcapable.betterandroid.ui.extension)
    implementation(com.highcapable.betterandroid.system.extension)

    implementation(com.github.duanhong169.drawabletoolbox)

    implementation(androidx.core.core.ktx)
    implementation(androidx.appcompat.appcompat)
    implementation(androidx.constraintlayout.constraintlayout)

    implementation(com.google.android.material.material)

    testImplementation(junit.junit)
    androidTestImplementation(androidx.test.ext.junit)
    androidTestImplementation(androidx.test.espresso.espresso.core)
}