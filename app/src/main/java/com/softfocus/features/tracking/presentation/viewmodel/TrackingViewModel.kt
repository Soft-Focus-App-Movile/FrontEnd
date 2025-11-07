package com.softfocus.features.tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.common.result.Result
import com.softfocus.features.tracking.domain.usecase.*
import com.softfocus.features.tracking.presentation.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val createCheckInUseCase: CreateCheckInUseCase,
    private val getCheckInsUseCase: GetCheckInsUseCase,
    private val getTodayCheckInUseCase: GetTodayCheckInUseCase,
    private val createEmotionalCalendarEntryUseCase: CreateEmotionalCalendarEntryUseCase,
    private val getEmotionalCalendarUseCase: GetEmotionalCalendarUseCase,
    private val getEmotionalCalendarByDateUseCase: GetEmotionalCalendarByDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrackingUiState>(TrackingUiState.Initial)
    val uiState: StateFlow<TrackingUiState> = _uiState.asStateFlow()

    private val _checkInFormState = MutableStateFlow<CheckInFormState>(CheckInFormState.Idle)
    val checkInFormState: StateFlow<CheckInFormState> = _checkInFormState.asStateFlow()

    private val _emotionalCalendarFormState = MutableStateFlow<EmotionalCalendarFormState>(EmotionalCalendarFormState.Idle)
    val emotionalCalendarFormState: StateFlow<EmotionalCalendarFormState> = _emotionalCalendarFormState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = TrackingUiState.Loading

            val todayCheckIn = getTodayCheckInUseCase()
            val emotionalCalendar = getEmotionalCalendarUseCase()

            if (todayCheckIn is Result.Success && emotionalCalendar is Result.Success) {
                _uiState.value = TrackingUiState.Success(
                    TrackingData(
                        todayCheckIn = todayCheckIn.data,
                        emotionalCalendar = emotionalCalendar.data
                    )
                )
            } else {
                _uiState.value = TrackingUiState.Error("Error loading data")
            }
        }
    }

    fun createCheckIn(
        emotionalLevel: Int,
        energyLevel: Int,
        moodDescription: String,
        sleepHours: Int,
        symptoms: List<String>,
        notes: String?
    ) {
        viewModelScope.launch {
            _checkInFormState.value = CheckInFormState.Loading

            when (val result = createCheckInUseCase(
                emotionalLevel = emotionalLevel,
                energyLevel = energyLevel,
                moodDescription = moodDescription,
                sleepHours = sleepHours,
                symptoms = symptoms,
                notes = notes
            )) {
                is Result.Success -> {
                    _checkInFormState.value = CheckInFormState.Success
                    loadTodayCheckIn()
                }
                is Result.Error -> {
                    _checkInFormState.value = CheckInFormState.Error(result.message)
                }
            }
        }
    }

    fun createEmotionalCalendarEntry(
        date: String,
        emotionalEmoji: String,
        moodLevel: Int,
        emotionalTags: List<String>
    ) {
        viewModelScope.launch {
            _emotionalCalendarFormState.value = EmotionalCalendarFormState.Loading

            when (val result = createEmotionalCalendarEntryUseCase(
                date = date,
                emotionalEmoji = emotionalEmoji,
                moodLevel = moodLevel,
                emotionalTags = emotionalTags
            )) {
                is Result.Success -> {
                    _emotionalCalendarFormState.value = EmotionalCalendarFormState.Success
                    loadEmotionalCalendar()
                }
                is Result.Error -> {
                    _emotionalCalendarFormState.value = EmotionalCalendarFormState.Error(result.message)
                }
            }
        }
    }

    fun loadTodayCheckIn() {
        viewModelScope.launch {
            when (val result = getTodayCheckInUseCase()) {
                is Result.Success -> {
                    _uiState.update { state ->
                        if (state is TrackingUiState.Success) {
                            state.copy(
                                data = state.data.copy(todayCheckIn = result.data)
                            )
                        } else {
                            TrackingUiState.Success(TrackingData(todayCheckIn = result.data))
                        }
                    }
                }
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun loadEmotionalCalendar(startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            when (val result = getEmotionalCalendarUseCase(startDate, endDate)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        if (state is TrackingUiState.Success) {
                            state.copy(
                                data = state.data.copy(emotionalCalendar = result.data)
                            )
                        } else {
                            TrackingUiState.Success(TrackingData(emotionalCalendar = result.data))
                        }
                    }
                }
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun loadCheckInHistory(
        startDate: String? = null,
        endDate: String? = null,
        pageNumber: Int? = null,
        pageSize: Int? = null
    ) {
        viewModelScope.launch {
            when (val result = getCheckInsUseCase(startDate, endDate, pageNumber, pageSize)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        if (state is TrackingUiState.Success) {
                            state.copy(
                                data = state.data.copy(checkInHistory = result.data)
                            )
                        } else {
                            TrackingUiState.Success(TrackingData(checkInHistory = result.data))
                        }
                    }
                }
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun resetCheckInFormState() {
        _checkInFormState.value = CheckInFormState.Idle
    }

    fun resetEmotionalCalendarFormState() {
        _emotionalCalendarFormState.value = EmotionalCalendarFormState.Idle
    }
}