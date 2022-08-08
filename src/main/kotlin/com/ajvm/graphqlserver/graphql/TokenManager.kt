package com.ajvm.graphqlserver.graphql

import com.ajvm.graphqlserver.db.DBRepositoryRetrieval
import graphql.schema.DataFetchingEnvironment
import io.github.cdimascio.dotenv.dotenv
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap

@Component
class TokenManager(val dbRetrieval: DBRepositoryRetrieval) {
    private final val dotenv = dotenv { directory = "./" }
    private val jwtSecret: String = dotenv["JWTSECRET"]
    fun generateJwtToken(email: String): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder().setClaims(claims).setSubject(email)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret).compact()
    }
    fun getEmailFromToken(token: String?): String {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        return claims.subject
    }
    fun validateJwtToken(token: String?): Boolean {
        val foundEmail = getEmailFromToken(token)
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        val isTokenExpired = claims.expiration.before(Date())
        return !isTokenExpired
    }
    fun verification(dfe: DataFetchingEnvironment) : Boolean {
        val dd = dfe.graphQlContext?.get( "Authorization") as String
        return try {
            validateJwtToken(dd)
            true
        } catch (e: SignatureException) {
            false
        }
    }
    companion object {
        const val TOKEN_VALIDITY = (10 * 60 * 60).toLong()
    }
}
