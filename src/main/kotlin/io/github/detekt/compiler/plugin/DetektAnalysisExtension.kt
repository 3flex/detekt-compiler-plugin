package io.github.detekt.compiler.plugin

import io.github.detekt.compiler.plugin.internal.DetektService
import io.github.detekt.compiler.plugin.internal.info
import io.github.detekt.tooling.api.spec.ProcessingSpec
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Path

class DetektAnalysisExtension(
    private val log: MessageCollector,
    private val spec: ProcessingSpec,
    private val projectPath: File? = null // TODO: pass a path around, not a file. Also should never be null
) : AnalysisHandlerExtension {

    override fun analysisCompleted(
        project: Project,
        module: ModuleDescriptor,
        bindingTrace: BindingTrace,
        files: Collection<KtFile>
    ): AnalysisResult? {
        // TODO: Don't hardcode this
        val matcher = FileSystems.getDefault().getPathMatcher("glob:build/generated/**")

        // TODO: Show excluded files in debug output
        val (bad, good) = files.partition {
            val relPath = projectPath!!.toPath()
                .relativize(Path.of(it.originalFile.virtualFile.presentableUrl))

            // TODO: allow multiple globs to be passed through and filter file list based on all of them
            matcher.matches(relPath)
        }

        if (spec.loggingSpec.debug) {
            log.info("$spec")
        }
        log.info("Running detekt on module '${module.name.asString()}'")
        DetektService(log, spec).analyze(good, bindingTrace.bindingContext)
        return null
    }
}
