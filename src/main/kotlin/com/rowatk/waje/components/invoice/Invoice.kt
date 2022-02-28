package com.rowatk.waje.components.invoice

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.LocalDate

enum class InvoiceStatus {
    UNPAID,
    PAID
}

@Document
data class Invoice(

    @MongoId
    val id: ObjectId = ObjectId.get(),
    val userId: ObjectId,
    val invoiceNumber: Int,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val deliveryDate: LocalDate,
    val status: InvoiceStatus,
    // Owned Company ID
    val sellerId: ObjectId,
    // Ass. Company ID
    val buyerId: ObjectId // ass. company id
)
