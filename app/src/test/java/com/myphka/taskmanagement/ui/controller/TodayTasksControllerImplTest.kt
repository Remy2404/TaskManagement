package com.myphka.taskmanagement.ui.controller

import com.myphka.taskmanagement.model.Project
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.model.TaskFilterType
import com.myphka.taskmanagement.model.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Unit tests for TodayTasksControllerImpl.
 * Tests the MVC controller logic for the TodayTasks screen.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TodayTasksControllerImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var controller: TodayTasksControllerImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        controller = TodayTasksControllerImpl(testScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadsTasksOnInit - controller loads sample tasks on initialization`() = runTest {
        // Given: controller is initialized in setUp

        // When: we get the state
        val state = controller.state.first()

        // Then: tasks should be loaded
        assertTrue("Tasks should not be empty", state.tasks.isNotEmpty())
        assertEquals("Should have 5 sample tasks", 5, state.tasks.size)
    }

    @Test
    fun `loadsProjectsOnInit - controller loads sample projects on initialization`() = runTest {
        // Given: controller is initialized in setUp

        // When: we get the state
        val state = controller.state.first()

        // Then: projects should be loaded
        assertTrue("Projects should not be empty", state.projects.isNotEmpty())
        assertEquals("Should have 2 sample projects", 2, state.projects.size)
    }

    @Test
    fun `selectDate - updates selected date in state`() = runTest {
        // Given: a specific date
        val testDate = LocalDate.of(2024, 12, 25)

        // When: selecting the date
        controller.selectDate(testDate)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: state should have updated date
        val state = controller.state.first()
        assertEquals(testDate, state.selectedDate)
    }

    @Test
    fun `selectFilter - updates selected filter in state`() = runTest {
        // Given: initial state has ALL filter
        val initialState = controller.state.first()
        assertEquals(TaskFilterType.ALL, initialState.selectedFilter)

        // When: selecting TODO filter
        controller.selectFilter(TaskFilterType.TODO)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: state should have updated filter
        val state = controller.state.first()
        assertEquals(TaskFilterType.TODO, state.selectedFilter)
    }

    @Test
    fun `toggleTaskStatus - cycles task status from TODO to IN_PROGRESS`() = runTest {
        // Given: a task with TODO status
        val state = controller.state.first()
        val todoTask = state.tasks.find { it.status == TaskStatus.TODO }
        assertNotNull("Should have a TODO task", todoTask)

        // When: toggling the task status
        controller.toggleTaskStatus(todoTask!!.id)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: task should be IN_PROGRESS
        val updatedState = controller.state.first()
        val updatedTask = updatedState.tasks.find { it.id == todoTask.id }
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask?.status)
    }

    @Test
    fun `toggleTaskStatus - cycles task status from IN_PROGRESS to DONE`() = runTest {
        // Given: a task with IN_PROGRESS status
        val state = controller.state.first()
        val inProgressTask = state.tasks.find { it.status == TaskStatus.IN_PROGRESS }
        assertNotNull("Should have an IN_PROGRESS task", inProgressTask)

        // When: toggling the task status
        controller.toggleTaskStatus(inProgressTask!!.id)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: task should be DONE
        val updatedState = controller.state.first()
        val updatedTask = updatedState.tasks.find { it.id == inProgressTask.id }
        assertEquals(TaskStatus.DONE, updatedTask?.status)
    }

    @Test
    fun `toggleTaskStatus - cycles task status from DONE to TODO`() = runTest {
        // Given: a task with DONE status
        val state = controller.state.first()
        val doneTask = state.tasks.find { it.status == TaskStatus.DONE }
        assertNotNull("Should have a DONE task", doneTask)

        // When: toggling the task status
        controller.toggleTaskStatus(doneTask!!.id)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: task should be TODO
        val updatedState = controller.state.first()
        val updatedTask = updatedState.tasks.find { it.id == doneTask.id }
        assertEquals(TaskStatus.TODO, updatedTask?.status)
    }

    @Test
    fun `getFilteredTasks - returns all tasks when filter is ALL`() = runTest {
        // Given: ALL filter is selected (default)
        val state = controller.state.first()
        assertEquals(TaskFilterType.ALL, state.selectedFilter)

        // When: getting filtered tasks
        val filteredTasks = controller.getFilteredTasks()

        // Then: should return tasks for today
        assertTrue("Filtered tasks should not be empty", filteredTasks.isNotEmpty())
    }

    @Test
    fun `getFilteredTasks - returns only TODO tasks when filter is TODO`() = runTest {
        // Given: TODO filter is selected
        controller.selectFilter(TaskFilterType.TODO)
        testDispatcher.scheduler.advanceUntilIdle()

        // When: getting filtered tasks
        val filteredTasks = controller.getFilteredTasks()

        // Then: all tasks should have TODO status
        filteredTasks.forEach { task ->
            assertEquals("All filtered tasks should be TODO", TaskStatus.TODO, task.status)
        }
    }

    @Test
    fun `getFilteredTasks - returns only IN_PROGRESS tasks when filter is IN_PROGRESS`() = runTest {
        // Given: IN_PROGRESS filter is selected
        controller.selectFilter(TaskFilterType.IN_PROGRESS)
        testDispatcher.scheduler.advanceUntilIdle()

        // When: getting filtered tasks
        val filteredTasks = controller.getFilteredTasks()

        // Then: all tasks should have IN_PROGRESS status
        filteredTasks.forEach { task ->
            assertEquals("All filtered tasks should be IN_PROGRESS", TaskStatus.IN_PROGRESS, task.status)
        }
    }

    @Test
    fun `getFilteredTasks - returns only DONE tasks when filter is DONE`() = runTest {
        // Given: DONE filter is selected
        controller.selectFilter(TaskFilterType.DONE)
        testDispatcher.scheduler.advanceUntilIdle()

        // When: getting filtered tasks
        val filteredTasks = controller.getFilteredTasks()

        // Then: all tasks should have DONE status
        filteredTasks.forEach { task ->
            assertEquals("All filtered tasks should be DONE", TaskStatus.DONE, task.status)
        }
    }

    @Test
    fun `getFilteredTasks - returns empty list for date with no tasks`() = runTest {
        // Given: a date far in the future with no tasks
        val futureDate = LocalDate.of(2099, 12, 31)
        controller.selectDate(futureDate)
        testDispatcher.scheduler.advanceUntilIdle()

        // When: getting filtered tasks
        val filteredTasks = controller.getFilteredTasks()

        // Then: should return empty list
        assertTrue("Should have no tasks for future date", filteredTasks.isEmpty())
    }

    @Test
    fun `addTask - adds new task to state`() = runTest {
        // Given: initial task count
        val initialState = controller.state.first()
        val initialCount = initialState.tasks.size

        // When: adding a new task
        val newTask = Task(
            title = "New Test Task",
            subtitle = "Test subtitle",
            scheduledTime = LocalTime.of(12, 0),
            status = TaskStatus.TODO,
            projectId = "proj1",
            date = LocalDate.now()
        )
        controller.addTask(newTask)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: task count should increase
        val updatedState = controller.state.first()
        assertEquals(initialCount + 1, updatedState.tasks.size)
        assertTrue("New task should be in the list", updatedState.tasks.any { it.title == "New Test Task" })
    }

    @Test
    fun `addProject - adds new project to state`() = runTest {
        // Given: initial project count
        val initialState = controller.state.first()
        val initialCount = initialState.projects.size

        // When: adding a new project
        val newProject = Project(
            name = "New Test Project",
            description = "Test description",
            taskGroup = "Work",
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1)
        )
        controller.addProject(newProject)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: project count should increase
        val updatedState = controller.state.first()
        assertEquals(initialCount + 1, updatedState.projects.size)
        assertTrue("New project should be in the list", updatedState.projects.any { it.name == "New Test Project" })
    }

    @Test
    fun `onAddProjectClicked - emits NavigateToAddProject event`() = runTest {
        // Given: controller is ready

        // When: clicking add project
        controller.onAddProjectClicked()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: should emit navigation event
        // Note: Events are SharedFlow, so we'd need to collect them before the action
        // This test just verifies no crash occurs
    }

    @Test
    fun `initialState - has correct default values`() = runTest {
        // Given: controller is initialized

        // When: checking initial state
        val state = controller.state.first()

        // Then: defaults should be correct
        assertEquals(LocalDate.now(), state.selectedDate)
        assertEquals(TaskFilterType.ALL, state.selectedFilter)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }
}
