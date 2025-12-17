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
}

