package com.amit.resumae.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.amit.resumae.repository.LocalRepository
import com.amit.resumae.repository.database.*
import com.amit.resumae.ui.activities.CreateResumeActivity
import kotlinx.coroutines.*

class CreateResumeViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    private val createResumeViewModelJob = Job()

    override val coroutineContext = Dispatchers.Main + createResumeViewModelJob

    private val TAG = this::class.java.simpleName
    private var resumeId: Long
    val resume: LiveData<Resume>
    val educationList: LiveData<List<Education>>
    val workSummaryList: LiveData<List<WorkSummary>>
    val skillList: LiveData<List<Skill>>
    val projectsList: LiveData<List<Project>>

    /*
    Initializing these values as true because
    the user might not want to add any of these
    details at all. These will be set to false
    upon the creation of an item in the respective
    categories, and then back to true when the item
    is saved. This also ensures that no empty item
    is saved too.
     */
    var personalDetailsSaved: Boolean = true
    var educationDetailsSaved: Boolean = true
    var workSummaryDetailsSaved: Boolean = true
    var projectDetailsSaved: Boolean = true
    var skillDetailsSaved: Boolean = true

    private var repository: LocalRepository = LocalRepository(getApplication())

    init {
        resumeId = CreateResumeActivity.currentResumeId
        if (resumeId == -1L) {
            /*
            We can run this in a non blocking way
            because we don't care about the resumeId
            for a new resume as long as the user does
            not press the save button on the personal fragment,
            or the add button in any other fragments. Those events
            occur a long time after the viewmodel is created,
            so we can ignore the possibility of resumeId being -1L
            any time when it actually matters.
             */
            runBlocking {
                resumeId = repository.insertResume(Resume("My Resume", "", "", "", "", "", "", ""))
            }
        }
        resume = repository.getResumeForId(resumeId)
        educationList = repository.getAllEducationForResume(resumeId)
        workSummaryList = repository.getAllWorkSummaryForResume(resumeId)
        skillList = repository.getAllSkillForResume(resumeId)
        projectsList = repository.getAllProjectsForResume(resumeId)
    }

    fun insertBlankEducation() = launch {
        val education = Education("", "", "", "", resumeId)
        repository.insertEducation(education)
    }

    fun insertBlankExperience() = launch {
        val workSummary = WorkSummary("", "", resumeId)
        repository.insertWorkSummary(workSummary)
    }
    fun insertBlankSkill() = launch {
        val skill = Skill("",  resumeId)
        repository.insertSkill(skill)
    }
    fun insertBlankProject() = launch {
        val project = Project("", "", "", "", "",resumeId)
        repository.insertProject(project)
    }

    fun updateResume(resume: Resume) = launch {
        resume.id = resumeId
        repository.updateResume(resume)
    }

    fun updateEducation(education: Education) = launch {
        repository.updateEducation(education)
    }

    fun updateExperience(workSummary: WorkSummary) = launch {
        repository.updateWorkSummary(workSummary)
    }
    fun updateSkill(skill: Skill) = launch {
        repository.updateSkill(skill)
    }
    fun updateProject(workSummary: Project) = launch {
        repository.updateProject(workSummary)
    }

    fun deleteEducation(education: Education) = launch {
        repository.deleteEducation(education)
    }

    fun deleteExperience(workSummary: WorkSummary) = launch {
        repository.deleteWorkSummary(workSummary)
    }
    fun deleteSkill(skill: Skill) = launch {
        repository.deleteSkill(skill)
    }
    fun deleteProject(workSummary: Project) = launch {
        repository.deleteProject(workSummary)
    }

    fun deleteTempResume() = launch {
        repository.deleteResumeForId(resumeId)
    }

    override fun onCleared() {
        super.onCleared()
        createResumeViewModelJob.cancel()
    }
}