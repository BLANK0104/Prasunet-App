package com.blank.prasunet.data.models

data class User(
    val id: String,
    val email: String,
    val full_name: String,
    val role: String,
    val approved: Boolean = true
)

enum class UserRole {
    STUDENT,
    MENTOR,
    ADMIN;

    fun toApiValue(): String = name.lowercase()

    companion object {
        fun fromString(role: String): UserRole {
            return when(role.lowercase()) {
                "student" -> STUDENT
                "mentor" -> MENTOR
                "admin" -> ADMIN
                else -> STUDENT
            }
        }
    }
}

