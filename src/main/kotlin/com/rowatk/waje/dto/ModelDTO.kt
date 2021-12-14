package com.rowatk.waje.dto

interface ModelDTO<T> : DTO {

    fun toModel() : T;
}
