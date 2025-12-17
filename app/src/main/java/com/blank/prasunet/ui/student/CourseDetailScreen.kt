package com.blank.prasunet.ui.student

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
import com.blank.prasunet.ui.components.ChapterListItem
import com.blank.prasunet.ui.components.EmptyState
import com.blank.prasunet.ui.components.ErrorMessage
import com.blank.prasunet.ui.components.LoadingIndicator
import com.blank.prasunet.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    navController: NavController,
    courseId: String,
    viewModel: StudentViewModel = hiltViewModel()
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = chaptersState) {
                is ChaptersUiState.Loading -> {
                    LoadingIndicator()
                }
                is ChaptersUiState.Empty -> {
                    EmptyState(message = "No chapters available yet")
                }
                is ChaptersUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Course Progress",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Complete chapters in order to unlock the next one",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val completedCount = state.chapters.count { it.is_completed }
                            val totalCount = state.chapters.size
                            val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
                            val progressPercentage = (progress * 100).toInt()

                            LinearProgressIndicator(
                                progress = progress,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$completedCount / $totalCount chapters completed ($progressPercentage%)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Certificate button when 100% complete
                            if (progressPercentage == 100) {
                                val certificateState by viewModel.certificateState.collectAsState()
                                val context = androidx.compose.ui.platform.LocalContext.current

                                LaunchedEffect(certificateState) {
                                    if (certificateState is CertificateUiState.Success) {
                                        val url = (certificateState as CertificateUiState.Success).certificateUrl
                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                        context.startActivity(intent)
                                        viewModel.resetCertificateState()
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "🎉 Course Completed!",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Congratulations! You've completed all chapters.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = { viewModel.downloadCertificate(courseId) },
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = certificateState !is CertificateUiState.Loading
                                        ) {
                                            if (certificateState is CertificateUiState.Loading) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(20.dp),
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            } else {
                                                Text("Download Certificate")
                                            }
                                        }

                                        if (certificateState is CertificateUiState.Error) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = (certificateState as CertificateUiState.Error).message,
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(state.chapters) { chapter ->
                            ChapterListItem(
                                title = chapter.title,
                                sequenceOrder = chapter.sequence_order,
                                isCompleted = chapter.is_completed,
                                isLocked = !chapter.is_unlocked,
                                onClick = {
                                    if (chapter.is_unlocked) {
                                        navController.navigate(
                                            Screen.ChapterViewer.createRoute(courseId, chapter.id)
                                        )
                                    }
                                }
                            )
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

