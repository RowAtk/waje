package com.rowatk.waje.dto.outgoingResponses

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.rowatk.waje.dto.DTO

class GeneralResponse(
    @JsonIgnore
    private val dataKey: String,
    @JsonIgnore
    private val value: Any
) : DTO {

    @get:JsonAnyGetter
    val values: MutableMap<String, Any> = hashMapOf(dataKey to value)
}
