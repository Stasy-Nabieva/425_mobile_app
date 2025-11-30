package utils

import java.util.Calendar

object DateCalculator {

    fun calculateWeekday(day: Int, month: Int): Result<String> {
        return try {
            // Валидация ввода
            if (month !in 1..12) {
                return Result.failure(IllegalArgumentException("INVALID_MONTH"))
            }
            if (day !in 1..31) {
                return Result.failure(IllegalArgumentException("INVALID_DAY"))
            }

            // Получаем текущий год (2025)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            // Проверка валидности даты для месяца с учетом года
            if (!isValidDate(day, month, currentYear)) {
                return Result.failure(IllegalArgumentException("INVALID_DATE"))
            }

            // Алгоритм Зеллера для вычисления дня недели
            val weekday = zellerAlgorithm(day, month, currentYear)

            Result.success(weekday)

        } catch (e: ArithmeticException) {
            Result.failure(ArithmeticException("OVERFLOW"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun zellerAlgorithm(day: Int, month: Int, year: Int): String {
        var m = month
        var y = year

        if (m < 3) {
            m += 12
            y -= 1
        }

        val k = y % 100
        val j = y / 100

        val h = try {
            (day + (13 * (m + 1)) / 5 + k + k / 4 + j / 4 + 5 * j) % 7
        } catch (e: ArithmeticException) {
            throw ArithmeticException("OVERFLOW")
        }

        return when (h) {
            0 -> "saturday"
            1 -> "sunday"
            2 -> "monday"
            3 -> "tuesday"
            4 -> "wednesday"
            5 -> "thursday"
            6 -> "friday"
            else -> throw IllegalArgumentException("Invalid weekday calculation")
        }
    }

    private fun isValidDate(day: Int, month: Int, year: Int): Boolean {
        val daysInMonth = when (month) {
            2 -> if (isLeapYear(year)) 29 else 28  // Используем переданный год
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day in 1..daysInMonth
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}