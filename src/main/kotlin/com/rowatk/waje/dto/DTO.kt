package com.rowatk.waje.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

interface DTO : Serializable {

    @JsonIgnore
    fun getKey() : String? { return null };
}
