package com.safayat.auth.controller

import com.safayat.auth.model.User
import com.safayat.auth.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
class UserController(val userService: UserService,
                     val bCryptPasswordEncoder: BCryptPasswordEncoder) {

    @PostMapping("/user/signup", consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun signUp(@RequestBody user: User) {
        userService.save(User(user.username, bCryptPasswordEncoder.encode(user.password)))
    }

    @GetMapping("/user/secure")
    fun reachSecureEndpoint(): ResponseEntity<Any> {

        return ResponseEntity("If your are reading this you reached a secure endpoint", HttpStatus.OK);
    }
}
