package com.rowatk.waje

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

class JwtTests {

    @Test
    fun generatesJWTs() {

        for(index in 1..10) {
            val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
            val secretKey: String = Encoders.BASE64.encode(key.encoded)
            Assertions.assertNotNull(secretKey)
        }
    }
}
