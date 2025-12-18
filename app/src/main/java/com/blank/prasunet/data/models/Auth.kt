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

data class UpdateUserRequest(
    val full_name: String? = null,
    val email: String? = null
)

data class UpdateCourseRequest(
    val title: String? = null,
    val description: String? = null
)

data class UpdateChapterRequest(
    val title: String? = null,
    val content: String? = null,
    val image_url: String? = null,
    val video_url: String? = null,
    val sequence_order: Int? = null
)
