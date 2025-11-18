package com.example.weekdayfinder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weekdayfinder.R
import com.example.weekdayfinder.utils.DateCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekdayFinderScreen() {
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), //отступ
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Заголовок
        Text(
            text = stringResource(R.string.find_weekday),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Поле ввода дня
        OutlinedTextField(
            value = day,
            onValueChange = {
                day = it
                error = "" // Сбрасываем ошибку при изменении
            },
            label = { Text(stringResource(R.string.enter_day)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = error.contains("day", ignoreCase = true)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода месяца
        OutlinedTextField(
            value = month,
            onValueChange = {
                month = it
                error = "" // Сбрасываем ошибку при изменении
            },
            label = { Text(stringResource(R.string.enter_month)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = error.contains("month", ignoreCase = true)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка вычисления
        Button(
            onClick = {
                if (day.isBlank() || month.isBlank()) {
                    error = "Please enter both day and month"
                    return@Button
                }

                val dayInt = day.toIntOrNull()
                val monthInt = month.toIntOrNull()

                when {
                    dayInt == null -> error = "INVALID_DAY"
                    monthInt == null -> error = "INVALID_MONTH"
                    else -> {
                        val calculationResult = DateCalculator.calculateWeekday(dayInt, monthInt)
                        if (calculationResult.isSuccess) {
                            result = calculationResult.getOrNull() ?: ""
                            error = ""
                        } else {
                            error = calculationResult.exceptionOrNull()?.message ?: "UNKNOWN_ERROR"
                            result = ""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.find_weekday))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Отображение результата или ошибки
        when {
            error.isNotBlank() -> {
                val errorText = when (error) {
                    "INVALID_DAY" -> stringResource(R.string.error_invalid_day)
                    "INVALID_MONTH" -> stringResource(R.string.error_invalid_month)
                    "INVALID_DATE" -> stringResource(R.string.error_invalid_date)
                    "OVERFLOW" -> stringResource(R.string.error_overflow)
                    else -> error
                }

                Text(
                    text = errorText,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            result.isNotBlank() -> {
                val weekdayResource = when (result) {
                    "monday" -> R.string.monday
                    "tuesday" -> R.string.tuesday
                    "wednesday" -> R.string.wednesday
                    "thursday" -> R.string.thursday
                    "friday" -> R.string.friday
                    "saturday" -> R.string.saturday
                    "sunday" -> R.string.sunday
                    else -> R.string.monday
                }

                Text(
                    text = stringResource(R.string.result, stringResource(weekdayResource)),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeekdayFinderScreenPreview() {
    WeekdayFinderScreen()
}