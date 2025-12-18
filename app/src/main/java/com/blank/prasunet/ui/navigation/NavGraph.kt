package com.blank.prasunet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.blank.prasunet.ui.admin.AdminHomeScreen
import com.blank.prasunet.ui.admin.AllCoursesScreen
import com.blank.prasunet.ui.admin.AllUsersScreen
import com.blank.prasunet.ui.admin.EnhancedAdminDashboard
import com.blank.prasunet.ui.admin.EnhancedAllUsersScreen
import com.blank.prasunet.ui.admin.EnhancedAllCoursesScreen
import com.blank.prasunet.ui.admin.EnrollmentsManagementScreen
import com.blank.prasunet.ui.admin.CertificatesManagementScreen
import com.blank.prasunet.ui.auth.LoginScreen
import com.blank.prasunet.ui.auth.RegisterScreen
import com.blank.prasunet.ui.mentor.AddChapterScreen
import com.blank.prasunet.ui.mentor.AssignCourseScreen
import com.blank.prasunet.ui.mentor.CreateCourseScreen
import com.blank.prasunet.ui.mentor.MentorCourseDetailScreen
import com.blank.prasunet.ui.mentor.MentorHomeScreen
import com.blank.prasunet.ui.mentor.ViewStudentsScreen
import com.blank.prasunet.ui.student.ChapterViewerScreen
import com.blank.prasunet.ui.student.CourseDetailScreen
import com.blank.prasunet.ui.student.StudentHomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth screens
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        // Student screens
        composable(Screen.StudentHome.route) {
            StudentHomeScreen(navController)
        }

        composable(
            route = Screen.CourseDetail.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            CourseDetailScreen(navController, courseId)
        }

        composable(
            route = Screen.ChapterViewer.route,
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("chapterId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            val chapterId = backStackEntry.arguments?.getString("chapterId")!!
            ChapterViewerScreen(navController, courseId, chapterId)
        }

        // Mentor screens
        composable(Screen.MentorHome.route) {
            MentorHomeScreen(navController)
        }

        composable(Screen.CreateCourse.route) {
            CreateCourseScreen(navController)
        }

        composable(
            route = Screen.MentorCourseDetail.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            MentorCourseDetailScreen(navController, courseId)
        }

        composable(
            route = Screen.AddChapter.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            AddChapterScreen(navController, courseId)
        }

        composable(
            route = Screen.AssignCourse.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            AssignCourseScreen(navController, courseId)
        }

        composable(
            route = Screen.ViewStudents.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            ViewStudentsScreen(navController, courseId)
        }

        // Admin screens - Enhanced with full control
        composable(Screen.AdminHome.route) {
            EnhancedAdminDashboard(navController)
        }

        composable(Screen.AllUsers.route) {
            EnhancedAllUsersScreen(navController)
        }

        composable(Screen.AllCourses.route) {
            EnhancedAllCoursesScreen(navController)
        }

        composable(Screen.Enrollments.route) {
            EnrollmentsManagementScreen(navController)
        }

        composable(Screen.Certificates.route) {
            CertificatesManagementScreen(navController)
        }
    }
}

