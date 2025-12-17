package com.blank.prasunet.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllUsersScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val allUsersState by viewModel.allUsersState.collectAsState()
    var userToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadAllUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Users") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (val state = allUsersState) {
                is AllUsersUiState.Loading -> {
                    LoadingIndicator()
                }
                is AllUsersUiState.Empty -> {
                    EmptyState(message = "No users found")
                }
                is AllUsersUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Platform Users",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total: ${state.users.size} users",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Group by role
                        val groupedUsers = state.users.groupBy { it.role }

                        groupedUsers.forEach { (role, users) ->
                            item {
                                Text(
                                    text = "${role.uppercase()}S (${users.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            items(users) { user ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = user.full_name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = user.email,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Row {
                                                Surface(
                                                    color = when(user.role.lowercase()) {
                                                        "student" -> MaterialTheme.colorScheme.primaryContainer
                                                        "mentor" -> MaterialTheme.colorScheme.secondaryContainer
                                                        "admin" -> MaterialTheme.colorScheme.tertiaryContainer
                                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                                    },
                                                    shape = MaterialTheme.shapes.small
                                                ) {
                                                    Text(
                                                        text = user.role.uppercase(),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }

                                                if (user.role.lowercase() == "mentor" && !user.approved) {
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Surface(
                                                        color = MaterialTheme.colorScheme.errorContainer,
                                                        shape = MaterialTheme.shapes.small
                                                    ) {
                                                        Text(
                                                            text = "PENDING",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = MaterialTheme.colorScheme.onErrorContainer,
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Don't allow deleting self or other admins
                                        if (user.role.lowercase() != "admin") {
                                            IconButton(onClick = { userToDelete = user.id }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Delete User",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Delete confirmation dialog
                    if (userToDelete != null) {
                        AlertDialog(
                            onDismissRequest = { userToDelete = null },
                            title = { Text("Delete User") },
                            text = { Text("Are you sure you want to delete this user? This action cannot be undone.") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        viewModel.deleteUser(userToDelete!!)
                                        userToDelete = null
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { userToDelete = null }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
                is AllUsersUiState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.loadAllUsers() }
                    )
                }
            }
        }
    }
}

