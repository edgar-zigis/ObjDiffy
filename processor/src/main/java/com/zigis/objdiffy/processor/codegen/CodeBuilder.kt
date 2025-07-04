package com.zigis.objdiffy.processor.codegen

import com.squareup.kotlinpoet.TypeSpec

interface CodeBuilder {
    fun build(): TypeSpec
}