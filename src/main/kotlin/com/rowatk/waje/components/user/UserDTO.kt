package com.rowatk.waje.components.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.rowatk.waje.dto.ModelDTO

data class UserDTO(
    val id: String,
    val username: String,
    @JsonIgnore
    val password: String,
//    val authorities: MutableCollection<out GrantedAuthority>
) : ModelDTO<User> {
    override fun toModel(): User = User(id = id, username = username)
}

fun User.toDto() = UserDTO(
    id = id,
    username = username,
    password = password,
//    authorities = authorities
)
