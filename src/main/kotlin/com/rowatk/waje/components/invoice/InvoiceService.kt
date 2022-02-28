package com.rowatk.waje.components.invoice

import com.rowatk.waje.components.counters.CounterType
import com.rowatk.waje.components.counters.CountersService
import com.rowatk.waje.components.user.User
import com.rowatk.waje.exceptions.ApiException
import com.rowatk.waje.utills.LoggerDelegate
import com.rowatk.waje.utills.getAuthenticatedUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

interface InvoiceRepo: ReactiveMongoRepository<Invoice, ObjectId> {

    fun findByUserId(userId: ObjectId): Flow<Invoice>
    fun findByIdAndUserId(id: ObjectId, userId: ObjectId): Mono<Invoice>
}

@Service
class InvoiceService (private val invoiceRepo: InvoiceRepo, private val countersService: CountersService) {

    companion object {
        val log by LoggerDelegate()
    }

    suspend fun addInvoice(newInvoice: InvoiceDTO): InvoiceDTO {
        val user = getAuthenticatedUser()
        newInvoice.userId = user.id
        newInvoice.invoiceNumber = countersService.incrementCount(CounterType.INVOICE_NUM)

        return invoiceRepo.insert(newInvoice.toModel())
            .doOnSuccess { log.info("Successfully saved user: {}", it) }
            .doOnError { throw ApiException(message = "error adding user") }
            .map { it.toDto() }
            .awaitSingle()
    }

    suspend fun getUserInvoices(): List<InvoiceDTO> {
        val user: User = getAuthenticatedUser()

        return invoiceRepo.findByUserId(user.id)
            .map { it.toDto() }
            .toList()
    }

    suspend fun verifyInvoiceOwnerShip(invoiceID: ObjectId) {
        val user = getAuthenticatedUser()

        val invoice = invoiceRepo.findById(invoiceID).awaitSingle()
            ?: throw ApiException(code = 404, message = "invoice not found");

        if(invoice.userId != user.id) {
            throw ApiException(code = 403, message = "Operation not allowed")
        }
    }

    @Transactional
    suspend fun updateInvoice(newInvoice: InvoiceDTO): ObjectId {
        val user = getAuthenticatedUser()
        newInvoice.userId = user.id

        verifyInvoiceOwnerShip(newInvoice.id!!)

        return invoiceRepo.save(newInvoice.toModel())
            .doOnSuccess { log.info("Successful update of invoice: {}", it) }
            .doOnError {
                log.error("Error deleting invoice: {}", it.message)
                throw ApiException(message = "error in invoice update")
            }
            .map { it.id }
            .awaitSingle()
    }

    @Transactional
    suspend fun deleteInvoice(id: ObjectId) {

        verifyInvoiceOwnerShip(id)

        invoiceRepo.deleteById(id)
            .doOnSuccess { log.info("successfully deleted invoice $id")}
            .doOnError { throw ApiException(message = "error deleting invoice w/ id: $id") }

    }
}
