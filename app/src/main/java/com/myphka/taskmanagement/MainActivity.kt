package com.myphka.taskmanagement

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.myphka.taskmanagement.ui.theme.TaskManagementTheme
import com.myphka.taskmanagement.viewmodel.TaskManagementViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TaskManagementViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskManagementTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
