package com.rowatk.waje.dto.outgoingResponses

import com.rowatk.waje.dto.DTO
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

class ApiResponse(
    val code: Int = 200,
    val messages: List<String>? = Collections.emptyList(),
    val result: DTO? = null,
) {

    val status: String
        get() = if ( code == 200 ) "ok" else "error";

    companion object Factory {
        fun ok(result: DTO?, messages: List<String>?): ApiResponse = ApiResponse(result = result, messages = messages);
        fun badRequest(result: DTO?, messages: List<String>?): ApiResponse = ApiResponse(code = 400, result = result, messages = messages);
        fun notFound(result: DTO?, messages: List<String>?): ApiResponse = ApiResponse(code = 404, result = result, messages = messages);
        fun unauthorized(result: DTO?, messages: List<String>?): ApiResponse = ApiResponse(code = 401, result = result, messages = messages);
        fun internalError(result: DTO?, messages: List<String>?): ApiResponse = ApiResponse(code = 500, result = result, messages = messages);
    }

    fun buildResponse() : Mono<ServerResponse> = ServerResponse.status(code).body(this,ApiResponse::class.java);

}
