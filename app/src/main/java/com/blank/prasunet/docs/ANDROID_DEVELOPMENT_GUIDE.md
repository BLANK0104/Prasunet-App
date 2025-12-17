# Android App Development Guide for Internship LMS

Complete guide for building the Android mobile application that consumes the Backend API.

---

## 📱 Android App Architecture Overview

### Technology Stack
- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Data Serialization**: Gson
- **Secure Storage**: EncryptedSharedPreferences
- **Testing**: JUnit5, MockK, Espresso

### Project Structure
```
app/
├── data/
│   ├── api/          # Retrofit API interfaces
│   ├── models/       # Data models (DTOs)
│   ├── repository/   # Repository pattern implementations
│   └── local/        # Local storage (SharedPreferences)
├── domain/
│   ├── models/       # Domain models
│   └── usecases/     # Business logic use cases
├── ui/
│   ├── screens/      # Compose screens
│   ├── viewmodels/   # ViewModels
│   ├── components/   # Reusable UI components
│   └── navigation/   # Navigation setup
├── di/               # Hilt dependency injection modules
└── utils/            # Utility classes
```

---

## 🚀 Initial Setup

### 1. Create New Android Project
- Open Android Studio
- Create new project: "Empty Compose Activity"
- Name: `InternshipLMS`
- Package: `com.prasunet.internshiplms`
- Language: Kotlin
- Minimum SDK: API 24 (Android 7.0)

### 2. Update build.gradle (Project level)

```gradle
plugins {
    id 'com.android.application' version '8.2.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.20' apply false
    id 'com.google.dagger.hilt.android' version '2.48' apply false
}
```

### 3. Update build.gradle (App level)

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'
    id 'com.google.dagger.hilt.android'
}

android {
    namespace 'com.prasunet.internshiplms'
    compileSdk 34

    defaultConfig {
        applicationId "com.prasunet.internshiplms"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        
        // Backend API URL - Use production Vercel deployment
        buildConfigField "String", "BASE_URL", "\"https://prasunet.vercel.app/api/\""
    }

    buildTypes {
        debug {
            // For local testing, use: "http://10.0.2.2:3000/api/"
            buildConfigField "String", "BASE_URL", "\"https://prasunet.vercel.app/api/\""
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            buildConfigField "String", "BASE_URL", "\"https://prasunet.vercel.app/api/\""
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = '17'
    }
    
    buildFeatures {
        compose true
        buildConfig true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.4'
    }
    
    packaging {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
}

dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.2'
    implementation 'androidx.activity:activity-compose:1.8.1'
    
    // Jetpack Compose
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-graphics'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.navigation:navigation-compose:2.7.5'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2'
    
    // Hilt Dependency Injection
    implementation 'com.google.dagger:hilt-android:2.48'
    kapt 'com.google.dagger:hilt-android-compiler:2.48'
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
    
    // Retrofit + OkHttp
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    
    // Gson
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Encrypted SharedPreferences
    implementation 'androidx.security:security-crypto:1.1.0-alpha06'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Coil (Image Loading)
    implementation 'io.coil-kt:coil-compose:2.5.0'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'io.mockk:mockk:1.13.8'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation platform('androidx.compose:compose-bom:2023.10.01')
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
    debugImplementation 'androidx.compose.ui:ui-tooling'
    debugImplementation 'androidx.compose.ui:ui-test-manifest'
}

kapt {
    correctErrorTypes true
}
```

### 4. Update AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:name=".InternshipLmsApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.InternshipLMS"
        android:usesCleartextTraffic="true">
        
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.InternshipLMS">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

---

## 📦 Data Layer Implementation

### 1. Data Models (`data/models/`)

**AuthModels.kt**
```kotlin
data class RegisterRequest(
    val email: String,
    val password: String,
    val full_name: String,
    val role: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val full_name: String,
    val role: String,
    val approved: Boolean
)
```

**CourseModels.kt**
```kotlin
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val mentor_name: String? = null,
    val total_chapters: Int,
    val completed_chapters: Int,
    val progress_percentage: Int,
    val created_at: String
)

