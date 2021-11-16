package com.rowatk.waje.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class BearerToken(val value: String) : AbstractAuthenticationToken (AuthorityUtils.NO_AUTHORITIES) {
    override fun getCredentials(): Any = value

    override fun getPrincipal(): Any = value

}

@Component
class JwtSupport {

    private val key = Keys.hmacShaKeyFor("4FdAhDfLy+4SRgvwp/M1dTop7t50Gr6E+uQlCT3RsZE=".toByteArray())
    private val parser = Jwts.parserBuilder().setSigningKey(key).build()

    fun generate(username: String): BearerToken {

        val now: Instant = Instant.now();

        val builder = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(15, ChronoUnit.MINUTES)))
            .signWith(key)

        return BearerToken(builder.compact())
    }

    fun getUsername(token: BearerToken): String {
        try {
            val c = parser.parseClaimsJws(token.value)
            return parser.parseClaimsJws(token.value).body.subject
        } catch (ex: Exception) {
            println(ex.message)
            throw ex
        }
    }

    fun isValid(token: BearerToken, user: UserDetails?): Boolean {
        val claims = parser.parseClaimsJws(token.value).body
        val unexpired = claims.expiration.after(Date.from(Instant.now()))

        return unexpired && (claims.subject == user?.username)
    }
}
