package com.blank.prasunet.ui.navigation

sealed class Screen(val route: String) {
    // Auth screens
    object Login : Screen("login")
    object Register : Screen("register")

    // Student screens
    object StudentHome : Screen("student_home")
    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: String) = "course_detail/$courseId"
    }
    object ChapterViewer : Screen("chapter_viewer/{courseId}/{chapterId}") {
        fun createRoute(courseId: String, chapterId: String) = "chapter_viewer/$courseId/$chapterId"
    }

    // Mentor screens
    object MentorHome : Screen("mentor_home")
    object CreateCourse : Screen("create_course")
    object MentorCourseDetail : Screen("mentor_course_detail/{courseId}") {
        fun createRoute(courseId: String) = "mentor_course_detail/$courseId"
    }
    object AddChapter : Screen("add_chapter/{courseId}") {
        fun createRoute(courseId: String) = "add_chapter/$courseId"
    }
    object AssignCourse : Screen("assign_course/{courseId}") {
        fun createRoute(courseId: String) = "assign_course/$courseId"
    }
    object ViewStudents : Screen("view_students/{courseId}") {
        fun createRoute(courseId: String) = "view_students/$courseId"
    }

    // Admin screens
    object AdminHome : Screen("admin_home")
    object PendingMentors : Screen("pending_mentors")
    object AllUsers : Screen("all_users")
    object AllCourses : Screen("all_courses")
}

