package com.myphka.taskmanagement.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.myphka.taskmanagement.model.AppState
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.model.TaskStatus
import com.myphka.taskmanagement.model.Project
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class TaskManagementViewModel : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(AppState())
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "TaskManagementViewModel"
    }

    init {
        // Initialize with sample data
        loadSampleData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadSampleData() {
        val today = LocalDate.now()
        val sampleTasks = listOf(
            Task(
                title = "Market Research",
                subtitle = "Grocery shopping app design",
                scheduledTime = LocalTime.of(10, 0),
                status = TaskStatus.TODO,
                projectId = "proj1",
                date = today
            ),
            Task(
                title = "Design Mockups",
                subtitle = "Create UI mockups for dashboard",
                scheduledTime = LocalTime.of(11, 30),
                status = TaskStatus.IN_PROGRESS,
                projectId = "proj1",
                date = today
            ),
            Task(
                title = "Team Meeting",
                subtitle = "Sprint planning session",
                scheduledTime = LocalTime.of(14, 0),
                status = TaskStatus.IN_PROGRESS,
                projectId = "proj2",
                date = today
            ),
            Task(
                title = "Code Review",
                subtitle = "Review PR #345",
                scheduledTime = LocalTime.of(15, 30),
                status = TaskStatus.DONE,
                projectId = "proj1",
                date = today
            ),
            Task(
                title = "Update Documentation",
                subtitle = "API endpoint specs",
                scheduledTime = LocalTime.of(16, 0),
                status = TaskStatus.TODO,
                projectId = "proj2",
                date = today
            )
        )

        val sampleProjects = listOf(
            Project(
                id = "proj1",
                name = "Grocery Shopping App",
                description = "Mobile application for grocery shopping",
                taskGroup = "Work",
                startDate = LocalDate.of(2024, 5, 1),
                endDate = LocalDate.of(2024, 6, 30)
            ),
            Project(
                id = "proj2",
                name = "Internal Tools",
                description = "Internal productivity tools",
                taskGroup = "Work",
                startDate = LocalDate.of(2024, 6, 1),
                endDate = LocalDate.of(2024, 7, 31)
            )
        )

        _uiState.value = _uiState.value.copy(
            tasks = sampleTasks,
            projects = sampleProjects
        )
        Log.d(TAG, "Sample data loaded: ${sampleTasks.size} tasks, ${sampleProjects.size} projects")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectDate(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        Log.d(TAG, "Date selected: $date")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectFilter(filter: TaskFilterType) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        Log.d(TAG, "Filter selected: $filter")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(task: Task) {
        val updatedTasks = _uiState.value.tasks + task
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        Log.d(TAG, "Task added: ${task.id} - ${task.title}")
        Log.d(TAG, "Total tasks now: ${updatedTasks.size}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addProject(project: Project) {
        val updatedProjects = _uiState.value.projects + project
        _uiState.value = _uiState.value.copy(projects = updatedProjects)
        Log.d(TAG, "Project added: ${project.id} - ${project.name}")
        Log.d(TAG, "Total projects now: ${updatedProjects.size}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        val updatedTasks = _uiState.value.tasks.map { task ->
            if (task.id == taskId) task.copy(status = newStatus) else task
        }
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        Log.d(TAG, "Task status updated: $taskId -> $newStatus")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFilteredTasks(state: AppState = _uiState.value): List<Task> {
        val filtered = state.tasks.filter { task ->
            task.date == state.selectedDate && when (state.selectedFilter) {
                TaskFilterType.ALL -> true
                TaskFilterType.TODO -> task.status == TaskStatus.TODO
                TaskFilterType.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS
                TaskFilterType.DONE -> task.status == TaskStatus.DONE
            }
        }
        Log.d(TAG, "=== Filter Debug ===")
        Log.d(TAG, "Selected date: ${state.selectedDate}")
        Log.d(TAG, "Selected filter: ${state.selectedFilter}")
        Log.d(TAG, "Total tasks in state: ${state.tasks.size}")
        Log.d(TAG, "Tasks by date:")
        state.tasks.groupBy { it.date }.forEach { (date, tasks) ->
            Log.d(TAG, "  $date: ${tasks.size} tasks")
        }
        Log.d(TAG, "Filtered result: ${filtered.size} tasks")
        filtered.forEach { task ->
            Log.d(TAG, "  - ${task.title} (${task.status}) on ${task.date}")
        }
        Log.d(TAG, "==================")
        return filtered
    }
}
