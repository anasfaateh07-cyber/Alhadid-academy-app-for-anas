package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rollNumber: String, // e.g., AHAD-001
    val fullName: String,
    val fatherName: String,
    val department: String, // Hifz, Nazra, Tajweed, Tuition, Computer
    val className: String, // e.g., Class 6
    val teacherName: String = "Anas Mustafa",
    val guardianPhone: String = "",
    val address: String = "Nasirabad Jatlan, Azad Kashmir",
    val monthlyFeePkr: Int = 2000,
    val admissionDate: String = "",
    val todaySabaq: String = "",
    val sabaqStatus: String = "Yaad", // Yaad, Kacha, Pakka
    val manzilText: String = "",
    val revisionType: String = "Sabaqi" // Sabaqi, Manzil
)

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val roleTitle: String,
    val assignedGroup: String,
    val phone: String
)

@Entity(tableName = "attendance_records")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val studentName: String,
    val rollNumber: String,
    val department: String,
    val date: String,
    val status: String, // "Present", "Absent", "Leave"
    val remarks: String = ""
)

@Entity(tableName = "fee_records")
data class FeeRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val studentName: String,
    val rollNumber: String,
    val department: String,
    val billingMonth: String,
    val amountDuePkr: Int,
    val amountPaidPkr: Int,
    val status: String, // "Paid", "Pending"
    val paymentDate: String,
    val receiptNo: String,
    val paymentMethod: String
)

@Entity(tableName = "book_evaluations")
data class BookEvaluationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int,
    val studentName: String,
    val subjectName: String, // Math, English, Urdu, Islamiat, Science, Computer
    val percentage: Int,
    val grade: String, // A+, A, B, C
    val remarks: String,
    val evaluatedBy: String = "Anas Mustafa",
    val date: String
)

@Entity(tableName = "exam_results")
data class ExamResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val testTitle: String,
    val subject: String,
    val classGroup: String,
    val date: String,
    val totalMarks: Int,
    val studentId: Int = 0,
    val studentName: String = "",
    val obtainedMarks: Int = 0,
    val grade: String = "A+",
    val createdBy: String = "Teacher Anas Mustafa"
)

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val message: String,
    val author: String,
    val date: String,
    val isPinned: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
