# 📋 API Endpoints Quick Reference

**Production URL:** `https://prasunet.vercel.app/api`  
**Local Development:** `http://localhost:3000/api`

---

## 🔐 Authentication Endpoints

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| `POST` | `/auth/register` | Public | Register student or mentor |
| `POST` | `/auth/login` | Public | Login and get JWT token |

**Register Request:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "full_name": "John Doe",
  "role": "student"
}
```

**Login Response:**
```json
{
  "token": "eyJhbGc...",
  "user": {
    "id": "uuid",
    "email": "user@example.com",
    "role": "student"
  }
}
```

---

## 👨‍🎓 Student Endpoints

**All require:** `Authorization: Bearer <token>` + `student` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/student/courses` | Get assigned courses |
| `GET` | `/student/courses/:id/chapters` | Get course chapters with lock status |
| `POST` | `/student/chapters/:id/complete` | Mark chapter complete |
| `GET` | `/student/courses/:id/progress` | Get detailed progress |
| `GET` | `/student/courses/:id/certificate` | Download certificate (100% required) |

**Example: Get Courses**
```bash
GET /student/courses
Authorization: Bearer <token>

Response:
{
  "courses": [
    {
      "id": "uuid",
      "title": "Web Development",
      "progress_percentage": 45,
      "total_chapters": 10,
      "completed_chapters": 4
    }
  ]
}
```

---

## 👨‍🏫 Mentor Endpoints

**All require:** `Authorization: Bearer <token>` + `mentor` role + approved status

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/mentor/courses` | Create new course |
| `GET` | `/mentor/courses` | Get my courses |
| `POST` | `/mentor/courses/:id/chapters` | Add chapter to course |
| `GET` | `/mentor/courses/:id/chapters` | Get course chapters |
| `POST` | `/mentor/courses/:id/assign` | Assign course to students |
| `GET` | `/mentor/courses/:id/students` | Get enrolled students |
| `GET` | `/mentor/courses/:courseId/students/:studentId/progress` | Get student progress |

**Example: Create Course**
```bash
POST /mentor/courses
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Python Programming",
  "description": "Learn Python from scratch"
}

Response:
{
  "message": "Course created successfully",
  "course": {
    "id": "uuid",
    "title": "Python Programming",
    "created_at": "2024-01-15T10:00:00Z"
  }
}
```

**Example: Add Chapter**
```bash
POST /mentor/courses/:courseId/chapters
Authorization: Bearer <token>

{
  "title": "Variables and Data Types",
  "content": "In Python, variables...",
  "image_url": "https://example.com/image.png",
  "video_url": "https://youtube.com/watch?v=...",
  "sequence_order": 1
}
```

**Example: Assign Course**
```bash
POST /mentor/courses/:courseId/assign
Authorization: Bearer <token>

{
  "student_ids": ["uuid1", "uuid2", "uuid3"]
}
```

---

## 👑 Admin Endpoints

**All require:** `Authorization: Bearer <token>` + `admin` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/admin/mentors/pending` | Get pending mentor approvals |
| `PUT` | `/admin/mentors/:id/approve` | Approve/reject mentor |
| `GET` | `/admin/users` | Get all users |
| `DELETE` | `/admin/users/:id` | Delete user |
| `GET` | `/admin/statistics` | Get platform statistics |
| `GET` | `/admin/courses` | Get all courses |
| `GET` | `/admin/students` | Get all students |

**Example: Approve Mentor**
```bash
PUT /admin/mentors/:mentorId/approve
Authorization: Bearer <token>

{
  "approved": true
}
```

**Example: Statistics Response**
```json
{
  "users": {
    "total": 150,
    "students": 120,
    "mentors": 25,
    "admins": 5
  },
  "courses": { "total": 30 },
  "enrollments": { "total": 450 },
  "certificates": { "total": 85 },
  "pending_mentors": 3,
  "average_completion_rate": 65
}
```

---

## 🔒 HTTP Status Codes

| Code | Meaning | When |
|------|---------|------|
| `200` | OK | Success |
| `201` | Created | Resource created |
| `400` | Bad Request | Invalid input |
| `401` | Unauthorized | Missing/invalid token |
| `403` | Forbidden | Insufficient permissions |
| `404` | Not Found | Resource not found |
| `500` | Server Error | Internal error |

---

## 🧪 Testing with cURL

### Register and Login
```bash
# Register student
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@test.com",
    "password": "password123",
    "full_name": "Test Student",
    "role": "student"
  }'

# Login
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@test.com",
    "password": "password123"
  }'

# Copy the token from response
```

### Use Token
```bash
# Get courses
curl -X GET http://localhost:3000/api/student/courses \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Mark chapter complete
curl -X POST http://localhost:3000/api/student/chapters/CHAPTER_ID/complete \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📱 Android Integration

### Retrofit Setup
```kotlin
interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @GET("student/courses")
    suspend fun getCourses(): Response<CoursesResponse>
}
```

### Making Requests
```kotlin
// In ViewModel
viewModelScope.launch {
    val response = apiService.getCourses()
    if (response.isSuccessful) {
        val courses = response.body()?.courses
        // Update UI
    } else {
        // Handle error (401, 403, etc.)
    }
}
```

### Handling 401/403
```kotlin
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Chain): Response {
        val response = chain.proceed(request)
        if (response.code == 401) {
            // Redirect to login
        }
        return response
    }
}
```

---

## 🔑 Role Access Matrix

| Endpoint Pattern | Student | Mentor | Admin |
|-----------------|---------|---------|-------|
| `/auth/*` | ✅ | ✅ | ✅ |
| `/student/*` | ✅ | ❌ | ❌ |
| `/mentor/*` | ❌ | ✅ (approved) | ❌ |
| `/admin/*` | ❌ | ❌ | ✅ |

---

## 💡 Common Patterns

### Sequential Chapter Access
```
Chapter 1: ✅ Completed → Unlocked
Chapter 2: ✅ Completed → Unlocked
Chapter 3: ⏳ Current → Unlocked
Chapter 4: 🔒 Locked
Chapter 5: 🔒 Locked
```

### Progress Calculation
```
Progress = (Completed Chapters / Total Chapters) × 100
Certificate = Available when Progress == 100%
```

### Error Handling
```
try {
    response = await apiCall()
} catch (error) {
    if (error.code === 401) {
        // Invalid/expired token → Logout
    } else if (error.code === 403) {
        // Insufficient permissions → Show error
    } else {
        // Other error → Show generic error
    }
}
```

---

## 🎯 Quick Commands

```bash
# Start backend
cd backend && npm run dev

# Run backend tests
cd backend && npm test

# Check API health
curl http://localhost:3000/health

# Login as admin
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@lms.com","password":"admin123"}'
```

---

**For complete API documentation, see [`backend/README.md`](backend/README.md)**
