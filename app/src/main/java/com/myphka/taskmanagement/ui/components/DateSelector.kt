package com.myphka.taskmanagement.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.myphka.taskmanagement.ui.theme.TextDark
import com.myphka.taskmanagement.ui.theme.TextMuted
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dates = (0..4).map { LocalDate.now().plusDays(it.toLong()) }

    LazyRow(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .background(
                color = Color.LightGray.copy(alpha = 0.2f), shape = CircleShape
            ).fillMaxWidth()
    ) {
        items(dates) { date ->
            DateItem(
                date = date,
                isSelected = date == selectedDate,
                onSelect = { onDateSelected(date) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateItem(
    date: LocalDate,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
    val dayOfMonth = date.dayOfMonth

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 10.dp)
            .clickable { onSelect() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isSelected) PrimaryBrand else Color.Transparent,
                    shape = RoundedCornerShape(360.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$dayOfMonth",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else TextDark
            )
        }
        Text(
            text = dayOfWeek,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) PrimaryBrand else TextMuted
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DateSelectorPreview() {
    DateSelector(
        selectedDate = LocalDate.now(),
        onDateSelected = {}
    )
}

