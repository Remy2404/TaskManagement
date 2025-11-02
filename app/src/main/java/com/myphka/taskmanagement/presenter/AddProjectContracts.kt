package com.myphka.taskmanagement.presenter

import android.os.Build
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.model.Project
import java.time.LocalDate

/**
 * View contract for AddProject screen
 */
@RequiresApi(Build.VERSION_CODES.O)
interface AddProjectView : BaseView {
    fun showTaskGroups(groups: List<String>)
    fun showSelectedTaskGroup(group: String)
    fun showProjectName(name: String)
    fun showDescription(description: String)
    fun showStartDate(date: LocalDate)
    fun showEndDate(date: LocalDate)
    fun showTaskTitles(titles: List<String>)
    fun showLoading(isLoading: Boolean)
    fun showError(message: String?)
    fun navigateBack()
}

/**
 * Presenter contract for AddProject
 */
@RequiresApi(Build.VERSION_CODES.O)
interface AddProjectPresenter : BasePresenter<AddProjectView> {
    fun onViewCreated()
    fun onTaskGroupSelected(group: String)
    fun onProjectNameChanged(name: String)
    fun onDescriptionChanged(description: String)
    fun onStartDateSelected(date: LocalDate)
    fun onEndDateSelected(date: LocalDate)
    fun onAddTaskTitle(title: String)
    fun onRemoveTaskTitle(index: Int)
    fun onSaveProject()
    fun onBackClicked()
}