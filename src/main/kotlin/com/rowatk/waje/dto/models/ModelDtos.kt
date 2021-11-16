package com.rowatk.waje.dto.models

import com.rowatk.waje.models.User
import org.springframework.security.core.GrantedAuthority

data class UserDTO(
    val id: String,
    val username: String,
    val authorities: MutableCollection<out GrantedAuthority>
)

fun User.toDto() = UserDTO(
    id = id,
    username = username,
    authorities = authorities
)
