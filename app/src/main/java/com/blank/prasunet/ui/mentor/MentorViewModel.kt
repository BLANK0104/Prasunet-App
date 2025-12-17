package com.blank.prasunet.ui.mentor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blank.prasunet.data.models.Chapter
import com.blank.prasunet.data.models.Course
import com.blank.prasunet.data.models.StudentWithProgress
import com.blank.prasunet.data.models.User
import com.blank.prasunet.data.repository.AuthRepository
import com.blank.prasunet.data.repository.MentorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MentorViewModel @Inject constructor(
    private val mentorRepository: MentorRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _coursesState = MutableStateFlow<MentorCoursesUiState>(MentorCoursesUiState.Loading)
    val coursesState: StateFlow<MentorCoursesUiState> = _coursesState.asStateFlow()

    private val _createCourseState = MutableStateFlow<CreateCourseUiState>(CreateCourseUiState.Idle)
    val createCourseState: StateFlow<CreateCourseUiState> = _createCourseState.asStateFlow()

    private val _chaptersState = MutableStateFlow<ChaptersUiState>(ChaptersUiState.Loading)
    val chaptersState: StateFlow<ChaptersUiState> = _chaptersState.asStateFlow()

    private val _addChapterState = MutableStateFlow<AddChapterUiState>(AddChapterUiState.Idle)
    val addChapterState: StateFlow<AddChapterUiState> = _addChapterState.asStateFlow()

    private val _studentsState = MutableStateFlow<StudentsUiState>(StudentsUiState.Loading)
    val studentsState: StateFlow<StudentsUiState> = _studentsState.asStateFlow()

    private val _assignCourseState = MutableStateFlow<AssignCourseUiState>(AssignCourseUiState.Idle)
    val assignCourseState: StateFlow<AssignCourseUiState> = _assignCourseState.asStateFlow()

    private val _availableStudentsState = MutableStateFlow<AvailableStudentsUiState>(AvailableStudentsUiState.Loading)
    val availableStudentsState: StateFlow<AvailableStudentsUiState> = _availableStudentsState.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _coursesState.value = MentorCoursesUiState.Loading
            val result = mentorRepository.getMentorCourses()
            _coursesState.value = if (result.isSuccess) {
                val courses = result.getOrNull()!!
                if (courses.isEmpty()) {
                    MentorCoursesUiState.Empty
                } else {
                    MentorCoursesUiState.Success(courses)
                }
            } else {
                MentorCoursesUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load courses")
            }
        }
    }

    fun createCourse(title: String, description: String) {
        if (title.isBlank() || description.isBlank()) {
            _createCourseState.value = CreateCourseUiState.Error("Title and description are required")
            return
        }

        viewModelScope.launch {
            _createCourseState.value = CreateCourseUiState.Loading
            val result = mentorRepository.createCourse(title, description)
            _createCourseState.value = if (result.isSuccess) {
                loadCourses() // Refresh course list
                CreateCourseUiState.Success
            } else {
                CreateCourseUiState.Error(result.exceptionOrNull()?.message ?: "Failed to create course")
            }
        }
    }

    fun loadChapters(courseId: String) {
        viewModelScope.launch {
            _chaptersState.value = ChaptersUiState.Loading
            val result = mentorRepository.getCourseChapters(courseId)
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

    fun addChapter(
        courseId: String,
        title: String,
        content: String,
        imageUrl: String?,
        videoUrl: String?,
        sequenceOrder: Int
    ) {
        if (title.isBlank() || content.isBlank()) {
            _addChapterState.value = AddChapterUiState.Error("Title and content are required")
            return
        }

        viewModelScope.launch {
            _addChapterState.value = AddChapterUiState.Loading
            val result = mentorRepository.addChapter(courseId, title, content, imageUrl, videoUrl, sequenceOrder)
            _addChapterState.value = if (result.isSuccess) {
                loadChapters(courseId) // Refresh chapters list
                AddChapterUiState.Success
            } else {
                AddChapterUiState.Error(result.exceptionOrNull()?.message ?: "Failed to add chapter")
            }
        }
    }

    fun loadEnrolledStudents(courseId: String) {
        viewModelScope.launch {
            _studentsState.value = StudentsUiState.Loading
            val result = mentorRepository.getEnrolledStudents(courseId)
            _studentsState.value = if (result.isSuccess) {
                val students = result.getOrNull()!!
                if (students.isEmpty()) {
                    StudentsUiState.Empty
                } else {
                    StudentsUiState.Success(students)
                }
            } else {
                StudentsUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load students")
            }
        }
    }

    fun resetCreateCourseState() {
        _createCourseState.value = CreateCourseUiState.Idle
    }

    fun assignCourse(courseId: String, studentIds: List<String>) {
        if (studentIds.isEmpty()) {
            _assignCourseState.value = AssignCourseUiState.Error("Please select at least one student")
            return
        }

        viewModelScope.launch {
            _assignCourseState.value = AssignCourseUiState.Loading
            val result = mentorRepository.assignCourse(courseId, studentIds)
            _assignCourseState.value = if (result.isSuccess) {
                AssignCourseUiState.Success
            } else {
                AssignCourseUiState.Error(result.exceptionOrNull()?.message ?: "Failed to assign course")
            }
        }
    }

    fun resetAddChapterState() {
        _addChapterState.value = AddChapterUiState.Idle
    }

    fun loadAvailableStudents() {
        viewModelScope.launch {
            _availableStudentsState.value = AvailableStudentsUiState.Loading
            val result = mentorRepository.getAllStudents()
            _availableStudentsState.value = if (result.isSuccess) {
                val students = result.getOrNull()
                if (students == null) {
                    AvailableStudentsUiState.Error("No student data received")
                } else if (students.isEmpty()) {
                    AvailableStudentsUiState.Empty
                } else {
                    AvailableStudentsUiState.Success(students)
                }
            } else {
                AvailableStudentsUiState.Error(result.exceptionOrNull()?.message ?: "Failed to load students")
            }
        }
    }

    fun resetAssignCourseState() {
        _assignCourseState.value = AssignCourseUiState.Idle
    }

    fun logout() {
        authRepository.logout()
    }
}

sealed class MentorCoursesUiState {
    object Loading : MentorCoursesUiState()
    object Empty : MentorCoursesUiState()
    data class Success(val courses: List<Course>) : MentorCoursesUiState()
    data class Error(val message: String) : MentorCoursesUiState()
}

sealed class CreateCourseUiState {
    object Idle : CreateCourseUiState()
    object Loading : CreateCourseUiState()
    object Success : CreateCourseUiState()
    data class Error(val message: String) : CreateCourseUiState()
}

sealed class ChaptersUiState {
    object Loading : ChaptersUiState()
    object Empty : ChaptersUiState()
    data class Success(val chapters: List<Chapter>) : ChaptersUiState()
    data class Error(val message: String) : ChaptersUiState()
}

sealed class AddChapterUiState {
    object Idle : AddChapterUiState()
    object Loading : AddChapterUiState()
    object Success : AddChapterUiState()
    data class Error(val message: String) : AddChapterUiState()
}

sealed class StudentsUiState {
    object Loading : StudentsUiState()
    object Empty : StudentsUiState()
    data class Success(val students: List<StudentWithProgress>) : StudentsUiState()
    data class Error(val message: String) : StudentsUiState()
}

sealed class AssignCourseUiState {
    object Idle : AssignCourseUiState()
    object Loading : AssignCourseUiState()
    object Success : AssignCourseUiState()
    data class Error(val message: String) : AssignCourseUiState()
}

sealed class AvailableStudentsUiState {
    object Loading : AvailableStudentsUiState()
    object Empty : AvailableStudentsUiState()
    data class Success(val students: List<User>) : AvailableStudentsUiState()
    data class Error(val message: String) : AvailableStudentsUiState()
}

