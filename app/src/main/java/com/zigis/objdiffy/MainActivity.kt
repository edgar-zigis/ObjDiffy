package com.zigis.objdiffy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.objdiffy.R
import com.zigis.objdiffy.entities.Person
import com.zigis.objdiffy.entities.PersonDiffUtil
import com.zigis.objdiffy.entities.PersonDiffUtilExtensions.calculateDiff

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val person_1 = Person("Lukas", "Sivickas", "Vilnius", "Lithuania", "22")
        val person_2 = Person("Edgar", "Zigis", "Kaunas", "Poland", "30")

        val result = PersonDiffUtil.calculateDiff(person_1, person_2)

        println(result)
    }
}

fun main() {
    val person_1 = Person("Lukas", "Sivickas", "Vilnius", "Lithuania", "22")
    val person_2 = Person("Edgar", "Zigis", "Vilnius", "Lithuania", "30")
    val cachedPerson = null

    val result = PersonDiffUtil.calculateDiff(person_1, person_2)
    println(result)

    val result_b = PersonDiffUtil.calculateDiff(person_1, cachedPerson)
    println(result_b)

    println(person_1.calculateDiff(cachedPerson))
}