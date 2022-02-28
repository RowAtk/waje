package com.rowatk.waje.exceptions

import com.rowatk.waje.dto.DTO
import com.rowatk.waje.dto.EmptyDTO
import com.rowatk.waje.dto.outgoingResponses.ApiResponse
import com.rowatk.waje.utills.LoggerDelegate
import java.lang.RuntimeException
import java.util.*

open class ApiException(
    private val code: Int? = 500,
    override val message: String? = "",
    private val result: DTO = EmptyDTO()
) : RuntimeException(message) {

    companion object {
        val log by LoggerDelegate()
    }

    init {
        log.error("ApiException: {}", toString())
    }

    fun getResponse() : ApiResponse {
        return ApiResponse(code!!, Collections.singletonList(message!!), result);
    }

    final override fun toString(): String {
        return "ApiException(code=$code, message=$message, result=$result)"
    }


}
