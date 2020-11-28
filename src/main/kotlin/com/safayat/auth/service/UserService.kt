package com.safayat.auth.service

import com.safayat.auth.model.User
import com.safayat.auth.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service class UserService(val repository: UserRepository): UserDetailsService {
    fun save(user:User) = repository.save(user)
    override fun loadUserByUsername(userName: String): UserDetails {
        val user = repository.findByUsername(userName)?: throw UsernameNotFoundException(userName)
        return org.springframework.security.core.userdetails.User(user.username, user.password, listOf<GrantedAuthority>())
    }
}
