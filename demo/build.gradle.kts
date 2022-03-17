plugins {
    kotlin("multiplatform")
    // kotlin("plugin.serialization") version "1.6.10"
}

kotlin {
    native {
        val main by compilations.getting
        binaries {
            executable()
        }
    }

    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation(project(":src:kotlin-libpq"))
                // implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
            }
        }
    }
}
