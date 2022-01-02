package com.rowatk.waje.components.user

import com.rowatk.waje.utills.getAuthenticatedUser
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserHandler(
    private val userService: UserService
) {

    suspend fun findAllUsersHandler(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().json().body(userService.getAll()).awaitSingle()
    }

    suspend fun getMe(request: ServerRequest): ServerResponse {

        val user: User = getAuthenticatedUser()
        val userDto = user.toDto()

        val username = request.principal().awaitSingleOrNull()?.name
//        val user = UserDTO(id = "", username = username!!)

        return ServerResponse.ok().json().bodyValueAndAwait(userDto)
    }
}
