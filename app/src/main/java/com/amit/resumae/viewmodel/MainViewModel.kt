package com.amit.resumae.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.amit.resumae.repository.LocalRepository
import com.amit.resumae.repository.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/*
This ViewModel serves as the ViewModel for
the main activity. The CreateResumeActivity has a
separate viewmodel.
*/
class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val mainViewModelJob = Job()

    override val coroutineContext = Dispatchers.Main + mainViewModelJob

    private val TAG: String = this::class.java.simpleName
    /*
      Cached list of resume with a private setter
      so that it can only be read from other classes,
      but not modified.
       */
    val resumesList: LiveData<List<Resume>>
    /*
    The main view model extends AndroidViewModel,
    so it gets an instance of application in its constructor.
    We use this to initialize the repository,
    which in turn initializes the database.
     */
    private var repository: LocalRepository = LocalRepository(getApplication())

    init {
        resumesList = repository.getAllResume()
    }

    fun deleteResume(resume: Resume) = launch {
        repository.deleteResume(resume)
    }

    fun getResumeForId(resumeId: Long): Resume = repository.getSingleResumeForId(resumeId)

    fun getEducationForResume(resumeId: Long): List<Education> = repository.getAllEducationForResumeOnce(resumeId)

    fun getWorkSummaryForResume(resumeId: Long): List<WorkSummary> = repository.getAllWorkSummaryForResumeOnce(resumeId)
    fun getSkillForResume(resumeId: Long): List<Skill> = repository.getAllSkillForResumeOnce(resumeId)
    fun getProjectForResume(resumeId: Long): List<Project> = repository.getAllProjectsForResumeOnce(resumeId)
}