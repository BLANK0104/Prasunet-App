# 🎓 Prasunet Internship LMS - Android App

A complete mobile Learning Management System for internship programs with role-based access control (Student/Mentor/Admin), built with **Kotlin** and **Jetpack Compose**.

## 📱 Features

### 👨‍🎓 Student Features
- ✅ Self-registration and secure login
- ✅ View assigned courses with progress tracking
- ✅ Sequential chapter access (locked/unlocked progression)
- ✅ Rich content viewing (text, images, video links)
- ✅ Mark chapters as complete
- ✅ Real-time progress tracking
- ✅ Certificate download (coming soon)

### 👨‍🏫 Mentor Features  
- ✅ Create and manage courses
- ✅ Add chapters with content, images, and video URLs
- ✅ View enrolled students and their progress
- ✅ Track completion rates
- 📝 Assign courses to students (via admin/backend)

### 👑 Admin Features
- ✅ Approve/reject mentor registrations
- ✅ View platform-wide statistics
- ✅ Monitor users, courses, and enrollments
- ✅ Platform oversight and management

## 🏗️ Architecture

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp
- **Secure Storage**: EncryptedSharedPreferences
- **Image Loading**: Coil
- **Navigation**: Navigation Compose

## 🚀 Tech Stack

```
├── Data Layer
│   ├── Models (User, Course, Chapter, Progress, etc.)
│   ├── API Service (Retrofit interfaces)
│   ├── Repositories (Auth, Student, Mentor, Admin)
│   └── Local Storage (TokenManager with encryption)
│
├── Domain Layer
│   └── Business Logic (handled in ViewModels)
│
└── UI Layer
    ├── Screens (Compose UI)
    ├── ViewModels (State management)
    ├── Components (Reusable UI elements)
    ├── Theme (Material 3 theming)
    └── Navigation (NavGraph)
```

## 📦 Setup & Installation

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK API 24+ (Android 7.0+)
- Internet connection for API access

### Backend API
The app connects to: **https://prasunet.vercel.app/api**

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Prasunet
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory

3. **Sync Gradle**
   ```bash
   ./gradlew build
   ```

4. **Run the App**
   - Connect an Android device (API 24+) or start an emulator
   - Click "Run" in Android Studio
   - Or use command line:
   ```bash
   ./gradlew installDebug
   ```

## 🔐 Authentication Flow

1. **Register**: Users can register as Student or Mentor
   - Students: Immediate access after registration
   - Mentors: Require admin approval before access
   - Admins: Must be created via backend

2. **Login**: Email/password authentication with JWT tokens
3. **Auto-login**: Secure token storage for persistent sessions

## 📱 App Structure

```
app/src/main/java/com/blank/prasunet/
├── data/
│   ├── api/
│   │   └── ApiService.kt                 # Retrofit API endpoints
│   ├── local/
│   │   └── TokenManager.kt               # Secure token storage
│   ├── models/                           # Data models
│   │   ├── User.kt
│   │   ├── Course.kt
│   │   ├── Chapter.kt
│   │   ├── Progress.kt
│   │   └── ...
│   ├── network/
│   │   └── AuthInterceptor.kt            # JWT token injection
│   └── repository/                       # Data repositories
│       ├── AuthRepository.kt
│       ├── StudentRepository.kt
│       ├── MentorRepository.kt
│       └── AdminRepository.kt
│
├── di/
│   └── NetworkModule.kt                  # Hilt DI modules
│
├── ui/
│   ├── auth/                             # Authentication screens
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   └── ViewModels
│   ├── student/                          # Student screens
│   │   ├── StudentHomeScreen.kt
│   │   ├── CourseDetailScreen.kt
│   │   ├── ChapterViewerScreen.kt
│   │   └── StudentViewModel.kt
│   ├── mentor/                           # Mentor screens
│   │   ├── MentorHomeScreen.kt
│   │   ├── CreateCourseScreen.kt
│   │   ├── AddChapterScreen.kt
│   │   ├── MentorCourseDetailScreen.kt
│   │   ├── ViewStudentsScreen.kt
│   │   └── MentorViewModel.kt
│   ├── admin/                            # Admin screens
│   │   ├── AdminHomeScreen.kt
│   │   └── AdminViewModel.kt
│   ├── components/                       # Reusable components
│   │   ├── CommonComponents.kt
│   │   └── CourseComponents.kt
│   ├── navigation/                       # Navigation setup
│   │   ├── Screen.kt
│   │   └── NavGraph.kt
│   └── theme/                            # Material 3 theme
│       ├── Color.kt
│       ├── Type.kt
│       └── Theme.kt
│
├── MainActivity.kt                       # Entry point
└── PrasunetApplication.kt                # Hilt application class
```

