package com.rowatk.waje.components.auth

import com.rowatk.waje.dto.incomingRequests.LoginRequest
import com.rowatk.waje.exceptions.ApiException
import com.rowatk.waje.exceptions.InvalidCredentialsException
import com.rowatk.waje.security.JwtSupport
import com.rowatk.waje.components.user.UserDTO
import com.rowatk.waje.components.user.UserService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*


@Component
class AuthHandler(
    private val jwtSupport: JwtSupport,
    private val encoder: PasswordEncoder,
    private val userService: UserService,
    private val userAuthService: UserAuthService
) {

    @FlowPreview
    suspend fun loginHandler(request: ServerRequest) : ServerResponse {
        val loginRequest = request.awaitBody<LoginRequest>();
//        val user = userAuthService.findByUsername(loginRequest.username) ?: throw NullPointerException()

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
