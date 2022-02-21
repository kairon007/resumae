package com.amit.resumae.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.amit.resumae.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.amit.resumae.adapter.FragmentAdapter
import com.amit.resumae.ui.activities.MainActivity.Companion.EXTRA_RESUME_ID
import com.amit.resumae.utilities.AppDispatchers
import com.amit.resumae.utilities.buildHtml
import com.amit.resumae.utilities.createPrintJob
import com.amit.resumae.utilities.hideKeyboard
import com.amit.resumae.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.activity_create_resume.*
import kotlinx.coroutines.*

class CreateResumeActivity : AppCompatActivity(), CoroutineScope {

    private val createResumeActivityJob = Job()
    override val coroutineContext = Dispatchers.Main + createResumeActivityJob

    private val TAG: String = this::class.java.simpleName
    private val EXTRA_HTML: String = "html"
    private lateinit var createResumeViewModel: CreateResumeViewModel
    private lateinit var resumeFragmentAdapter: FragmentAdapter
    private lateinit var createResumeFab: FloatingActionButton
    private lateinit var viewPager: ViewPager
    private lateinit var webView: WebView
    private var addIcon: Drawable? = null
    private var doneIcon: Drawable? = null

    companion object {
        var currentResumeId: Long = -1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_resume)

        setSupportActionBar(createResumeToolbar)
        supportActionBar?.title = resources.getString(R.string.createResumeTitle)

        createResumeFab = findViewById(R.id.createResumeFab)
        viewPager = findViewById(R.id.createResumeViewpager)
        addIcon = ContextCompat.getDrawable(this, R.drawable.ic_round_add_24px)
        doneIcon = ContextCompat.getDrawable(this, R.drawable.ic_round_done_24px)

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_RESUME_ID)) {
            /*
            This means that we are currently working with
            an existing resume in the database
             */
            currentResumeId = intent.getLongExtra(EXTRA_RESUME_ID, -1L)
        }

        createResumeViewModel = ViewModelProviders
                .of(this)
                .get(CreateResumeViewModel::class.java)

        resumeFragmentAdapter = FragmentAdapter(supportFragmentManager)
        viewPager.adapter = resumeFragmentAdapter
        viewPager.offscreenPageLimit = 5
        createResumeTabs.setupWithViewPager(createResumeViewpager)
        createResumeFab.hide()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
                // Do nothing
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                // Do nothing
            }

            override fun onPageSelected(position: Int) {
                adjustFabBehaviour(position)
            }
        })

    }

    override fun onBackPressed() {
        /*
        We use the value of saved from the ViewModel
        because the value inside the activity is destroyed
        on every configuration change. It should only be used
        once: while initializing the viewmodel.
         */
        if (!createResumeViewModel.personalDetailsSaved || !createResumeViewModel.educationDetailsSaved || !createResumeViewModel.workSummaryDetailsSaved || !createResumeViewModel.projectDetailsSaved|| !createResumeViewModel.skillDetailsSaved) {
            AlertDialog.Builder(ContextThemeWrapper(this, R.style.MyAlertDialog))
                    .setTitle("Unsaved Details")
                    .setMessage("Some details remain unsaved. Stay to view them.")
                    .setPositiveButton("Stay") { _, _ ->
                        checkIfDetailsSaved()
                    }
                    .setNegativeButton("Delete") { _, _ ->
                        createResumeViewModel.deleteTempResume()
                        super.onBackPressed()
                    }
                    .create()
                    .show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_resume_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.done -> run {
                this@CreateResumeActivity.hideKeyboard()
                if (checkIfDetailsSaved()) {
                    finish()
                }
                return true
            }
            R.id.print -> run {
                this@CreateResumeActivity.hideKeyboard()
                if (checkIfDetailsSaved()) {
                    launch(AppDispatchers.computationDispatcher) {
                        val html = buildHtml(createResumeViewModel.resume.value!!, createResumeViewModel.educationList.value!!, createResumeViewModel.workSummaryList.value!!,createResumeViewModel.skillList.value!!, createResumeViewModel.projectsList.value!!)
                        withContext(AppDispatchers.mainThreadDispatcher) {
                            webView = WebView(this@CreateResumeActivity)
                            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                            webView.createPrintJob(this@CreateResumeActivity)
                        }
                    }
                }
                true
            }
            R.id.preview -> run {
                this@CreateResumeActivity.hideKeyboard()
                if (checkIfDetailsSaved()) {
                    launch(AppDispatchers.computationDispatcher) {
                        val html = buildHtml(createResumeViewModel.resume.value!!, createResumeViewModel.educationList.value!!, createResumeViewModel.workSummaryList.value!!,createResumeViewModel.skillList.value!!, createResumeViewModel.projectsList.value!!)
                        val intent = Intent(this@CreateResumeActivity, PreviewActivity::class.java)
                        intent.putExtra(EXTRA_HTML, html)
                        startActivity(intent)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun adjustFabBehaviour(position: Int) {
        when (position) {
            0 -> fabBehaviourPersonalFragment()
            3 -> fabBehaviourEducationFragment()
            1 -> fabBehaviourWorkSummaryFragment()
            2 -> fabBehaviourSkillFragment()
            4 -> fabBehaviourProjectFragment()
        }
    }

    private fun fabBehaviourPersonalFragment() {
        createResumeFab.hide()
    }

    private fun fabBehaviourEducationFragment() {
        createResumeFab.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankEducation()
                    educationDetailsSaved = false
                }
            }
        }
    }

    private fun fabBehaviourWorkSummaryFragment() {
        createResumeFab.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankExperience()
                    workSummaryDetailsSaved = false
                }
            }
        }
    }

    private fun fabBehaviourProjectFragment() {
        createResumeFab.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankProject()
                    projectDetailsSaved = false
                }
            }
        }
    }
    private fun fabBehaviourSkillFragment() {
        createResumeFab.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankSkill()
                    skillDetailsSaved = false
                }
            }
        }
    }
    fun displaySnackbar(text: String) {
        Snackbar.make(rootCoordinatorLayout, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun checkIfDetailsSaved(): Boolean {
        with(createResumeViewModel) {
            if (!personalDetailsSaved) {
                viewPager.setCurrentItem(0, true)
                Snackbar.make(rootCoordinatorLayout, "Personal details unsaved", Snackbar.LENGTH_SHORT).show()
                return false
            }
            if (!workSummaryDetailsSaved) {
                viewPager.setCurrentItem(1, true)
                Snackbar.make(rootCoordinatorLayout, "Work Summary details unsaved", Snackbar.LENGTH_SHORT).show()
                return false
            }
            if (!educationDetailsSaved) {
                viewPager.setCurrentItem(3, true)
                Snackbar.make(rootCoordinatorLayout, "Education details unsaved", Snackbar.LENGTH_SHORT).show()
                return false
            }
            if (!skillDetailsSaved) {
                viewPager.setCurrentItem(2, true)
                Snackbar.make(rootCoordinatorLayout, "Skill details unsaved", Snackbar.LENGTH_SHORT).show()
                return false
            }
            if (!projectDetailsSaved) {
                viewPager.setCurrentItem(4, true)
                Snackbar.make(rootCoordinatorLayout, "Project details unsaved", Snackbar.LENGTH_SHORT).show()
                return false
            }
            return true
        }
    }
}

