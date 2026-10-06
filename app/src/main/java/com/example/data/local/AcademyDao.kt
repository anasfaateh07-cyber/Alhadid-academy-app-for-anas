package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademyDao {

    // Students
    @Query("SELECT * FROM students ORDER BY id ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    // Teachers
    @Query("SELECT * FROM teachers ORDER BY id ASC")
    fun getAllTeachers(): Flow<List<TeacherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: TeacherEntity)

    @Delete
    suspend fun deleteTeacher(teacher: TeacherEntity)

    // Attendance
    @Query("SELECT * FROM attendance_records ORDER BY date DESC, id ASC")
    fun getAllAttendance(): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId AND date = :date LIMIT 1")
    suspend fun getAttendanceForStudentDate(studentId: Int, date: String): AttendanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceEntity)

    // Fees
    @Query("SELECT * FROM fee_records ORDER BY id DESC")
    fun getAllFeeRecords(): Flow<List<FeeRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeRecord(record: FeeRecordEntity)

    @Update
    suspend fun updateFeeRecord(record: FeeRecordEntity)

    @Delete
    suspend fun deleteFeeRecord(record: FeeRecordEntity)

    // Book Evaluations
    @Query("SELECT * FROM book_evaluations ORDER BY id DESC")
    fun getAllBookEvaluations(): Flow<List<BookEvaluationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookEvaluation(evaluation: BookEvaluationEntity)

    @Delete
    suspend fun deleteBookEvaluation(evaluation: BookEvaluationEntity)

    // Exam Results / Tests
    @Query("SELECT * FROM exam_results ORDER BY id DESC")
    fun getAllExamResults(): Flow<List<ExamResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamResult(result: ExamResultEntity)

    @Delete
    suspend fun deleteExamResult(result: ExamResultEntity)

    // Announcements / Alerts
    @Query("SELECT * FROM announcements ORDER BY timestamp DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity)

    @Delete
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity)
}
