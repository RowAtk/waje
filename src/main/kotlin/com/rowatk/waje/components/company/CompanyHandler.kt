package com.rowatk.waje.components.company

import com.rowatk.waje.components.user.UserService
import com.rowatk.waje.dto.outgoingResponses.ApiResponse
import com.rowatk.waje.dto.outgoingResponses.ObjectIdResponse
import com.rowatk.waje.exceptions.ApiException
import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class CompanyHandler(
    private val userService: UserService
) {

    suspend fun addCompanyHandler(request: ServerRequest): ServerResponse {
        val company = request.awaitBody<CompanyDTO>()
        val owned: Boolean = extractOwnedParam(request)

        return ApiResponse(result = ObjectIdResponse(userService.addUserCompany(company, owned).awaitSingle()))
            .buildResponse()
    }

    suspend fun editCompanyHandler(request: ServerRequest): ServerResponse {
        val company = request.awaitBody<CompanyDTO>()
        val id: ObjectId = ObjectId(request.pathVariable("id"))
        val owned: Boolean = extractOwnedParam(request)

        return ApiResponse(result = ObjectIdResponse(userService.editUserCompany(id,company, owned).awaitSingle()))
            .buildResponse()
    }

    suspend fun deleteCompanyHandler(request: ServerRequest): ServerResponse {
        val id: ObjectId = ObjectId(request.pathVariable("id"))
        val owned: Boolean = extractOwnedParam(request)

        return ApiResponse(result = ObjectIdResponse(userService.editUserCompany(id, null, owned).awaitSingle()))
            .buildResponse()
    }

    private fun extractOwnedParam(request: ServerRequest): Boolean {
        return request.queryParam("owned")
            .map { it -> it.toBoolean() }
            .orElseThrow { ApiException(400, "no owned flag") }
    }
}
