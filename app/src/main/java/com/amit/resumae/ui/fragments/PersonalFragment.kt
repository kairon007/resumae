package com.amit.resumae.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.amit.resumae.R
import com.amit.resumae.repository.database.Resume
import com.amit.resumae.repository.database.ResumeEntity
import com.amit.resumae.utilities.inputvalidator.InputValidator
import com.amit.resumae.utilities.inputvalidator.Text
import com.amit.resumae.utilities.inputvalidator.addValidation
import com.amit.resumae.utilities.inputvalidator.validateAll
import com.amit.resumae.utilities.showKeyboard
import com.amit.resumae.viewmodel.CreateResumeViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_personal.*


class PersonalFragment : Fragment() {

    private lateinit var createResumeViewModel: CreateResumeViewModel
    private lateinit var resume: Resume

    private var tempResumeName = ""
    private var tempName = ""
    private var tempEmail = ""
    private var tempPhone = ""
    private var tempAddress = ""
    private var tempExp = ""
    private var tempObjective = ""
    private var tempImagePath = ""
    private val REQUEST_CODE = 101
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).also {
            it.addCategory(Intent.CATEGORY_OPENABLE)
            it.type = "image/*"
            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val uri = data?.data

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                uri?.let {
                    requireContext().contentResolver.takePersistableUriPermission(it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                }
            }
            tempImagePath = uri.toString()
            mImageView.setImageURI(uri) // handle chosen image

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createResumeViewModel = ViewModelProviders
            .of(requireActivity())
            .get(CreateResumeViewModel::class.java)

        createResumeViewModel.resume
            .observe(viewLifecycleOwner, Observer {
                this.resume = it ?: Resume.emptyResume()
                fillTextFieldsWithData(this.resume)
                adjustSaveButton(this.resume.saved)
                createResumeViewModel.personalDetailsSaved = this.resume.saved
            })
    }

    private fun fillTextFieldsWithData(resume: Resume) {
        with(resume) {
            personalNameEditText.setText(name)
            personalPhoneEditText.setText(phone)
            personalEmailEditText.setText(email)
            personalAddressEditText.setText(address)
            personalExpEditText.setText(skills)
            personalObjectiveEditText.setText(objective)
            resumeNameEditText.setText(resumeName)
            Log.d("Tag", "imagepath is $image" )
            if (image.isNotEmpty()) {
                mImageView.setImageURI(Uri.parse(image))
            }
            mImageView.setOnClickListener { openGalleryForImage() }
        }
    }

    private fun adjustSaveButton(resumeSavedStatus: Boolean) {
        personalSaveButton.apply {
            if (resumeSavedStatus) {
                // Edit Mode
                this.text = context.resources.getString(R.string.editButtonText)
                disableTextFields()
                this.setOnClickListener {
                    enableTextFields()
                    onEditButtonClicked(resume)
                    this.text = context.resources.getString(R.string.saveButtonText)
                }
            } else {
                // Save Mode
                this.text = context.resources.getString(R.string.saveButtonText)
                setOnClickListener {
                    if (runChecks()) {
                        resume.apply {
                            resumeName = tempResumeName
                            name = tempName
                            phone = tempPhone
                            email = tempEmail
                            address = tempAddress
                            skills = tempExp
                            objective = tempObjective
                            image = tempImagePath
                        }
                        onSaveButtonClick(resume)
                        disableTextFields()
                        this.text = context.resources.getString(R.string.editButtonText)
                    }
                }
            }
        }
    }


    private fun <T : ResumeEntity> onSaveButtonClick(item: T) {
        item.saved = true
        createResumeViewModel.apply {
            updateResume(item as Resume)
        }
    }

    private fun <T : ResumeEntity> onEditButtonClicked(item: T) {
        item.saved = false
        createResumeViewModel.apply {
            updateResume(item as Resume)
        }
    }

    private fun runChecks(): Boolean {
        tempResumeName = resumeNameEditText.text.toString()
        tempName = personalNameEditText.text.toString()
        tempPhone = personalPhoneEditText.text.toString()
        tempEmail = personalEmailEditText.text.toString()
        tempAddress = personalAddressEditText.text.toString()
        tempExp = personalExpEditText.text.toString()
        tempObjective = personalObjectiveEditText.text.toString()

        arrayOf<Pair<InputValidator<Text>, TextInputLayout>>(
            tempName.addValidation()
                .required("Please enter your name")
                .validate() to personalNameWrapper,

            tempPhone.addValidation()
                .required("Please enter your phone number")
                .matches(
                    "^[+]?[0-9]{10,13}\$".toRegex(RegexOption.IGNORE_CASE),
                    "Please enter a valid phone number"
                )
                .validate() to personalPhoneWrapper,

            tempEmail.addValidation()
                .required("Please enter your email address")
                .matches(
                    "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$".toRegex(RegexOption.IGNORE_CASE),
                    "Please enter a valid email"
                )
                .validate() to personalEmailWrapper,

            tempAddress.addValidation()
                .required("Please enter an Address")
                .validate() to personalAddressWrapper,





            tempExp.addValidation()
                .required("Please enter your experience in years")
                .minLength(1, "Please add valid experience")
                .maxLength(2,"Please add valid experience")
                .validate() to personalExpWrapper,

            tempObjective.addValidation()
                .required("Please type your Career objective")
                .minLength(20, "Please add at least 20 characters")
                .validate() to personalDescriptionWrapper,


            tempResumeName.addValidation()
                .required("Resume name is required")
                .matches("^[a-zA-Z0-9 ]*$".toRegex(RegexOption.IGNORE_CASE),
                    "Can contain only alphabets and numbers")
                .validate() to resumeNameWrapper
        )
            .validateAll()
            .also { result ->
                return result
            }
    }

    private fun toggleTextFields(value: Boolean) {
        personalNameWrapper.isEnabled = value
        personalEmailWrapper.isEnabled = value
        personalPhoneWrapper.isEnabled = value
        personalAddressWrapper.isEnabled = value
        personalExpWrapper.isEnabled = value
        personalDescriptionWrapper.isEnabled = value
        resumeNameWrapper.isEnabled = value
        mImageView.isEnabled = value
    }

    private fun enableTextFields() {
        toggleTextFields(true)
        personalNameWrapper.showKeyboard(context)
    }

    private fun disableTextFields() = toggleTextFields(false)
}