package com.amit.resumae.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.amit.resumae.repository.database.*
import com.amit.resumae.utilities.AppDispatchers
import kotlinx.coroutines.withContext


/*
This is the local repository layer that
interacts with the database.
 */
class LocalRepository(application: Application) : Repository {

    val database: ResumeDatabase = ResumeDatabase.getInstance(application)

    override fun getAllResume(): LiveData<List<Resume>> = database.resumeDAO().getAllResume()

    override fun getResumeForId(resumeId: Long): LiveData<Resume> = database.resumeDAO().getResumeForId(resumeId)

    override fun getSingleResumeForId(resumeId: Long) = database.resumeDAO().getSingleResume(resumeId)

    override suspend fun insertResume(resume: Resume): Long = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().insertResume(resume)
    }

    override suspend fun deleteResume(resume: Resume) = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().deleteResume(resume)
    }

    override suspend fun deleteResumeForId(resumeId: Long) = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().deleteResumeForId(resumeId)
    }

    override suspend fun updateResume(resume: Resume) = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().updateResume(resume)
    }

    override fun getLastResumeId(): LiveData<Long> = database.resumeDAO().getLastResumeId()

    override fun getAllEducationForResume(resumeId: Long): LiveData<List<Education>> = database.educationDAO().getEducationForResume(resumeId)

    override fun getAllEducationForResumeOnce(resumeId: Long): List<Education> = database.educationDAO().getEducationForResumeOnce(resumeId)

    override suspend fun insertEducation(education: Education): Long =
            withContext(AppDispatchers.diskDispatcher) {
                database.educationDAO().insertEducation(education)
            }

    override suspend fun deleteEducation(education: Education) = withContext(AppDispatchers.diskDispatcher) {
        database.educationDAO().deleteEducation(education)
    }

    override suspend fun updateEducation(education: Education) = withContext(AppDispatchers.diskDispatcher) {
        database.educationDAO().updateEducation(education)
    }
    override fun getAllSkillForResume(resumeId: Long): LiveData<List<Skill>> = database.skillDAO().getSkillForResume(resumeId)
    override fun getAllWorkSummaryForResume(resumeId: Long): LiveData<List<WorkSummary>> = database.workSummaryDAO().getWorkSummaryForResume(resumeId)

    override fun getAllWorkSummaryForResumeOnce(resumeId: Long): List<WorkSummary> = database.workSummaryDAO().getWorkSummaryForResumeOnce(resumeId)
    override fun getAllSkillForResumeOnce(resumeId: Long): List<Skill> = database.skillDAO().getSkillForResumeOnce(resumeId)
    override suspend fun insertWorkSummary(workSummary: WorkSummary): Long =
            withContext(AppDispatchers.diskDispatcher) {
                database.workSummaryDAO().insertWorkSummary(workSummary)
            }

    override suspend fun deleteWorkSummary(workSummary: WorkSummary) = withContext(AppDispatchers.diskDispatcher) {
        database.workSummaryDAO().deleteWorkSummary(workSummary)
    }
    override suspend fun deleteSkill(skill: Skill) = withContext(AppDispatchers.diskDispatcher) {
        database.skillDAO().deleteSkill(skill)
    }
    override suspend fun updateWorkSummary(workSummary: WorkSummary) = withContext(AppDispatchers.diskDispatcher) {
        database.workSummaryDAO().updateWorkSummary(workSummary)
    }
    override suspend fun insertSkill(skill: Skill): Long =
        withContext(AppDispatchers.diskDispatcher) {
            database.skillDAO().insertSkill(skill)
        }
    override suspend fun updateSkill(skill: Skill) = withContext(AppDispatchers.diskDispatcher) {
        database.skillDAO().updateSkill(skill)
    }
    override fun getAllProjectsForResume(resumeId: Long): LiveData<List<Project>> = database.projectsDAO().getProjectsForResume(resumeId)

    override fun getAllProjectsForResumeOnce(resumeId: Long): List<Project> = database.projectsDAO().getProjectsForResumeOnce(resumeId)

    override suspend fun insertProject(workSummary: Project): Long = withContext(AppDispatchers.diskDispatcher) {
        database.projectsDAO().insertProject(workSummary)
    }

    override suspend fun deleteProject(workSummary: Project) = withContext(AppDispatchers.diskDispatcher) {
        database.projectsDAO().deleteProject(workSummary)
    }

    override suspend fun updateProject(workSummary: Project) = withContext(AppDispatchers.diskDispatcher) {
        database.projectsDAO().updateProject(workSummary)
    }
}