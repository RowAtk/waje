package com.rowatk.waje.components.user

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepo : ReactiveMongoRepository<User, ObjectId> {

    suspend fun findByUsername(username: String): Mono<User?>
}
