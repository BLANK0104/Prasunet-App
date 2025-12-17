package com.blank.prasunet.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blank.prasunet.data.models.Statistics
import com.blank.prasunet.data.models.User
import com.blank.prasunet.data.repository.AdminRepository
import com.blank.prasunet.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val adminRepository: AdminRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _statisticsState = MutableStateFlow<StatisticsUiState>(StatisticsUiState.Loading)
    val statisticsState: StateFlow<StatisticsUiState> = _statisticsState.asStateFlow()

    private val _pendingMentorsState = MutableStateFlow<PendingMentorsUiState>(PendingMentorsUiState.Loading)
    val pendingMentorsState: StateFlow<PendingMentorsUiState> = _pendingMentorsState.asStateFlow()

    private val _allUsersState = MutableStateFlow<AllUsersUiState>(AllUsersUiState.Loading)
    val allUsersState: StateFlow<AllUsersUiState> = _allUsersState.asStateFlow()

    private val _allCoursesState = MutableStateFlow<AllCoursesUiState>(AllCoursesUiState.Loading)
    val allCoursesState: StateFlow<AllCoursesUiState> = _allCoursesState.asStateFlow()

    init {
        loadStatistics()
        loadPendingMentors()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _statisticsState.value = StatisticsUiState.Loading
            val result = adminRepository.getStatistics()
            _statisticsState.value = if (result.isSuccess) {
                StatisticsUiState.Success(result.getOrNull()!!)
            } else {
                StatisticsUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load statistics")
            }
        }
    }

    fun loadPendingMentors() {
        viewModelScope.launch {
            _pendingMentorsState.value = PendingMentorsUiState.Loading
            val result = adminRepository.getPendingMentors()
            _pendingMentorsState.value = if (result.isSuccess) {
                val mentors = result.getOrNull()!!
                if (mentors.isEmpty()) {
                    PendingMentorsUiState.Empty
                } else {
                    PendingMentorsUiState.Success(mentors)
                }
            } else {
                PendingMentorsUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load pending mentors")
            }
        }
    }

    fun approveMentor(mentorId: String, approved: Boolean) {
        viewModelScope.launch {
            val result = adminRepository.approveMentor(mentorId, approved)
            if (result.isSuccess) {
                loadPendingMentors()
                loadStatistics()
            }
        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            _allUsersState.value = AllUsersUiState.Loading
            val result = adminRepository.getAllUsers()
            _allUsersState.value = if (result.isSuccess) {
                val users = result.getOrNull()!!
                if (users.isEmpty()) {
                    AllUsersUiState.Empty
                } else {
                    AllUsersUiState.Success(users)
                }
            } else {
                AllUsersUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load users")
            }
        }
    }

    fun loadAllCourses() {
        viewModelScope.launch {
            _allCoursesState.value = AllCoursesUiState.Loading
            val result = adminRepository.getAllCourses()
            _allCoursesState.value = if (result.isSuccess) {
                val courses = result.getOrNull()!!
                if (courses.isEmpty()) {
                    AllCoursesUiState.Empty
                } else {
                    AllCoursesUiState.Success(courses)
                }
            } else {
                AllCoursesUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load courses")
            }
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            val result = adminRepository.deleteUser(userId)
            if (result.isSuccess) {
                loadAllUsers()
                loadStatistics()
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}

sealed class StatisticsUiState {
    object Loading : StatisticsUiState()
    data class Success(val statistics: Statistics) : StatisticsUiState()
    data class Error(val message: String) : StatisticsUiState()
}

sealed class PendingMentorsUiState {
    object Loading : PendingMentorsUiState()
    object Empty : PendingMentorsUiState()
    data class Success(val mentors: List<User>) : PendingMentorsUiState()
    data class Error(val message: String) : PendingMentorsUiState()
}

sealed class AllUsersUiState {
    object Loading : AllUsersUiState()
    object Empty : AllUsersUiState()
    data class Success(val users: List<User>) : AllUsersUiState()
    data class Error(val message: String) : AllUsersUiState()
}

sealed class AllCoursesUiState {
    object Loading : AllCoursesUiState()
    object Empty : AllCoursesUiState()
    data class Success(val courses: List<com.blank.prasunet.data.models.Course>) : AllCoursesUiState()
    data class Error(val message: String) : AllCoursesUiState()
}

