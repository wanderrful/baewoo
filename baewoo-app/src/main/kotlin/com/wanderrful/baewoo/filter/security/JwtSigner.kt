package com.wanderrful.baewoo.filter.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * This utility class allows us to work with JWT tokens for Security purposes.
 */
@Service
class JwtSigner {
    private val keyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

    private val issuer: String = "baewoo"

    fun createJwt(userId: String): String {
        return Jwts.builder()
            .signWith(keyPair.private, SignatureAlgorithm.RS256)
            .setSubject(userId)
            .setIssuer(issuer)
            .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
            .setIssuedAt(Date.from(Instant.now()))
//            .claim("myClaimKey", "myClaimValue")  // TODO | use .claim() to add custom stuff to the JWT
            .compact()
    }

    /**
     * Validate the JWT where it will throw an exception if it isn't valid.
     */
    fun validateJwt(jwt: String): Jws<Claims> {
        return Jwts.parserBuilder()
            .setSigningKey(keyPair.public)
            .build()
            .parseClaimsJws(jwt)
    }

}