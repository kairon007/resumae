package com.amit.resumae.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amit.resumae.R
import com.amit.resumae.adapter.WorkSummaryAdapter
import com.amit.resumae.repository.database.WorkSummary
import com.amit.resumae.ui.activities.CreateResumeActivity
import com.amit.resumae.utilities.areAllItemsSaved
import com.amit.resumae.utilities.invisible
import com.amit.resumae.utilities.visible
import com.amit.resumae.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_work_summary.*


class WorkSummaryFragment : Fragment() {

	private lateinit var workSummaryAdapter : WorkSummaryAdapter
	private lateinit var linearLayoutManager : LinearLayoutManager
	private lateinit var createResumeViewModel : CreateResumeViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		workSummaryAdapter = WorkSummaryAdapter(
				{ item: WorkSummary ->
					// On Save Button Click
					item.saved = true
					createResumeViewModel.apply {
						updateExperience(item)
					}
				},
				{ item: WorkSummary ->
					// On Delete Button Click
					createResumeViewModel.deleteExperience(item)
					(activity as CreateResumeActivity).displaySnackbar("Work Summary deleted.")
				},
				{ item: WorkSummary ->
					// On Edit Button Click
					item.saved = false
					createResumeViewModel.apply {
						updateExperience(item)
					}
				})
		linearLayoutManager = LinearLayoutManager(context)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_work_summary, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let {
			createResumeViewModel = ViewModelProviders
					.of(it)
					.get(CreateResumeViewModel::class.java)
		}

		createResumeViewModel.workSummaryList
				.observe(viewLifecycleOwner, Observer {
					workSummaryAdapter.updateWorkSummaryList(it ?: emptyList())
					createResumeViewModel.workSummaryDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoExperienceLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		skillRecyclerView.apply {
			adapter = workSummaryAdapter
			layoutManager = linearLayoutManager
		}
	}

	private fun toggleNoExperienceLayout(size : Int) {
		if (size > 0) {
			skillRecyclerView.visible()
			noExperienceView.invisible()
		} else {
			skillRecyclerView.invisible()
			noExperienceView.visible()
		}
	}
}
