package com.myphka.taskmanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myphka.taskmanagement.ui.theme.PrimaryBrand
import com.myphka.taskmanagement.ui.theme.TextMuted

@Composable
fun FilterTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = Modifier
            .background(
                color = if (isSelected) PrimaryBrand.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) PrimaryBrand else TextMuted
        )
    }
}
@Preview
@Composable
fun FilterTabPreview() {
    FilterTab(
        label = "Today",
        isSelected = true,
        onClick = {},
        modifier = Modifier
    )
}