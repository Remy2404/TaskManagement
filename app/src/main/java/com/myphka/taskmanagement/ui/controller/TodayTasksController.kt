package com.myphka.taskmanagement.ui.controller

import android.os.Build
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.model.TodayTasksState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/**
 * Controller interface for TodayTasks screen in MVC pattern.
 * This defines the contract between the View (Composable) and the Controller.
 */
@RequiresApi(Build.VERSION_CODES.O)
interface TodayTasksController {
    /**
     * Observable UI state that the view collects.
     */
    val state: StateFlow<TodayTasksState>

    /**
     * One-shot UI events (navigation, messages, etc.)
     */
    val events: SharedFlow<UiEvent>

    /**
     * Called when user selects a date.
     */
    fun selectDate(date: LocalDate)

    /**
     * Called when user selects a filter type.
     */
    fun selectFilter(filter: TaskFilterType)

    /**
     * Called when user clicks on a task to toggle its status.
     */
    fun toggleTaskStatus(taskId: String)

    /**
     * Called when user clicks the add project button.
     */
    fun onAddProjectClicked()

    /**
     * Called when user clicks the add task button.
     */
    fun onAddTaskClicked()

    /**
     * Returns filtered tasks based on current state.
     */
    fun getFilteredTasks(): List<Task>

    /**
     * Lifecycle method to cancel any ongoing coroutines.
     * Must be called when the controller is no longer needed.
     */
    fun cancel()
}
