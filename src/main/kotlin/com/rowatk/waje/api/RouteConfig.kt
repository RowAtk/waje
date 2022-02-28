package com.rowatk.waje.api

import com.rowatk.waje.components.company.CompanyHandler
import com.rowatk.waje.components.user.UserHandler
import com.rowatk.waje.components.auth.AuthHandler
import com.rowatk.waje.components.invoice.InvoiceHandler
import com.rowatk.waje.components.misc.MiscHandler
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

    @Bean
    @FlowPreview
    fun invoiceRouter(invoiceHandler: InvoiceHandler) = coRouter {
        "/invoice".nest {
            GET("", invoiceHandler::findUserInvoicesHandler)
            POST("", invoiceHandler::createInvoice)
            DELETE("/{id}", invoiceHandler::deleteInvoice)
        }
    }


    @FlowPreview
    @Bean
    fun miscRouter(miscHandler: MiscHandler) = coRouter {
        POST("/error", miscHandler::throwError)
    }
}
