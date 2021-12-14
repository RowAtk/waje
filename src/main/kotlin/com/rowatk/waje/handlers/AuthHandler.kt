package com.rowatk.waje.handlers

import com.mongodb.internal.connection.Server
import com.rowatk.waje.dto.incoming.LoginRequest
import com.rowatk.waje.dto.models.UserDTO
import com.rowatk.waje.dto.responses.ApiResponse
import com.rowatk.waje.exceptions.ApiException
import com.rowatk.waje.exceptions.InvalidCredentialsException
import com.rowatk.waje.exceptions.UserNotFoundException
import com.rowatk.waje.models.User
import com.rowatk.waje.security.JwtSupport
import com.rowatk.waje.services.UserAuthService
import com.rowatk.waje.services.UserService
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
    private val userService: UserService,
    private val userAuthService: UserAuthService) {

    @FlowPreview
    suspend fun loginHandler(request: ServerRequest) : ServerResponse {
        val loginRequest = request.awaitBody<LoginRequest>();
//        val user = userAuthService.findByUsername(loginRequest.username) ?: throw NullPointerException()
//        val user = users.findByUsername(loginRequest.username).awaitSingleOrNull()

        try {
            val user = userService.findByUsername(loginRequest.username).awaitSingle()
//            TODO: implement encode
//            if(encoder.matches(loginRequest.password, user.password)) {
            if(loginRequest.password == user.password) {
                return ServerResponse.ok().json().bodyValueAndAwait(JWT(jwtSupport.generate(user.username).value))
            }

            throw InvalidCredentialsException()

        } catch (ex: ApiException) {
            throw ApiException(code = 401, message = "Incorrect username and/or password")
        } catch (ex: Exception) {
            throw ex
        }

    }
}

data class JWT(val token: String)
