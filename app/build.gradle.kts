plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.lushdrobe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lushdrobe"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Enable Java 8 features
        vectorDrawables.useSupportLibrary = true
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
        // Enable Java 8 features
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat.v171)
    implementation(libs.material.v1120)
    implementation(libs.androidx.constraintlayout.v221)
    implementation(libs.androidx.activity.v182)

    // Lifecycle components
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)

    // Image loading
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // SQLite (direct implementation - no Room)
    // No additional dependencies needed for standard SQLite

    // Java 8+ API desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs.v215)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)

}