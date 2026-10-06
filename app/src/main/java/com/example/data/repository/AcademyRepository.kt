package com.example.data.repository

import com.example.data.local.AcademyDao
import com.example.data.local.AnnouncementEntity
import com.example.data.local.AttendanceEntity
import com.example.data.local.BookEvaluationEntity
import com.example.data.local.ExamResultEntity
import com.example.data.local.FeeRecordEntity
import com.example.data.local.StudentEntity
import com.example.data.local.TeacherEntity
import kotlinx.coroutines.flow.Flow

class AcademyRepository(private val dao: AcademyDao) {

    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()
    val allTeachers: Flow<List<TeacherEntity>> = dao.getAllTeachers()
    val allAttendance: Flow<List<AttendanceEntity>> = dao.getAllAttendance()
    val allFeeRecords: Flow<List<FeeRecordEntity>> = dao.getAllFeeRecords()
    val allBookEvaluations: Flow<List<BookEvaluationEntity>> = dao.getAllBookEvaluations()
    val allExamResults: Flow<List<ExamResultEntity>> = dao.getAllExamResults()
    val allAnnouncements: Flow<List<AnnouncementEntity>> = dao.getAllAnnouncements()

    suspend fun enrollStudent(student: StudentEntity): Long = dao.insertStudent(student)

    suspend fun updateStudent(student: StudentEntity) = dao.updateStudent(student)

    suspend fun deleteStudent(student: StudentEntity) = dao.deleteStudent(student)

    suspend fun addTeacher(teacher: TeacherEntity) = dao.insertTeacher(teacher)

    suspend fun deleteTeacher(teacher: TeacherEntity) = dao.deleteTeacher(teacher)

    suspend fun markAttendance(
        student: StudentEntity,
        date: String,
        status: String,
        remarks: String = ""
    ) {
        val existing = dao.getAttendanceForStudentDate(student.id, date)
        if (existing != null) {
            dao.insertAttendance(existing.copy(status = status, remarks = remarks))
        } else {
            dao.insertAttendance(
                AttendanceEntity(
                    studentId = student.id,
                    studentName = student.fullName,
                    rollNumber = student.rollNumber,
                    department = student.department,
                    date = date,
                    status = status,
                    remarks = remarks
                )
            )
        }
    }

    suspend fun markAllPresentForDate(students: List<StudentEntity>, date: String) {
        for (student in students) {
            markAttendance(student, date, "Present", "Auto-Save Present")
        }
    }

    suspend fun addFeeRecord(record: FeeRecordEntity) = dao.insertFeeRecord(record)

    suspend fun updateFeeRecord(record: FeeRecordEntity) = dao.updateFeeRecord(record)

    suspend fun deleteFeeRecord(record: FeeRecordEntity) = dao.deleteFeeRecord(record)

    suspend fun addBookEvaluation(evaluation: BookEvaluationEntity) =
        dao.insertBookEvaluation(evaluation)

    suspend fun deleteBookEvaluation(evaluation: BookEvaluationEntity) =
        dao.deleteBookEvaluation(evaluation)

    suspend fun addExamResult(result: ExamResultEntity) = dao.insertExamResult(result)

    suspend fun deleteExamResult(result: ExamResultEntity) = dao.deleteExamResult(result)

    suspend fun addAnnouncement(announcement: AnnouncementEntity) =
        dao.insertAnnouncement(announcement)

    suspend fun deleteAnnouncement(announcement: AnnouncementEntity) =
        dao.deleteAnnouncement(announcement)
}
