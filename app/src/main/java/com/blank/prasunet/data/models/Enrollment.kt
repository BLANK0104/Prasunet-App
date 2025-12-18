package com.blank.prasunet.data.models

data class EnrollmentDetail(
    val id: String,
    val student: EnrollmentUser,
    val course: EnrollmentCourse,
    val total_chapters: Int,
    val completed_chapters: Int,
    val progress_percentage: Int,
    val assigned_at: String
)

data class EnrollmentUser(
    val id: String,
    val name: String,
    val email: String
)

data class EnrollmentCourse(
    val id: String,
    val title: String
)

