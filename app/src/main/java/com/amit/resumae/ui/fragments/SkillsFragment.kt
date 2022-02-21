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
import com.amit.resumae.adapter.SkillsAdapter
import com.amit.resumae.repository.database.Skill
import com.amit.resumae.ui.activities.CreateResumeActivity
import com.amit.resumae.utilities.areAllItemsSaved
import com.amit.resumae.utilities.invisible
import com.amit.resumae.utilities.visible
import com.amit.resumae.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_skill.*


class SkillsFragment : Fragment() {

	private lateinit var skillsAdapter : SkillsAdapter
	private lateinit var linearLayoutManager : LinearLayoutManager
	private lateinit var createResumeViewModel : CreateResumeViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		skillsAdapter = SkillsAdapter(
				{ item: Skill ->
					// On Save Button Click
					item.saved = true
					createResumeViewModel.apply {
						updateSkill(item)
					}
				},
				{ item: Skill ->
					// On Delete Button Click
					createResumeViewModel.deleteSkill(item)
					(activity as CreateResumeActivity).displaySnackbar("Skill deleted.")
				},
				{ item: Skill ->
					// On Edit Button Click
					item.saved = false
					createResumeViewModel.apply {
						updateSkill(item)
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

		createResumeViewModel.skillList
				.observe(viewLifecycleOwner, Observer {
					skillsAdapter.updateSkillList(it ?: emptyList())
					createResumeViewModel.skillDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoExperienceLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		skillRecyclerView.apply {
			adapter = skillsAdapter
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
