plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.chaquo.python")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile =
                file("C:\\Users\\chaox\\AppData\\Local\\Python\\pythoncore-3.14-64\\python.exe")
        }
    }
    namespace = "com.example.countandcatch"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.countandcatch"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {

            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")

        }
        flavorDimensions += "pyVersion"

        productFlavors {

            create("py311") {

                dimension = "pyVersion"

            }

        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
chaquopy {
    defaultConfig {
        version = "3.11"

        pip {
            install("numpy")
            install("pandas")
            install("matplotlib")
        }
    }
}


dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.compose.animation.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}