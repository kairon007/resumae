package com.amit.resumae.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(Resume::class), (Education::class), (Skill::class),(WorkSummary::class), (Project::class)], version = 1)
abstract class ResumeDatabase : RoomDatabase() {

	abstract fun resumeDAO() : ResumeDAO
	abstract fun educationDAO() : EducationDAO
	abstract fun workSummaryDAO() : WorkSummaryDAO
	abstract fun skillDAO() : SkillDAO
	abstract fun projectsDAO() : ProjectsDAO

	companion object : SingletonHolder<ResumeDatabase, Context>(
			{
				Room.databaseBuilder(it.applicationContext,
						ResumeDatabase::class.java,
						"resumes")
						.addCallback(object : Callback() {
						})
						.fallbackToDestructiveMigration()
						.build()
			}
	)

}