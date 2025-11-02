package com.myphka.taskmanagement.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.AppState
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import java.time.LocalDate

/**
 * View contract for TodayTasks screen
 */
@RequiresApi(Build.VERSION_CODES.O)
interface TodayTasksView : BaseView {
    fun showTasks(tasks: List<Task>)
    fun showProjects(projects: List<com.myphka.taskmanagement.model.Project>)
    fun showSelectedDate(date: LocalDate)
    fun showSelectedFilter(filter: TaskFilterType)
    fun updateTaskStatus(taskId: String, newStatus: com.myphka.taskmanagement.model.TaskStatus)
    fun navigateToAddProject()
    fun showNoTasksMessage()
}

/**
 * Presenter contract for TodayTasks
 */
@RequiresApi(Build.VERSION_CODES.O)
interface TodayTasksPresenter : BasePresenter<TodayTasksView> {
    fun onViewCreated()
    fun onDateSelected(date: LocalDate)
    fun onFilterSelected(filter: TaskFilterType)
    fun onTaskClicked(task: Task)
    fun onAddProjectClicked()
}