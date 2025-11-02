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
import com.myphka.taskmanagement.viewmodel.TaskManagementViewModel

enum class Screen {
    TODAY_TASKS, ADD_PROJECT
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(viewModel: TaskManagementViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.TODAY_TASKS) }

    when (currentScreen) {
        Screen.TODAY_TASKS -> {
            TodayTasksScreen(
                viewModel = viewModel,
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
                viewModel = viewModel,
                onBackClick = {
                    currentScreen = Screen.TODAY_TASKS
                }
            )
        }
    }
}
