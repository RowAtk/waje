package com.rowatk.waje.handlers

import kotlinx.coroutines.FlowPreview
import org.intellij.lang.annotations.Flow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
import org.xml.sax.ErrorHandler


@Configuration
class RouteConfig {

    @FlowPreview
    @Bean
    fun authRouter(authHandler: AuthHandler) = coRouter {
        "/auth".nest {
            POST("/login", authHandler::loginHandler)
        }
    }

    @FlowPreview
    @Bean
    fun userRouter(userHandler: UserHandler) = coRouter {
        "/users".nest {
            GET("/me", userHandler::getMe)
            GET("", userHandler::findAllUsersHandler)
        }
    }


    @FlowPreview
    @Bean
    fun miscRouter(miscHandler: MiscHandler) = coRouter {
        POST("/error", miscHandler::throwError)
    }
}
