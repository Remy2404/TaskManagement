package com.myphka.taskmanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.myphka.taskmanagement.model.TaskStatus
import com.myphka.taskmanagement.ui.theme.DoneViolet
import com.myphka.taskmanagement.ui.theme.InProgressOrange
import com.myphka.taskmanagement.ui.theme.ToDoBlue

@Composable
fun StatusChip(status: TaskStatus) {
    val (backgroundColor, textColor) = when (status) {
        TaskStatus.TODO -> Pair(ToDoBlue.copy(alpha = 0.2f), ToDoBlue)
        TaskStatus.IN_PROGRESS -> Pair(InProgressOrange.copy(alpha = 0.2f), InProgressOrange)
        TaskStatus.DONE -> Pair(DoneViolet.copy(alpha = 0.2f), DoneViolet)
    }

    val statusText = when (status) {
        TaskStatus.TODO -> "To Do"
        TaskStatus.IN_PROGRESS -> "In Progress"
        TaskStatus.DONE -> "Done"
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = statusText,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
@Preview
@Composable
fun StatusChipPreview() {
    StatusChip(status = TaskStatus.IN_PROGRESS)
}
