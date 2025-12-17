package com.blank.prasunet.data.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String,
    val role: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

