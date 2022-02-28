package com.rowatk.waje.utills

import com.rowatk.waje.components.counters.CountersService
import com.rowatk.waje.components.user.User
import com.rowatk.waje.exceptions.ApiException
import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder
import reactor.core.publisher.Mono
import kotlin.coroutines.CoroutineContext

fun extractUserFromContext(context: SecurityContext?): User {
    return context?.authentication?.principal as User? ?: throw ApiException(message = "not authenticated")
}

suspend fun getAuthenticatedUser(): User {
    val context = ReactiveSecurityContextHolder.getContext().awaitSingle()
    return extractUserFromContext(context)
}

fun getSecurityContext(): Mono<SecurityContext> {
    return ReactiveSecurityContextHolder.getContext()
}

fun getAuthenticatedUserBlocking(): User {
    val context = ReactiveSecurityContextHolder.getContext();
    var user: User

    runBlocking {
        user = extractUserFromContext(context.awaitSingle())

//        coroutineScope {
//            user = extractUserFromContext(context.awaitSingle())
//        }
    }

//    CoroutineScope().launch {
//        user = extractUserFromContext(context.awaitSingle())
//    }

//    runBlocking {
//        coroutineScope {
//
//        }(currentCoroutineContext()) {
//            user = extractUserFromContext(context.awaitSingle())
//        }
//    }

    return user;
}

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

fun isEqual(id1: ObjectId, id2: ObjectId): Boolean {
    return id1.toHexString() == id2.toHexString()
}
