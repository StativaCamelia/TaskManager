package server

import model.Task
import java.util.UUID.*

class TaskService{
private val tasks = mutableListOf<Task>()

    fun assignTask(auth:Boolean, taskName: String, assignTo: String):String{
        return when {
            !auth -> "You should be authenticated (sign in) in order to perform this operation"
            else -> {
                return if (taskName !in tasks.map { it.title }) {
                    "The task doesn't exist. Make sure to create the task first"
                } else {
                    val task = tasks.find { it.title == taskName }
                    if (task != null) {
                        task.assignedTo = assignTo
                    }
                    task.toString()
                }
            }
        }
    }

    fun changeStatus(auth:Boolean, taskName: String, newStatus: Task.Status):String{
        return when {
            !auth -> "You should be authenticated (sign in) in order to perform this operation"
            taskName !in tasks.map { it.title } -> "The task doesn't exist. Make sure to create the task first"
            else -> {
                val task = tasks.find { it.title == taskName }
                if (task != null) {
                    task.status = newStatus
                }
                task.toString()
            }
        }
    }

    fun listByStatus(auth: Boolean, status: Task.Status):String = when {
        !auth -> "You should be authenticated (sign in) in order to perform this operation"
        else -> {
            val taskList = tasks.filter { it.status == status }
            taskList.toString()
        }
    }

    fun listByUser(auth: Boolean,assignedTo: String) :String = when {
        !auth -> "You should be authenticated (sign in) in order to perform this operation"
        else -> {
            val taskList = tasks.filter { it.assignedTo == assignedTo }
            taskList.toString()
        }
    }

    fun listByType(auth: Boolean, type: Task.TaskTypes):String = when {
        !auth -> "You should be authenticated (sign in) in order to perform this operation"
        else -> {
            val taskList = tasks.filter { it.taskType == type }
            taskList.toString()
        }
    }


    fun listAll(auth: Boolean): String = when {
        !auth -> "You should be authenticated (sign in) in order to perform this operation"
        else -> tasks.toString()
    }

    fun deleteTask(auth: Boolean, taskName : String):String = when {
        !auth -> "You should be authenticated (sign in) in order to perform this operation"
        else -> {
            tasks.removeAll { it.title == taskName }
            tasks.toString()
        }
    }


fun findTaskByName(taskName: String): Task? = when (taskName) {
    !in tasks.map { it.title } -> null
    else -> tasks.find { it.title == taskName }
}


fun addTask(auth: Boolean,task: Task): String = when {
    auth -> {
        task.id = randomUUID().toString()
        require(task.title !in tasks.map { it.title })
        tasks.add(task)
        task.toString()
    }
    else -> "You should be authenticated (sign in) in order to perform this operation"
}
}