data class Chapter(
    val id: String,
    val title: String,
    val content: String,
    val image_url: String?,
    val video_url: String?,
    val sequence_order: Int,
    val is_completed: Boolean,
    val is_unlocked: Boolean
)

data class Progress(
    val total_chapters: Int,
    val completed_chapters: Int,
    val progress_percentage: Int,
    val chapters: List<ChapterProgress>
)

data class ChapterProgress(
    val id: String,
    val title: String,
    val sequence_order: Int,
    val is_completed: Boolean,
    val completed_at: String?
)

data class CertificateResponse(
    val certificate_url: String,
    val message: String
)
```

**ApiResponse.kt**
```kotlin
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
)

data class ErrorResponse(
    val error: String
)
```

### 2. API Interface (`data/api/`)

**ApiService.kt**
```kotlin
interface ApiService {
    // Auth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<LoginResponse>>
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    // Student
    @GET("student/courses")
    suspend fun getStudentCourses(): Response<CoursesResponse>
    
    @GET("student/courses/{courseId}/chapters")
    suspend fun getCourseChapters(@Path("courseId") courseId: String): Response<ChaptersResponse>
    
    @POST("student/chapters/{chapterId}/complete")
    suspend fun markChapterComplete(@Path("chapterId") chapterId: String): Response<ApiResponse<String>>
    
    @GET("student/courses/{courseId}/progress")
    suspend fun getCourseProgress(@Path("courseId") courseId: String): Response<Progress>
    
    @GET("student/courses/{courseId}/certificate")
    suspend fun downloadCertificate(@Path("courseId") courseId: String): Response<CertificateResponse>
    
    // Mentor
    @POST("mentor/courses")
    suspend fun createCourse(@Body request: CreateCourseRequest): Response<CourseCreatedResponse>
    
    @GET("mentor/courses")
    suspend fun getMentorCourses(): Response<CoursesResponse>
    
    @POST("mentor/courses/{courseId}/chapters")
    suspend fun addChapter(
        @Path("courseId") courseId: String,
        @Body request: CreateChapterRequest
    ): Response<ChapterCreatedResponse>
    
    @POST("mentor/courses/{courseId}/assign")
    suspend fun assignCourse(
        @Path("courseId") courseId: String,
        @Body request: AssignCourseRequest
    ): Response<ApiResponse<String>>
    
    @GET("mentor/courses/{courseId}/students")
    suspend fun getEnrolledStudents(@Path("courseId") courseId: String): Response<StudentsResponse>
    
    // Admin
    @GET("admin/mentors/pending")
    suspend fun getPendingMentors(): Response<MentorsResponse>
    
    @PUT("admin/mentors/{mentorId}/approve")
    suspend fun approveMentor(
        @Path("mentorId") mentorId: String,
        @Body request: ApproveMentorRequest
    ): Response<ApiResponse<String>>
    
    @GET("admin/statistics")
    suspend fun getStatistics(): Response<Statistics>
    
    @GET("admin/users")
    suspend fun getAllUsers(): Response<UsersResponse>
}

