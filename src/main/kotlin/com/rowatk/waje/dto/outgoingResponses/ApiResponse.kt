package com.rowatk.waje.dto.outgoingResponses

import com.rowatk.waje.dto.DTO
import com.rowatk.waje.dto.EmptyDTO
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import reactor.core.publisher.Mono
import java.util.*

class ApiResponse(
    val code: Int = 200,
    val messages: MutableList<String> = Collections.emptyList(),
    result: DTO = EmptyDTO(),
    var additionalInfo: Any? = null
) {

    val status: String
        get() = if (code == 200) "ok" else "error";

    var result: Any = result
    get() {
        if (field is DTO && (field as DTO).getKey() != null) {
            val hashMap: MutableMap<String, DTO> = LinkedHashMap()
            hashMap[(field as DTO).getKey()!!] = field as DTO
            return hashMap
        }
        return field
    }

    companion object Factory {
        fun ok(result: DTO, messages: MutableList<String>): ApiResponse =
            ApiResponse(result = result, messages = messages);

        fun badRequest(result: DTO, messages: MutableList<String>): ApiResponse =
            ApiResponse(code = 400, result = result, messages = messages);

        fun notFound(result: DTO, messages: MutableList<String>): ApiResponse =
            ApiResponse(code = 404, result = result, messages = messages);

        fun unauthorized(result: DTO, messages: MutableList<String>): ApiResponse =
            ApiResponse(code = 401, result = result, messages = messages);

        fun internalError(result: DTO, messages: MutableList<String>): ApiResponse =
            ApiResponse(code = 500, result = result, messages = messages);

        fun ok(): ApiResponse = ApiResponse()
        fun code(code: Int): ApiResponse = ApiResponse(code = code)
    }

    fun message(message: String): ApiResponse {
        messages.add(message)
        return this
    }

    fun result(result: DTO): ApiResponse {
        this.result = result
        return this
    }

    fun result(dataKey: String, value: Any): ApiResponse {
        this.result = GeneralResponse(dataKey, value)
        return this
    }

//    @JsonGetter
//    fun getResult(): Any {
//        var returnnValue: Any = result
//        if (result.getKey() != null) {
//            val hashMap: MutableMap<String, DTO> = LinkedHashMap()
//            hashMap[result.getKey()!!] = result
//            returnnValue = hashMap
//        }
//        return returnnValue
//    }

    suspend fun buildResponse(): ServerResponse = ServerResponse
        .status(code)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValueAndAwait(this)

    fun buildMonoResponse(): Mono<ServerResponse> = ServerResponse
        .status(code)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(this)
}
