package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        StudentEntity::class,
        TeacherEntity::class,
        AttendanceEntity::class,
        FeeRecordEntity::class,
        BookEvaluationEntity::class,
        ExamResultEntity::class,
        AnnouncementEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AcademyDatabase : RoomDatabase() {
    abstract fun academyDao(): AcademyDao

    companion object {
        @Volatile
        private var INSTANCE: AcademyDatabase? = null

        fun getDatabase(context: Context): AcademyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AcademyDatabase::class.java,
                    "alhadid_academy_v5_ultra_clean_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
