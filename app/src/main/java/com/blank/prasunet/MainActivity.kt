package com.blank.prasunet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.blank.prasunet.data.repository.AuthRepository
import com.blank.prasunet.ui.navigation.NavGraph
import com.blank.prasunet.ui.navigation.Screen
import com.blank.prasunet.ui.theme.PrasunetTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PrasunetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Determine start destination based on authentication state
                    val startDestination = if (authRepository.isLoggedIn()) {
                        val user = authRepository.getCurrentUser()
                        when (user?.role?.lowercase()) {
                            "student" -> Screen.StudentHome.route
                            "mentor" -> Screen.MentorHome.route
                            "admin" -> Screen.AdminHome.route
                            else -> Screen.Login.route
                        }
                    } else {
                        Screen.Login.route
                    }

                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}