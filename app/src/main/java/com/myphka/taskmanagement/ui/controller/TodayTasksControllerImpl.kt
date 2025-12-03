package com.myphka.taskmanagement.ui.controller

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.Project
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.model.TaskStatus
import com.myphka.taskmanagement.model.TodayTasksState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

/**
 * Implementation of TodayTasksController for MVC pattern.
 * This is the Controller in MVC - handles user input and updates the Model (state).
 *
 * @param scope CoroutineScope for async operations. Caller is responsible for cancellation
 *              if a custom scope is provided. Otherwise, call [cancel] when done.
 *              Note: The scope should use Dispatchers.Main for UI state updates.
 */
@RequiresApi(Build.VERSION_CODES.O)
class TodayTasksControllerImpl(
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) : TodayTasksController {

    companion object {
        private const val TAG = "TodayTasksController"
    }

    private val _state = MutableStateFlow(TodayTasksState())
    override val state: StateFlow<TodayTasksState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    override val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
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

        _state.update { it.copy(tasks = sampleTasks, projects = sampleProjects) }
        Log.d(TAG, "Sample data loaded: ${sampleTasks.size} tasks, ${sampleProjects.size} projects")
    }

    override fun selectDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        Log.d(TAG, "Date selected: $date")
    }

    override fun selectFilter(filter: TaskFilterType) {
        _state.update { it.copy(selectedFilter = filter) }
        Log.d(TAG, "Filter selected: $filter")
    }

    override fun toggleTaskStatus(taskId: String) {
        _state.update { currentState ->
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    val newStatus = when (task.status) {
                        TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                        TaskStatus.IN_PROGRESS -> TaskStatus.DONE
                        TaskStatus.DONE -> TaskStatus.TODO
                    }
                    task.copy(status = newStatus)
                } else {
                    task
                }
            }
            currentState.copy(tasks = updatedTasks)
        }
        Log.d(TAG, "Task status toggled: $taskId")
    }

    override fun onAddProjectClicked() {
        scope.launch {
            _events.emit(UiEvent.NavigateToAddProject)
        }
    }

    override fun onAddTaskClicked() {
        scope.launch {
            _events.emit(UiEvent.NavigateToAddTask)
        }
    }

    override fun getFilteredTasks(): List<Task> {
        val currentState = _state.value
        val filtered = currentState.tasks.filter { task ->
            task.date == currentState.selectedDate && when (currentState.selectedFilter) {
                TaskFilterType.ALL -> true
                TaskFilterType.TODO -> task.status == TaskStatus.TODO
                TaskFilterType.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS
                TaskFilterType.DONE -> task.status == TaskStatus.DONE
            }
        }
        Log.d(TAG, "=== Filter Debug ===")
        Log.d(TAG, "Selected date: ${currentState.selectedDate}")
        Log.d(TAG, "Selected filter: ${currentState.selectedFilter}")
        Log.d(TAG, "Total tasks in state: ${currentState.tasks.size}")
        Log.d(TAG, "Filtered result: ${filtered.size} tasks")
        Log.d(TAG, "==================")
        return filtered
    }

    /**
     * Adds a task to the state.
     */
    fun addTask(task: Task) {
        _state.update { currentState ->
            currentState.copy(tasks = currentState.tasks + task)
        }
        Log.d(TAG, "Task added: ${task.id} - ${task.title}")
    }

    /**
     * Adds a project to the state.
     */
    fun addProject(project: Project) {
        _state.update { currentState ->
            currentState.copy(projects = currentState.projects + project)
        }
        Log.d(TAG, "Project added: ${project.id} - ${project.name}")
    }

    override fun cancel() {
        scope.cancel()
        Log.d(TAG, "Controller cancelled")
    }
}
