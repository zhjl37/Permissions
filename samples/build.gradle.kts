plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = permissionsAndroid.compileSdk

    defaultConfig {
        applicationId = permissionsAndroid.applicationIdSample//"com.zhjl37.permission.samples"

        minSdk = permissionsAndroid.minSdkSample
        targetSdk = permissionsAndroid.targetSdk

        versionCode = permissionsAndroid.versionCodeSample
        versionName = permissionsAndroid.versionNameSample

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    sourceSets {
        getByName("main").java.srcDir("src/main/kotlin")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":permissions"))
    //implementation("com.zhjl37.permission:permission:1.0.0")
}
