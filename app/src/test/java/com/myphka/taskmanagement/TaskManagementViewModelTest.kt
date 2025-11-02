package com.myphka.taskmanagement

import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskStatus
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.viewmodel.TaskManagementViewModel
import java.time.LocalDate
import java.time.LocalTime

class TaskManagementViewModelTest {

    private lateinit var viewModel: TaskManagementViewModel

    @Before
    fun setup() {
        viewModel = TaskManagementViewModel()
    }

    @Test
    fun testSelectDate() {
        val testDate = LocalDate.now().plusDays(2)
        viewModel.selectDate(testDate)
        
        val state = viewModel.uiState.value
        assertEquals(testDate, state.selectedDate)
    }

    @Test
    fun testSelectFilter() {
        viewModel.selectFilter(TaskFilterType.TODO)
        
        val state = viewModel.uiState.value
        assertEquals(TaskFilterType.TODO, state.selectedFilter)
    }

    @Test
    fun testAddTask() {
        val initialSize = viewModel.uiState.value.tasks.size
        val newTask = Task(
            title = "Test Task",
            subtitle = "Test Subtitle",
            scheduledTime = LocalTime.of(10, 0),
            status = TaskStatus.TODO,
            projectId = "proj1",
            date = LocalDate.now()
        )
        
        viewModel.addTask(newTask)
        
        val state = viewModel.uiState.value
        assertEquals(initialSize + 1, state.tasks.size)
        assertTrue(state.tasks.contains(newTask))
    }

    @Test
    fun testUpdateTaskStatus() {
        val tasks = viewModel.uiState.value.tasks
        if (tasks.isNotEmpty()) {
            val taskId = tasks[0].id
            viewModel.updateTaskStatus(taskId, TaskStatus.DONE)
            
            val updatedTask = viewModel.uiState.value.tasks.find { it.id == taskId }
            assertEquals(TaskStatus.DONE, updatedTask?.status)
        }
    }

    @Test
    fun testGetFilteredTasks() {
        viewModel.selectFilter(TaskFilterType.TODO)
        val todoTasks = viewModel.getFilteredTasks()
        
        assertTrue(todoTasks.all { it.status == TaskStatus.TODO })
        assertTrue(todoTasks.all { it.date == LocalDate.now() })
    }

    @Test
    fun testFilterAllTasks() {
        viewModel.selectFilter(TaskFilterType.ALL)
        val allTasks = viewModel.getFilteredTasks()
        
        val expectedCount = viewModel.uiState.value.tasks
            .count { it.date == LocalDate.now() }
        assertEquals(expectedCount, allTasks.size)
    }
}
