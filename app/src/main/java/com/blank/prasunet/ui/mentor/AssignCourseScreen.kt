package com.blank.prasunet.ui.mentor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blank.prasunet.data.models.User
import com.blank.prasunet.ui.components.EmptyState
import com.blank.prasunet.ui.components.ErrorMessage
import com.blank.prasunet.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignCourseScreen(
    navController: NavController,
    courseId: String,
    viewModel: MentorViewModel = hiltViewModel()
) {
    val assignCourseState by viewModel.assignCourseState.collectAsState()
    val studentsState by viewModel.availableStudentsState.collectAsState()
    val selectedStudentIds = remember { mutableStateListOf<String>() }

    // Load students when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadAvailableStudents()
    }

    LaunchedEffect(assignCourseState) {
        if (assignCourseState is AssignCourseUiState.Success) {
            // Show success and navigate back
            navController.popBackStack()
        }
    }

    // Navigate back on success
    LaunchedEffect(assignCourseState) {
        if (assignCourseState is AssignCourseUiState.Success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assign Course to Students") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetAssignCourseState()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (selectedStudentIds.isNotEmpty()) {
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${selectedStudentIds.size} student(s) selected",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(
                            onClick = {
                                viewModel.assignCourse(courseId, selectedStudentIds.toList())
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = assignCourseState !is AssignCourseUiState.Loading
                        ) {
                            if (assignCourseState is AssignCourseUiState.Loading) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text("Assign Course to Selected Students")
                            }
                        }

                        if (assignCourseState is AssignCourseUiState.Error) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = (assignCourseState as AssignCourseUiState.Error).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = studentsState) {
                is AvailableStudentsUiState.Loading -> {
                    LoadingIndicator()
                }
                is AvailableStudentsUiState.Empty -> {
                    EmptyState(message = "No students available")
                }
                is AvailableStudentsUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadAvailableStudents() }
                    )
                }
                is AvailableStudentsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Select Students",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Choose one or more students to assign this course to:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(state.students) { student ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = selectedStudentIds.contains(student.id),
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                selectedStudentIds.add(student.id)
                                            } else {
                                                selectedStudentIds.remove(student.id)
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = student.full_name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = student.email,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // Bottom padding for bottom bar
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