data class CoursesResponse(val courses: List<Course>)
data class ChaptersResponse(val chapters: List<Chapter>)
data class CreateCourseRequest(val title: String, val description: String)
data class CourseCreatedResponse(val message: String, val course: Course)
data class CreateChapterRequest(
    val title: String,
    val content: String,
    val image_url: String?,
    val video_url: String?,
    val sequence_order: Int
)
data class ChapterCreatedResponse(val message: String, val chapter: Chapter)
data class AssignCourseRequest(val student_ids: List<String>)
data class StudentsResponse(val students: List<StudentWithProgress>)
data class StudentWithProgress(
    val id: String,
    val email: String,
    val full_name: String,
    val progress_percentage: Int
)
data class MentorsResponse(val mentors: List<User>)
data class ApproveMentorRequest(val approved: Boolean)
data class UsersResponse(val users: List<User>)
data class Statistics(
    val users: UserStats,
    val courses: CourseStats,
    val pending_mentors: Int
)
data class UserStats(val total: Int, val students: Int, val mentors: Int)
data class CourseStats(val total: Int)
```

### 3. Token Manager (`data/local/`)

**TokenManager.kt**
```kotlin
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        encryptedPrefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return encryptedPrefs.getString(KEY_TOKEN, null)
    }

    fun saveUser(user: User) {
        val gson = Gson()
        val userJson = gson.toJson(user)
        encryptedPrefs.edit().putString(KEY_USER, userJson).apply()
    }

    fun getUser(): User? {
        val userJson = encryptedPrefs.getString(KEY_USER, null) ?: return null
        return Gson().fromJson(userJson, User::class.java)
    }

    fun clearAll() {
        encryptedPrefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_USER = "user_data"
    }
}
```

### 4. Interceptor for Authentication

**AuthInterceptor.kt**
```kotlin
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenManager.getToken()

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}
```

### 5. Dependency Injection (`di/`)

**NetworkModule.kt**
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }
}
```

### 6. Repository Pattern

**AuthRepository.kt**
```kotlin
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        role: String
    ): Result<LoginResponse> {
        return try {
            val response = apiService.register(
                RegisterRequest(email, password, fullName, role)
            )
            if (response.isSuccessful && response.body()?.data != null) {
                Result.success(response.body()!!.data!!)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                Result.failure(Exception(error?.error ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                tokenManager.saveToken(loginResponse.token)
                tokenManager.saveUser(loginResponse.user)
                Result.success(loginResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val error = Gson().fromJson(errorBody, ErrorResponse::class.java)
                Result.failure(Exception(error?.error ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        tokenManager.clearAll()
    }

    fun getCurrentUser(): User? {
        return tokenManager.getUser()
    }
}
```

---

## 🎨 UI Layer Implementation

### Navigation Setup

**Screen.kt**
```kotlin
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object StudentDashboard : Screen("student_dashboard")
    object CourseDetails : Screen("course_details/{courseId}") {
        fun createRoute(courseId: String) = "course_details/$courseId"
    }
    object ChapterViewer : Screen("chapter_viewer/{chapterId}") {
        fun createRoute(chapterId: String) = "chapter_viewer/$chapterId"
    }
    object MentorDashboard : Screen("mentor_dashboard")
    object AdminDashboard : Screen("admin_dashboard")
}
```

**NavGraph.kt**
```kotlin
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.StudentDashboard.route) {
            StudentDashboardScreen(navController)
        }
        composable(
            route = Screen.CourseDetails.route,
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")!!
            CourseDetailsScreen(navController, courseId)
        }
        // Add more routes...
    }
}
```

### Example ViewModel

**LoginViewModel.kt**
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = authRepository.login(email, password)
            _uiState.value = if (result.isSuccess) {
                val user = result.getOrNull()!!.user
                LoginUiState.Success(user)
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
```

### Example Screen

**LoginScreen.kt**
```kotlin
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            val user = (uiState as LoginUiState.Success).user
            when (user.role) {
                "student" -> navController.navigate(Screen.StudentDashboard.route)
                "mentor" -> navController.navigate(Screen.MentorDashboard.route)
                "admin" -> navController.navigate(Screen.AdminDashboard.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Login")
            }
        }
        
        if (uiState is LoginUiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (uiState as LoginUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("Don't have an account? Register")
        }
    }
}
```

---

## 🧪 Testing Strategy

### Unit Test Example

**LoginViewModelTest.kt**
```kotlin
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success updates state correctly`() = runTest {
        // Given
        val user = User("1", "test@test.com", "Test User", "student", true)
        val loginResponse = LoginResponse("token", user)
        coEvery { authRepository.login(any(), any()) } returns Result.success(loginResponse)

        // When
        viewModel.login("test@test.com", "password")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is LoginUiState.Success)
        assertEquals(user, (viewModel.uiState.value as LoginUiState.Success).user)
    }

    @Test
    fun `login failure updates state with error`() = runTest {
        // Given
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception("Invalid credentials"))

        // When
        viewModel.login("wrong@test.com", "wrong")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is LoginUiState.Error)
    }
}
```

