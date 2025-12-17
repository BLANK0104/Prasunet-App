package com.blank.prasunet.ui.mentor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blank.prasunet.ui.components.CourseCard
import com.blank.prasunet.ui.components.EmptyState
import com.blank.prasunet.ui.components.ErrorMessage
import com.blank.prasunet.ui.components.LoadingIndicator
import com.blank.prasunet.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorHomeScreen(
    navController: NavController,
    viewModel: MentorViewModel = hiltViewModel()
) {
    val coursesState by viewModel.coursesState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courses") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateCourse.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Course")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = coursesState) {
                is MentorCoursesUiState.Loading -> {
                    LoadingIndicator()
                }
                is MentorCoursesUiState.Empty -> {
                    EmptyState(message = "No courses created yet. Click + to create one!")
                }
                is MentorCoursesUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Welcome, Mentor!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Manage your courses and students",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(state.courses) { course ->
                            CourseCard(
                                title = course.title,
                                description = course.description,
                                progress = 0,
                                totalChapters = course.total_chapters,
                                completedChapters = 0,
                                onClick = {
                                    navController.navigate(Screen.MentorCourseDetail.createRoute(course.id))
                                }
                            )
                        }
                    }
                }
                is MentorCoursesUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadCourses() }
                    )
                }
            }
        }
    }
}

