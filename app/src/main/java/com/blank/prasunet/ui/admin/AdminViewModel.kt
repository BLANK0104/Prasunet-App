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

    private val _enrollmentsState = MutableStateFlow<EnrollmentsUiState>(EnrollmentsUiState.Loading)
    val enrollmentsState: StateFlow<EnrollmentsUiState> = _enrollmentsState.asStateFlow()

    private val _certificatesState = MutableStateFlow<CertificatesUiState>(CertificatesUiState.Loading)
    val certificatesState: StateFlow<CertificatesUiState> = _certificatesState.asStateFlow()

    private val _operationState = MutableStateFlow<OperationUiState>(OperationUiState.Idle)
    val operationState: StateFlow<OperationUiState> = _operationState.asStateFlow()

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

    fun updateUser(userId: String, fullName: String?, email: String?) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading
            val result = adminRepository.updateUser(userId, fullName, email)
            _operationState.value = if (result.isSuccess) {
                loadAllUsers()
                OperationUiState.Success("User updated successfully")
            } else {
                OperationUiState.Error(result.exceptionOrNull()?.message ?: "Failed to update user")
            }
        }
    }

    fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading
            val result = adminRepository.deleteCourse(courseId)
            _operationState.value = if (result.isSuccess) {
                loadAllCourses()
                loadStatistics()
                val (message, affectedStudents) = result.getOrNull()!!
                OperationUiState.Success("$message (${affectedStudents ?: 0} students affected)")
            } else {
                OperationUiState.Error(result.exceptionOrNull()?.message ?: "Failed to delete course")
            }
        }
    }

    fun updateCourse(courseId: String, title: String?, description: String?) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading
            val result = adminRepository.updateCourse(courseId, title, description)
            _operationState.value = if (result.isSuccess) {
                loadAllCourses()
                OperationUiState.Success("Course updated successfully")
            } else {
                OperationUiState.Error(result.exceptionOrNull()?.message ?: "Failed to update course")
            }
        }
    }

    fun loadEnrollments() {
        viewModelScope.launch {
            _enrollmentsState.value = EnrollmentsUiState.Loading
            val result = adminRepository.getAllEnrollments()
            _enrollmentsState.value = if (result.isSuccess) {
                val enrollments = result.getOrNull()!!
                if (enrollments.isEmpty()) {
                    EnrollmentsUiState.Empty
                } else {
                    EnrollmentsUiState.Success(enrollments)
                }
            } else {
                EnrollmentsUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load enrollments")
            }
        }
    }

    fun deleteEnrollment(enrollmentId: String) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading
            val result = adminRepository.deleteEnrollment(enrollmentId)
            _operationState.value = if (result.isSuccess) {
                loadEnrollments()
                loadStatistics()
                OperationUiState.Success("Enrollment deleted successfully")
            } else {
                OperationUiState.Error(result.exceptionOrNull()?.message ?: "Failed to delete enrollment")
            }
        }
    }

    fun resetEnrollmentProgress(enrollmentId: String) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading
            val result = adminRepository.resetEnrollmentProgress(enrollmentId)
            _operationState.value = if (result.isSuccess) {
                loadEnrollments()
                OperationUiState.Success("Progress reset successfully")
            } else {
                OperationUiState.Error(result.exceptionOrNull()?.message ?: "Failed to reset progress")
            }
        }
    }

    fun loadCertificates() {
        viewModelScope.launch {
            _certificatesState.value = CertificatesUiState.Loading
            val result = adminRepository.getAllCertificates()
            _certificatesState.value = if (result.isSuccess) {
                val certificates = result.getOrNull()!!
                if (certificates.isEmpty()) {
                    CertificatesUiState.Empty
                } else {
                    CertificatesUiState.Success(certificates)
                }
            } else {
                CertificatesUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load certificates")
            }
        }
    }

    fun revokeCertificate(certificateId: String) {
        viewModelScope.launch {
            _operationState.value = OperationUiState.Loading
            val result = adminRepository.revokeCertificate(certificateId)
            _operationState.value = if (result.isSuccess) {
                loadCertificates()
                OperationUiState.Success("Certificate revoked successfully")
            } else {
                OperationUiState.Error(result.exceptionOrNull()?.message ?: "Failed to revoke certificate")
            }
        }
    }

    fun resetOperationState() {
        _operationState.value = OperationUiState.Idle
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

sealed class EnrollmentsUiState {
    object Loading : EnrollmentsUiState()
    object Empty : EnrollmentsUiState()
    data class Success(val enrollments: List<com.blank.prasunet.data.models.EnrollmentDetail>) : EnrollmentsUiState()
    data class Error(val message: String) : EnrollmentsUiState()
}

sealed class CertificatesUiState {
    object Loading : CertificatesUiState()
    object Empty : CertificatesUiState()
    data class Success(val certificates: List<com.blank.prasunet.data.models.CertificateDetail>) : CertificatesUiState()
    data class Error(val message: String) : CertificatesUiState()
}

sealed class OperationUiState {
    object Idle : OperationUiState()
    object Loading : OperationUiState()
    data class Success(val message: String) : OperationUiState()
    data class Error(val message: String) : OperationUiState()
}
