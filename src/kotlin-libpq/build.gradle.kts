plugins {
    kotlin("multiplatform")
    `maven-publish`
    signing
}

version = "0.1-SNAPSHOT"
description = "Kotlin bindings for libpq"

repositories {
    mavenCentral()
}

kotlin {
    native {
        val main by compilations.getting
        val pq by main.cinterops.creating

        binaries {
            sharedLib()
        }
    }
}

apply(plugin = "native-publish")
