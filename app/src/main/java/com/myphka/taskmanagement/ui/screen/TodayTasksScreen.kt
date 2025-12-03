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
import androidx.compose.runtime.DisposableEffect
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
import com.myphka.taskmanagement.presenter.TodayTasksPresenter
import com.myphka.taskmanagement.presenter.TodayTasksView
import com.myphka.taskmanagement.presenter.TodayTasksPresenterImpl
import com.myphka.taskmanagement.ui.controller.TodayTasksController
import com.myphka.taskmanagement.ui.controller.TodayTasksControllerFactory
import com.myphka.taskmanagement.ui.controller.TodayTasksControllerImpl
import com.myphka.taskmanagement.ui.controller.UiEvent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import android.util.Log
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskStatus
import com.myphka.taskmanagement.model.Project
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayTasksScreen(
    presenter: TodayTasksPresenter,
    onNavigateToAddProject: () -> Unit = {},
    onAddTask: () -> Unit,
    onScreenVisible: () -> Unit = {}
) {
    val view = remember { TodayTasksViewImpl(presenter, onNavigateToAddProject) }

    // Attach view to presenter
    LaunchedEffect(presenter) {
        presenter.attachView(view)
        presenter.onViewCreated()
    }

    // Call onScreenVisible when screen becomes visible
    androidx.compose.runtime.LaunchedEffect(Unit) {
        onScreenVisible()
    }

    val listState = rememberLazyListState()

    // Debug logging
    Log.d("TodayTasksScreen", "Screen recomposed | Filter: ${view.selectedFilter} | Date: ${view.selectedDate} | Total tasks: ${view.tasks.size}")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { presenter.onAddProjectClicked() }, containerColor = PrimaryBrand,
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
                onNavigateToAddProject = { presenter.onAddProjectClicked() }
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
                selectedDate = view.selectedDate,
                onDateSelected = { date ->
                    presenter.onDateSelected(date)
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
                    isSelected = view.selectedFilter == TaskFilterType.ALL,
                    onClick = { presenter.onFilterSelected(TaskFilterType.ALL) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "To Do",
                    isSelected = view.selectedFilter == TaskFilterType.TODO,
                    onClick = { presenter.onFilterSelected(TaskFilterType.TODO) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "In Progress",
                    isSelected = view.selectedFilter == TaskFilterType.IN_PROGRESS,
                    onClick = { presenter.onFilterSelected(TaskFilterType.IN_PROGRESS) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "Done",
                    isSelected = view.selectedFilter == TaskFilterType.DONE,
                    onClick = { presenter.onFilterSelected(TaskFilterType.DONE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tasks List
            val filteredTasks = remember(view.tasks, view.selectedDate, view.selectedFilter) {
                view.tasks.filter { task ->
                    task.date == view.selectedDate && when (view.selectedFilter) {
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
            Log.d("TodayTasksScreen", "All tasks in state: ${view.tasks.size}")
            view.tasks.forEachIndexed { index, task ->
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
                    val project = view.projects.find { it.id == task.projectId }
                    val projectName = project?.name ?: "Unknown Project"
                    TaskCard(
                        task = task,
                        projectName = projectName,
                        onClick = {
                            presenter.onTaskClicked(task)
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

@RequiresApi(Build.VERSION_CODES.O)
class TodayTasksViewImpl(
    private val presenter: TodayTasksPresenter,
    private val onNavigateToAddProject: () -> Unit
) : TodayTasksView {

    var tasks by mutableStateOf<List<Task>>(emptyList())
    var projects by mutableStateOf<List<Project>>(emptyList())
    var selectedDate by mutableStateOf(LocalDate.now())
    var selectedFilter by mutableStateOf(TaskFilterType.ALL)

    override fun showTasks(tasks: List<Task>) {
        this.tasks = tasks
    }

    override fun showProjects(projects: List<Project>) {
        this.projects = projects
    }

    override fun showSelectedDate(date: LocalDate) {
        selectedDate = date
    }

    override fun showSelectedFilter(filter: TaskFilterType) {
        selectedFilter = filter
    }

    override fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        tasks = tasks.map { task ->
            if (task.id == taskId) task.copy(status = newStatus) else task
        }
    }

    override fun navigateToAddProject() {
        onNavigateToAddProject()
    }

    override fun showNoTasksMessage() {
        // Message is shown in the UI when filteredTasks is empty
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
    val presenter = TodayTasksPresenterImpl()
    TodayTasksScreen(
        presenter = presenter,
        onAddTask = {},
        onScreenVisible = {}
    )
}

// ============================================================================
// MVC Pattern - Controller-based TodayTasksScreen
// ============================================================================

/**
 * TodayTasksScreen using MVC Controller pattern.
 * This is the new preferred way to use this screen.
 * 
 * @param controller The TodayTasksController instance
 * @param onNavigateToAddProject Callback for navigating to add project screen
 * @param onAddTask Callback for adding a task
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayTasksScreen(
    controller: TodayTasksController,
    onNavigateToAddProject: () -> Unit = {},
    onAddTask: () -> Unit = {}
) {
    val state by controller.state.collectAsState()
    val listState = rememberLazyListState()

    // Handle one-shot events
    LaunchedEffect(Unit) {
        controller.events.collect { event ->
            when (event) {
                is UiEvent.NavigateToAddProject -> onNavigateToAddProject()
                is UiEvent.NavigateToAddTask -> onAddTask()
                is UiEvent.ShowError -> Log.e("TodayTasksScreen", "Error: ${event.message}")
                is UiEvent.ShowMessage -> Log.d("TodayTasksScreen", "Message: ${event.message}")
            }
        }
    }

    // Debug logging
    Log.d("TodayTasksScreen", "Controller Screen recomposed | Filter: ${state.selectedFilter} | Date: ${state.selectedDate} | Total tasks: ${state.tasks.size}")

    // Calculate filtered tasks
    val filteredTasks = remember(state.tasks, state.selectedDate, state.selectedFilter) {
        state.tasks.filter { task ->
            task.date == state.selectedDate && when (state.selectedFilter) {
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { controller.onAddProjectClicked() },
                containerColor = PrimaryBrand,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomNavigationBar(
                onNavigateToAddProject = { controller.onAddProjectClicked() }
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Date Selector
            DateSelector(
                selectedDate = state.selectedDate,
                onDateSelected = { date ->
                    controller.selectDate(date)
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
                    isSelected = state.selectedFilter == TaskFilterType.ALL,
                    onClick = { controller.selectFilter(TaskFilterType.ALL) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "To Do",
                    isSelected = state.selectedFilter == TaskFilterType.TODO,
                    onClick = { controller.selectFilter(TaskFilterType.TODO) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "In Progress",
                    isSelected = state.selectedFilter == TaskFilterType.IN_PROGRESS,
                    onClick = { controller.selectFilter(TaskFilterType.IN_PROGRESS) }
                )
                FilterTab(
                    modifier = Modifier.weight(1f),
                    label = "Done",
                    isSelected = state.selectedFilter == TaskFilterType.DONE,
                    onClick = { controller.selectFilter(TaskFilterType.DONE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Log.d("TodayTasksScreen", "Rendering ${filteredTasks.size} tasks in LazyColumn (Controller)")

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
                    val project = state.projects.find { it.id == task.projectId }
                    val projectName = project?.name ?: "Unknown Project"
                    TaskCard(
                        task = task,
                        projectName = projectName,
                        onClick = {
                            controller.toggleTaskStatus(task.id)
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

/**
 * Backward-compatible overload that creates a controller from a presenter.
 * Use this during migration to keep existing presenter-based code working.
 * 
 * @param presenter The existing TodayTasksPresenterImpl
 * @param onNavigateToAddProject Callback for navigating to add project screen  
 * @param onAddTask Callback for adding a task
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayTasksScreenWithController(
    presenter: TodayTasksPresenterImpl,
    onNavigateToAddProject: () -> Unit = {},
    onAddTask: () -> Unit = {}
) {
    val controller = remember { TodayTasksControllerFactory.createFromPresenter(presenter) }
    
    // Cancel controller when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            controller.cancel()
        }
    }
    
    TodayTasksScreen(
        controller = controller,
        onNavigateToAddProject = onNavigateToAddProject,
        onAddTask = onAddTask
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TodayTasksScreenControllerPreview() {
    val controller = TodayTasksControllerImpl()
    TodayTasksScreen(
        controller = controller,
        onAddTask = {}
    )
}
