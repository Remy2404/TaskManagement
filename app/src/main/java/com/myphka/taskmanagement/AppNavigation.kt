package com.myphka.taskmanagement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.myphka.taskmanagement.ui.screen.TodayTasksScreen
import com.myphka.taskmanagement.ui.screen.AddProjectScreen
import com.myphka.taskmanagement.presenter.TodayTasksPresenterImpl
import com.myphka.taskmanagement.presenter.AddProjectPresenterImpl

enum class Screen {
    TODAY_TASKS, ADD_PROJECT
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.TODAY_TASKS) }

    // Create presenters
    val todayTasksPresenter = remember { TodayTasksPresenterImpl() }
    val addProjectPresenter = remember { AddProjectPresenterImpl(todayTasksPresenter) }

    when (currentScreen) {
        Screen.TODAY_TASKS -> {
            TodayTasksScreen(
                presenter = todayTasksPresenter,
                onNavigateToAddProject = {
                    currentScreen = Screen.ADD_PROJECT
                },
                onAddTask = {
                    // Handle add task navigation/dialog
                }
            )
        }
        Screen.ADD_PROJECT -> {
            AddProjectScreen(
                presenter = addProjectPresenter,
                viewModel = addProjectPresenter,
                onBackClick = {
                    currentScreen = Screen.TODAY_TASKS
                }
            )
        }
    }
}
