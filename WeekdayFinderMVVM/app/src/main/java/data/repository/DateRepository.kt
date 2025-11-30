package data.repository

import domain.model.DateResult
import domain.model.ErrorType
import utils.DateCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DateRepository {

    fun calculateWeekday(day: String, month: String): Flow<DateResult> = flow {
        // Эмитим пустой результат для сброса предыдущих состояний
        emit(DateResult.Empty)

        // Валидация пустых значений
        if (day.isBlank() || month.isBlank()) {
            emit(DateResult.Empty)
            return@flow
        }

        val dayInt = day.toIntOrNull()
        val monthInt = month.toIntOrNull()

        when {
            dayInt == null -> emit(DateResult.Error(ErrorType.INVALID_DAY))
            monthInt == null -> emit(DateResult.Error(ErrorType.INVALID_MONTH))
            else -> {
                val result = DateCalculator.calculateWeekday(dayInt, monthInt)
                if (result.isSuccess) {
                    emit(DateResult.Success(result.getOrNull() ?: ""))
                } else {
                    val errorMessage = result.exceptionOrNull()?.message
                    val errorType = when (errorMessage) {
                        "INVALID_DAY" -> ErrorType.INVALID_DAY
                        "INVALID_MONTH" -> ErrorType.INVALID_MONTH
                        "INVALID_DATE" -> ErrorType.INVALID_DATE
                        "OVERFLOW" -> ErrorType.OVERFLOW
                        else -> ErrorType.INVALID_DATE
                    }
                    emit(DateResult.Error(errorType))
                }
            }
        }
    }
}