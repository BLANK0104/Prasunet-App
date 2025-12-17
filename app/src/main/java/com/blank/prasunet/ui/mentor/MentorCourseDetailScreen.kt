package com.blank.prasunet.ui.mentor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.blank.prasunet.ui.components.EmptyState
import com.blank.prasunet.ui.components.ErrorMessage
import com.blank.prasunet.ui.components.LoadingIndicator
import com.blank.prasunet.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorCourseDetailScreen(
    navController: NavController,
    courseId: String,
    viewModel: MentorViewModel = hiltViewModel()
) {
    val chaptersState by viewModel.chaptersState.collectAsState()

    LaunchedEffect(courseId) {
        viewModel.loadChapters(courseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course Chapters") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddChapter.createRoute(courseId)) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Chapter")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = chaptersState) {
                is ChaptersUiState.Loading -> {
                    LoadingIndicator()
                }
                is ChaptersUiState.Empty -> {
                    EmptyState(message = "No chapters yet. Click + to add one!")
                }
                is ChaptersUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Course Chapters",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(state.chapters) { chapter ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${chapter.sequence_order}. ${chapter.title}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = chapter.content.take(100) + if (chapter.content.length > 100) "..." else "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { navController.navigate(Screen.AssignCourse.createRoute(courseId)) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Assign Students")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { navController.navigate(Screen.ViewStudents.createRoute(courseId)) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("View Enrolled Students")
                            }
                        }
                    }
                }
                is ChaptersUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadChapters(courseId) }
                    )
                }
            }
        }
    }
}

