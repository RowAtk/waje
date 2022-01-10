package com.rowatk.waje.utills

import com.rowatk.waje.components.user.User
import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.ReactiveSecurityContextHolder

suspend fun getAuthenticatedUser(): User {
    val context = ReactiveSecurityContextHolder.getContext().awaitSingle()
    return context.authentication.principal as User
}

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

fun isEqual(id1: ObjectId, id2: ObjectId): Boolean {
    return id1.toHexString() == id2.toHexString()
}
