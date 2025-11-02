package com.myphka.taskmanagement.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksByDate(date: java.time.LocalDate): Flow<List<Task>>
    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
}

class TaskRepositoryImpl : TaskRepository {
    private val tasks = mutableListOf<Task>()

    override fun getAllTasks(): Flow<List<Task>> = flowOf(tasks)

    override fun getTasksByDate(date: java.time.LocalDate): Flow<List<Task>> {
        return flowOf(tasks.filter { it.date == date })
    }

    override fun getTasksByStatus(status: TaskStatus): Flow<List<Task>> {
        return flowOf(tasks.filter { it.status == status })
    }

    override suspend fun addTask(task: Task) {
        tasks.add(task)
    }

    override suspend fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    override suspend fun deleteTask(taskId: String) {
        tasks.removeAll { it.id == taskId }
    }
}
