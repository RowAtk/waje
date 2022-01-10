package com.rowatk.waje.handlers

import com.rowatk.waje.exceptions.ApiException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

data class ErrorRequest(
    val code: Int,
    val message: String,
    val data: Any?
)

@Component
class MiscHandler {


    suspend fun throwError(request: ServerRequest): ServerResponse {

        val errorRequest = request.bodyToMono(ErrorRequest::class.java).awaitSingle()
        throw ApiException(errorRequest.code, errorRequest.message);
    }
}
