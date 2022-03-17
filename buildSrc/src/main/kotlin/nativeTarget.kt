import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.konan.target.HostManager

fun KotlinMultiplatformExtension.native(
  configure: KotlinNativeTargetWithHostTests.() -> Unit = {}
) {
  when {
    HostManager.hostIsMingw -> mingwX64("native")
    HostManager.hostIsLinux -> linuxX64("native")
    HostManager.hostIsMac -> if (HostManager.hostArch() == "arm64") {
      macosArm64("native")
    } else {
      macosX64("native")
    }
    else -> error("Unsupported Host OS: ${HostManager.hostOs()}")
  }.apply(configure)
}

