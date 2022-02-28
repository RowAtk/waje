package com.rowatk.waje.components.invoice

import com.rowatk.waje.dto.outgoingResponses.ApiResponse
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody

@Component
class InvoiceHandler(val invoiceService: InvoiceService) {

    suspend fun findUserInvoicesHandler(request: ServerRequest): ServerResponse {
        return ApiResponse.ok()
            .result("invoices", invoiceService.getUserInvoices())
            .buildResponse()
    }

    suspend fun createInvoice(request: ServerRequest): ServerResponse {
        val requestBody = request.awaitBody<InvoiceDTO>();

        return ApiResponse.ok()
            .result(invoiceService.addInvoice(requestBody))
            .buildResponse()
    }

    suspend fun deleteInvoice(request: ServerRequest): ServerResponse {
        val id: ObjectId = ObjectId(request.pathVariable("id"))
        invoiceService.deleteInvoice(id)

        return ApiResponse.ok()
            .buildResponse()
    }
}
