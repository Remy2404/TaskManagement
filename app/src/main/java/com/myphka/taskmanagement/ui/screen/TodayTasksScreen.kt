package com.myphka.taskmanagement.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.ui.components.DateSelector
import com.myphka.taskmanagement.ui.components.FilterTab
import com.myphka.taskmanagement.ui.components.TaskCard
import com.myphka.taskmanagement.ui.theme.BackgroundLavender
import com.myphka.taskmanagement.ui.theme.NeutralGray
import com.myphka.taskmanagement.ui.theme.PrimaryBrand
import com.myphka.taskmanagement.ui.theme.TextDark
import com.myphka.taskmanagement.viewmodel.TaskManagementViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import android.util.Log
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.myphka.taskmanagement.model.TaskStatus


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayTasksScreen(
    viewModel: TaskManagementViewModel,
    onNavigateToAddProject: () -> Unit = {},
    onAddTask: () -> Unit,
    onScreenVisible: () -> Unit = {}
) {
    // Call onScreenVisible when screen becomes visible
    androidx.compose.runtime.LaunchedEffect(Unit) {
        onScreenVisible()
    }
    
    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()

    // Debug logging
    Log.d("TodayTasksScreen", "Screen recomposed | Filter: ${uiState.selectedFilter} | Date: ${uiState.selectedDate} | Total tasks: ${uiState.tasks.size}")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddProject, containerColor = PrimaryBrand,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }, floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomNavigationBar(
                onNavigateToAddProject = onNavigateToAddProject
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLavender)
                .padding(innerPadding)
        ) {
            // Header
            Text(
                text = "Today's Tasks",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Date Selector
            DateSelector(
                selectedDate = uiState.selectedDate,
                onDateSelected = { date ->
                    viewModel.selectDate(date)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "All",
                    isSelected = uiState.selectedFilter == TaskFilterType.ALL,
                    onClick = { viewModel.selectFilter(TaskFilterType.ALL) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "To Do",
                    isSelected = uiState.selectedFilter == TaskFilterType.TODO,
                    onClick = { viewModel.selectFilter(TaskFilterType.TODO) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "In Progress",
                    isSelected = uiState.selectedFilter == TaskFilterType.IN_PROGRESS,
                    onClick = { viewModel.selectFilter(TaskFilterType.IN_PROGRESS) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "Done",
                    isSelected = uiState.selectedFilter == TaskFilterType.DONE,
                    onClick = { viewModel.selectFilter(TaskFilterType.DONE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tasks List - directly filter from uiState, ensuring reactivity
            val filteredTasks = remember(uiState.tasks, uiState.selectedDate, uiState.selectedFilter) {
                uiState.tasks.filter { task ->
                    task.date == uiState.selectedDate && when (uiState.selectedFilter) {
                        TaskFilterType.ALL -> true
                        TaskFilterType.TODO -> task.status == TaskStatus.TODO
                        TaskFilterType.IN_PROGRESS -> task.status == TaskStatus.IN_PROGRESS
                        TaskFilterType.DONE -> task.status == TaskStatus.DONE
                    }
                }
            }

            // Scroll to bottom when new tasks are added
            LaunchedEffect(filteredTasks.size) {
                if (filteredTasks.isNotEmpty()) {
                    listState.animateScrollToItem(filteredTasks.size - 1)
                }
            }

            Log.d("TodayTasksScreen", "Rendering ${filteredTasks.size} tasks in LazyColumn")
            Log.d("TodayTasksScreen", "All tasks in state: ${uiState.tasks.size}")
            uiState.tasks.forEachIndexed { index, task ->
                Log.d("TodayTasksScreen", "  State[$index]: ${task.title} on ${task.date}")
            }
            filteredTasks.forEachIndexed { index, task ->
                Log.d("TodayTasksScreen", "  Filtered[$index]: ${task.title} - ${task.id}")
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredTasks,
                    key = { task -> task.id }
                ) { task ->
                    val project = uiState.projects.find { it.id == task.projectId }
                    val projectName = project?.name ?: "Unknown Project"
                    TaskCard(
                        task = task,
                        projectName = projectName,
                        onClick = {
                            val nextStatus = when (task.status) {
                                TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                                TaskStatus.IN_PROGRESS -> TaskStatus.DONE
                                TaskStatus.DONE -> TaskStatus.TODO
                            }
                            viewModel.updateTaskStatus(task.id, nextStatus)
                        }
                    )
                }
                
                if (filteredTasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No tasks for this filter",
                                fontSize = 16.sp,
                                color = NeutralGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onNavigateToAddProject: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Tasks",
                    tint = PrimaryBrand,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Calendar",
                    tint = NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = onNavigateToAddProject) {
                Icon(
                    imageVector = Icons.Filled.Folder,
                    contentDescription = "Projects",
                    tint = NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TodayTasksScreenPreview() {
    TodayTasksScreen(
        viewModel = TaskManagementViewModel(),
        onAddTask = {},
        onScreenVisible = {}
    )
}
