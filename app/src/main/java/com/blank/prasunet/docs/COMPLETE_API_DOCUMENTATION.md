# 📱 Complete API Documentation for Android Development
## Internship LMS - All Endpoints with Requirements Mapping

**Base URL (Production):** `https://prasunet.vercel.app/api`  
**Base URL (Local):** `http://localhost:3000/api`

---

## 🔐 Authentication

All authenticated endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 📋 Requirements Coverage

### ✅ Student Requirements
- ✅ Register and log in using the Android app
- ✅ View only the courses assigned to them
- ✅ Access course chapters in a strict sequential order
- ✅ Complete chapters one by one without skipping
- ✅ Track chapter-wise and overall progress
- ✅ View course content including images and video links
- ✅ Download a course completion certificate only after 100% completion

### ✅ Mentor Requirements
- ✅ Log in after approval by an Admin
- ✅ Create new internship courses
- ✅ Add chapters step by step to a course
- ✅ Assign courses to selected students
- ✅ Track the progress of students enrolled in their courses

### ✅ Admin Requirements
- ✅ Approve or reject mentor accounts
- ✅ Manage all users
- ✅ View platform-wide statistics
- ✅ Access all system data without restriction
- ✅ Assign courses to students

---

## 1️⃣ AUTHENTICATION ENDPOINTS

### 1.1 Register User
**Endpoint:** `POST /auth/register`  
**Access:** Public  
**Description:** Register a new student or mentor account

**Request Body:**
```json
{
  "email": "student@example.com",
  "password": "password123",
  "full_name": "John Doe",
  "role": "student"
}
```

**Field Validation:**
- `email`: Required, must be valid email format, unique
- `password`: Required, minimum 6 characters
- `full_name`: Required, string
- `role`: Required, must be either "student" or "mentor"

**Success Response (201):**
```json
{
  "message": "Registration successful. Mentors require admin approval.",
  "user": {
    "id": "uuid",
    "email": "student@example.com",
    "full_name": "John Doe",
    "role": "student",
    "approved": true
  }
}
```

**Notes:**
- Students are automatically approved (`approved: true`)
- Mentors require admin approval (`approved: false`)
- Cannot register as "admin" role

---

### 1.2 Login
**Endpoint:** `POST /auth/login`  
**Access:** Public  
**Description:** Login and receive JWT token

**Request Body:**
```json
{
  "email": "admin@lms.com",
  "password": "admin123"
}
```

