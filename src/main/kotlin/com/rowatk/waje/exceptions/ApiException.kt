package com.rowatk.waje.exceptions

import com.rowatk.waje.dto.DTO
import com.rowatk.waje.dto.EmptyDTO
import com.rowatk.waje.dto.outgoingResponses.ApiResponse
import java.lang.RuntimeException
import java.util.*

open class ApiException(
    private val code: Int? = 500,
    override val message: String? = "",
    private val result: DTO? = EmptyDTO()
) : RuntimeException(message) {

    fun getResponse() : ApiResponse {
        return ApiResponse(code!!, Collections.singletonList(message!!), result);
    }
}
