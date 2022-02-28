package com.rowatk.waje.components.counters

import com.rowatk.waje.components.user.UserService
import com.rowatk.waje.exceptions.ApiException
import com.rowatk.waje.exceptions.RecordNotFoundException
import com.rowatk.waje.utills.LoggerDelegate
import kotlinx.coroutines.reactor.awaitSingle
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

enum class CounterType {

    INVOICE_NUM
}

@Document
data class Counter(
    @Id
    val id: ObjectId = ObjectId.get(),
    val counterType: CounterType,
    var count: Int
);

interface CountersRepo: ReactiveMongoRepository<Counter, ObjectId> {
    suspend fun findByCounterType(counterType: CounterType): Mono<Counter?>
}

@Service
class CountersService(private val counterRepo: CountersRepo) {

    companion object {
        val log by LoggerDelegate()
    }

    suspend fun getLatestInvoiceNumber(): Int {
        return getLatestCount(CounterType.INVOICE_NUM)
    }

    suspend fun getLatestCount(counterType: CounterType): Int {
        return counterRepo.findByCounterType(counterType)
            .switchIfEmpty(Mono.error(RecordNotFoundException("counter", counterType.toString())))
            .map { it!!.count }
            .awaitSingle()
    }

    suspend fun getCounterByType(counterType: CounterType): Counter {
        return counterRepo.findByCounterType(counterType)
            .switchIfEmpty(Mono.error(RecordNotFoundException("counter", counterType.toString())))
            .map { it!! }
            .awaitSingle()
    }

    suspend fun incrementCount(counterType: CounterType): Int {
        val counter = getCounterByType(counterType)
        counter.count += 1

        return counterRepo.save(counter)
            .doOnSuccess { log.info("Successfully incremented $counterType counter: {}", it) }
            .doOnError { throw ApiException(message = "error incrementing $counterType counter") }
            .map { it.count }
            .awaitSingle()
    }
}
