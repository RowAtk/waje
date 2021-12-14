package com.rowatk.waje.services

import com.rowatk.waje.dto.models.CompanyDTO
import com.rowatk.waje.dto.models.UserDTO
import com.rowatk.waje.dto.models.toDto
import com.rowatk.waje.exceptions.ApiException
import com.rowatk.waje.exceptions.RecordNotFoundException
import com.rowatk.waje.exceptions.UserNotFoundException
import com.rowatk.waje.models.Company
import com.rowatk.waje.models.User
import com.rowatk.waje.repos.user.UserRepo
import com.rowatk.waje.utills.getAuthenticatedUser
import kotlinx.coroutines.reactor.awaitFirst
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepo: UserRepo
) {

    suspend fun getAll(): Flux<UserDTO> {
        val dbUsers = userRepo.findAll().map { user -> user.toDto() }
        return dbUsers;
    }

    suspend fun findByUsername(username: String) : Mono<UserDTO> {
        val dbUser = userRepo.findByUsername(username)
        return dbUser.map { user -> user?.toDto() ?: throw RecordNotFoundException(username, "username") }
    }

    suspend fun getUserCompanies(owned: Boolean) : List<CompanyDTO> {
        val user = getAuthenticatedUser();
        var companies: List<Company> = user.ownedCompanies;
        if(!owned) {
            companies = user.associatedCompanies
        }

        return companies.map { company: Company -> company.toDto() }
    }

    suspend fun getUserOwnedCompanies() : List<CompanyDTO> {
        return getUserCompanies(true);
    }

    suspend fun getUserAssociatedCompanies(): List<CompanyDTO> {
        return getUserCompanies(false);
    }
}
