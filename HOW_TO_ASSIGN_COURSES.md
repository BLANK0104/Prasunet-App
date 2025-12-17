# 📚 How to Assign Students to Courses - Complete Guide

## Overview
This guide explains how mentors can assign courses to students in the Prasunet LMS Android app.

---

## 🎯 For Mentors: Assigning Courses

### Step-by-Step Process

#### 1. **Login as Mentor**
- Open the app
- Login with your mentor credentials
- **Note**: Your account must be approved by an admin first

#### 2. **Navigate to Your Courses**
- You'll see your "My Courses" home screen
- List of all courses you've created will be displayed

#### 3. **Select a Course**
- Tap on the course you want to assign
- You'll see the course details with chapters

#### 4. **Click "Assign Students"**
- Tap the blue "Assign Students" button
- This opens the course assignment screen

#### 5. **Select Students from List**
- You'll see a list of all available students
- Each student is shown with:
  - Checkbox for selection
  - Full name
  - Email address
- Select one or more students by checking the boxes

#### 6. **Review Selection**
- Bottom bar shows: "X student(s) selected"
- You can select/deselect as many students as needed
- Scroll through the list to find more students

#### 7. **Assign Course**
- Tap the "Assign Course to Selected Students" button
- The app will assign the course to each student individually
- You'll be automatically redirected back when done

#### 8. **Verification**
- Students will now see the course in their home screen
- You can view enrolled students by clicking "View Enrolled Students"

---

## ✅ No Need to Get Student IDs!

The app now automatically loads all available students with a **checkbox selection UI**. You don't need to manually enter any IDs!

**Benefits:**
- ✅ See student names and emails
- ✅ Easy checkbox selection
- ✅ Multi-select support
- ✅ No manual ID entry needed

---

## 📱 Screenshots Guide

### 1. Mentor Home Screen
```
┌─────────────────────────────┐
│  My Courses            🚪   │
├─────────────────────────────┤
│  Welcome, Mentor!           │
│  Manage your courses        │
│                             │
│  ┌─────────────────────┐   │
│  │ Python Programming  │   │
│  │ Learn Python basics │   │
│  │ 0 / 5 chapters      │   │
│  │ ▓▓▓▓░░░░░░ 0%      │   │
│  └─────────────────────┘   │
│         [+] CREATE          │
└─────────────────────────────┘
```

### 2. Course Detail Screen
```
┌─────────────────────────────┐
│  ← Course Chapters          │
├─────────────────────────────┤
│  Course Chapters            │
│                             │
│  1. Introduction            │
│  2. Variables               │
│  3. Functions               │
│                             │
│  [  Assign Students  ]      │
│  [View Enrolled Students]   │
└─────────────────────────────┘
```

### 3. Assign Course Screen
```
┌─────────────────────────────┐
│  ← Assign Course        ℹ️  │
├─────────────────────────────┤
│  Assign Students to Course  │
│                             │
│  ℹ️ Enter student IDs       │
│  separated by commas or     │
│  newlines. Click info icon  │
│                             │
│  Student IDs:               │
│  ┌─────────────────────┐   │
│  │ uuid1, uuid2, ...   │   │
│  │                     │   │
│  │                     │   │
│  └─────────────────────┘   │
│                             │
│  [  Assign Course  ]        │
└─────────────────────────────┘
```

### 4. Success Message
```
┌─────────────────────────────┐
│  ✓ Course Assigned          │
│    Successfully!            │
│                             │
│  Course assigned to all 3   │
│  student(s) successfully    │
│                             │
│  [  Back to Course  ]       │
└─────────────────────────────┘
```

---

## 🔧 Technical Details

### API Endpoint Used
```http
POST /mentor/assign-course
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "studentId": "student-uuid",
  "courseId": "course-uuid"
}
```

### How It Works
1. User enters multiple student IDs
2. App splits IDs by comma/newline
3. For each student ID:
   - Makes API call to assign course
   - Collects success/failure results
4. Shows summary of assignment results

### Response Handling
- ✅ **All Success**: "Course assigned to all X students"
- ⚠️ **Partial Success**: "Course assigned to X out of Y students"
- ❌ **All Failed**: Shows error message

---

## ⚠️ Important Notes

### 1. API Limitation
**Current API Design**: Assigns one student at a time

The backend API changed from:
- ❌ Old: `POST /mentor/courses/:id/assign` (batch)
- ✅ New: `POST /mentor/assign-course` (single)

**Impact**: The app makes multiple API calls when assigning to multiple students.

### 2. Missing Feature
**No Student Selection UI**: The backend doesn't provide a `GET /mentor/students` endpoint.

**Current Workaround**: Manual ID input  
**Ideal Solution**: Checkbox list of all students (requires backend API addition)

### 3. UUID Format
Student IDs must be valid UUIDs:
```
Valid:   a1b2c3d4-e5f6-7890-abcd-ef1234567890
Invalid: student123, john@example.com
```

---

## 🆘 Troubleshooting

### Error: "Insufficient permissions"
**Cause**: Not logged in as approved mentor  
**Solution**: Ensure admin has approved your mentor account

### Error: "Student already enrolled"
**Cause**: Student is already assigned to this course  
**Solution**: Remove duplicate IDs from your list

### Error: "Student not found"
**Cause**: Invalid student ID  
**Solution**: Double-check the UUID format and try again

### Error: "Course not found"
**Cause**: Course was deleted or you don't own it  
**Solution**: Verify you're the course creator

---

## 💡 Tips & Best Practices

### For Mentors
1. **Keep a Student List**: Maintain a spreadsheet of student names and IDs
2. **Test First**: Try assigning to one student first to verify IDs
3. **Check Enrollment**: Use "View Enrolled Students" to verify assignments
4. **Coordinate with Admin**: Ask admin for student lists if needed

### For Admins
1. **Provide Student Lists**: Export and share student IDs with mentors
2. **Handle Bulk Assignments**: Consider doing bulk assignments yourself
3. **Monitor Assignments**: Check that courses are properly assigned

---

## 🎯 Future Improvements

### Requested Backend API Addition
```http
GET /mentor/students
Authorization: Bearer <jwt-token>

Response:
{
  "students": [
    {
      "id": "uuid",
      "email": "student@example.com",
      "full_name": "John Doe"
    }
  ]
}
```

**Benefits**:
- ✅ No manual ID entry needed
- ✅ Select students with checkboxes
- ✅ Search/filter students by name
- ✅ Better user experience

**Status**: Documented in `MISSING_API_ENDPOINT.md`

---

## 📞 Need Help?

### For Mentors
- Contact your platform admin
- Check the help dialog in the assign screen (ℹ️ icon)
- Refer to this guide

### For Admins
- Review `COMPLETE_API_DOCUMENTATION.md`
- Check backend logs for API errors
- Verify user roles and permissions

### For Developers
- See `ANDROID_DEVELOPMENT_GUIDE.md`
- Check `MentorRepository.kt` for implementation
- Review `AssignCourseScreen.kt` for UI logic

---

## ✅ Summary

**Current Status**:
- ✅ Feature works (courses can be assigned)
- ⚠️ UX is suboptimal (manual ID entry)
- 📝 Backend enhancement recommended

**How to Assign**:
1. Login as approved mentor
2. Open course → Click "Assign Students"
3. Enter student UUIDs (comma-separated)
4. Click "Assign Course"
5. Done! Students can now see the course

**Recommendation**: Ask your backend team to add the `GET /mentor/students` endpoint for better user experience.

---

**Last Updated**: December 17, 2025  
**Status**: ✅ Working with workaround  
**Documentation**: Complete

