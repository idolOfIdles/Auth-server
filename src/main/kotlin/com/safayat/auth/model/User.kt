package com.safayat.auth.model

import org.springframework.data.annotation.Id

data class User(@Id val username:String, val password:String)
