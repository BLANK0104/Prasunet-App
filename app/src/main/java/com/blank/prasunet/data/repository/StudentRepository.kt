package com.blank.prasunet.data.repository

import com.blank.prasunet.data.api.ApiService
import com.blank.prasunet.data.models.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getCourses(): Result<List<Course>> {
        return try {
            val response = apiService.getStudentCourses()
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
            val response = apiService.getCourseChapters(courseId)
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

    suspend fun markChapterComplete(chapterId: String): Result<String> {
        return try {
            val response = apiService.markChapterComplete(chapterId)
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Chapter marked complete")
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to mark chapter complete"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourseProgress(courseId: String): Result<Progress> {
        return try {
            val response = apiService.getCourseProgress(courseId)
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

    suspend fun downloadCertificate(courseId: String): Result<CertificateResponse> {
        return try {
            val response = apiService.downloadCertificate(courseId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Failed to download certificate"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

