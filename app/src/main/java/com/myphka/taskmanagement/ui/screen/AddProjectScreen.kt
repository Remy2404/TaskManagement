package com.myphka.taskmanagement.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myphka.taskmanagement.ui.theme.BackgroundLavender
import com.myphka.taskmanagement.ui.theme.NeutralGray
import com.myphka.taskmanagement.ui.theme.PrimaryBrand
import com.myphka.taskmanagement.ui.theme.TextDark
import com.myphka.taskmanagement.ui.theme.TextMuted
import com.myphka.taskmanagement.presenter.AddProjectPresenter
import com.myphka.taskmanagement.presenter.AddProjectView
import com.myphka.taskmanagement.presenter.TodayTasksPresenterImpl
import com.myphka.taskmanagement.presenter.AddProjectPresenterImpl
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(
    presenter: AddProjectPresenter,
    onBackClick: () -> Unit = {},
    viewModel: AddProjectPresenterImpl
) {
    val view = remember { AddProjectViewImpl(presenter, onBackClick) }

    // Attach view to presenter
    LaunchedEffect(presenter) {
        presenter.attachView(view)
        presenter.onViewCreated()
    }

    // Handle system back button
    BackHandler {
        presenter.onBackClicked()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLavender)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextDark,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = "Add Project",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.width(40.dp))
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Task Group Dropdown
            item {
                FormField(label = "Task Group") {
                    Box {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { view.showTaskGroupDropdown = !view.showTaskGroupDropdown }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = view.selectedTaskGroup,
                                fontSize = 16.sp,
                                color = TextDark,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        DropdownMenu(
                            expanded = view.showTaskGroupDropdown,
                            onDismissRequest = { view.showTaskGroupDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            view.taskGroups.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group) },
                                    onClick = {
                                        presenter.onTaskGroupSelected(group)
                                        view.showTaskGroupDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Project Name
            item {
                FormField(label = "Project Name") {
                    OutlinedTextField(
                        value = view.projectName,
                        onValueChange = {
                            presenter.onProjectNameChanged(it)
                        },
                        placeholder = {
                            Text(
                                "Grocery Shopping App",
                                color = TextMuted
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            }

            // Description
            item {
                FormField(label = "Description") {
                    OutlinedTextField(
                        value = view.description,
                        onValueChange = { presenter.onDescriptionChanged(it) },
                        placeholder = {
                            Text(
                                "This application is designed for super-shops…",
                                color = TextMuted
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 4
                    )
                }
            }

            // Date Pickers
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        FormField(label = "Start Date") {
                            Button(
                                onClick = { view.showStartDatePicker = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = TextDark
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = view.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        FormField(label = "End Date") {
                            Button(
                                onClick = { view.showEndDatePicker = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = TextDark
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = view.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Logo Upload
            item {
                FormField(label = "Logo") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = PrimaryBrand.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "GS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBrand
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                            ) {
                                Text(
                                    text = "Grocery shop",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDark
                                )
                            }
                        }
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Change Logo",
                                tint = NeutralGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Task Titles Management
            item {
                FormField(label = "Task Titles") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Display existing task titles
                        view.taskTitles.forEachIndexed { index, title ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "• $title",
                                    fontSize = 14.sp,
                                    color = TextDark,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        presenter.onRemoveTaskTitle(index)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Remove task",
                                        tint = Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        
                        // Add new task title section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = view.newTaskTitle,
                                onValueChange = { view.newTaskTitle = it },
                                placeholder = {
                                    Text(
                                        "Enter task title...",
                                        color = TextMuted,
                                        fontSize = 14.sp
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    val trimmedTitle = view.newTaskTitle.trim()
                                    if (trimmedTitle.isNotEmpty() && !view.taskTitles.contains(trimmedTitle)) {
                                        presenter.onAddTaskTitle(trimmedTitle)
                                        view.newTaskTitle = ""
                                    }
                                },
                                enabled = view.newTaskTitle.trim().isNotEmpty(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryBrand
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text(
                                    text = "Add",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                        
                        if (view.taskTitles.isEmpty()) {
                            Text(
                                text = "Add at least one task title",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Error Message
            view.errorMessage?.let { error ->
                item {
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }

        // Add Project Button
        Button(
            onClick = {
                presenter.onSaveProject()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBrand
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = !view.isLoading && view.projectName.trim().isNotEmpty() && view.taskTitles.isNotEmpty()
        ) {
            Text(
                text = if (view.isLoading) "Adding..." else "Add Project",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }

    // Start Date Picker Dialog
    if (view.showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = view.startDate.toEpochDay() * 24 * 60 * 60 * 1000
        )
        DatePickerDialog(
            onDismissRequest = { view.showStartDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                        presenter.onStartDateSelected(selectedDate)
                    }
                    view.showStartDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { view.showStartDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // End Date Picker Dialog
    if (view.showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = view.endDate.toEpochDay() * 24 * 60 * 60 * 1000
        )
        DatePickerDialog(
            onDismissRequest = { view.showEndDatePicker = false },
            confirmButton = {
                androidx.compose.material3.Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                        presenter.onEndDateSelected(selectedDate)
                    }
                    view.showEndDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { view.showEndDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class AddProjectViewImpl(
    private val presenter: AddProjectPresenter,
    private val onBackClick: () -> Unit
) : AddProjectView {

    var taskGroups by mutableStateOf(listOf("Work", "Personal", "Shopping", "Health"))
    var selectedTaskGroup by mutableStateOf("Work")
    var projectName by mutableStateOf("")
    var description by mutableStateOf("")
    var startDate by mutableStateOf(LocalDate.now())
    var endDate by mutableStateOf(LocalDate.now().plusMonths(1))
    var taskTitles by mutableStateOf(listOf("Initial Planning", "Setup Development Environment"))
    var newTaskTitle by mutableStateOf("")
    var showTaskGroupDropdown by mutableStateOf(false)
    var showStartDatePicker by mutableStateOf(false)
    var showEndDatePicker by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    override fun showTaskGroups(groups: List<String>) {
        taskGroups = groups
    }

    override fun showSelectedTaskGroup(group: String) {
        selectedTaskGroup = group
    }

    override fun showProjectName(name: String) {
        projectName = name
    }

    override fun showDescription(description: String) {
        this.description = description
    }

    override fun showStartDate(date: LocalDate) {
        startDate = date
    }

    override fun showEndDate(date: LocalDate) {
        endDate = date
    }

    override fun showTaskTitles(titles: List<String>) {
        taskTitles = titles
    }

    override fun showLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }

    override fun showError(message: String?) {
        errorMessage = message
    }

    override fun navigateBack() {
        onBackClick()
    }
}

@Composable
fun FormField(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )
        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AddProjectScreenPreview() {
    val todayTasksPresenter = TodayTasksPresenterImpl()
    val presenter = AddProjectPresenterImpl(todayTasksPresenter)
    AddProjectScreen(
        presenter = presenter,
    )
}
