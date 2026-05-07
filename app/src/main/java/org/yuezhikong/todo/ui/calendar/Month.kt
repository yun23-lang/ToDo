package org.yuezhikong.todo.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Year

@Composable
fun Month(
    today: LocalDate,
    selected: Int = 0,
    onValueChange: (Int) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val year = today.year
        val month = today.month
        val firstDay = LocalDate.of(year, month, 1).dayOfWeek.value
        val days = month.length(Year.of(year).isLeap)
        Week(1, 7 - firstDay + 1, firstDay, selected, onValueChange)
        var day = 8 - firstDay
        while (day < days) {
            Week(day + 1, minOf(day + 7, days), 1, selected, onValueChange)
            day += 7
        }
    }
}

@Composable
fun Week(start: Int, end: Int, weekday: Int, selected: Int, onValueChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        repeat(weekday - 1) {
            Day(0, selected, Modifier.weight(1f), onValueChange)
        }
        for (day in start..end) {
            Day(day, selected, Modifier.weight(1f), onValueChange)
        }
        val count = (weekday - 1) + (end - start + 1)
        repeat(7 - count) {
            Day(0, selected, Modifier.weight(1f), onValueChange)
        }
    }
}

/**
 * 处理跨月的情况
 * 例如：当月第一天是周五，那么第一行就会显示周五、周六、周日，第二行从周一开始显示1号，直到7号；当月最后一天是周二，那么最后一行就会显示28号、29号、30号，下一行从周三开始显示1号，直到周日。
 * @param start 当前周的第一天
 * @param end 当前周的最后一天
 * @param selected 当前选中的日期
 * @param onValueChange 日期变化时的回调函数
 */
@Composable
fun Week(start: Int, end: Int, selected: Int, onValueChange: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        if (start < end){
            for (day in start..end) {
                Day(day, selected, Modifier.weight(1f), onValueChange)
            }
        } else{
            var e = end
            var i = 0
            while (e >= 1) {
                e--
                i++
            }
            var n = 7 - i
            var x = n
            var s = start
            while (x != 0) {
                Day(s, selected, Modifier.weight(1f), onValueChange)
                x--
                s++
            }
            for (day in 1..end) {
                Day(day, selected, Modifier.weight(1f), onValueChange)
            }
        }
    }
}

@Composable
fun Day(day: Int, selected: Int, modifier: Modifier = Modifier, onValueChange: (Int) -> Unit) {
    val isSelected = day == selected
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(CircleShape)
            .background(
                if (day == 0) Color.Transparent
                else if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable(
                enabled = day != 0,
                onClick = {
                    if (day != 0) {
                        onValueChange(day)
                    }
                },
            )
    ) {
        if (day != 0) {
            Text(
                text = day.toString(),
                modifier = Modifier.align(Alignment.Center),
                color = if (!isSelected){ MaterialTheme.colorScheme.onBackground } else { MaterialTheme.colorScheme.onPrimary }
            )
        }
    }
}

@Composable
fun Day() {
    Row(modifier = Modifier.fillMaxWidth()) {
        repeat(7) {index ->
            val text = when (index) {
                0 -> "一"
                1 -> "二"
                2 -> "三"
                3 -> "四"
                4 -> "五"
                5 -> "六"
                6 -> "日"
                else -> ""
            }
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(1.dp)
                    .weight(1f)
            ) {
                Text(
                    text = text,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}