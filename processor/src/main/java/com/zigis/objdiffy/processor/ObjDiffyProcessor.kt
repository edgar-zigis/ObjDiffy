package com.zigis.objdiffy.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.zigis.objdiffy.processor.codegen.DiffResultCodeBuilder
import com.zigis.objdiffy.processor.codegen.DiffUtilCodeBuilder
import com.zigis.objdiffy.processor.codegen.DiffUtilExtensionsCodeBuilder
import com.zigis.objdiffy.processor.models.ModelData
import com.zigis.objdiffy.processor.models.ModelField
import com.squareup.kotlinpoet.FileSpec
import com.zigis.objdiffy.annotations.DiffEntity
import java.io.OutputStreamWriter

class ObjDiffyProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val logger = environment.logger
    private val codeGenerator = environment.codeGenerator

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(DiffEntity::class.qualifiedName!!)
        val ret = mutableListOf<KSAnnotated>()

        symbols.forEach { symbol ->
            if (symbol is KSClassDeclaration) {
                val className = symbol.simpleName.asString()
                val packageName = symbol.packageName.asString()

                val fields = symbol.getAllProperties().mapNotNull { prop ->
                    val name = prop.simpleName.asString()
                    val type = prop.type.resolve().declaration.qualifiedName?.asString() ?: return@mapNotNull null
                    ModelField(name)
                }.toList()

                val model = ModelData(packageName, className, fields)

                val builders = listOf(
                    DiffResultCodeBuilder(model),
                    DiffUtilCodeBuilder(model),
                    DiffUtilExtensionsCodeBuilder(model)
                )

                builders.forEach { builder ->
                    val typeSpec = builder.build()
                    val fileSpec = FileSpec.builder(model.packageName, typeSpec.name!!)
                        .addType(typeSpec)
                        .build()
                    val file = codeGenerator.createNewFile(
                        Dependencies(false, symbol.containingFile!!),
                        fileSpec.packageName,
                        fileSpec.name
                    )
                    OutputStreamWriter(file, Charsets.UTF_8).use {
                        fileSpec.writeTo(it)
                    }
                }
            } else {
                ret.add(symbol)
            }
        }

        return ret
    }
}

class ObjDiffyProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ObjDiffyProcessor(environment)
    }
}
