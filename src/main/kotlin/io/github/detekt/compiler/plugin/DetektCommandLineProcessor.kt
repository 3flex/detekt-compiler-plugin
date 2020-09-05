package io.github.detekt.compiler.plugin

import io.github.detekt.gradle.DETEKT_COMPILER_PLUGIN
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.io.File

class DetektCommandLineProcessor : CommandLineProcessor {

    override val pluginId: String = DETEKT_COMPILER_PLUGIN

    override val pluginOptions: Collection<AbstractCliOption> = listOf(
        CliOption(
            Options.config,
            "<path|paths>",
            "Comma separated paths to detekt config files.",
            false
        ),
        CliOption(
            Options.baseline,
            "<path>",
            "Path to a detekt baseline file.",
            false
        ),
        CliOption(
            Options.debug,
            "<true|false>",
            "Print debug messages.",
            false
        ),
        CliOption(
            Options.isEnabled,
            "<true|false>",
            "Should detekt run?",
            false
        ),
        CliOption(
            Options.projectPath,
            "<path>",
            "The root project directory",
            false
        ),
        // TODO: add CLI flags for globbing, allowing multiple filters e.g. -filter build/generated/** -filter build/whatever/**
        CliOption(
            Options.useDefaultConfig,
            "<true|false>",
            "Use the default detekt config as baseline.",
            false
        )
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when (option.optionName) {
            Options.baseline -> configuration.put(Keys.BASELINE, value)
            Options.config -> configuration.put(Keys.CONFIG, value)
            Options.debug -> configuration.put(Keys.DEBUG, value.toBoolean())
            Options.isEnabled -> configuration.put(Keys.IS_ENABLED, value.toBoolean())
            Options.useDefaultConfig -> configuration.put(Keys.USE_DEFAULT_CONFIG, value)
            Options.projectPath -> configuration.put(Keys.PROJECT_PATH, File(value))
        }
    }
}
