package com.amit.resumae.repository

import androidx.lifecycle.LiveData
import com.amit.resumae.repository.database.*

interface Repository {

	/*
	Any data repository should implement
	following methods.
	 */

	// Tasks related to resume-list
	fun getAllResume() : LiveData<List<Resume>>
	fun getResumeForId(resumeId: Long): LiveData<Resume>
	fun getSingleResumeForId(resumeId : Long) : Resume
	fun getLastResumeId() : LiveData<Long>
	suspend fun insertResume(resume: Resume): Long
	suspend fun deleteResume(resume : Resume)
	suspend fun updateResume(resume : Resume)
	suspend fun deleteResumeForId(resumeId : Long)

	// Tasks related to education-list
	fun getAllEducationForResume(resumeId: Long) : LiveData<List<Education>>
	fun getAllEducationForResumeOnce(resumeId: Long) : List<Education>
	suspend fun insertEducation(education: Education): Long
	suspend fun deleteEducation(education: Education)
	suspend fun updateEducation(education: Education)

	// Tasks related to worksummary-list
	fun getAllWorkSummaryForResume(resumeId: Long) : LiveData<List<WorkSummary>>
	fun getAllWorkSummaryForResumeOnce(resumeId: Long) : List<WorkSummary>
	suspend fun insertWorkSummary(workSummary: WorkSummary): Long
	suspend fun deleteWorkSummary(workSummary: WorkSummary)
	suspend fun updateWorkSummary(workSummary: WorkSummary)


	// Tasks related to skill-list
	fun getAllSkillForResume(resumeId: Long) : LiveData<List<Skill>>
	fun getAllSkillForResumeOnce(resumeId: Long) : List<Skill>
	suspend fun insertSkill(skill: Skill): Long
	suspend fun deleteSkill(skill: Skill)
	suspend fun updateSkill(skill: Skill)
	// Tasks related to projects-list
	fun getAllProjectsForResume(resumeId: Long) : LiveData<List<Project>>
	fun getAllProjectsForResumeOnce(resumeId : Long) : List<Project>
	suspend fun insertProject(workSummary: Project): Long
	suspend fun deleteProject(workSummary: Project)
	suspend fun updateProject(workSummary: Project)
}