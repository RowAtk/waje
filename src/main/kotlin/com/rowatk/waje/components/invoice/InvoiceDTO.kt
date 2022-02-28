package com.rowatk.waje.components.invoice

import com.rowatk.waje.dto.ModelDTO
import org.bson.types.ObjectId
import java.time.LocalDate

data class InvoiceDTO(
//    @JsonDeserialize(using = Config.ObjectIdDeserializer::class)
    val id: ObjectId?,
    var userId: ObjectId?,
    var invoiceNumber: Int?,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val deliveryDate: LocalDate,
    val status: InvoiceStatus,
    // Owned Company ID
    val sellerId: ObjectId,
    // Ass. Company ID
    val buyerId: ObjectId // ass. company id
): ModelDTO<Invoice> {


    override fun toModel(): Invoice = Invoice(
        id = id ?: ObjectId.get(),
        userId = userId!!,
        invoiceNumber = invoiceNumber!!,
        issueDate = issueDate,
        dueDate = dueDate,
        deliveryDate = deliveryDate,
        status = status,
        sellerId = sellerId,
        buyerId = buyerId
    )
}

fun Invoice.toDto() = InvoiceDTO(
    id = id,
    userId = userId,
    invoiceNumber = invoiceNumber,
    issueDate = issueDate,
    dueDate = dueDate,
    deliveryDate = deliveryDate,
    status = status,
    sellerId = sellerId,
    buyerId = buyerId
)
