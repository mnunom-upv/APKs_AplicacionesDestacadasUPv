plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.z_iti_271304_u3_e05"
    /*
        IMPORTANTE: CAMBIAR SDK AL 35
        34 -> 35
     */
    compileSdk = 35

    defaultConfig {
        applicationId = "com.z_iti_271304_u3_e05"
        minSdk = 24
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Versión antigua de Filament
    //Se usó esta por practicidad
    implementation ("com.google.android.filament:filament-android:1.6.0")
    implementation ("com.google.android.filament:filament-utils-android:1.6.0")
    implementation ("com.google.android.filament:gltfio-android:1.6.0")
}