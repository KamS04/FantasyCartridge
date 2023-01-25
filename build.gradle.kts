import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "ca.kam.fantasycartridge"
version = "1.0"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

val jar: Jar by tasks
jar.apply {
    manifest.attributes.apply {
        put("Cartridge", "ca.kam.fantasycartridge.MagicCartridge")
    }

    exclude("ca/kam/fantasycartridge/test/**")
    exclude("rom", "cmeta", "code")
}

fun DependencyHandlerScope.compileRuntimeJarLibs(vararg jarNames: String) {
    val runtimeFiles = jarNames.map { "C:\\home\\code\\Kotlin\\$it\\build\\libs\\$it-1.0.jar" }
    val compileFiles = jarNames.map { "C:\\home\\code\\Kotlin\\$it\\build\\libs\\$it-no-deps-1.0.jar" }

    compileOnly(files(compileFiles))
    runtimeOnly(files(runtimeFiles))
}

dependencies {
    compileRuntimeJarLibs("VMHardwareLibraries")
}
