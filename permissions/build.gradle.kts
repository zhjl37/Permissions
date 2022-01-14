plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = permissionsAndroid.compileSdk

    defaultConfig {
        minSdk = permissionsAndroid.minSdk
        targetSdk = permissionsAndroid.targetSdk
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(deps.androidx.appCompat)
    api(deps.androidx.materialDesign)
    //api(deps.androidx.recyclerView)

    implementation(deps.libs.sdpAndroid)
    implementation(deps.libs.sspAndroid)
}

allprojects {
    plugins.withId("com.vanniktech.maven.publish") {
        mavenPublish {
            sonatypeHost = com.vanniktech.maven.publish.SonatypeHost.S01
        }
    }
}
