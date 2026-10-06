package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AcademyDatabase
import com.example.data.local.AnnouncementEntity
import com.example.data.local.AttendanceEntity
import com.example.data.local.BookEvaluationEntity
import com.example.data.local.ExamResultEntity
import com.example.data.local.FeeRecordEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TeacherEntity
import com.example.data.repository.AcademyRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AcademyTab {
    HOME,
    STUDENTS,
    FEES,
    ATTENDANCE,
    QURAN,
    BOOKS,
    TESTS
}

data class AcademyUiState(
    val students: List<StudentEntity> = emptyList(),
    val teachers: List<TeacherEntity> = emptyList(),
    val attendanceRecords: List<AttendanceEntity> = emptyList(),
    val feeRecords: List<FeeRecordEntity> = emptyList(),
    val bookEvaluations: List<BookEvaluationEntity> = emptyList(),
    val examResults: List<ExamResultEntity> = emptyList(),
    val announcements: List<AnnouncementEntity> = emptyList(),
    val selectedDate: String = "",
    val selectedDepartment: String = "All",
    val searchQuery: String = "",
    val isReady: Boolean = false
)

class AcademyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AcademyRepository
    val todayFormatted: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())

    private val _currentTab = MutableStateFlow(AcademyTab.HOME)
    val currentTab: StateFlow<AcademyTab> = _currentTab.asStateFlow()

    private val _selectedDate = MutableStateFlow(todayFormatted)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedDepartment = MutableStateFlow("All")
    val selectedDepartment: StateFlow<String> = _selectedDepartment.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusBannerMessage = MutableStateFlow<String?>(null)
    val statusBannerMessage: StateFlow<String?> = _statusBannerMessage.asStateFlow()

    init {
        val dao = AcademyDatabase.getDatabase(application).academyDao()
        repository = AcademyRepository(dao)
        // Clean database on first install: zero dummy students, zero fees, zero attendance
    }

    val uiState: StateFlow<AcademyUiState> = combine(
        repository.allStudents,
        repository.allTeachers,
        repository.allAttendance,
        repository.allFeeRecords,
        repository.allBookEvaluations
    ) { students, teachers, attendance, fees, books ->
        AcademyUiState(
            students = students,
            teachers = teachers,
            attendanceRecords = attendance,
            feeRecords = fees,
            bookEvaluations = books,
            isReady = true
        )
    }.combine(
        combine(
            repository.allExamResults,
            repository.allAnnouncements,
            _selectedDate,
            _selectedDepartment,
            _searchQuery
        ) { exams, announcements, date, dept, query ->
            Quint(exams, announcements, date, dept, query)
        }
    ) { base, extra ->
        base.copy(
            examResults = extra.first,
            announcements = extra.second,
            selectedDate = extra.third,
            selectedDepartment = extra.fourth,
            searchQuery = extra.fifth
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AcademyUiState(selectedDate = todayFormatted)
    )

    fun selectTab(tab: AcademyTab) {
        _currentTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedDepartment(department: String) {
        _selectedDepartment.value = department
    }

    fun updateSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun clearStatusBanner() {
        _statusBannerMessage.value = null
    }

    private fun showBanner(msg: String) {
        _statusBannerMessage.value = msg
    }

    fun addTeacher(fullName: String, roleTitle: String, assignedGroup: String, phone: String) {
        viewModelScope.launch {
            repository.addTeacher(
                TeacherEntity(
                    fullName = fullName.trim(),
                    roleTitle = roleTitle.trim().ifEmpty { "Senior Teacher" },
                    assignedGroup = assignedGroup.trim().ifEmpty { "Hifz & Nazra" },
                    phone = phone.trim()
                )
            )
            showBanner("Added Teacher: ${fullName.trim()}")
        }
    }

    fun enrollNewStudent(
        fullName: String,
        fatherName: String,
        department: String,
        className: String,
        guardianPhone: String,
        address: String,
        monthlyFeePkr: Int
    ) {
        viewModelScope.launch {
            val nextNum = uiState.value.students.size + 1
            val rollNo = String.format(Locale.ENGLISH, "AHAD-%03d", nextNum)
            val newStudent = StudentEntity(
                rollNumber = rollNo,
                fullName = fullName.trim(),
                fatherName = fatherName.trim(),
                department = department,
                className = className.trim().ifEmpty { "Class 6" },
                teacherName = "Anas Mustafa",
                guardianPhone = guardianPhone.trim(),
                address = address.trim().ifEmpty { "Nasirabad Jatlan, Azad Kashmir" },
                monthlyFeePkr = monthlyFeePkr,
                admissionDate = todayFormatted
            )
            val id = repository.enrollStudent(newStudent).toInt()
            repository.addFeeRecord(
                FeeRecordEntity(
                    studentId = id,
                    studentName = newStudent.fullName,
                    rollNumber = rollNo,
                    department = department,
                    billingMonth = "October 2026",
                    amountDuePkr = monthlyFeePkr,
                    amountPaidPkr = 0,
                    status = "Pending",
                    paymentDate = "-",
                    receiptNo = "CH-$rollNo",
                    paymentMethod = "Pending"
                )
            )
            showBanner("Enrolled ${newStudent.fullName} ($rollNo)")
        }
    }

    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            showBanner("Removed ${student.fullName}")
        }
    }

    fun updateStudentQuranSabaq(
        student: StudentEntity,
        todaySabaq: String,
        sabaqStatus: String,
        manzilText: String,
        revisionType: String
    ) {
        viewModelScope.launch {
            repository.updateStudent(
                student.copy(
                    todaySabaq = todaySabaq,
                    sabaqStatus = sabaqStatus,
                    manzilText = manzilText,
                    revisionType = revisionType
                )
            )
            showBanner("Auto-saved Sabaq for ${student.fullName}")
        }
    }

    fun markStudentAttendance(student: StudentEntity, status: String) {
        viewModelScope.launch {
            repository.markAttendance(
                student = student,
                date = _selectedDate.value,
                status = status,
                remarks = "Instant Auto-Save"
            )
            showBanner("${student.fullName} marked $status")
        }
    }

    fun markAllPresentForSelectedDate() {
        viewModelScope.launch {
            val students = uiState.value.students
            if (students.isEmpty()) {
                showBanner("No students enrolled yet to mark attendance")
                return@launch
            }
            repository.markAllPresentForDate(students, _selectedDate.value)
            showBanner("Marked all ${students.size} students Present")
        }
    }

    fun recordFeePayment(record: FeeRecordEntity, paidAmountPkr: Int, paymentMethod: String) {
        viewModelScope.launch {
            val totalPaid = (record.amountPaidPkr + paidAmountPkr).coerceAtMost(record.amountDuePkr)
            val newStatus = if (totalPaid >= record.amountDuePkr) "Paid" else "Pending"
            repository.updateFeeRecord(
                record.copy(
                    amountPaidPkr = totalPaid,
                    status = newStatus,
                    paymentDate = todayFormatted,
                    paymentMethod = paymentMethod
                )
            )
            showBanner("Collected Rs $paidAmountPkr from ${record.studentName}")
        }
    }

    fun saveBookEvaluation(
        student: StudentEntity,
        subjectName: String,
        percentage: Int,
        remarks: String
    ) {
        viewModelScope.launch {
            val safePct = percentage.coerceIn(0, 100)
            val grade = when {
                safePct >= 90 -> "A+"
                safePct >= 80 -> "A"
                safePct >= 70 -> "B"
                else -> "C"
            }
            repository.addBookEvaluation(
                BookEvaluationEntity(
                    studentId = student.id,
                    studentName = student.fullName,
                    subjectName = subjectName.trim(),
                    percentage = safePct,
                    grade = grade,
                    remarks = remarks.trim(),
                    evaluatedBy = "Anas Mustafa",
                    date = todayFormatted
                )
            )
            showBanner("Saved $subjectName evaluation ($safePct% $grade)")
        }
    }

    fun createTestOrExam(
        testTitle: String,
        subject: String,
        classGroup: String,
        totalMarks: Int,
        studentName: String,
        obtainedMarks: Int
    ) {
        viewModelScope.launch {
            val safeTotal = totalMarks.coerceAtLeast(1)
            val safeObtained = obtainedMarks.coerceIn(0, safeTotal)
            val pct = (safeObtained * 100) / safeTotal
            val grade = when {
                pct >= 90 -> "A+"
                pct >= 80 -> "A"
                pct >= 70 -> "B"
                else -> "C"
            }
            repository.addExamResult(
                ExamResultEntity(
                    testTitle = testTitle.trim(),
                    subject = subject.trim(),
                    classGroup = classGroup.trim(),
                    date = todayFormatted,
                    totalMarks = safeTotal,
                    studentName = studentName.trim(),
                    obtainedMarks = safeObtained,
                    grade = grade,
                    createdBy = "Teacher Anas Mustafa"
                )
            )
            showBanner("Created test: ${testTitle.trim()}")
        }
    }

    fun deleteExamResult(exam: ExamResultEntity) {
        viewModelScope.launch {
            repository.deleteExamResult(exam)
            showBanner("Removed test record")
        }
    }

    fun postAlert(title: String, category: String, message: String) {
        viewModelScope.launch {
            repository.addAnnouncement(
                AnnouncementEntity(
                    title = title.trim(),
                    category = category,
                    message = message.trim(),
                    author = "Teacher Anas Mustafa",
                    date = todayFormatted,
                    isPinned = true
                )
            )
            showBanner("Alert published: ${title.trim()}")
        }
    }

    fun deleteAlert(alert: AnnouncementEntity) {
        viewModelScope.launch {
            repository.deleteAnnouncement(alert)
        }
    }
}

private data class Quint<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
