package com.rowatk.waje.utills

import com.rowatk.waje.components.user.User
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.core.context.ReactiveSecurityContextHolder

suspend fun getAuthenticatedUser(): User {
    val context = ReactiveSecurityContextHolder.getContext().awaitSingle()
    return context.authentication.principal as User
}
