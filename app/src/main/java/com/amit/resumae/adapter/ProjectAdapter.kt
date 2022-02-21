package com.amit.resumae.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amit.resumae.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.amit.resumae.repository.database.Project
import com.amit.resumae.utilities.showKeyboard

class ProjectAdapter(val onSaveButtonClick: (Project) -> Unit,
					 val onDeleteButtonClick: (Project) -> Unit,
					 val onEditButtonClick: (Project) -> Unit) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

	private var projectList: List<Project> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): ProjectViewHolder {
		return ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_project, parent, false))
	}

	override fun getItemCount(): Int = projectList.size

	override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
		val project = projectList[position]
		holder.apply {
			setItem(project)
			bindClick()
		}
	}

	inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private lateinit var mProject: Project

		private val projectNameWrapper: TextInputLayout = itemView.findViewById(R.id.projectNameWrapper)
		private val projectName: TextInputEditText = itemView.findViewById(R.id.projectName)
		private val projectRoleWrapper: TextInputLayout = itemView.findViewById(R.id.projectRoleWrapper)
		private val projectRole: TextInputEditText = itemView.findViewById(R.id.projectRole)
		private val projectTeamSizeWrapper: TextInputLayout = itemView.findViewById(R.id.projectTeamSizeWrapper)
		private val projectTeamSize: TextInputEditText = itemView.findViewById(R.id.projectTeamSize)
		private val projectSummaryWrapper: TextInputLayout = itemView.findViewById(R.id.projectSummaryWrapper)
		private val projectSummary: TextInputEditText = itemView.findViewById(R.id.projectSummary)
		private val projectTechUsedWrapper: TextInputLayout = itemView.findViewById(R.id.projectTechUsedWrapper)
		private val projectTechUsed: TextInputEditText = itemView.findViewById(R.id.projectTechUsed)
		private val saveButton: MaterialButton = itemView.findViewById(R.id.projectSaveButton)
		private val deleteButton: MaterialButton = itemView.findViewById(R.id.projectDeleteButton)

		fun setItem(workSummary: Project) {
			mProject = workSummary
			this.apply {
				projectName.setText(mProject.projectName)
				projectRole.setText(mProject.role)
				projectTeamSize.setText(mProject.teamSize)
				projectSummary.setText(mProject.summary)
				projectTechUsed.setText(mProject.techUsed)
				saveButton.apply {
					text = if (mProject.saved) {
						this.context.getString(R.string.editButtonText)
					} else {
						this.context.getString(R.string.saveButtonText)
					}
				}
				projectNameWrapper.isEnabled = !mProject.saved
				projectRoleWrapper.isEnabled = !mProject.saved
				projectTeamSizeWrapper.isEnabled = !mProject.saved
				projectSummaryWrapper.isEnabled = !mProject.saved
				projectTechUsedWrapper.isEnabled = !mProject.saved
			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					if (mProject.saved) {
						// Edit Mode

						onEditButtonClick(mProject)

						// Enable text fields
						projectNameWrapper.apply {
							isEnabled = true
							requestFocus()
							showKeyboard(itemView.context)
						}
						projectRoleWrapper.isEnabled = true
						projectTeamSizeWrapper.isEnabled = true
						projectSummaryWrapper.isEnabled = true
						this.text = this.context.getString(R.string.saveButtonText)
					} else {
						// Save Mode
						val tempProjectName = this@ProjectViewHolder.projectName.text?.toString()
								?: ""
						val tempProjectRole = this@ProjectViewHolder.projectRole.text?.toString()
								?: ""
						val tempProjectTeamSize = this@ProjectViewHolder.projectTeamSize.text?.toString()
								?: ""
						val tempProjectSummary = this@ProjectViewHolder.projectSummary.text?.toString()
								?: ""
						val tempProjectTechUsed = this@ProjectViewHolder.projectTechUsed.text?.toString()
							?: ""
						var passed = true

						if (tempProjectName.trim().isEmpty()) {
							projectNameWrapper.error = "Please enter the project name"
							passed = false
						} else {
							projectNameWrapper.isErrorEnabled = false
						}
						if (tempProjectRole.trim().isEmpty()) {
							projectRoleWrapper.error = "Please enter your role in the project"
							passed = false
						} else {
							projectRoleWrapper.isErrorEnabled = false
						}
						if (tempProjectTeamSize.trim().isEmpty()) {
							projectTeamSizeWrapper.error = "Please enter the size of your team"
							passed = false
						} else {
							projectTeamSizeWrapper.isErrorEnabled = false
						}
						
						if (tempProjectSummary.trim().isEmpty()) {
							projectSummaryWrapper.error = "Please provide a short summary of the project"
							passed = false
						} else {
							projectSummaryWrapper.isErrorEnabled = false
						}
						if (tempProjectTechUsed.trim().isEmpty()) {
							projectTechUsedWrapper.error = "Please enter the technology used in the project"
							passed = false
						} else {
							projectSummaryWrapper.isErrorEnabled = false
						}
						if (passed) {
							// Save the new values into the member variable
							mProject.projectName = tempProjectName
							mProject.role = tempProjectRole
							mProject.teamSize = tempProjectTeamSize
							mProject.summary = tempProjectSummary
							mProject.techUsed = tempProjectTechUsed
							onSaveButtonClick(mProject)

							this.text = this.context.getString(R.string.editButtonText)

							// Disable text fields
							projectNameWrapper.isEnabled = false
							projectRoleWrapper.isEnabled = false
							projectTeamSizeWrapper.isEnabled = false
							projectSummaryWrapper.isEnabled = false
							projectTechUsedWrapper.isEnabled = false
						}
					}
				}
			}
			deleteButton.setOnClickListener {
				AlertDialog.Builder(ContextThemeWrapper(itemView.context, R.style.MyAlertDialog))
						.setMessage("Are you sure you want to delete this project card?")
						.setPositiveButton("Yes") { _, _ ->
							onDeleteButtonClick(mProject)
						}
						.setNegativeButton("No") { dialog, _ ->
							dialog.dismiss()
						}
						.create()
						.show()
			}
		}
	}

	fun updateProjectList(newWorkSummaryList: List<Project>) {
		val projectDiffUtilCallback = DiffUtilCallback(this.projectList, newWorkSummaryList)
		val diffResult = DiffUtil.calculateDiff(projectDiffUtilCallback)
		this.projectList = newWorkSummaryList
		diffResult.dispatchUpdatesTo(this)
	}
}