---

## 📱 Key Features Implementation Guide

### 1. Sequential Chapter Access

Implement in `ChapterListScreen`:
```kotlin
chapters.forEach { chapter ->
    ChapterCard(
        chapter = chapter,
        onClick = {
            if (chapter.is_unlocked) {
                navController.navigate(Screen.ChapterViewer.createRoute(chapter.id))
            } else {
                showLockedChapterDialog = true
            }
        },
        isLocked = !chapter.is_unlocked
    )
}
```

### 2. Progress Tracking

Use `LinearProgressIndicator`:
```kotlin
LinearProgressIndicator(
    progress = progress.progress_percentage / 100f,
    modifier = Modifier.fillMaxWidth()
)
Text("${progress.progress_percentage}% Complete")
```

### 3. Certificate Download

```kotlin
Button(
    onClick = { viewModel.downloadCertificate(courseId) },
    enabled = progress.progress_percentage == 100
) {
    Text("Download Certificate")
}
```

Open PDF using Intent:
```kotlin
val intent = Intent(Intent.ACTION_VIEW).apply {
    data = Uri.parse(certificateUrl)
}
context.startActivity(intent)
```

---

## 🚀 Build & Run

### Run on Emulator
1. Create Android Virtual Device (API 24+)
2. Click "Run" in Android Studio
3. Ensure backend server is running on `localhost:3000`

### Generate APK
```
Build > Build Bundle(s) / APK(s) > Build APK(s)
```

### Generate Signed APK
```
Build > Generate Signed Bundle / APK
```

---

## 📋 Required Screens Checklist

- [ ] Login Screen
- [ ] Registration Screen
- [ ] Student Dashboard (Course List)
- [ ] Course Details (Chapters with lock status)
- [ ] Chapter Viewer (Content, Image, Video)
- [ ] Progress Screen
- [ ] Certificate Download Screen
- [ ] Mentor Dashboard
- [ ] Create Course Screen
- [ ] Add Chapter Screen
- [ ] Student Progress Tracking
- [ ] Admin Dashboard
- [ ] Approve Mentors Screen
- [ ] Platform Statistics Screen

---

## 🔐 Security Considerations

1. **Token Storage**: Use EncryptedSharedPreferences
2. **Network Security**: HTTPS in production
3. **Input Validation**: Validate all user inputs
4. **Error Handling**: Never expose sensitive error details
5. **Certificate Pinning**: Consider for production

---

## 🎓 AI Usage Disclosure

Include in your README:

```markdown
## AI Usage Disclosure

This Android application was developed with assistance from AI tools including:
- GitHub Copilot for code suggestions and boilerplate
- ChatGPT for architecture decisions and best practices

All AI-generated code has been:
- Reviewed line-by-line for correctness and security
- Tested thoroughly with unit and integration tests
- Modified to meet specific project requirements
- Understood completely by the developer

The developer can explain all implementation details and architectural decisions.
```

---

## 📞 Backend Integration Testing

Test with Postman or cURL before integrating:

```bash
# Login
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"student@test.com","password":"password123"}'

# Get Courses (with token)
curl -X GET http://localhost:3000/api/student/courses \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Network timeout | Increase timeout in OkHttpClient |
| 401 Unauthorized | Check token storage and retrieval |
| Empty response | Verify API endpoint and request format |
| UI not updating | Use StateFlow and collectAsState |
| App crash on rotation | Use ViewModel for state management |

---

## 📚 Additional Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [MVVM Architecture Guide](https://developer.android.com/topic/architecture)
- [Material Design 3](https://m3.material.io/)

---

**This guide provides everything needed to build the Android app. Good luck with your TDD Kata!** 🚀
