package com.rowatk.waje.components.company

import com.rowatk.waje.components.address.Address
import com.rowatk.waje.dto.ModelDTO
import org.bson.types.ObjectId


data class CompanyDTO(
    var id: ObjectId?,
    var companyName: String,
    var address: Address,
    var phone: String,
    var email: String,
    var favourite: Boolean = false,
) : ModelDTO<Company> {

    companion object {
        val key: String = "company"
    }

    override fun toModel(): Company = Company(
        id = id ?: ObjectId.get(),
        companyName = companyName,
        address = address,
        phone = phone,
        email = email,
        favourite = favourite
    );

    override fun getKey(): String {
        return key
    }
}

fun Company.toDto() = CompanyDTO(
    id = id,
    companyName = companyName,
    address = address,
    phone = phone,
    email = email,
    favourite = favourite
);
