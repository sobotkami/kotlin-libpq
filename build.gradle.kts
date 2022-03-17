plugins {
	kotlin("multiplatform")
}

allprojects {
	group = "org.postgresql"

	repositories {
		mavenCentral()
	}
}

kotlin {
	linuxX64()
}

