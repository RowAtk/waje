package com.rowatk.waje.components.user

import com.rowatk.waje.components.company.Company
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Document
class User(
    @Id
    val id: ObjectId = ObjectId.get(),
    private val username: String,
    private val password: String? = "",
    private val isEnabled: Boolean? = true, //Disabled account can not log in
    private val isCredentialsNonExpired: Boolean? = true, //credential can be expired,eg. Change the password every three months
    private val isAccountNonExpired: Boolean? = true, //eg. Demo account（guest） can only be online  24 hours
    private val isAccountNonLocked: Boolean? = true, //eg. Users who malicious attack system,lock their account for one year
//    private val authorities: MutableCollection<out GrantedAuthority>,
    val email: String,
    var ownedCompanies: List<Company> = Collections.emptyList(),
    var associatedCompanies: List<Company> = Collections.emptyList(),
) : UserDetails {

    // override functions
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities;

    override fun getPassword(): String {
        return password!!;
    }

    override fun getUsername(): String {
        return username;
    }



    override fun isAccountNonExpired(): Boolean {
        return isAccountNonExpired!!;
    }

    override fun isAccountNonLocked(): Boolean {
        return isAccountNonLocked!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isCredentialsNonExpired!!
    }

    override fun isEnabled(): Boolean {
        return isEnabled!!
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', password=$password, isEnabled=$isEnabled, isCredentialsNonExpired=$isCredentialsNonExpired, isAccountNonExpired=$isAccountNonExpired, isAccountNonLocked=$isAccountNonLocked, email='$email', ownedCompanies=$ownedCompanies, associatedCompanies=$associatedCompanies)"
    }


}
