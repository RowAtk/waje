package com.rowatk.waje

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.reactive.config.EnableWebFlux


@EnableWebFlux
@SpringBootApplication
class WajeApplication

fun main(args: Array<String>) {
	runApplication<WajeApplication>(*args)
}
