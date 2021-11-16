package com.rowatk.waje.services

import com.rowatk.waje.dto.models.UserDTO
import com.rowatk.waje.dto.models.toDto
import com.rowatk.waje.repos.user.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserAuthService {

    @Autowired
    private lateinit var userRepo: UserRepo

    suspend fun findByUsername(username: String) : UserDTO? {
        val user = userRepo.findByUsername(username).block()

        return user?.toDto();
    }
}
