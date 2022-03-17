import java.io.FileInputStream
import java.util.*

plugins {
	kotlin("multiplatform")
	`maven-publish`
	signing
}

fun loadPropertiesFile(path: String): Properties? {
	val propertiesFile = rootProject.file(path)

	return if (propertiesFile.exists()) {
		Properties().apply {
			load(FileInputStream(propertiesFile))
		}
	} else {
		println("No such properties file: $path")
		null
	}
}

val releasesRepoUrl =
	uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")

val snapshotsRepoUrl =
	uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")

kotlin {
	publishing {

		publications {
			publications.filterIsInstance<MavenPublication>().forEach {
				it.artifactId = it.artifactId.replace("-kt", "")

				it.pom {
					name.set(project.name)
					description.set(project.description)
					url.set("https://github.com/sobotkami/pq-kt")
					licenses {
						license {
							name.set("GNU Lesser General Public License")
							url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
						}
					}
					developers {
						developer {
							id.set("sobotkami")
							name.set("Miroslav Sobotka")
							email.set("sobotka.mira@gmail.com")
						}
					}
					scm {
						connection.set("scm:git:git://github.com/sobotkami/pq-kt.git")
						connection.set("scm:git:ssh://github.com/sobotkami/pq-kt.git")
						url.set("https://github.com/sobotkami/pq-kt")
					}
				}
			}

			repositories {
				maven {
					url =
						if (version.toString().endsWith("SNAPSHOT")) {
							snapshotsRepoUrl
						} else {
							releasesRepoUrl
						}

					val mavenProperties =
						loadPropertiesFile("./maven.properties")

					if (mavenProperties != null) {
						credentials {
							username =
								mavenProperties.getProperty("SONATYPE_USERNAME")
							password =
								mavenProperties.getProperty("SONATYPE_PASSWORD")
						}
					} else {
						println("No `maven.properties` file")
					}
				}
			}
		}
	}

	val signingProperties = loadPropertiesFile("./signing.properties")

	if (signingProperties != null) {
		project.ext.set(
			"signing.gnupg.keyName",
			signingProperties["signing.gnupg.keyName"]
		)
		signing {
			useGpgCmd()
			sign(publishing.publications)
		}
	} else {
		println("No `signing.properties` file")
	}
}

