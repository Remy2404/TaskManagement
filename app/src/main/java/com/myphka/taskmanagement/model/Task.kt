package com.myphka.taskmanagement.model

import java.time.LocalDate
import java.time.LocalTime

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val subtitle: String,
    val scheduledTime: LocalTime?,
    val status: TaskStatus,
    val projectId: String,
    val date: LocalDate
)
