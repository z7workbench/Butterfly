// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.1.0-beta04" apply false
    id("com.android.library") version "7.1.0-beta04" apply false
    kotlin("android") version "1.6.0" apply false
    id("com.google.devtools.ksp") version "1.6.0-1.0.1"
//    id("org.jetbrains.kotlin.android")

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}