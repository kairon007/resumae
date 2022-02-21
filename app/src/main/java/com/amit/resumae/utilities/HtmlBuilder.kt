package com.amit.resumae.utilities

import com.amit.resumae.repository.database.*

fun createBaseHTML(): String {
    var html = ""
    html += "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>Resume</title>\n" +
            "\n" + "<style>\n" +
            "        /* body {\n" +
            "            background: #f5f5f5;\n" +
            "        } */\n" +
            "        a {\n" +
            "            color: #FFFFFF;\n" +
            "        }\n" +
            "        a:hover {\n" +
            "            color: #f5f5f5;\n" +
            "        }\n" +
            "        .main-content {\n" +
            "            width: 80%;\n" +
            "            font-family: Helvetica, sans-serif;\n" +
            "            padding-left: 20px;\n" +
            "            padding-top: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .name {\n" +
            "            padding-bottom: 10px;\n" +
            "        }\n" +
            "        .phone, .email {\n" +
            "            line-height: 0.5;\n" +
            "        }\n" +
            "\n" +
            "        .phone-container, .email-container {\n" +
            "            display: flex;\n" +
            "            flex-direction: row;\n" +
            "        }\n" +
            "\n" +
            "        #phone-number, #email-address, #current-city {\n" +
            "            color: #c2c2c2;\n" +
            "            line-height: 0.6;\n" +
            "        }\n" +
            "\n" +
            "        .cards-list {\n" +
            "            display: flex;\n" +
            "            flex-flow: row wrap;\n" +
            "        }\n" +
            "\n" +
            "        .card {\n" +
            "            width: auto;\n" +
            "            max-width: 400px;\n" +
            "            min-width: 200px;\n" +
            "            margin: 10px 10px 10px 0px;\n" +
            "            box-shadow: 0px 0px 6px 4px rgba(0,0,0,0.2);\n" +
            "            border-radius: 6px;\n" +
            "            background: #3f89f4;\n" +
            "        }\n" +
            "\n" +
            "        .card-container {\n" +
            "            color: white;\n" +
            "            width: auto;\n" +
            "            height: auto;\n" +
            "        }\n" +
            "\n" +
            "        .card-title {\n" +
            "            font-weight: 700;\n" +
            "            font-size: 1.2em;\n" +
            "            border-bottom: 1px solid #7eb8ff;\n" +
            "        }\n" +
            "\n" +
            "        .card-content {\n" +
            "            padding: 0px 10px 0px 10px;\n" +
            "        }\n" +
            "\n" +
            "        .card-title-text {\n" +
            "            padding: 10px 10px 0px 10px;\n" +
            "        }\n" +
            "\n" +
            "        .skills-list {\n" +
            "            width: fit-content;\n" +
            "        }\n" +
            "        li {\n" +
            "            line-height: 1.5;\n" +
            "        }\n" +
            "    </style>" +
            "</head>" +
            "<body class=\"main-content\">\n"

    return html
}

fun addPersonalInfo(resume: Resume): String {
    var html = ""
    if (resume.isNotEmpty()) {
        html += "<div class=\"personal-info\">\n" +
                "        <div class=\"name-container\">\n" +
                "            <h1 class=\"name\">${resume.name.toUpperCase()}</h1>\n" +
                "        </div>\n" +
                "        <div class=\"contact-info-container\">\n" +
                "            <p id=\"phone-number\">${resume.phone}</p> \n" +
                "            <p id=\"email-address\">${resume.email}</p>\n" +
                "            <p id=\"address\">${resume.address}</p>\n" +
                "        </div>\n" +
                "        <br>\n" +
                "        <div class=\"description\">\n" +
                "            <p id=\"description\">${resume.objective}</p>\n" +
                "        </div>\n" +
                "        <br>\n" +
                "    </div>"
    }
    return html
}

fun addEducationInfo(educationList: List<Education>): String {
    var html = ""
    if (!educationList.isEmpty() && educationList.first().isNotEmpty()) {
        html += "<div class=\"education-info\">\n" +
                "        <h2>Education</h2>\n" +
                "        <div class=\"cards-list\">"
        for (education in educationList) {
            if (education.isNotEmpty()) {
                html += "<div class=\"card education-card\">\n" +
                        "                <div class=\"card-container\">\n" +
                        "                    <div class=\"card-title\">\n" +
                        "                        <p class=\"card-title-text\" id=\"institute-name\">${education.instituteName}</p>\n" +
                        "                    </div>\n" +
                        "                    <div class=\"card-content\">\n" +
                        "                        <p id=\"degree\">${education.degree}</p>\n" +
                        "                        <p id=\"performance\">${education.performance}</p>\n" +
                        "                        <p id=\"year-of-graduation\">${education.year}</p>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "            </div>"
            }
        }
        html += "</div>\n" +
                "    </div>"
    }
    return html
}

