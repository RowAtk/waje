package com.rowatk.waje.exceptions

import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalErrorWebExceptionHandler(
    private var g: DefaultErrorAttributes,
    private val applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(
    g, WebProperties.Resources(), applicationContext
) {

    init {
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
    }


    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {

        val exception = getError(request)

        if(exception is ApiException) {
            val apiResponse = exception.getResponse()
            val errorPropertiesMap = getErrorAttributes(
                request,
                ErrorAttributeOptions.defaults()
            )

            errorPropertiesMap["customMessages"] = apiResponse.messages
            errorPropertiesMap["customData"] = apiResponse.result

            return ServerResponse.status(apiResponse.code)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap))
        }

        val errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults())

        errorPropertiesMap["customMessage"] = exception.message
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorPropertiesMap))
    }


}