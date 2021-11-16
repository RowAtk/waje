package com.rowatk.waje.handlers

import com.mongodb.internal.connection.Server
import com.rowatk.waje.dto.incoming.LoginRequest
import com.rowatk.waje.dto.models.UserDTO
import com.rowatk.waje.models.User
import com.rowatk.waje.security.JwtSupport
import com.rowatk.waje.services.UserAuthService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException
import java.security.Principal
import java.util.*


@Component
class AuthHandler(
    private val jwtSupport: JwtSupport,
    private val encoder: PasswordEncoder,
    private val users: ReactiveUserDetailsService,
    private val userAuthService: UserAuthService) {

    @FlowPreview
    suspend fun loginHandler(request: ServerRequest) : ServerResponse {
        val loginRequest = request.awaitBody<LoginRequest>();
//        val user = userAuthService.findByUsername(loginRequest.username) ?: throw NullPointerException()
        val user = users.findByUsername(loginRequest.username).awaitSingleOrNull()

        user?.let {
            if(encoder.matches(loginRequest.password, it.password)) {
                return ServerResponse.ok().json().bodyValueAndAwait(JWT(jwtSupport.generate(it.username).value))
            }
        }
//        return ServerResponse.ok().json().bodyValueAndAwait(user)

        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)

    }

    suspend fun getMe(request: ServerRequest): ServerResponse {
        val username = request.principal().awaitSingleOrNull()?.name
        val user = UserDTO(id = "", username = username!!, authorities = Collections.emptyList())

        return ServerResponse.ok().json().bodyValueAndAwait(user)
    }
}

data class JWT(val token: String)
