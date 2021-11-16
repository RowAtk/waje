package com.rowatk.waje.handlers

import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter


@Configuration
class RouteConfig {

    @FlowPreview
    @Bean
    fun authRouter(authHandler: AuthHandler) = coRouter {
        "/auth".nest {
            POST("/login", authHandler::loginHandler)
            GET("/me", authHandler::getMe)
        }
    }
}
