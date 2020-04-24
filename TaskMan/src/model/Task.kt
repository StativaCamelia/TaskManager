package model

import java.util.*


data class Task(var id: String? = null,var boardName: String?=null, val title: String, val priority: Priority?=null, var status: Status?=Status.TO_DO, var assignedTo: String ? = null, val createdAt: Date?=Calendar.getInstance().time, val descriptions : String?=null, val taskType : TaskTypes?=null, val estimatedTime: Int?=null){

    enum class TaskTypes{
        DEV,
        TEST,
        REVIEW
    }
    enum class Status {
        TO_DO,
        DOING,
        IN_REVIEW,
        DONE
    }
    enum class Priority{
        URGENT,
        IMPORTANT,
        NOT_IMPORTANT
    }
}
