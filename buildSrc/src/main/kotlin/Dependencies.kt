@file:Suppress("ClassName")

object deps {
    const val gradlePlugin = "com.android.tools.build:gradle:${versions.gradle}"

    object plugins {
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val mavenPublish = "com.vanniktech:gradle-maven-publish-plugin:${versions.mavenPublish}"
        const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${versions.dokka}"
    }

    object kotlin {
        const val gradlePlugin = "gradle-plugin"
        const val stdlib = "stdlib-jdk7"
    }

    object androidx {
        const val appCompat = "androidx.appcompat:appcompat:${versions.appCompat}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${versions.recyclerView}"
        const val materialDesign = "com.google.android.material:material:${versions.materialDesign}"
    }

    object libs {
        const val permissions = "com.zhjl37.permission"

        const val sdpAndroid = "com.intuit.sdp:sdp-android:${versions.sdpAndroid}"
        const val sspAndroid = "com.intuit.ssp:ssp-android:${versions.sspAndroid}"
    }
}
