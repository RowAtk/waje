package com.rowatk.waje.components.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.rowatk.waje.components.company.CompanyDTO
import com.rowatk.waje.components.company.toDto
import com.rowatk.waje.dto.ModelDTO
import org.bson.types.ObjectId

data class UserDTO(
    val id: ObjectId,
    val username: String,
    val email: String,
    @JsonIgnore
    val password: String,
    val ownedCompanies: List<CompanyDTO>,
    val associatedCompanies: List<CompanyDTO>
//    val authorities: MutableCollection<out GrantedAuthority>
) : ModelDTO<User> {
    override fun toModel(): User = User(
        id = id,
        username = username,
        password = password,
        email = email,
        ownedCompanies = ownedCompanies.map { it.toModel() },
        associatedCompanies =  associatedCompanies.map { it.toModel() }
    )
}

fun User.toDto() = UserDTO(
    id = id,
    username = username,
    email = email,
    password = password,
    ownedCompanies = ownedCompanies.map { it.toDto() },
    associatedCompanies = associatedCompanies.map { it.toDto() }
//    authorities = authorities
)
