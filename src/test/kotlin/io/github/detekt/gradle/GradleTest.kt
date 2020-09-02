package io.github.detekt.gradle

import org.assertj.core.api.Assertions.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File

object GradleTest: Spek({
    val projectDir = createTempDir().apply { deleteOnExit() }
    val runner = GradleRunner.create()
        .withProjectDir(projectDir)
        .forwardOutput()
        .withPluginClasspath()

    describe("describe") {
        File(projectDir, "build.gradle").writeText("""
            buildscript {
                repositories {
                    mavenCentral()
                }
                dependencies {
                    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0"
                    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.4.0"
                }
            }
            plugins {
                id 'org.jetbrains.kotlin.jvm' version '1.4.0'
                id "io.github.detekt.gradle.compiler-plugin"
            }
            apply plugin: "kotlin"
            apply plugin: "detekt-compiler-plugin"
//            repositories {
//                mavenCentral()
//            }
            detekt {
                buildUponDefaultConfig = true
            }
        """.trimIndent())

        File(projectDir, "/src/main/kotlin").mkdirs()

        File(projectDir, "/src/main/kotlin/hello.kt").writeText("""
            class KClass {
                fun foo() {
                    val x = 3
                    println(x)
                }
            }
        """.trimIndent())

        it("runs without error") {
            val result = runner.withArguments("compileKotlin").build()

            assertThat(result.task(":compileKotlin")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        }
    }
})
