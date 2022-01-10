package com.rowatk.waje.components.user

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepo : ReactiveMongoRepository<User, String> {

    suspend fun findByUsername(username: String): Mono<User?>
}