**Success Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "email": "admin@lms.com",
    "full_name": "System Admin",
    "role": "admin",
    "approved": true
  }
}
```

**Error Responses:**
- `400`: Email and password required
- `401`: Invalid credentials
- `403`: Account pending approval (for mentors)

**Default Credentials:**
- Admin: `admin@lms.com` / `admin123`

---

## 2️⃣ STUDENT ENDPOINTS

All student endpoints require authentication and student role.

### 2.1 Get Assigned Courses
**Endpoint:** `GET /student/courses`  
**Access:** Private (Student)  
**Description:** Get all courses assigned to the logged-in student

**Success Response (200):**
```json
{
  "courses": [
    {
      "id": "uuid",
      "title": "Web Development Basics",
      "description": "Learn HTML, CSS, and JavaScript",
      "mentor_name": "Jane Smith",
      "total_chapters": 10,
      "completed_chapters": 3,
      "progress_percentage": 30,
      "assigned_at": "2025-12-01T10:00:00Z"
    }
  ]
}
```

**Notes:**
- Returns only courses assigned to the student
- Shows progress for each course
- Empty array if no courses assigned

---

### 2.2 Get Course Chapters
**Endpoint:** `GET /student/courses/:courseId/chapters`  
**Access:** Private (Student)  
**Description:** Get all chapters for an assigned course

**URL Parameters:**
- `courseId`: UUID of the course

**Success Response (200):**
```json
{
  "chapters": [
    {
      "id": "uuid",
      "title": "Introduction to HTML",
      "content": "HTML is the standard markup language...",
      "image_url": "https://example.com/image.jpg",
      "video_url": "https://youtube.com/watch?v=xyz",
      "sequence_order": 1,
      "completed": true
    },
    {
      "id": "uuid",
      "title": "CSS Fundamentals",
      "content": "CSS is used for styling...",
      "image_url": null,
      "video_url": null,
      "sequence_order": 2,
      "completed": false
    }
  ]
}
```

**Sequential Access Rule:**
- `completed: true` - Chapter is completed, user can access
- `completed: false` - Chapter is not completed
- User can only access chapters up to the first incomplete chapter
- Example: If chapters 1-3 are completed, user can access chapter 4 but NOT chapter 5

**Error Responses:**
- `403`: Student not enrolled in this course
- `404`: Course not found

---

### 2.3 Mark Chapter as Complete
**Endpoint:** `POST /student/chapters/:chapterId/complete`  
**Access:** Private (Student)  
**Description:** Mark a chapter as completed (sequential progression enforced)

**URL Parameters:**
- `chapterId`: UUID of the chapter

**Success Response (200):**
```json
{
  "message": "Chapter marked as complete",
  "progress": {
    "completed_chapters": 4,
    "total_chapters": 10,
    "progress_percentage": 40,
    "course_completed": false
  }
}
```

**Sequential Enforcement:**
- System checks if all previous chapters are completed
- If previous chapters are incomplete, returns error
- Cannot skip chapters

**Error Responses:**
- `400`: Previous chapters must be completed first
- `400`: Chapter already completed
- `403`: Student not enrolled in this course
- `404`: Chapter not found

---

### 2.4 Get Course Progress
**Endpoint:** `GET /student/courses/:courseId/progress`  
**Access:** Private (Student)  
**Description:** Get detailed progress for a specific course

**URL Parameters:**
- `courseId`: UUID of the course

**Success Response (200):**
```json
{
  "course": {
    "id": "uuid",
    "title": "Web Development Basics"
  },
  "total_chapters": 10,
  "completed_chapters": 5,
  "progress_percentage": 50,
  "chapters": [
    {
      "id": "uuid",
      "title": "Introduction to HTML",
      "sequence_order": 1,
      "completed": true,
      "completed_at": "2025-12-01T10:00:00Z"
    }
  ]
}
```

---

### 2.5 Get Certificate
**Endpoint:** `GET /student/courses/:courseId/certificate`  
**Access:** Private (Student)  
**Description:** Download course completion certificate (100% completion required)

**URL Parameters:**
- `courseId`: UUID of the course

**Success Response (200):**
```json
{
  "certificate_url": "https://supabase.storage.url/certificates/uuid.pdf",
  "issued_at": "2025-12-15T10:00:00Z"
}
```

**Certificate Generation:**
- Only available when progress is 100%
- PDF is generated on first request and stored
- Subsequent requests return the same certificate URL

**Error Responses:**
- `400`: Course not completed yet
- `403`: Student not enrolled in this course

---

## 3️⃣ MENTOR ENDPOINTS

All mentor endpoints require authentication and mentor role (approved).

### 3.1 Create Course
**Endpoint:** `POST /mentor/courses`  
**Access:** Private (Mentor)  
**Description:** Create a new course

**Request Body:**
```json
{
  "title": "Advanced JavaScript",
  "description": "Master modern JavaScript concepts"
}
```

**Success Response (201):**
```json
{
  "message": "Course created successfully",
  "course": {
    "id": "uuid",
    "title": "Advanced JavaScript",
    "description": "Master modern JavaScript concepts",
    "created_at": "2025-12-17T10:00:00Z"
  }
}
```

**Field Validation:**
- `title`: Required, string
- `description`: Optional, string

---

### 3.2 Get My Courses
**Endpoint:** `GET /mentor/courses`  
**Access:** Private (Mentor)  
**Description:** Get all courses created by the logged-in mentor

**Success Response (200):**
```json
{
  "courses": [
    {
      "id": "uuid",
      "title": "Advanced JavaScript",
      "description": "Master modern JavaScript concepts",
      "total_chapters": 8,
      "enrolled_students": 15,
      "created_at": "2025-12-17T10:00:00Z"
    }
  ]
}
```

---

### 3.3 Add Chapter to Course
**Endpoint:** `POST /mentor/courses/:courseId/chapters`  
**Access:** Private (Mentor)  
**Description:** Add a new chapter to a course

**URL Parameters:**
- `courseId`: UUID of the course

**Request Body:**
```json
{
  "title": "Variables and Data Types",
  "content": "In JavaScript, you can declare variables using let, const, or var...",
  "image_url": "https://example.com/variables.png",
  "video_url": "https://youtube.com/watch?v=abc123",
  "sequence_order": 1
}
```

**Field Validation:**
- `title`: Required, string
- `content`: Optional, string (chapter text content)
- `image_url`: Optional, string (URL to chapter image)
- `video_url`: Optional, string (URL to video tutorial)
- `sequence_order`: Required, integer (chapter position in course)

**Success Response (201):**
```json
{
  "message": "Chapter added successfully",
  "chapter": {
    "id": "uuid",
    "title": "Variables and Data Types",
    "content": "In JavaScript...",
    "image_url": "https://example.com/variables.png",
    "video_url": "https://youtube.com/watch?v=abc123",
    "sequence_order": 1,
    "created_at": "2025-12-17T10:00:00Z"
  }
}
```

**Error Responses:**
- `400`: Sequence order already exists for this course
- `403`: Mentor doesn't own this course

---

### 3.4 Get Course Chapters
**Endpoint:** `GET /mentor/courses/:courseId/chapters`  
**Access:** Private (Mentor)  
**Description:** Get all chapters for a course owned by the mentor

**URL Parameters:**
- `courseId`: UUID of the course

**Success Response (200):**
```json
{
  "chapters": [
    {
      "id": "uuid",
      "title": "Variables and Data Types",
      "content": "In JavaScript...",
      "image_url": "https://example.com/variables.png",
      "video_url": "https://youtube.com/watch?v=abc123",
      "sequence_order": 1,
      "created_at": "2025-12-17T10:00:00Z"
    }
  ]
}
```

---

### 3.5 Assign Course to Student
**Endpoint:** `POST /mentor/assign-course`  
**Access:** Private (Mentor)  
**Description:** Assign a course to a student

**Request Body:**
```json
{
  "studentId": "student-uuid",
  "courseId": "course-uuid"
}
```

**Success Response (201):**
```json
{
  "message": "Course assigned to student successfully",
  "enrollment": {
    "studentId": "student-uuid",
    "courseId": "course-uuid"
  }
}
```

**Error Responses:**
- `400`: Student already enrolled in this course
- `400`: User is not a student
- `403`: Mentor doesn't own this course
- `404`: Student or course not found

---

### 3.6 Get Enrolled Students
**Endpoint:** `GET /mentor/courses/:courseId/students`  
**Access:** Private (Mentor)  
**Description:** Get all students enrolled in a course

**URL Parameters:**
- `courseId`: UUID of the course

**Success Response (200):**
```json
{
  "students": [
    {
      "id": "uuid",
      "email": "student@example.com",
      "full_name": "John Doe",
      "assigned_at": "2025-12-01T10:00:00Z",
      "total_chapters": 10,
      "completed_chapters": 5,
      "progress_percentage": 50
    }
  ]
}
```

---

### 3.7 Get Student Progress
**Endpoint:** `GET /mentor/courses/:courseId/students/:studentId/progress`  
**Access:** Private (Mentor)  
**Description:** Get detailed progress of a specific student

**URL Parameters:**
- `courseId`: UUID of the course
- `studentId`: UUID of the student

**Success Response (200):**
```json
{
  "student": {
    "id": "uuid",
    "full_name": "John Doe",
    "email": "student@example.com"
  },
  "course": {
    "id": "uuid",
    "title": "Web Development Basics"
  },
  "total_chapters": 10,
  "completed_chapters": 5,
  "progress_percentage": 50,
  "chapters": [
    {
      "id": "uuid",
      "title": "Introduction to HTML",
      "sequence_order": 1,
      "completed": true,
      "completed_at": "2025-12-01T10:00:00Z"
    }
  ]
}
```

---

## 4️⃣ ADMIN ENDPOINTS

All admin endpoints require authentication and admin role.

### 4.1 Get Pending Mentors
**Endpoint:** `GET /admin/mentors/pending`  
**Access:** Private (Admin)  
**Description:** Get all mentors awaiting approval

**Success Response (200):**
```json
{
  "mentors": [
    {
      "id": "uuid",
      "email": "mentor@example.com",
      "full_name": "Jane Smith",
      "created_at": "2025-12-15T10:00:00Z"
    }
  ]
}
```

---

### 4.2 Approve/Reject Mentor
**Endpoint:** `PUT /admin/mentors/:mentorId/approve`  
**Access:** Private (Admin)  
**Description:** Approve or reject a mentor account

**URL Parameters:**
- `mentorId`: UUID of the mentor

**Request Body:**
```json
{
  "approved": true
}
```

**Success Response (200):**
```json
{
  "message": "Mentor approved successfully",
  "mentor": {
    "id": "uuid",
    "approved": true
  }
}
```

**Notes:**
- `approved: true` - Approves the mentor
- `approved: false` - Rejects the mentor
- Only unapproved mentors can log in after approval

---

### 4.3 Get All Users
**Endpoint:** `GET /admin/users`  
**Access:** Private (Admin)  
**Description:** Get all users in the system

**Query Parameters:**
- `role` (optional): Filter by role (student, mentor, admin)

**Success Response (200):**
```json
{
  "users": [
    {
      "id": "uuid",
      "email": "user@example.com",
      "full_name": "John Doe",
      "role": "student",
      "approved": true,
      "created_at": "2025-12-01T10:00:00Z"
    }
  ]
}
```

---

### 4.4 Delete User
**Endpoint:** `DELETE /admin/users/:userId`  
**Access:** Private (Admin)  
**Description:** Delete a user from the system

**URL Parameters:**
- `userId`: UUID of the user

**Success Response (200):**
```json
{
  "message": "User deleted successfully"
}
```

**Notes:**
- Cascading delete removes all related data (enrollments, progress, etc.)
- Cannot delete yourself (the logged-in admin)

---

### 4.5 Get Statistics
**Endpoint:** `GET /admin/statistics`  
**Access:** Private (Admin)  
**Description:** Get platform-wide statistics

**Success Response (200):**
```json
{
  "users": {
    "total": 156,
    "students": 120,
    "mentors": 35,
    "admins": 1
  },
  "courses": {
    "total": 42
  },
  "enrollments": {
    "total": 380
  },
  "avgCourseCompletion": 67.5
}
```

---

### 4.6 Get All Courses
**Endpoint:** `GET /admin/courses`  
**Access:** Private (Admin)  
**Description:** Get all courses in the system

**Success Response (200):**
```json
{
  "courses": [
    {
      "id": "uuid",
      "title": "Web Development Basics",
      "description": "Learn HTML, CSS, and JavaScript",
      "mentor_name": "Jane Smith",
      "total_chapters": 10,
      "enrolled_students": 25,
      "created_at": "2025-12-01T10:00:00Z"
    }
  ]
}
```

---

### 4.7 Get All Students
**Endpoint:** `GET /admin/students`  
**Access:** Private (Admin)  
**Description:** Get all students in the system

**Success Response (200):**
```json
{
  "students": [
    {
      "id": "uuid",
      "email": "student@example.com",
      "full_name": "John Doe",
      "created_at": "2025-12-01T10:00:00Z"
    }
  ]
}
```

---

### 4.8 Assign Course to Student
**Endpoint:** `POST /admin/assign-course`  
**Access:** Private (Admin)  
**Description:** Assign any course to any student

**Request Body:**
```json
{
  "studentId": "student-uuid",
  "courseId": "course-uuid"
}
```

**Success Response (201):**
```json
{
  "message": "Course assigned to student successfully",
  "enrollment": {
    "studentId": "student-uuid",
    "courseId": "course-uuid"
  }
}
```

**Error Responses:**
- `400`: Student already enrolled in this course
- `400`: User is not a student
- `404`: Student or course not found

---

## 5️⃣ ERROR HANDLING

All endpoints follow consistent error response format:

**Error Response Structure:**
```json
{
  "error": "Error message describing what went wrong"
}
```

**Common HTTP Status Codes:**
- `200`: Success
- `201`: Created successfully
- `400`: Bad request (validation error)
- `401`: Unauthorized (invalid/missing token)
- `403`: Forbidden (insufficient permissions or account not approved)
- `404`: Resource not found
- `500`: Internal server error

---

## 6️⃣ ANDROID IMPLEMENTATION CHECKLIST

### Authentication Flow
- [ ] Implement login screen with email/password
- [ ] Implement registration screen with role selection
- [ ] Store JWT token securely (EncryptedSharedPreferences)
- [ ] Add token to all API requests (Interceptor)
- [ ] Handle 401 errors (redirect to login)
- [ ] Handle 403 errors for unapproved mentors

### Student Features
- [ ] Display assigned courses list
- [ ] Show course progress (percentage, completed chapters)
- [ ] Display chapters with sequential access control
- [ ] Show chapter content (text, image, video)
- [ ] Implement "Mark as Complete" button (disabled if previous chapters incomplete)
- [ ] Show progress tracking UI
- [ ] Implement certificate download (only at 100%)
- [ ] Handle certificate PDF display/download

### Mentor Features
- [ ] Create course form
- [ ] Add chapter form (with sequence order)
- [ ] List my courses with stats
- [ ] View course chapters list
- [ ] Assign course to students (student picker)
- [ ] View enrolled students list
- [ ] View individual student progress
- [ ] Show approval status on login

### Admin Features
- [ ] Pending mentors list with approve/reject buttons
- [ ] All users list with filters
- [ ] User deletion with confirmation
- [ ] Platform statistics dashboard
- [ ] All courses view
- [ ] All students list
- [ ] Assign course to student interface

### UI/UX Guidelines
- [ ] Use role-based navigation (different screens per role)
- [ ] Show loading states for API calls
- [ ] Display error messages clearly
- [ ] Implement pull-to-refresh on lists
- [ ] Add confirmation dialogs for destructive actions
- [ ] Show progress indicators
- [ ] Implement offline handling

---

## 7️⃣ TESTING GUIDE

### Test Admin Account
```
Email: admin@lms.com
Password: admin123
```

### Test Flow
1. **Admin Login** → Approve mentors
2. **Mentor Login** → Create course → Add chapters → Assign to students
3. **Student Login** → View courses → Complete chapters sequentially → Download certificate

### Sample API Calls (cURL)

**Login as Admin:**
```bash
curl -X POST https://prasunet.vercel.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@lms.com","password":"admin123"}'
```

**Get Pending Mentors:**
```bash
curl -X GET https://prasunet.vercel.app/api/admin/mentors/pending \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Create Course (Mentor):**
```bash
curl -X POST https://prasunet.vercel.app/api/mentor/courses \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Course","description":"Test Description"}'
```

---

## 8️⃣ IMPORTANT NOTES

### Sequential Chapter Access
- Students can only access chapters in order
- Cannot skip to chapter N+2 if chapter N+1 is incomplete
- UI should disable/hide inaccessible chapters

### Certificate Generation
- Certificates are only generated after 100% completion
- First request generates PDF, subsequent requests return same URL
- Store certificate URL in database for quick access

### Role-Based Access
- Students: Can only see assigned courses
- Mentors: Can only manage their own courses (except in Admin's case)
- Admins: Can access everything without restrictions

### Mentor Approval Flow
- Mentors cannot log in until approved by admin
- Login returns 403 with message "Account pending approval"
- After approval, mentor can access all mentor features

---

## 🚀 Ready for Android Development!

You now have complete API documentation covering all requirements:
- ✅ All student features (sequential chapters, progress, certificates)
- ✅ All mentor features (create courses, add chapters, assign courses, track progress)
- ✅ All admin features (approve mentors, manage users, view statistics, assign courses)

Use this documentation with Android Studio Copilot to build the complete app!
