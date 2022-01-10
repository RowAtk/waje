package com.rowatk.waje.components.company

import com.rowatk.waje.components.address.Address
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Company(
    @Id
    val id: ObjectId = ObjectId.get(),
    val companyName: String,
    val address: Address,
    val phone: String,
    val email: String,
    val favourite: Boolean = false,
)
