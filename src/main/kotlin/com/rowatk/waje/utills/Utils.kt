package com.rowatk.waje.utills

import com.rowatk.waje.models.User
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder

suspend fun getAuthenticatedUser(): User {
    val context = ReactiveSecurityContextHolder.getContext().awaitSingle()
    val user = context.authentication.principal as User
    return user
}
