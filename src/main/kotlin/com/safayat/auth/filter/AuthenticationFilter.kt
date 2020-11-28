package com.safayat.auth.filter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.safayat.auth.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.security.Key
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.Throws

class AuthenticationFilter(authenticationManager: AuthenticationManager, var authJwtKey:String, var authExpirationTime:Int) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication {
        return try {
            val applicationUser: User = jacksonObjectMapper().readValue(req.inputStream, User::class.java)
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(applicationUser.username,
                            applicationUser.password, ArrayList())
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest?, res: HttpServletResponse, chain: FilterChain?,
                                          auth: Authentication) {
        val exp = Date(System.currentTimeMillis() + authExpirationTime)
        val key: Key = Keys.hmacShaKeyFor(authJwtKey.toByteArray())
        val claims: Claims = Jwts.claims().setSubject((auth.getPrincipal() as org.springframework.security.core.userdetails.User).username)
        val token: String = Jwts.builder().setClaims(claims).signWith(key, SignatureAlgorithm.HS512).setExpiration(exp).compact()
        res.addHeader("token", token)
    }

    init {
        this.authenticationManager = authenticationManager
    }
}
