plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.stela.stockapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.stela.stockapp"
        minSdk = 24
        targetSdk = 30
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
    implementation(libs.cardview)
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("androidx.room:room-runtime:2.8.4")
    annotationProcessor ("androidx.room:room-compiler:2.8.4")
    implementation(files("libs/grmt288-1.0.0.7.aar"))
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("androidx.core:core-ktx:1.12.0")


}