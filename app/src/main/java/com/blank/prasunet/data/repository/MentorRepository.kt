package com.blank.prasunet.data.repository

import com.blank.prasunet.data.api.ApiService
import com.blank.prasunet.data.models.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MentorRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun createCourse(title: String, description: String): Result<Course> {
        return try {
            val response = apiService.createCourse(CreateCourseRequest(title, description))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.course)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to create course"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMentorCourses(): Result<List<Course>> {
        return try {
            val response = apiService.getMentorCourses()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.courses)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch courses"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourseChapters(courseId: String): Result<List<Chapter>> {
        return try {
            val response = apiService.getMentorCourseChapters(courseId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.chapters)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch chapters"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addChapter(
        courseId: String,
        title: String,
        content: String,
        imageUrl: String?,
        videoUrl: String?,
        sequenceOrder: Int
    ): Result<Chapter> {
        return try {
            val response = apiService.addChapter(
                courseId,
                CreateChapterRequest(title, content, imageUrl, videoUrl, sequenceOrder)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.chapter)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to add chapter"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun assignCourse(courseId: String, studentIds: List<String>): Result<String> {
        return try {
            // New API assigns one student at a time
            var successCount = 0
            var lastError: String? = null

            for (studentId in studentIds) {
                val response = apiService.assignCourse(
                    AssignSingleCourseRequest(studentId = studentId, courseId = courseId)
                )
                if (response.isSuccessful) {
                    successCount++
                } else {
                    val errorBody = response.errorBody()?.string()
                    val error = try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    lastError = error?.error ?: "Failed to assign to student $studentId"
                }
            }

            if (successCount == studentIds.size) {
                Result.success("Course assigned to all $successCount student(s) successfully")
            } else if (successCount > 0) {
                Result.success("Course assigned to $successCount out of ${studentIds.size} student(s). Last error: $lastError")
            } else {
                Result.failure(Exception(lastError ?: "Failed to assign course"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEnrolledStudents(courseId: String): Result<List<StudentWithProgress>> {
        return try {
            val response = apiService.getEnrolledStudents(courseId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.students)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch students"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStudentProgress(courseId: String, studentId: String): Result<Progress> {
        return try {
            val response = apiService.getStudentProgress(courseId, studentId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch progress"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllStudents(): Result<List<User>> {
        return try {
            val response = apiService.getAllStudentsForMentor()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.students)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch students"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

