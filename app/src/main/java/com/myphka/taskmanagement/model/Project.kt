package com.myphka.taskmanagement.model

import java.time.LocalDate

data class Project(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val taskGroup: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val logoUrl: String? = null
)
