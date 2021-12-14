package com.rowatk.waje.dto.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.rowatk.waje.dto.DTO
import com.rowatk.waje.dto.ModelDTO
import com.rowatk.waje.models.Address
import com.rowatk.waje.models.Company
import com.rowatk.waje.models.User
import org.springframework.security.core.GrantedAuthority

class EmptyDTO() : DTO

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

data class CompanyDTO(
    private val companyName: String,
    private val address: Address,
    private val phone: String,
    private val email: String,
    private val favourite: Boolean,
) : ModelDTO<Company> {
    override fun toModel(): Company = Company(
        companyName = companyName,
        address = address,
        phone = phone,
        email = email,
        favourite = favourite
    );
}

fun Company.toDto() = CompanyDTO(
    companyName = companyName,
    address = address,
    phone = phone,
    email = email,
    favourite = favourite
);
