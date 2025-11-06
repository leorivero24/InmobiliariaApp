plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.inmobiliaria"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.inmobiliaria"
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

    buildFeatures {
        viewBinding = true
    }





    dependencies {

        // Navigation Component
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.0")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.0")

        implementation("com.github.bumptech.glide:glide:4.16.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)
        implementation(libs.lifecycle.viewmodel)
        implementation(libs.lifecycle.livedata)
        implementation(libs.lifecycle.runtime)
        implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)

        implementation("com.google.android.gms:play-services-maps:18.1.0")

        // --- Retrofit ---
        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    }
}
dependencies {
    implementation(libs.databinding.runtime)
}
