package com.amit.resumae.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amit.resumae.R
import com.amit.resumae.adapter.ProjectAdapter
import com.amit.resumae.repository.database.Project
import com.amit.resumae.ui.activities.CreateResumeActivity
import com.amit.resumae.utilities.areAllItemsSaved
import com.amit.resumae.utilities.invisible
import com.amit.resumae.utilities.visible
import com.amit.resumae.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_projects.*


class ProjectsFragment : Fragment() {

	private val TAG : String = this::class.java.simpleName
	private lateinit var projectAdapter: ProjectAdapter
	private lateinit var linearLayoutManager: LinearLayoutManager
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var projectRecyclerView : androidx.recyclerview.widget.RecyclerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		projectAdapter = ProjectAdapter(
				{ item: Project ->
					item.saved = true
					createResumeViewModel.apply {
						updateProject(item)
						projectDetailsSaved = true
					}
				},
				{ item: Project ->
					createResumeViewModel.deleteProject(item)
					(activity as CreateResumeActivity).displaySnackbar("Project deleted.")
				},
				{ item: Project ->
					item.saved = false
					createResumeViewModel.apply {
						updateProject(item)
						projectDetailsSaved = false
					}
				})
		linearLayoutManager = LinearLayoutManager(context)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_projects, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let {
			createResumeViewModel = ViewModelProviders
					.of(it)
					.get(CreateResumeViewModel::class.java)
		}

		createResumeViewModel.projectsList
				.observe(viewLifecycleOwner, Observer {
					projectAdapter.updateProjectList(it ?: emptyList())
					createResumeViewModel.projectDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoProjectsLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		projectRecyclerView = view.findViewById(R.id.projectsRecyclerView)
		projectRecyclerView.apply {
			adapter = projectAdapter
			layoutManager = linearLayoutManager
		}
	}

	private fun toggleNoProjectsLayout(size : Int) {
		val value = if (size > 0) {
			projectRecyclerView.visible()
			noProjectsView.invisible()
		} else {
			projectRecyclerView.invisible()
			noProjectsView.visible()
		}
	}
}