## 🎨 UI Screenshots

### Student Flow
1. **Login Screen**: Email/password authentication
2. **Student Home**: List of assigned courses with progress
3. **Course Details**: Chapters with sequential unlock
4. **Chapter Viewer**: Content with images, videos, and completion button

### Mentor Flow
1. **Mentor Home**: Created courses list
2. **Create Course**: Title and description form
3. **Course Management**: Add/view chapters
4. **Student Progress**: Monitor enrolled students

### Admin Flow
1. **Admin Dashboard**: Platform statistics
2. **Pending Mentors**: Approve/reject requests

## 🔧 Configuration

### API Base URL
Located in `app/build.gradle.kts`:
```kotlin
buildConfigField("String", "BASE_URL", "\"https://prasunet.vercel.app/api/\"")
```

### Theme Customization
Edit colors in `ui/theme/Color.kt`:
```kotlin
val PrimaryBlue = Color(0xFF2196F3)
val SecondaryGreen = Color(0xFF4CAF50)
```

## 📝 API Integration

All API calls are handled through Retrofit interfaces:

```kotlin
// Example: Student getting courses
suspend fun getCourses(): Response<CoursesResponse>

// Example: Mentor creating a course
suspend fun createCourse(@Body request: CreateCourseRequest): Response<CourseCreatedResponse>

// Example: Admin approving mentor
suspend fun approveMentor(@Path("mentorId") mentorId: String, @Body request: ApproveMentorRequest): Response<ApiResponse<String>>
```

## 🔒 Security

- **Encrypted Storage**: JWT tokens stored using `EncryptedSharedPreferences`
- **HTTPS Only**: All API calls use secure HTTPS connections
- **Token Injection**: Automatic Bearer token injection via OkHttp interceptor
- **Role-Based Access**: Server-side validation of user roles

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## 📚 Dependencies

Key dependencies (see `gradle/libs.versions.toml` for versions):

- **Jetpack Compose**: Modern declarative UI
- **Hilt**: Dependency injection
- **Retrofit**: REST API client
- **OkHttp**: HTTP client with interceptors
- **Coil**: Image loading for Compose
- **Navigation Compose**: Type-safe navigation
- **Security Crypto**: Encrypted data storage
- **Material 3**: Design system components

## 🚀 Building for Production

### Generate Release APK
```bash
./gradlew assembleRelease
```

### Generate Signed APK
1. Create a keystore:
   ```bash
   keytool -genkey -v -keystore prasunet.keystore -alias prasunet -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Add signing config to `app/build.gradle.kts`:
   ```kotlin
   signingConfigs {
       create("release") {
           storeFile = file("prasunet.keystore")
           storePassword = "your_password"
           keyAlias = "prasunet"
           keyPassword = "your_password"
       }
   }
   ```

3. Build:
   ```bash
   ./gradlew assembleRelease
   ```

## 📄 License

This project is part of the Prasunet internship program.

## 👥 Contributors

Built with ❤️ by the Prasunet team

## 📞 Support

For issues or questions, please contact the development team or create an issue in the repository.

---

**Note**: Ensure the backend API is running and accessible before using the app. Default backend URL: `https://prasunet.vercel.app/api`

