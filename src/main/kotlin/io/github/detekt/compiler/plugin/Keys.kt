package io.github.detekt.compiler.plugin

import org.jetbrains.kotlin.config.CompilerConfigurationKey
import java.io.File

object Options {

    const val isEnabled: String = "isEnabled"
    const val debug: String = "debug"
    const val config = "config"
    const val baseline: String = "baseline"
    const val useDefaultConfig: String = "useDefaultConfig"
    const val projectPath: String = "projectPath"
}

object Keys {

    val DEBUG = CompilerConfigurationKey.create<Boolean>(Options.debug)
    val IS_ENABLED = CompilerConfigurationKey.create<Boolean>(Options.isEnabled)
    val CONFIG = CompilerConfigurationKey.create<String>(Options.config)
    val BASELINE = CompilerConfigurationKey.create<String>(Options.baseline)
    val USE_DEFAULT_CONFIG = CompilerConfigurationKey.create<String>(Options.useDefaultConfig)
    val PROJECT_PATH = CompilerConfigurationKey.create<File>(Options.projectPath)
}
