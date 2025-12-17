package com.blank.prasunet.ui.mentor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChapterScreen(
    navController: NavController,
    courseId: String,
    viewModel: MentorViewModel = hiltViewModel()
) {
    val addChapterState by viewModel.addChapterState.collectAsState()
    val chaptersState by viewModel.chaptersState.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }

    // Calculate next sequence order
    val nextSequenceOrder = remember(chaptersState) {
        if (chaptersState is ChaptersUiState.Success) {
            val chapters = (chaptersState as ChaptersUiState.Success).chapters
            if (chapters.isEmpty()) 1 else chapters.maxOf { it.sequence_order } + 1
        } else 1
    }

    LaunchedEffect(addChapterState) {
        if (addChapterState is AddChapterUiState.Success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Chapter") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetAddChapterState()
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Chapter #$nextSequenceOrder",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Chapter Title") },
                modifier = Modifier.fillMaxWidth(),
                enabled = addChapterState !is AddChapterUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Chapter Content") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 8,
                enabled = addChapterState !is AddChapterUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("Image URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = addChapterState !is AddChapterUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("Video URL (optional)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = addChapterState !is AddChapterUiState.Loading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.addChapter(
                        courseId = courseId,
                        title = title,
                        content = content,
                        imageUrl = imageUrl.takeIf { it.isNotBlank() },
                        videoUrl = videoUrl.takeIf { it.isNotBlank() },
                        sequenceOrder = nextSequenceOrder
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = addChapterState !is AddChapterUiState.Loading
            ) {
                if (addChapterState is AddChapterUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Add Chapter")
                }
            }

            if (addChapterState is AddChapterUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (addChapterState as AddChapterUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

