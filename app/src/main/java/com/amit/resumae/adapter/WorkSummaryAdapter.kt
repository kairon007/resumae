package com.amit.resumae.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amit.resumae.R
import com.amit.resumae.repository.database.WorkSummary
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.amit.resumae.utilities.showKeyboard

class WorkSummaryAdapter(val onSaveButtonClick: (WorkSummary) -> Unit,
						 val onDeleteButtonClick: (WorkSummary) -> Unit,
						 val onEditButtonClick: (WorkSummary) -> Unit) : RecyclerView.Adapter<WorkSummaryAdapter.WorkSummaryViewHolder>() {

	private var workSummaryList: List<WorkSummary> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): WorkSummaryViewHolder {
		return WorkSummaryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_work_summary, parent, false))
	}

	override fun getItemCount(): Int = workSummaryList.size

	override fun onBindViewHolder(holder: WorkSummaryViewHolder, position: Int) {
		val experience = workSummaryList[position]
		holder.apply {
			setItem(experience)
			bindClick()
		}
	}

	inner class WorkSummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private lateinit var mWorkSummary: WorkSummary

		private val companyNameWrapper: TextInputLayout = itemView.findViewById(R.id.skillNameWrapper)
		private val companyName: TextInputEditText = itemView.findViewById(R.id.experienceCompanyName)
		private val durationWrapper: TextInputLayout = itemView.findViewById(R.id.experienceDurationWrapper)
		private val duration: TextInputEditText = itemView.findViewById(R.id.experienceDuration)
		private val saveButton: MaterialButton = itemView.findViewById(R.id.experienceSaveButton)
		private val deleteButton: MaterialButton = itemView.findViewById(R.id.experienceDeleteButton)

		fun setItem(workSummary: WorkSummary) {
			mWorkSummary = workSummary
			this.apply {
				companyName.setText(mWorkSummary.companyName)
				duration.setText(mWorkSummary.duration)
				saveButton.apply {
					this.text = if (mWorkSummary.saved) {
						this.context.getString(R.string.editButtonText)
					} else {
						this.context.getString(R.string.saveButtonText)
					}
				}
				companyNameWrapper.isEnabled = !mWorkSummary.saved
				durationWrapper.isEnabled = !mWorkSummary.saved
			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					if (mWorkSummary.saved) {
						// Edit Mode
						onEditButtonClick(mWorkSummary)

						// Enable text fields
						companyNameWrapper.apply {
							isEnabled = true
							requestFocus()
							showKeyboard(itemView.context)
						}
						durationWrapper.isEnabled = true
						this.text = this.context.getString(R.string.saveButtonText)
					} else {
						// Save Mode

						// Save text from text fields and run checks
						val tempCompanyName = this@WorkSummaryViewHolder.companyName.text?.toString()
								?: ""
						val tempDuration = this@WorkSummaryViewHolder.duration.text?.toString() ?: ""

						var passed = true
						if (tempCompanyName.trim().isEmpty()) {
							companyNameWrapper.error = "Please enter the company name"
							passed = false
						} else {
							companyNameWrapper.isErrorEnabled = false
						}
						if (tempDuration.trim().isEmpty()) {
							durationWrapper.error = "Please enter the duration of your job"
							passed = false
						} else {
							durationWrapper.isErrorEnabled = false
						}

						if (passed) {
							// Save the new values into the member variable
							mWorkSummary.companyName = tempCompanyName.trim()
							mWorkSummary.duration = tempDuration.trim()
							onSaveButtonClick(mWorkSummary)

							this.text = this.context.getString(R.string.editButtonText)

							// Disable text fields
							companyNameWrapper.isEnabled = false
							durationWrapper.isEnabled = false
						}
					}

				}
			}
			deleteButton.setOnClickListener {
				AlertDialog.Builder(ContextThemeWrapper(itemView.context, R.style.MyAlertDialog))
						.setMessage("Are you sure you want to delete this work summary card?")
						.setPositiveButton("Yes") { _, _ ->
							onDeleteButtonClick(mWorkSummary)
						}
						.setNegativeButton("No") { dialog, _ ->
							dialog.dismiss()
						}
						.create()
						.show()
			}
		}
	}

	fun updateWorkSummaryList(newWorkSummaryList: List<WorkSummary>) {
		val workSummaryDiffUtilCallback = DiffUtilCallback(this.workSummaryList, newWorkSummaryList)
		val diffResult = DiffUtil.calculateDiff(workSummaryDiffUtilCallback)
		this.workSummaryList = newWorkSummaryList
		diffResult.dispatchUpdatesTo(this)
	}
}