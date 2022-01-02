package com.rowatk.waje.components.company

import com.rowatk.waje.dto.ModelDTO
import com.rowatk.waje.models.Address
import com.rowatk.waje.models.Company

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
