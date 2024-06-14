package model

import data.TaskData

data class TasksModel(
    val tasks: List<TaskData>,
    val currentTask: TaskData?,
)
