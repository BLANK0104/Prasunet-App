package com.blank.prasunet.ui.mentor

import androidx.compose.foundation.layout.*
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
fun CreateCourseScreen(
    navController: NavController,
    viewModel: MentorViewModel = hiltViewModel()
) {
    val createCourseState by viewModel.createCourseState.collectAsState()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(createCourseState) {
        if (createCourseState is CreateCourseUiState.Success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Course") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetCreateCourseState()
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Course Title") },
                modifier = Modifier.fillMaxWidth(),
                enabled = createCourseState !is CreateCourseUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Course Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                enabled = createCourseState !is CreateCourseUiState.Loading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.createCourse(title, description) },
                modifier = Modifier.fillMaxWidth(),
                enabled = createCourseState !is CreateCourseUiState.Loading
            ) {
                if (createCourseState is CreateCourseUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Create Course")
                }
            }

            if (createCourseState is CreateCourseUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (createCourseState as CreateCourseUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

