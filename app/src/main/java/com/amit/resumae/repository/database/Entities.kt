package com.amit.resumae.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

abstract class ResumeEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L

    @ColumnInfo(name = "saved")
    var saved: Boolean = false

    abstract fun isEmpty(): Boolean

    fun isNotEmpty(): Boolean = !isEmpty()
}

@Entity(tableName = "resumes")
data class Resume(
    @ColumnInfo(name = "resumeName") var resumeName: String = "My Resume",
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "objective") var objective: String,
    @ColumnInfo(name = "skills") var skills: String,
    @ColumnInfo(name = "image") var image: String,
) : ResumeEntity() {

    companion object {
        fun emptyResume() = Resume("", "", "", "", "", "", "", "")
    }

    override fun isEmpty(): Boolean {
        if (name.isBlank() &&
            phone.isBlank() &&
            email.isBlank() &&
            address.isBlank() &&
            objective.isBlank() &&
            skills.isBlank() &&
            image.isBlank()

        ) {
            return true
        }
        return false
    }
}

@Entity(tableName = "education",
    foreignKeys = [(ForeignKey(entity = Resume::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("resumeId"),
        onDelete = ForeignKey.CASCADE))])
data class Education(
    @ColumnInfo(name = "instituteName")
    var instituteName: String,
    @ColumnInfo(name = "degree")
    var degree: String,
    @ColumnInfo(name = "yearOfGraduation")
    var year: String,
    @ColumnInfo(name = "performance")
    var performance: String,
    @ColumnInfo(name = "resumeId")
    var resumeId: Long,
) : ResumeEntity() {

    override fun isEmpty(): Boolean {
        if (instituteName.isBlank() &&
            degree.isBlank() &&
            year.isBlank() &&
            performance.isBlank()
        ) {
            return true
        }
        return false
    }

}

@Entity(tableName = "worksummary",
    foreignKeys = [(ForeignKey(entity = Resume::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("resumeId"),
        onDelete = ForeignKey.CASCADE))])
data class WorkSummary(

    @ColumnInfo(name = "companyName")
    var companyName: String,
    @ColumnInfo(name = "duration")
    var duration: String,
    @ColumnInfo(name = "resumeId")
    var resumeId: Long,
) : ResumeEntity() {

    override fun isEmpty(): Boolean {
        if (companyName.isBlank() &&
            duration.isBlank()
        ) {
            return true
        }
        return false
    }
}

@Entity(tableName = "skill",
    foreignKeys = [(ForeignKey(entity = Resume::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("resumeId"),
        onDelete = ForeignKey.CASCADE))])
data class Skill(

    @ColumnInfo(name = "skillName")
    var skillName: String,
    @ColumnInfo(name = "resumeId")
    var resumeId: Long,
) : ResumeEntity() {

    override fun isEmpty(): Boolean {
        if (skillName.isBlank()
        ) {
            return true
        }
        return false
    }
}

@Entity(tableName = "projects",
    foreignKeys = [(ForeignKey(entity = Resume::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("resumeId"),
        onDelete = ForeignKey.CASCADE))])
data class Project(

    @ColumnInfo(name = "projectName")
    var projectName: String,
    @ColumnInfo(name = "role")
    var role: String,
    @ColumnInfo(name = "teamSize")
    var teamSize: String,
    @ColumnInfo(name = "techUsed")
    var techUsed: String,
    @ColumnInfo(name = "summary")
    var summary: String,
    @ColumnInfo(name = "resumeId")
    var resumeId: Long,
) : ResumeEntity() {

    override fun isEmpty(): Boolean {
        if (projectName.isBlank() &&
            role.isBlank() &&
            teamSize.isBlank() &&
            techUsed.isBlank() &&
            summary.isBlank()
        ) {
            return true
        }
        return false
    }
}