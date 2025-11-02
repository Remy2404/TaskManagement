package com.myphka.taskmanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myphka.taskmanagement.ui.theme.NeutralGray
import com.myphka.taskmanagement.ui.theme.PrimaryBrand

@Composable
fun BottomNavigationBar(
    onNavigateToTasks: () -> Unit = {},
    onNavigateToCalendar: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    activeTab: Int = 0
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 16.dp)
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateToTasks,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Tasks",
                    tint = if (activeTab == 0) PrimaryBrand else NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = onNavigateToCalendar,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Calendar",
                    tint = if (activeTab == 1) PrimaryBrand else NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = onNavigateToProjects,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Folder,
                    contentDescription = "Projects",
                    tint = if (activeTab == 2) PrimaryBrand else NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = onNavigateToProfile,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = if (activeTab == 3) PrimaryBrand else NeutralGray,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
@Preview
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar()
}
