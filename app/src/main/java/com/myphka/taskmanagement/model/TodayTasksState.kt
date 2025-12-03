package com.myphka.taskmanagement.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

/**
 * UI state for TodayTasks screen in MVC pattern.
 * This is the Model in MVC.
 */
@RequiresApi(Build.VERSION_CODES.O)
data class TodayTasksState(
    val tasks: List<Task> = emptyList(),
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedFilter: TaskFilterType = TaskFilterType.ALL
)
