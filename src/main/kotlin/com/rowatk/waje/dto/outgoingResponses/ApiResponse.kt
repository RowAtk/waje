package com.rowatk.waje.dto.outgoingResponses

import com.rowatk.waje.dto.DTO
import com.rowatk.waje.dto.EmptyDTO
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.publisher.Mono
import java.util.*

class ApiResponse(
    val code: Int = 200,
    val messages: MutableList<String>? = Collections.emptyList(),
    val result: DTO? = null,
    var additionalInfo: Any? = null
) {

    val status: String
        get() = if ( code == 200 ) "ok" else "error";

    companion object Factory {
        fun ok(result: DTO?, messages: MutableList<String>?): ApiResponse = ApiResponse(result = result, messages = messages);
        fun badRequest(result: DTO?, messages: MutableList<String>?): ApiResponse = ApiResponse(code = 400, result = result, messages = messages);
        fun notFound(result: DTO?, messages: MutableList<String>?): ApiResponse = ApiResponse(code = 404, result = result, messages = messages);
        fun unauthorized(result: DTO?, messages: MutableList<String>?): ApiResponse = ApiResponse(code = 401, result = result, messages = messages);
        fun internalError(result: DTO?, messages: MutableList<String>?): ApiResponse = ApiResponse(code = 500, result = result, messages = messages);
    }

    suspend fun buildResponse() : ServerResponse = ServerResponse
        .status(code)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValueAndAwait(this)

    fun buildMonoResponse() : Mono<ServerResponse> = ServerResponse
        .status(code)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(this)
}
