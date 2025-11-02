package com.myphka.taskmanagement.presenter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.AppState
import com.myphka.taskmanagement.model.Project
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.model.TaskRepository
import com.myphka.taskmanagement.model.TaskRepositoryImpl
import com.myphka.taskmanagement.model.TaskStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TodayTasksPresenterImpl(
    private val taskRepository: TaskRepository = TaskRepositoryImpl(),
    private val projectRepository: com.myphka.taskmanagement.model.ProjectRepository = com.myphka.taskmanagement.model.ProjectRepositoryImpl()
) : TodayTasksPresenter {

    private var view: TodayTasksView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(AppState())
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "TodayTasksPresenter"
    }

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
                scheduledTime = java.time.LocalTime.of(10, 0),
                status = TaskStatus.TODO,
                projectId = "proj1",
                date = today
            ),
            Task(
                title = "Design Mockups",
                subtitle = "Create UI mockups for dashboard",
                scheduledTime = java.time.LocalTime.of(11, 30),
                status = TaskStatus.IN_PROGRESS,
                projectId = "proj1",
                date = today
            ),
            Task(
                title = "Team Meeting",
                subtitle = "Sprint planning session",
                scheduledTime = java.time.LocalTime.of(14, 0),
                status = TaskStatus.IN_PROGRESS,
                projectId = "proj2",
                date = today
            ),
            Task(
                title = "Code Review",
                subtitle = "Review PR #345",
                scheduledTime = java.time.LocalTime.of(15, 30),
                status = TaskStatus.DONE,
                projectId = "proj1",
                date = today
            ),
            Task(
                title = "Update Documentation",
                subtitle = "API endpoint specs",
                scheduledTime = java.time.LocalTime.of(16, 0),
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

    override fun attachView(view: TodayTasksView) {
        this.view = view
        Log.d(TAG, "View attached")
    }

    override fun detachView() {
        this.view = null
        Log.d(TAG, "View detached")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated() {
        view?.showSelectedDate(_uiState.value.selectedDate)
        view?.showSelectedFilter(_uiState.value.selectedFilter)
        view?.showProjects(_uiState.value.projects)
        updateTasksDisplay()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        view?.showSelectedDate(date)
        updateTasksDisplay()
        Log.d(TAG, "Date selected: $date")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFilterSelected(filter: TaskFilterType) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        view?.showSelectedFilter(filter)
        updateTasksDisplay()
        Log.d(TAG, "Filter selected: $filter")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTaskClicked(task: Task) {
        val nextStatus = when (task.status) {
            TaskStatus.TODO -> TaskStatus.IN_PROGRESS
            TaskStatus.IN_PROGRESS -> TaskStatus.DONE
            TaskStatus.DONE -> TaskStatus.TODO
        }
        updateTaskStatus(task.id, nextStatus)
    }

    override fun onAddProjectClicked() {
        view?.navigateToAddProject()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTasksDisplay() {
        val filteredTasks = getFilteredTasks(_uiState.value)
        if (filteredTasks.isEmpty()) {
            view?.showNoTasksMessage()
        } else {
            view?.showTasks(filteredTasks)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(task: Task) {
        val updatedTasks = _uiState.value.tasks + task
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        updateTasksDisplay()
        Log.d(TAG, "Task added: ${task.id} - ${task.title}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addProject(project: Project) {
        val updatedProjects = _uiState.value.projects + project
        _uiState.value = _uiState.value.copy(projects = updatedProjects)
        view?.showProjects(updatedProjects)
        Log.d(TAG, "Project added: ${project.id} - ${project.name}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        val updatedTasks = _uiState.value.tasks.map { task ->
            if (task.id == taskId) task.copy(status = newStatus) else task
        }
        _uiState.value = _uiState.value.copy(tasks = updatedTasks)
        view?.updateTaskStatus(taskId, newStatus)
        updateTasksDisplay()
        Log.d(TAG, "Task status updated: $taskId -> $newStatus")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getFilteredTasks(state: AppState = _uiState.value): List<Task> {
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