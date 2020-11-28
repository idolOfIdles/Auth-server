package com.safayat.auth.repository

import com.safayat.auth.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository interface UserRepository:MongoRepository<User, String> {
    fun findByUsername(userName:String):User?
}
