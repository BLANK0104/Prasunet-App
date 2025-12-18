package com.blank.prasunet.data.api

import com.blank.prasunet.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ==================== Auth Endpoints ====================

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<LoginResponse>>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // ==================== Student Endpoints ====================

    @GET("student/courses")
    suspend fun getStudentCourses(): Response<CoursesResponse>

    @GET("student/courses/{courseId}/chapters")
    suspend fun getCourseChapters(
        @Path("courseId") courseId: String
    ): Response<ChaptersResponse>

    @POST("student/chapters/{chapterId}/complete")
    suspend fun markChapterComplete(
        @Path("chapterId") chapterId: String
    ): Response<ApiResponse<String>>

    @GET("student/courses/{courseId}/progress")
    suspend fun getCourseProgress(
        @Path("courseId") courseId: String
    ): Response<Progress>

    @GET("student/courses/{courseId}/certificate")
    suspend fun downloadCertificate(
        @Path("courseId") courseId: String
    ): Response<CertificateResponse>

    // ==================== Mentor Endpoints ====================

    @POST("mentor/courses")
    suspend fun createCourse(
        @Body request: CreateCourseRequest
    ): Response<CourseCreatedResponse>

    @GET("mentor/courses")
    suspend fun getMentorCourses(): Response<CoursesResponse>

    @GET("mentor/courses/{courseId}/chapters")
    suspend fun getMentorCourseChapters(
        @Path("courseId") courseId: String
    ): Response<ChaptersResponse>

    @POST("mentor/courses/{courseId}/chapters")
    suspend fun addChapter(
        @Path("courseId") courseId: String,
        @Body request: CreateChapterRequest
    ): Response<ChapterCreatedResponse>

    @POST("mentor/assign-course")
    suspend fun assignCourse(
        @Body request: AssignSingleCourseRequest
    ): Response<ApiResponse<String>>

    @GET("mentor/courses/{courseId}/students")
    suspend fun getEnrolledStudents(
        @Path("courseId") courseId: String
    ): Response<StudentsResponse>

    @GET("mentor/courses/{courseId}/students/{studentId}/progress")
    suspend fun getStudentProgress(
        @Path("courseId") courseId: String,
        @Path("studentId") studentId: String
    ): Response<Progress>

    @GET("mentor/students")
    suspend fun getAllStudentsForMentor(): Response<AvailableStudentsResponse>

    // ==================== Admin Endpoints ====================

    @GET("admin/mentors/pending")
    suspend fun getPendingMentors(): Response<MentorsResponse>

    @PUT("admin/mentors/{mentorId}/approve")
    suspend fun approveMentor(
        @Path("mentorId") mentorId: String,
        @Body request: ApproveMentorRequest
    ): Response<ApiResponse<String>>

    @GET("admin/statistics")
    suspend fun getStatistics(): Response<Statistics>

    @GET("admin/users")
    suspend fun getAllUsers(): Response<UsersResponse>

    @DELETE("admin/users/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: String
    ): Response<ApiResponse<String>>

    @GET("admin/students")
    suspend fun getAllStudents(): Response<UsersResponse>

    @GET("admin/courses")
    suspend fun getAllCourses(): Response<CoursesResponse>

    // ==================== Enhanced Admin Endpoints ====================

    // User Management
    @PUT("admin/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body request: UpdateUserRequest
    ): Response<ApiResponse<User>>

    // Course Management
    @DELETE("admin/courses/{courseId}")
    suspend fun deleteCourse(
        @Path("courseId") courseId: String
    ): Response<MessageResponse>

    @PUT("admin/courses/{courseId}")
    suspend fun updateCourse(
        @Path("courseId") courseId: String,
        @Body request: UpdateCourseRequest
    ): Response<ApiResponse<Course>>

    @GET("admin/courses/{courseId}/analytics")
    suspend fun getCourseAnalytics(
        @Path("courseId") courseId: String
    ): Response<CourseAnalyticsResponse>

    // Chapter Management
    @PUT("admin/chapters/{chapterId}")
    suspend fun updateChapter(
        @Path("chapterId") chapterId: String,
        @Body request: UpdateChapterRequest
    ): Response<ApiResponse<Chapter>>

    @DELETE("admin/chapters/{chapterId}")
    suspend fun deleteChapter(
        @Path("chapterId") chapterId: String
    ): Response<MessageResponse>

    // Enrollment Management
    @GET("admin/enrollments")
    suspend fun getAllEnrollments(): Response<EnrollmentsResponse>

    @DELETE("admin/enrollments/{enrollmentId}")
    suspend fun deleteEnrollment(
        @Path("enrollmentId") enrollmentId: String
    ): Response<MessageResponse>

    @PUT("admin/enrollments/{enrollmentId}/reset")
    suspend fun resetEnrollmentProgress(
        @Path("enrollmentId") enrollmentId: String
    ): Response<MessageResponse>

    // Certificate Management
    @GET("admin/certificates")
    suspend fun getAllCertificates(): Response<CertificatesResponse>

    @DELETE("admin/certificates/{certificateId}")
    suspend fun revokeCertificate(
        @Path("certificateId") certificateId: String
    ): Response<MessageResponse>
}

