package com.blank.prasunet.data.models

data class Course(
    val id: String,
    val title: String,
    val description: String = "",
    val mentor_name: String? = null,
    val total_chapters: String = "0",
    val enrolled_students: String = "0",
    val completed_chapters: Int = 0,
    val progress_percentage: Int = 0,
    val created_at: String? = null
)

data class CreateCourseRequest(
    val title: String,
    val description: String = ""
)

data class CourseCreatedResponse(
    val message: String,
    val course: Course
)

