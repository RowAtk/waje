package com.rowatk.waje

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.bson.types.ObjectId
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration
class Config : WebFluxConfigurer {

    fun objectIdModule(): SimpleModule {
        val module = SimpleModule("ObjectIdModule")
        module.addSerializer(ObjectId::class.java, ToStringSerializer())
        return module
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs()
            .jackson2JsonEncoder(Jackson2JsonEncoder(ObjectMapper().registerModule(objectIdModule())))
    }
}
