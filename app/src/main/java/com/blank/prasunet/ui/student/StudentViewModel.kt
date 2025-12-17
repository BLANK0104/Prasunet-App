package com.blank.prasunet.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blank.prasunet.data.models.Chapter
import com.blank.prasunet.data.models.Course
import com.blank.prasunet.data.repository.AuthRepository
import com.blank.prasunet.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _coursesState = MutableStateFlow<CoursesUiState>(CoursesUiState.Loading)
    val coursesState: StateFlow<CoursesUiState> = _coursesState.asStateFlow()

    private val _chaptersState = MutableStateFlow<ChaptersUiState>(ChaptersUiState.Loading)
    val chaptersState: StateFlow<ChaptersUiState> = _chaptersState.asStateFlow()

    private val _certificateState = MutableStateFlow<CertificateUiState>(CertificateUiState.Idle)
    val certificateState: StateFlow<CertificateUiState> = _certificateState.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _coursesState.value = CoursesUiState.Loading
            val result = studentRepository.getCourses()
            _coursesState.value = if (result.isSuccess) {
                val courses = result.getOrNull()!!
                if (courses.isEmpty()) {
                    CoursesUiState.Empty
                } else {
                    CoursesUiState.Success(courses)
                }
            } else {
                CoursesUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load courses")
            }
        }
    }

    fun loadChapters(courseId: String) {
        viewModelScope.launch {
            _chaptersState.value = ChaptersUiState.Loading
            val result = studentRepository.getCourseChapters(courseId)
            _chaptersState.value = if (result.isSuccess) {
                val chapters = result.getOrNull()!!
                if (chapters.isEmpty()) {
                    ChaptersUiState.Empty
                } else {
                    ChaptersUiState.Success(chapters)
                }
            } else {
                ChaptersUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load chapters")
            }
        }
    }

    fun markChapterComplete(chapterId: String, courseId: String) {
        viewModelScope.launch {
            val result = studentRepository.markChapterComplete(chapterId)
            if (result.isSuccess) {
                // Reload chapters to update UI
                loadChapters(courseId)
                loadCourses() // Refresh course list to update progress
            }
        }
    }

    fun downloadCertificate(courseId: String) {
        viewModelScope.launch {
            _certificateState.value = CertificateUiState.Loading
            val result = studentRepository.downloadCertificate(courseId)
            _certificateState.value = if (result.isSuccess) {
                CertificateUiState.Success(result.getOrNull()!!.certificate_url)
            } else {
                CertificateUiState.Error(result.exceptionOrNull()?.message ?: "Failed to download certificate")
            }
        }
    }

    fun resetCertificateState() {
        _certificateState.value = CertificateUiState.Idle
    }

    fun logout() {
        authRepository.logout()
    }
}

sealed class CoursesUiState {
    object Loading : CoursesUiState()
    object Empty : CoursesUiState()
    data class Success(val courses: List<Course>) : CoursesUiState()
    data class Error(val message: String) : CoursesUiState()
}

sealed class ChaptersUiState {
    object Loading : ChaptersUiState()
    object Empty : ChaptersUiState()
    data class Success(val chapters: List<Chapter>) : ChaptersUiState()
    data class Error(val message: String) : ChaptersUiState()
}

sealed class CertificateUiState {
    object Idle : CertificateUiState()
    object Loading : CertificateUiState()
    data class Success(val certificateUrl: String) : CertificateUiState()
    data class Error(val message: String) : CertificateUiState()
}

