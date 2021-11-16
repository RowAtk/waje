package com.rowatk.waje

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class WajeApplication

fun main(args: Array<String>) {
	runApplication<WajeApplication>(*args)
}
