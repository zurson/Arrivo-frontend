import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
}


allprojects {
    configurations.all {
        exclude(group = "com.google.android.gms", module = "play-services-maps")
    }
}