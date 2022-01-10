package com.rowatk.waje.api

import com.rowatk.waje.components.company.CompanyHandler
import com.rowatk.waje.components.user.UserHandler
import com.rowatk.waje.components.auth.AuthHandler
import com.rowatk.waje.handlers.MiscHandler
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
        }
    }

    @FlowPreview
    @Bean
    fun userRouter(userHandler: UserHandler) = coRouter {

        "/users".nest {
            POST("/register", userHandler::registerHandler)
            GET("/me", userHandler::getMe)
            GET("", userHandler::findAllUsersHandler)
        }
    }

    @Bean
    fun companyRouter(companyHandler: CompanyHandler) = coRouter {
        "/company".nest {
            POST("", companyHandler::addCompanyHandler)
            PUT("/{id}", companyHandler::editCompanyHandler)
            DELETE("/{id}", companyHandler::deleteCompanyHandler)
        }
    }


    @FlowPreview
    @Bean
    fun miscRouter(miscHandler: MiscHandler) = coRouter {
        POST("/error", miscHandler::throwError)
    }
}
