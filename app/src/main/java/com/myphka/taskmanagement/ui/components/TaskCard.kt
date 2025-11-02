package com.myphka.taskmanagement.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.myphka.taskmanagement.R
import com.myphka.taskmanagement.model.Task
import com.myphka.taskmanagement.ui.theme.TextDark
import com.myphka.taskmanagement.ui.theme.TextMuted

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCard(
    task: Task,
    projectName: String = "",
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.subtitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextMuted
                    )
                    if (projectName.isNotEmpty()) {
                        Text(
                            text = "Project: $projectName",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextMuted
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.1f))
                StatusChip(task.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "time icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(end = 4.dp),
                )
                Text(
                    text = task.scheduledTime?.format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")) ?: "No time",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextMuted
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreTaskCard() {
    TaskCard(
        task = Task(
            id = "1",
            title = "Design Meeting",
            subtitle = "Discuss UI/UX designs",
            status = com.myphka.taskmanagement.model.TaskStatus.IN_PROGRESS,
            projectId = "project-1",
            date = java.time.LocalDate.now(),
            scheduledTime = java.time.LocalTime.now()
        ),
        projectName = "Sample Project"
    )
}