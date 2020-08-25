import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.io.ByteArrayOutputStream

val kotlinVersion: String by project
val kotlinCompilerChecksum: String by project

val verifyKotlinCompilerDownload by tasks.creating(Verify::class) {
    src(file("$rootDir/.kotlinc/kotlin-compiler-$kotlinVersion.zip"))
    algorithm("SHA-256")
    checksum(kotlinCompilerChecksum)
    outputs.upToDateWhen { true }
}

val downloadKotlinCompiler by tasks.creating(Download::class) {
    src("https://github.com/JetBrains/kotlin/releases/download/v$kotlinVersion/kotlin-compiler-$kotlinVersion.zip")
    dest(file("$rootDir/.kotlinc/kotlin-compiler-$kotlinVersion.zip"))
    overwrite(false)
    finalizedBy(verifyKotlinCompilerDownload)
}

val unzipKotlinCompiler by tasks.creating(Copy::class) {
    dependsOn(downloadKotlinCompiler)
    from(zipTree(downloadKotlinCompiler.dest))
    into(file("$rootDir/.kotlinc/$kotlinVersion"))
}

val testPluginKotlinc by tasks.creating(RunTestExecutable::class) {
    dependsOn(unzipKotlinCompiler, tasks.named<ShadowJar>("shadowJar"))
    executable(file("${unzipKotlinCompiler.destinationDir}/kotlinc/bin/kotlinc"))
    args(
        listOf(
            "$rootDir/src/test/resources/hello.kt",
            "-Xplugin=${tasks.named<ShadowJar>("shadowJar").get().archiveFile.get().asFile.absolutePath}",
            "-P",
            "plugin:detekt-compiler-plugin:debug=true"
        )
    )
    errorOutput = ByteArrayOutputStream()
    // dummy path - required for RunTestExecutable task but doesn't do anything
    outputDir = file("$buildDir/tmp/kotlinc")

    doLast {
        if (!errorOutput.toString().contains("MagicNumber - [x] at hello.kt")) {
            throw GradleException(
                "kotlinc $kotlinVersion run with compiler plugin did not find MagicNumber issue as expected"
            )
        }
        (this as RunTestExecutable).execResult!!.assertNormalExitValue()
    }
}
