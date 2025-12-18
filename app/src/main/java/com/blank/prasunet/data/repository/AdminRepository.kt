package com.blank.prasunet.data.repository

import com.blank.prasunet.data.api.ApiService
import com.blank.prasunet.data.models.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getPendingMentors(): Result<List<User>> {
        return try {
            val response = apiService.getPendingMentors()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.mentors)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch pending mentors"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun approveMentor(mentorId: String, approved: Boolean): Result<String> {
        return try {
            val response = apiService.approveMentor(mentorId, ApproveMentorRequest(approved))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Mentor ${if (approved) "approved" else "rejected"}")
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to approve/reject mentor"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStatistics(): Result<Statistics> {
        return try {
            val response = apiService.getStatistics()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch statistics"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.users)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch users"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: String): Result<String> {
        return try {
            val response = apiService.deleteUser(userId)
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "User deleted successfully")
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to delete user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllStudents(): Result<List<User>> {
        return try {
            val response = apiService.getAllStudents()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.users)
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

    suspend fun getAllCourses(): Result<List<Course>> {
        return try {
            val response = apiService.getAllCourses()
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

    // ==================== Enhanced Admin Methods ====================

    suspend fun updateUser(userId: String, fullName: String?, email: String?): Result<User> {
        return try {
            val response = apiService.updateUser(userId, UpdateUserRequest(fullName, email))
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to update user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCourse(courseId: String): Result<Pair<String, Int?>> {
        return try {
            val response = apiService.deleteCourse(courseId)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(Pair(body.message, body.affectedStudents))
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to delete course"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCourse(courseId: String, title: String?, description: String?): Result<Course> {
        return try {
            val response = apiService.updateCourse(courseId, UpdateCourseRequest(title, description))
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to update course"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourseAnalytics(courseId: String): Result<CourseAnalyticsResponse> {
        return try {
            val response = apiService.getCourseAnalytics(courseId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch course analytics"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateChapter(
        chapterId: String,
        title: String? = null,
        content: String? = null,
        imageUrl: String? = null,
        videoUrl: String? = null,
        sequenceOrder: Int? = null
    ): Result<Chapter> {
        return try {
            val response = apiService.updateChapter(
                chapterId,
                UpdateChapterRequest(title, content, imageUrl, videoUrl, sequenceOrder)
            )
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to update chapter"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteChapter(chapterId: String): Result<String> {
        return try {
            val response = apiService.deleteChapter(chapterId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to delete chapter"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllEnrollments(): Result<List<EnrollmentDetail>> {
        return try {
            val response = apiService.getAllEnrollments()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.enrollments)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch enrollments"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEnrollment(enrollmentId: String): Result<String> {
        return try {
            val response = apiService.deleteEnrollment(enrollmentId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to delete enrollment"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetEnrollmentProgress(enrollmentId: String): Result<String> {
        return try {
            val response = apiService.resetEnrollmentProgress(enrollmentId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to reset progress"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllCertificates(): Result<List<CertificateDetail>> {
        return try {
            val response = apiService.getAllCertificates()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.certificates)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to fetch certificates"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun revokeCertificate(certificateId: String): Result<String> {
        return try {
            val response = apiService.revokeCertificate(certificateId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to revoke certificate"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

