package com.myphka.taskmanagement.ui.controller

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.model.TaskStatus
import com.myphka.taskmanagement.model.TodayTasksState
import com.myphka.taskmanagement.presenter.TodayTasksPresenter
import com.myphka.taskmanagement.presenter.TodayTasksPresenterImpl
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Adapter that wraps an existing TodayTasksPresenterImpl to implement TodayTasksController.
 * This allows gradual migration from MVP pattern to MVC pattern.
 * 
 * @param presenter The existing presenter to adapt
 * @param scope CoroutineScope for async operations
 */
@RequiresApi(Build.VERSION_CODES.O)
class PresenterControllerAdapter(
    private val presenter: TodayTasksPresenterImpl,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) : TodayTasksController {

    companion object {
        private const val TAG = "PresenterControllerAdapter"
    }

    private val _state = MutableStateFlow(TodayTasksState())
    override val state: StateFlow<TodayTasksState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    override val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        // Observe presenter's uiState and map to TodayTasksState
        presenter.uiState.onEach { appState ->
            _state.update {
                TodayTasksState(
                    tasks = appState.tasks,
                    projects = appState.projects,
                    selectedDate = appState.selectedDate,
                    selectedFilter = appState.selectedFilter
                )
            }
        }.launchIn(scope)
        Log.d(TAG, "Adapter initialized, listening to presenter state")
    }

    override fun selectDate(date: LocalDate) {
        presenter.onDateSelected(date)
    }

    override fun selectFilter(filter: TaskFilterType) {
        presenter.onFilterSelected(filter)
    }

    override fun toggleTaskStatus(taskId: String) {
        // Find the task and delegate to presenter
        val task = _state.value.tasks.find { it.id == taskId }
        if (task != null) {
            presenter.onTaskClicked(task)
        }
    }

    override fun onAddProjectClicked() {
        scope.launch {
            _events.emit(UiEvent.NavigateToAddProject)
        }
        presenter.onAddProjectClicked()
    }

    override fun onAddTaskClicked() {
        scope.launch {
            _events.emit(UiEvent.NavigateToAddTask)
        }
    }

    override fun getFilteredTasks(): List<Task> {
        val currentState = _state.value
        return currentState.tasks.filter { task ->
            task.date == currentState.selectedDate && when (currentState.selectedFilter) {
                TaskFilterType.ALL -> true
                TaskFilterType.TODO -> task.status == TaskStatus.TODO
                TaskFilterType.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS
                TaskFilterType.DONE -> task.status == TaskStatus.DONE
            }
        }
    }

    override fun cancel() {
        scope.cancel()
        presenter.detachView()
        Log.d(TAG, "Adapter cancelled")
    }
}