fun addWorkSummaryInfo(workSummaryList: List<WorkSummary>): String {
    var html = ""
    if (workSummaryList.isNotEmpty() && workSummaryList.first().isNotEmpty()) {
        html += "<br>\n" +
                "    <div class=\"experience-info\">\n" +
                "        <h2>Work Summary</h2>\n" +
                "        <div class=\"cards-list\">"
        for (experience in workSummaryList) {
            if (experience.isNotEmpty()) {
                html += "<div class=\"card experience-card\">\n" +
                        "                <div class=\"card-container\">\n" +
                        "                    <div class=\"card-title\">\n" +
                        "                        <p class=\"card-title-text\" id=\"experience-name\">${experience.companyName}</p>\n" +
                        "                    </div>\n" +
                        "                    <div class=\"card-content\">\n" +
                        "                        <p id=\"experience-duration\">${experience.duration}</p>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "            </div>"
            }
        }

        html += "</div>\n" +
                "    </div>\n" +
                "    <br>"
    }
    return html
}

fun addSkillInfo(skillList: List<Skill>): String {
    var html = ""
    if (!skillList.isEmpty() && skillList.first().isNotEmpty()) {
        html += "<br>\n" +
                "    <div class=\"skill-info\">\n" +
                "        <h2>Skills</h2>\n" +
                "        <div class=\"cards-list\">"
        for (skill in skillList) {
            if (skill.isNotEmpty()) {
                html += "<div class=\"card experience-card\">\n" +
                        "                <div class=\"card-container\">\n" +
                        "                    <div class=\"card-title\">\n" +
                        "                        <p class=\"card-title-text\" id=\"skill-name\">${skill.skillName}</p>\n" +
                        "                    </div>\n" +
                        "                </div>\n" +
                        "            </div>"
            }
        }

        html += "</div>\n" +
                "    </div>\n" +
                "    <br>"
    }
    return html
}

fun addProjectInfo(projectsList: List<Project>): String {
    var html = ""
    if (!projectsList.isEmpty() && projectsList.first().isNotEmpty()) {
        html += "<div class=\"projects-info\">\n" +
                "        <h2>Projects</h2>\n" +
                "        <div class=\"cards-list\">"
        for (project in projectsList) {
            if (project.isNotEmpty()) {
                html += "<div class=\"card project-card\">\n" +
                        "                <div class=\"card-container\">\n" +
                        "                    <div class=\"card-title\">\n" +
                        "                        <p class=\"card-title-text\" id=\"project-name\">${project.projectName}</p>\n" +
                        "                    </div>\n" +
                        "                    <div class=\"card-content\">\n" +
                        "                        <p id=\"project-role\">${project.role}</p>\n"
                // We need to add this check because projectLink might be empty
                if (!project.teamSize.isEmpty()) {
                    html += "<p id=\"project-link\"><a href=\"${project.teamSize}\">${project.teamSize}</a></p>\n"
                }
                html +=
                    "                        <p id=\"project-description\">${project.summary}</p>\n" +
                            "                    </div>\n" +
                            "                </div>\n" +
                            "            </div>"
            }

            html += "</div>\n" +
                    "    </div>\n" +
                    "    <br>"
        }
    }
    return html
}


fun closeHtmlFile(): String {
    var html = ""
    html += "</body>\n" +
            "</html>"
    return html
}

fun buildHtml(
    resume: Resume,
    educationList: List<Education>,
    experienceList: List<WorkSummary>,
    skillList: List<Skill>,
    workSummaryList: List<Project>,
): String {
    var html = ""
    html += createBaseHTML()
    html += addPersonalInfo(resume)
    html += addWorkSummaryInfo(experienceList)
    html += addSkillInfo(skillList)
    html += addEducationInfo(educationList)
    html += addProjectInfo(workSummaryList)
    html += closeHtmlFile()
    return html
}