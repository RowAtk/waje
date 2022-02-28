package com.rowatk.waje.dto

interface ModelDTO<T> : DTO {

    fun toModel() : T;

    override fun getKey(): String {
        val dtoName: String = this.javaClass.simpleName
        val strippedName = dtoName.removeSuffix("DTO")
        return strippedName.replaceFirstChar { it.lowercaseChar() }
    }
}

class EmptyDTO: DTO
