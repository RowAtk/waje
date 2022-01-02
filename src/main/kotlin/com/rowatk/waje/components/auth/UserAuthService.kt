package com.rowatk.waje.components.auth

import com.rowatk.waje.dto.models.UserDTO
import com.rowatk.waje.dto.models.toDto
import com.rowatk.waje.components.user.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserAuthService {

    @Autowired
    private lateinit var userRepo: UserRepo

    suspend fun findByUsername(username: String) : UserDTO? {
        val user = userRepo.findByUsername(username).block()

        return user?.toDto();
    }
}
