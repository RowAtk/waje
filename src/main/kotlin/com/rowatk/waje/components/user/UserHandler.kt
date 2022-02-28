package com.rowatk.waje.components.user

import com.rowatk.waje.dto.incomingRequests.RegisterRequest
import com.rowatk.waje.dto.outgoingResponses.ApiResponse
import com.rowatk.waje.dto.outgoingResponses.GeneralResponse
import com.rowatk.waje.utills.getAuthenticatedUser
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserHandler(
    private val userService: UserService
) {

    suspend fun registerHandler(request: ServerRequest): ServerResponse {
        val registerRequest = request.awaitBody<RegisterRequest>()

        return ApiResponse.ok().result(userService.addUser(registerRequest)).buildResponse()
//        return ServerResponse.ok().json().bodyValueAndAwait(userService.addUser(registerRequest))
    }

    @FlowPreview
    suspend fun findAllUsersHandler(request: ServerRequest): ServerResponse {
        val r = GeneralResponse("users", userService.getAll().toList())
        return ApiResponse.ok()
            .result(r)
            .buildResponse()
//        return ServerResponse.ok().json().bodyAndAwait(userService.getAll())
    }

    suspend fun getMe(request: ServerRequest): ServerResponse {

        val user: User = getAuthenticatedUser()
        val userDto = user.toDto()
        val key = userDto.getKey()
        val sample = ApiResponse.ok().result(userDto).result

        val username = request.principal().awaitSingleOrNull()?.name
//        val user = UserDTO(id = "", username = username!!)

        return ApiResponse.ok()
            .result(userDto)
            .buildResponse()
//        return ServerResponse.ok().json().bodyValueAndAwait(userDto)
    }
}
