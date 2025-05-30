// build.gradle.kts (Project: BengkelAppCompose)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // alias(libs.plugins.kotlin.compose) apply false // Compose plugin biasanya sudah dihandle oleh android.application
    id("com.google.dagger.hilt.android") version "2.50" apply false // PASTIKAN VERSI HILT SAMA
}