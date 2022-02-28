package com.rowatk.waje.components.user


import com.rowatk.waje.components.company.Company
import com.rowatk.waje.components.company.CompanyDTO
import com.rowatk.waje.components.company.toDto
import com.rowatk.waje.dto.incomingRequests.RegisterRequest
import com.rowatk.waje.exceptions.ApiException
import com.rowatk.waje.exceptions.RecordNotFoundException
import com.rowatk.waje.utills.LoggerDelegate
import com.rowatk.waje.utills.getAuthenticatedUser
import com.rowatk.waje.utills.isEqual
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import lombok.extern.slf4j.Slf4j
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Slf4j
@Service
class UserService(
    private val userRepo: UserRepo
) {
    companion object {
        val log by LoggerDelegate()
    }

    suspend fun addUser(registerRequest: RegisterRequest): UserDTO {
        return userRepo.findByUsername(registerRequest.username)
            .flatMap { Mono.error<UserDTO>(ApiException(400, "user already exists")) }
            .switchIfEmpty(Mono.defer {
                userRepo.insert(
                    User(
                        username = registerRequest.username,
                        email = registerRequest.email,
                        password = registerRequest.password
                    )
                )
                    .doOnSuccess { log.info("Successfully saved user: {}", it) }
                    .doOnError { throw ApiException(message = "error adding user") }
                    .map { it.toDto() }
            })
            .awaitSingle()
    }

    @FlowPreview
    fun getAll(): Flow<UserDTO> {
        val dbUsers = userRepo.findAll().asFlow().map { user -> user.toDto() }
        return dbUsers;
    }

    suspend fun findByUsername(username: String): UserDTO {
        val dbUser = userRepo.findByUsername(username)
        return dbUser.map { it?.toDto() ?: throw RecordNotFoundException(username, "username") }.awaitSingle()
    }

    suspend fun getUserCompanies(owned: Boolean): List<CompanyDTO> {
        val user = getAuthenticatedUser();
        var companies: List<Company> = user.ownedCompanies;
        if (!owned) {
            companies = user.associatedCompanies
        }

        return companies.map { company: Company -> company.toDto() }
    }

    suspend fun getUserOwnedCompanies(): List<CompanyDTO> {
        return getUserCompanies(true);
    }

    suspend fun getUserAssociatedCompanies(): List<CompanyDTO> {
        return getUserCompanies(false);
    }

    suspend fun addUserCompany(company: CompanyDTO, owned: Boolean): ObjectId {
        val user = getAuthenticatedUser();

        if (owned) {
            user.ownedCompanies = user.ownedCompanies.plus(company.toModel())
        } else {
            user.associatedCompanies = user.associatedCompanies.plus(company.toModel())
        }

        return userRepo.save(user)
            .doOnSuccess { log.info("Successfully saved company: {}", it) }
            .doOnError { throw ApiException(message = "error adding company") }
            .map { it.id }
            .awaitSingle()
    }

    suspend fun addUserOwnedCompanies(company: CompanyDTO): ObjectId {
        return addUserCompany(company, true)
    }

    suspend fun addUserAssociatedCompanies(company: CompanyDTO): ObjectId {
        return addUserCompany(company, false)
    }

    suspend fun editUserCompany(id: ObjectId, updatedCompany: CompanyDTO?, owned: Boolean): ObjectId {

        var operation: String;
        val user = getAuthenticatedUser()
        var companyList: MutableList<Company> =
            (if (owned) user.ownedCompanies else user.associatedCompanies).toMutableList()
        val existingIndex = companyList.indexOfFirst { company: Company -> isEqual(company.id, id) }

        if (existingIndex > -1) {

            if(updatedCompany != null) {
                //validate updatedCompany
//        validate()
                operation = "update"
                updatedCompany.id = id
                companyList[existingIndex] = updatedCompany.toModel()

            } else {
                operation = "delete"
                companyList.removeAt(existingIndex)
            }

            if (owned) {
                user.ownedCompanies = companyList
            } else {
                user.associatedCompanies = companyList
            }

            return userRepo.save(user)
                .doOnSuccess { log.info("Successful $operation of company: {}", it) }
                .doOnError { throw ApiException(message = "error in company $operation") }
                .map { it.id }
                .awaitSingle()
        }

        throw RecordNotFoundException(key = CompanyDTO.key, id.toString())
    }
}
