package com.blank.prasunet.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCoursesScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val allCoursesState by viewModel.allCoursesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllCourses()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Courses") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = allCoursesState) {
                is AllCoursesUiState.Loading -> {
                    LoadingIndicator()
                }
                is AllCoursesUiState.Empty -> {
                    EmptyState(message = "No courses found")
                }
                is AllCoursesUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Platform Courses",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total: ${state.courses.size} courses",
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
                                totalChapters = course.total_chapters.toIntOrNull() ?: 0,
                                completedChapters = 0,
                                onClick = { /* Admin view - no navigation */ }
                            )
                        }
                    }
                }
                is AllCoursesUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadAllCourses() }
                    )
                }
            }
        }
    }
}

