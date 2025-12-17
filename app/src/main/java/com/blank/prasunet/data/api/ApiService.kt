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
}

