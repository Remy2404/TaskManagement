package com.myphka.taskmanagement.model

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
data class AppState(
    val selectedDate: java.time.LocalDate = java.time.LocalDate.now(),
    val selectedFilter: TaskFilterType = TaskFilterType.ALL,
    val tasks: List<Task> = emptyList(),
    val projects: List<Project> = emptyList()
)

enum class TaskFilterType {
    ALL, TODO, IN_PROGRESS, DONE
}
