package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.DateRepository
import domain.model.DateResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DateViewModel : ViewModel() {

    private val repository = DateRepository()

    // State для UI
    private val _day = MutableStateFlow("")
    private val _month = MutableStateFlow("")
    private val _result = MutableStateFlow<DateResult>(DateResult.Empty)
    private val _isCalculating = MutableStateFlow(false)

    val day: StateFlow<String> = _day
    val month: StateFlow<String> = _month
    val result: StateFlow<DateResult> = _result
    val isCalculating: StateFlow<Boolean> = _isCalculating

    init {
        setupCalculationFlow()
    }

    fun setDay(value: String) {
        _day.value = value
    }

    fun setMonth(value: String) {
        _month.value = value
    }

    private fun setupCalculationFlow() {
        viewModelScope.launch {
            kotlinx.coroutines.flow.combine(_day, _month) { day, month ->
                day to month
            }
                .debounce(300) // Задержка для избежания лишних вычислений
                .collect { (day, month) ->
                    _isCalculating.value = true
                    repository.calculateWeekday(day, month)
                        .onEach { result ->
                            _result.value = result
                            _isCalculating.value = false
                        }
                        .launchIn(viewModelScope)
                }
        }
    }
}