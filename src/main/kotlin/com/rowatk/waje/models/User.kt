package com.rowatk.waje.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
class User(
    @Id
    val id: String,
    private val username: String,
    private val password: String,
    private val isEnabled: Boolean, //Disabled account can not log in
    private val isCredentialsNonExpired: Boolean, //credential can be expired,eg. Change the password every three months
    private val isAccountNonExpired: Boolean, //eg. Demo account（guest） can only be online  24 hours
    private val isAccountNonLocked: Boolean, //eg. Users who malicious attack system,lock their account for one year
    private val authorities: MutableCollection<out GrantedAuthority>,
) : UserDetails {

    // override functions
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities;

    override fun getPassword(): String {
        return password;
    }

    override fun getUsername(): String {
        return username;
    }

    override fun isAccountNonExpired(): Boolean {
        return isAccountNonExpired;
    }

    override fun isAccountNonLocked(): Boolean {
        return isAccountNonLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isCredentialsNonExpired
    }

    override fun isEnabled(): Boolean {
        return isEnabled
    }

}
