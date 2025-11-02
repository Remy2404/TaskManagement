package com.myphka.taskmanagement.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeExtensions {
    fun LocalTime.formatTime(): String {
        return this.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    fun LocalDate.formatDate(): String {
        return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    fun LocalDate.formatShortDate(): String {
        return this.format(DateTimeFormatter.ofPattern("dd MMM"))
    }

    fun LocalDate.getDayOfWeekShort(): String {
        return this.dayOfWeek.toString().substring(0, 3)
    }
}
