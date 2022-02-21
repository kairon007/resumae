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
import com.amit.resumae.repository.database.Skill
import com.amit.resumae.utilities.showKeyboard

class SkillsAdapter(val onSaveButtonClick: (Skill) -> Unit,
					val onDeleteButtonClick: (Skill) -> Unit,
					val onEditButtonClick: (Skill) -> Unit) : RecyclerView.Adapter<SkillsAdapter.SkillViewHolder>() {

	private var skillList: List<Skill> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): SkillViewHolder {
		return SkillViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_skill, parent, false))
	}

	override fun getItemCount(): Int = skillList.size

	override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
		val experience = skillList[position]
		holder.apply {
			setItem(experience)
			bindClick()
		}
	}

	inner class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private lateinit var mSkill: Skill

		private val skillNameWrapper: TextInputLayout = itemView.findViewById(R.id.skillNameWrapper)
		private val skillName: TextInputEditText = itemView.findViewById(R.id.skillName)
		private val saveButton: MaterialButton = itemView.findViewById(R.id.skillSaveButton)
		private val deleteButton: MaterialButton = itemView.findViewById(R.id.skillDeleteButton)

		fun setItem(skill: Skill) {
			mSkill = skill
			this.apply {
				skillName.setText(mSkill.skillName)
				saveButton.apply {
					this.text = if (mSkill.saved) {
						this.context.getString(R.string.editButtonText)
					} else {
						this.context.getString(R.string.saveButtonText)
					}
				}
				skillNameWrapper.isEnabled = !mSkill.saved

			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					if (mSkill.saved) {
						// Edit Mode
						onEditButtonClick(mSkill)

						// Enable text fields
						skillNameWrapper.apply {
							isEnabled = true
							requestFocus()
							showKeyboard(itemView.context)
						}
						this.text = this.context.getString(R.string.saveButtonText)
					} else {
						// Save Mode

						// Save text from text fields and run checks
						val tempSkillName = this@SkillViewHolder.skillName.text?.toString()
								?: ""


						var passed = true
						if (tempSkillName.trim().isEmpty()) {
							skillNameWrapper.error = "Please enter the skill name"
							passed = false
						} else {
							skillNameWrapper.isErrorEnabled = false
						}


						if (passed) {
							// Save the new values into the member variable
							mSkill.skillName = tempSkillName.trim()
							onSaveButtonClick(mSkill)

							this.text = this.context.getString(R.string.editButtonText)

							// Disable text fields
							skillNameWrapper.isEnabled = false
						}
					}

				}
			}
			deleteButton.setOnClickListener {
				AlertDialog.Builder(ContextThemeWrapper(itemView.context, R.style.MyAlertDialog))
						.setMessage("Are you sure you want to delete this skill card?")
						.setPositiveButton("Yes") { _, _ ->
							onDeleteButtonClick(mSkill)
						}
						.setNegativeButton("No") { dialog, _ ->
							dialog.dismiss()
						}
						.create()
						.show()
			}
		}
	}

	fun updateSkillList(skillList: List<Skill>) {
		val skillDiffUtilCallback = DiffUtilCallback(this.skillList, skillList)
		val diffResult = DiffUtil.calculateDiff(skillDiffUtilCallback)
		this.skillList = skillList
		diffResult.dispatchUpdatesTo(this)
	}
}