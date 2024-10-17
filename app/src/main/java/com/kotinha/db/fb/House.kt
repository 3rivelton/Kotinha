package com.kotinha.db.fb

import com.kotinha.model.House


class FBHouse {
    var name: String? = null
    fun toHouse() = House(name!!)
}

fun House.toFBHouse(): FBHouse {
    val fbHouse = FBHouse()
    fbHouse.name = this.nome
    return fbHouse
}