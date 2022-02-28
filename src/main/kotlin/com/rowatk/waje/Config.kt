package com.rowatk.waje

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.addDeserializer
import org.bson.types.ObjectId
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.aggregation.ConvertOperators
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration
class Config : WebFluxConfigurer {

    class ObjectIdDeserializer: JsonDeserializer<ObjectId>() {
        override fun deserialize(jp: JsonParser?, ctx: DeserializationContext?): ObjectId {
            val stringId = jp?.codec?.readValue(jp, String::class.java)

            return ObjectId(stringId)
        }


    }

    fun objectIdModule(): SimpleModule {
        val module = SimpleModule("ObjectIdModule")
        module.addSerializer(ObjectId::class.java, ToStringSerializer())
        module.addDeserializer(ObjectId::class.java, ObjectIdDeserializer())
        return module
    }

    fun getObjectMapper(): ObjectMapper {

        return ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModule(objectIdModule())

            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs()
            .jackson2JsonEncoder(Jackson2JsonEncoder(getObjectMapper()))

//        configurer.defaultCodecs()
//            .jackson2JsonDecoder(Jackson2JsonDecoder(ObjectMapper().registerModule(objectIdModule())))
    }
}
