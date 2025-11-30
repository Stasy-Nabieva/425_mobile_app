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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weekdayfinder.R
import domain.model.DateResult
import domain.model.ErrorType
import ui.viewmodel.DateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekdayFinderScreen(
    viewModel: DateViewModel = viewModel()
) {
    val day by viewModel.day.collectAsStateWithLifecycle()
    val month by viewModel.month.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()
    val isCalculating by viewModel.isCalculating.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
            onValueChange = viewModel::setDay,
            label = { Text(stringResource(R.string.enter_day)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = result is DateResult.Error && (result as DateResult.Error).errorType == ErrorType.INVALID_DAY
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода месяца
        OutlinedTextField(
            value = month,
            onValueChange = viewModel::setMonth,
            label = { Text(stringResource(R.string.enter_month)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = result is DateResult.Error && (result as DateResult.Error).errorType == ErrorType.INVALID_MONTH
        )

        Spacer(modifier = Modifier.height(24.dp)) // Исправлено: было Mododer

        // Индикатор загрузки
        if (isCalculating) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Отображение результата или ошибки
        when (result) {
            is DateResult.Empty -> {
                // Ничего не показываем при пустом результате
            }
            is DateResult.Success -> {
                val weekday = (result as DateResult.Success).weekday
                val weekdayResource = getWeekdayResource(weekday)

                Text(
                    text = stringResource(R.string.result, stringResource(weekdayResource)),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is DateResult.Error -> {
                val errorType = (result as DateResult.Error).errorType
                val errorText = getErrorText(errorType)

                Text(
                    text = errorText,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun getErrorText(errorType: ErrorType): String {
    return when (errorType) {
        ErrorType.INVALID_DAY -> stringResource(R.string.error_invalid_day)
        ErrorType.INVALID_MONTH -> stringResource(R.string.error_invalid_month)
        ErrorType.INVALID_DATE -> stringResource(R.string.error_invalid_date)
        ErrorType.OVERFLOW -> stringResource(R.string.error_overflow)
    }
}

private fun getWeekdayResource(weekday: String): Int {
    return when (weekday) {
        "monday" -> R.string.monday
        "tuesday" -> R.string.tuesday
        "wednesday" -> R.string.wednesday
        "thursday" -> R.string.thursday
        "friday" -> R.string.friday
        "saturday" -> R.string.saturday
        "sunday" -> R.string.sunday
        else -> R.string.monday
    }
}

@Preview(showBackground = true)
@Composable
fun WeekdayFinderScreenPreview() {
    WeekdayFinderScreen()
}