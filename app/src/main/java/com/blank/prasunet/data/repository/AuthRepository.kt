package com.blank.prasunet.data.repository

import com.blank.prasunet.data.api.ApiService
import com.blank.prasunet.data.local.TokenManager
import com.blank.prasunet.data.models.*
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        role: String
    ): Result<LoginResponse> {
        return try {
            val response = apiService.register(
                RegisterRequest(
                    email = email,
                    password = password,
                    full_name = fullName,
                    role = role
                )
            )

            if (response.isSuccessful && response.body()?.data != null) {
                val loginResponse = response.body()!!.data!!
                tokenManager.saveToken(loginResponse.token)
                tokenManager.saveUser(loginResponse.user)
                Result.success(loginResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                tokenManager.saveToken(loginResponse.token)
                tokenManager.saveUser(loginResponse.user)
                Result.success(loginResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = try {
                    Gson().fromJson(errorBody, ErrorResponse::class.java)
                } catch (e: Exception) {
                    null
                }
                Result.failure(Exception(error?.error ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clearAll()
    }

    fun getCurrentUser(): User? {
        return tokenManager.getUser()
    }

    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}

