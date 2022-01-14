// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(deps.gradlePlugin)
        classpath(kotlin(deps.kotlin.gradlePlugin, versions.kotlin))
        classpath(deps.plugins.mavenPublish)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

