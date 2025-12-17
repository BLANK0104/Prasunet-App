package com.blank.prasunet.data.models

data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)

data class ErrorResponse(
    val error: String
)

data class CoursesResponse(
    val courses: List<Course>
)

data class ChaptersResponse(
    val chapters: List<Chapter>
)

data class StudentsResponse(
    val students: List<StudentWithProgress>
)

data class MentorsResponse(
    val mentors: List<User>
)

data class UsersResponse(
    val users: List<User>
)

data class AvailableStudentsResponse(
    val students: List<User>
)

