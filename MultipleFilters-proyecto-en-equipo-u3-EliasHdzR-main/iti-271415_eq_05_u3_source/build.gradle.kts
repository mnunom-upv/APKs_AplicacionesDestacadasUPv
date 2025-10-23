plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "upvictoria.pm_may_ago_2025.iti_271415.pg1u3_eq05"
    compileSdk = 36

    defaultConfig {
        applicationId = "upvictoria.pm_may_ago_2025.iti_271415.pg1u3_eq05"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.sceneform)
    implementation(libs.gridlayout.v100)
}