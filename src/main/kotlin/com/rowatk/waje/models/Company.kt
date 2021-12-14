package com.rowatk.waje.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Company(
    val companyName: String,
    val address: Address,
    val phone: String,
    val email: String,
    val favourite: Boolean,
)
