pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://api.xposed.info/")
        maven("https://jitpack.io")
    }
}

plugins {
    id("com.highcapable.gropify") version "1.0.1"
}

gropify {
    rootProject {
        common {
            isEnabled = false
        }
    }
}

rootProject.name = "RemoteMate"

include(":app")