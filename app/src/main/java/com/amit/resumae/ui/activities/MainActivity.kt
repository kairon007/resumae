package com.amit.resumae.ui.activities

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.amit.resumae.R
import com.amit.resumae.adapter.ResumeAdapter
import com.amit.resumae.adapter.SwipeToDeleteCallback
import com.amit.resumae.repository.database.*
import com.amit.resumae.utilities.*
import com.amit.resumae.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), CoroutineScope {

    private val mainActivityJob = Job()
    override val coroutineContext = Dispatchers.Main + mainActivityJob

    private val TAG = this::class.java.simpleName
    private lateinit var mainViewModel: MainViewModel
    private lateinit var resumeAdapter: ResumeAdapter
    private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
    private lateinit var resumesRecyclerView: RecyclerView
    private lateinit var webView: WebView


    companion object {
        const val EXTRA_RESUME_ID: String = "resumeId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainActivityToolbar)
        collapsingToolbarLayout.title = resources.getString(R.string.app_name)

        mainViewModel = ViewModelProviders
            .of(this)
            .get(MainViewModel::class.java)

        resumesRecyclerView = findViewById(R.id.resumesListRecyclerView)
        webView = WebView(this)

        setupRecyclerView()

        mainViewModel.resumesList
            .observe(this, Observer {
                resumeAdapter.updateResumesList(it ?: emptyList())
                toggleNoResumesLayout(it?.size ?: 0)
            })

        addResumeFab.setOnClickListener {
            val newResumeId: Long = -1
            val intent = Intent(this, CreateResumeActivity::class.java)
            intent.putExtra(EXTRA_RESUME_ID, newResumeId)
            startActivity(intent)
        }
    }

    private fun toggleNoResumesLayout(size: Int) {
        if (size > 0) {
            resumesListRecyclerView.visible()
            noResumesView.invisible()
        } else {
            resumesListRecyclerView.invisible()
            noResumesView.visible()
            mainActivityAppbarLayout.setExpanded(true, true)
        }
    }

    private fun setupRecyclerView() {
        resumeAdapter = ResumeAdapter { resumeId: Long ->
            val intent = Intent(this, CreateResumeActivity::class.java)
            intent.putExtra(EXTRA_RESUME_ID, resumeId)
            startActivity(intent)
        }
        linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(resumesRecyclerView.context, linearLayoutManager.orientation)
        this.getDrawable(R.drawable.list_divider)?.let { dividerItemDecoration.setDrawable(it) }
        resumesRecyclerView.apply {
            adapter = resumeAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(dividerItemDecoration)
        }
        val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewholder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewholder.adapterPosition
                val id: Long = resumeAdapter.getResumeAtPosition(position).id
                if (direction == ItemTouchHelper.LEFT) {
                    AlertDialog.Builder(ContextThemeWrapper(this@MainActivity, R.style.MyAlertDialog))
                        .setMessage("Are you sure you want to delete this resume?")
                        .setPositiveButton("Yes") { _, _ ->
                            mainViewModel.deleteResume(resumeAdapter.getResumeAtPosition(position))
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            resumeAdapter.notifyItemChanged(position)
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                } else {
                    launch {
                        lateinit var resume: Resume
                        lateinit var educationList: List<Education>
                        lateinit var experienceList: List<WorkSummary>
                        lateinit var skillList: List<Skill>
                        lateinit var workSummaryList: List<Project>
                        lateinit var html: String

                        withContext(AppDispatchers.diskDispatcher) {
                            resume = mainViewModel.getResumeForId(id)
                            educationList = mainViewModel.getEducationForResume(id)
                            experienceList = mainViewModel.getWorkSummaryForResume(id)
                            skillList = mainViewModel.getSkillForResume(id)
                            workSummaryList = mainViewModel.getProjectForResume(id)
                        }
                        withContext(AppDispatchers.computationDispatcher) {
                            html = buildHtml(resume, educationList, experienceList,skillList, workSummaryList)
                        }

                        webView = WebView(this@MainActivity)
                        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                        webView.createPrintJob(this@MainActivity)
                        resumeAdapter.notifyItemChanged(position)
                    }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(resumesRecyclerView)
    }
}
