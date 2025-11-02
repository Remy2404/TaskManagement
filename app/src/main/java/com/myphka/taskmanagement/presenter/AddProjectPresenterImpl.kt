package com.myphka.taskmanagement.presenter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.Project
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskStatus
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class AddProjectPresenterImpl(
    private val todayTasksPresenter: TodayTasksPresenterImpl
) : AddProjectPresenter {

    private var view: AddProjectView? = null

    private var selectedTaskGroup = "Work"
    private var projectName = ""
    private var description = ""
    private var startDate = LocalDate.now()
    private var endDate = LocalDate.now().plusMonths(1)
    private var taskTitles = mutableListOf("Initial Planning", "Setup Development Environment")
    private var isLoading = false
    private var errorMessage: String? = null

    private val taskGroups = listOf("Work", "Personal", "Shopping", "Health")

    companion object {
        private const val TAG = "AddProjectPresenter"
    }

    override fun attachView(view: AddProjectView) {
        this.view = view
        Log.d(TAG, "View attached")
    }

    override fun detachView() {
        this.view = null
        Log.d(TAG, "View detached")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated() {
        view?.showTaskGroups(taskGroups)
        view?.showSelectedTaskGroup(selectedTaskGroup)
        view?.showProjectName(projectName)
        view?.showDescription(description)
        view?.showStartDate(startDate)
        view?.showEndDate(endDate)
        view?.showTaskTitles(taskTitles)
        view?.showLoading(isLoading)
        view?.showError(errorMessage)
    }

    override fun onTaskGroupSelected(group: String) {
        selectedTaskGroup = group
        view?.showSelectedTaskGroup(group)
        Log.d(TAG, "Task group selected: $group")
    }

    override fun onProjectNameChanged(name: String) {
        projectName = name
        view?.showProjectName(name)
        errorMessage = null
        view?.showError(null)
    }

    override fun onDescriptionChanged(description: String) {
        this.description = description
        view?.showDescription(description)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartDateSelected(date: LocalDate) {
        if (date <= endDate) {
            startDate = date
            view?.showStartDate(date)
            Log.d(TAG, "Start date selected: $date")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEndDateSelected(date: LocalDate) {
        if (date >= startDate) {
            endDate = date
            view?.showEndDate(date)
            Log.d(TAG, "End date selected: $date")
        }
    }

    override fun onAddTaskTitle(title: String) {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isNotEmpty() && !taskTitles.contains(trimmedTitle)) {
            taskTitles.add(trimmedTitle)
            view?.showTaskTitles(taskTitles)
            Log.d(TAG, "Task title added: $trimmedTitle")
        }
    }

    override fun onRemoveTaskTitle(index: Int) {
        if (index in taskTitles.indices) {
            val removed = taskTitles.removeAt(index)
            view?.showTaskTitles(taskTitles)
            Log.d(TAG, "Task title removed: $removed")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSaveProject() {
        val trimmedProjectName = projectName.trim()
        if (trimmedProjectName.isEmpty()) {
            errorMessage = "Project name cannot be empty"
            view?.showError(errorMessage)
            Log.e(TAG, "Project name is empty")
            return
        }

        if (taskTitles.isEmpty()) {
            errorMessage = "Please add at least one task title"
            view?.showError(errorMessage)
            Log.e(TAG, "No task titles specified")
            return
        }

        try {
            isLoading = true
            view?.showLoading(true)
            Log.d(TAG, "Adding project: $trimmedProjectName")

            val newProject = Project(
                name = trimmedProjectName,
                description = description.trim(),
                taskGroup = selectedTaskGroup,
                startDate = startDate,
                endDate = endDate
            )

            todayTasksPresenter.addProject(newProject)
            Log.d(TAG, "Project added with ID: ${newProject.id}")

            // Add tasks for the new project based on user-defined titles
            val taskDate = LocalDate.now()
            Log.d(TAG, "Creating ${taskTitles.size} tasks for date: $taskDate")
            val dynamicTasks = taskTitles.mapIndexed { index, title ->
                Task(
                    title = title,
                    subtitle = "Task for $trimmedProjectName project",
                    scheduledTime = LocalTime.of(9 + index, 0), // Stagger times
                    status = TaskStatus.TODO,
                    projectId = newProject.id,
                    date = taskDate
                )
            }
            dynamicTasks.forEach { task ->
                Log.d(TAG, "Adding task: ${task.title} | Date: ${task.date} | Project: ${task.projectId}")
                todayTasksPresenter.addTask(task)
                Log.d(TAG, "Task added: ${task.id}")
            }

            Log.d(TAG, "Project and sample tasks added successfully")
            isLoading = false
            view?.showLoading(false)

            // Navigate back after state update
            view?.navigateBack()
        } catch (e: Exception) {
            Log.e(TAG, "Error adding project: ${e.message}", e)
            errorMessage = "Error adding project: ${e.message}"
            view?.showError(errorMessage)
            isLoading = false
            view?.showLoading(false)
        }
    }

    override fun onBackClicked() {
        view?.navigateBack()
    }
}