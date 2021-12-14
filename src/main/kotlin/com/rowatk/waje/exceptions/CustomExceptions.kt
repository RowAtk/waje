package com.rowatk.waje.exceptions

import com.rowatk.waje.dto.responses.ApiResponse
import java.lang.RuntimeException

open class RecordNotFoundException(private val key: String, private val identifier: String) :
    ApiException(code = 404, message = "$key with identifier: $identifier was not found")

class UserNotFoundException(private val identifier: String) : RecordNotFoundException("User", identifier)

class InvalidCredentialsException() : ApiException(401, null)
