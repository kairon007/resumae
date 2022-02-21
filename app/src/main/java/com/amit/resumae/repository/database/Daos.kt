package com.amit.resumae.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ResumeDAO {

	@Query("SELECT * FROM resumes")
	fun getAllResume(): LiveData<List<Resume>>

	@Query("SELECT * FROM resumes WHERE id=:resumeId")
	fun getResumeForId(resumeId : Long) : LiveData<Resume>

	@Query("SELECT MAX(id) FROM resumes")
	fun getLastResumeId() : LiveData<Long>

	@Insert
	fun insertResume(resume: Resume) : Long

	@Delete
	fun deleteResume(resume : Resume)

	@Query("DELETE FROM resumes WHERE id=:resumeId")
	fun deleteResumeForId(resumeId : Long)

	@Update
	fun updateResume(resume : Resume)

	@Query("SELECT * FROM resumes WHERE id=:resumeId")
	fun getSingleResume(resumeId: Long) : Resume
}

@Dao
interface EducationDAO {

	@Query("SELECT * FROM education")
	fun getAllEducation() : MutableList<Education>

	@Query("SELECT * FROM education WHERE resumeId=:resumeId")
	fun getEducationForResume(resumeId : Long) : LiveData<List<Education>>

	@Query("SELECT * FROM education WHERE resumeId=:resumeId")
	fun getEducationForResumeOnce(resumeId : Long) : List<Education>

	@Query("SELECT count(*) FROM education")
	fun getEducationId() : Long

	@Insert
	fun insertEducation(education : Education) : Long

	@Delete
	fun deleteEducation(education : Education)

	@Update
	fun updateEducation(education: Education)
}

@Dao
interface WorkSummaryDAO {

	@Query("SELECT * FROM worksummary")
	fun getAllWorkSummary() : LiveData<List<WorkSummary>>

	@Query("SELECT * FROM worksummary WHERE resumeId=:resumeId")
	fun getWorkSummaryForResume(resumeId : Long) : LiveData<List<WorkSummary>>

	@Query("SELECT * FROM worksummary WHERE resumeId=:resumeId")
	fun getWorkSummaryForResumeOnce(resumeId : Long) : List<WorkSummary>

	@Query("SELECT count(*) FROM worksummary")
	fun getWorkSummaryId() : Long

	@Insert
	fun insertWorkSummary(workSummary : WorkSummary) : Long

	@Delete
	fun deleteWorkSummary(workSummary : WorkSummary)

	@Update
	fun updateWorkSummary(workSummary : WorkSummary)
}

@Dao
interface SkillDAO {

	@Query("SELECT * FROM skill")
	fun getAllSkill() : LiveData<List<Skill>>

	@Query("SELECT * FROM skill WHERE resumeId=:resumeId")
	fun getSkillForResume(resumeId : Long) : LiveData<List<Skill>>

	@Query("SELECT * FROM skill WHERE resumeId=:resumeId")
	fun getSkillForResumeOnce(resumeId : Long) : List<Skill>

	@Query("SELECT count(*) FROM skill")
	fun getSkillId() : Long

	@Insert
	fun insertSkill(skill : Skill) : Long

	@Delete
	fun deleteSkill(skill : Skill)

	@Update
	fun updateSkill(skill : Skill)
}
@Dao
interface ProjectsDAO {

	@Query("SELECT * FROM projects")
	fun getAllProjects() : LiveData<List<Project>>

	@Query("SELECT * FROM projects WHERE resumeId=:resumeId")
	fun getProjectsForResume(resumeId: Long) : LiveData<List<Project>>

	@Query("SELECT * FROM projects WHERE resumeId=:resumeId")
	fun getProjectsForResumeOnce(resumeId: Long) : List<Project>

	@Query("SELECT count(*) FROM projects")
	fun getProjectId() : Long

	@Insert
	fun insertProject(workSummary : Project) : Long

	@Update
	fun updateProject(workSummary : Project)

	@Delete
	fun deleteProject(workSummary : Project)
}
