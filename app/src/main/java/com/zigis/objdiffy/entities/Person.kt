package com.zigis.objdiffy.entities

import com.zigis.objdiffy.annotations.DiffEntity

@DiffEntity
data class Person(
    val firstName: String,
    val lastName: String,
    val address: String,
    val country: String,
    val age: String,
)