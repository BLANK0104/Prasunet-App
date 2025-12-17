package com.blank.prasunet.data.models

data class Progress(
    val total_chapters: Int,
    val completed_chapters: Int,
    val progress_percentage: Int,
    val chapters: List<ChapterProgress>? = null
)

data class ChapterProgress(
    val id: String,
    val title: String,
    val sequence_order: Int,
    val is_completed: Boolean,
    val completed_at: String? = null
)

data class CertificateResponse(
    val certificate_url: String,
    val message: String
)

data class StudentWithProgress(
    val id: String,
    val email: String,
    val full_name: String,
    val progress_percentage: Int
)

data class AssignCourseRequest(
    val student_ids: List<String>
)

data class AssignSingleCourseRequest(
    val studentId: String,
    val courseId: String
)

data class ApproveMentorRequest(
    val approved: Boolean
)

data class Statistics(
    val users: UserStats,
    val courses: CourseStats,
    val enrollments: EnrollmentStats? = null,
    val certificates: CertificateStats? = null,
    val pending_mentors: Int = 0,
    val average_completion_rate: Int? = null
)

data class UserStats(
    val total: Int,
    val students: Int,
    val mentors: Int,
    val admins: Int? = null
)

data class CourseStats(
    val total: Int
)

data class EnrollmentStats(
    val total: Int
)

data class CertificateStats(
    val total: Int
)

