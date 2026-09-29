plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.cognicode.remotedesktop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cognicode.remotedesktop"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "1.5"
    }

    signingConfigs {
        create("debugFixed") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debugFixed")
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debugFixed")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
