package domain.model

sealed class DateResult {
    data object Empty : DateResult()
    data class Success(val weekday: String) : DateResult()
    data class Error(val errorType: ErrorType) : DateResult()
}

enum class ErrorType {
    INVALID_DAY, INVALID_MONTH, INVALID_DATE, OVERFLOW
}