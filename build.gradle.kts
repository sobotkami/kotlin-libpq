plugins {
	kotlin("multiplatform")
}

allprojects {
	group = "io.github.sobotkami.postgresql"

	repositories {
		mavenCentral()
	}
}

kotlin {
	linuxX64()
}

