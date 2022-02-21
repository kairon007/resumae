package com.amit.resumae.adapter

import com.amit.resumae.ui.fragments.*

class FragmentAdapter(manager: androidx.fragment.app.FragmentManager) :
    androidx.fragment.app.FragmentPagerAdapter(manager) {

    private val listOfFragments: List<androidx.fragment.app.Fragment> = listOf(PersonalFragment(),
        WorkSummaryFragment(),
        SkillsFragment(),
        EducationFragment(),
        ProjectsFragment())
    private val listOfTitles: List<String> =
        listOf("Personal", "Work Summary", "Skills", "Education", "Projects")

    override fun getItem(position: Int): androidx.fragment.app.Fragment = listOfFragments[position]

    override fun getCount(): Int = listOfFragments.size

    override fun getPageTitle(position: Int) = listOfTitles[position]

}