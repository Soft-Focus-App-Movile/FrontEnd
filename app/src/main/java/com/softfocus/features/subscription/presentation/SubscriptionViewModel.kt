package com.softfocus.features.subscription.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.core.data.local.LocalUserDataSource
import com.softfocus.core.utils.SessionManager
import com.softfocus.features.auth.domain.models.UserType
import com.softfocus.features.subscription.domain.models.Subscription
import com.softfocus.features.subscription.domain.models.SubscriptionPlan
import com.softfocus.features.subscription.domain.models.UsageStats
import com.softfocus.features.subscription.domain.usecases.CancelSubscriptionUseCase
import com.softfocus.features.subscription.domain.usecases.CreateCheckoutSessionUseCase
import com.softfocus.features.subscription.domain.usecases.GetMySubscriptionUseCase
import com.softfocus.features.subscription.domain.usecases.GetUsageStatsUseCase
import com.softfocus.features.subscription.presentation.di.SubscriptionPresentationModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {

    private val getMySubscriptionUseCase: GetMySubscriptionUseCase =
        SubscriptionPresentationModule.getGetMySubscriptionUseCase(application)
    private val getUsageStatsUseCase: GetUsageStatsUseCase =
        SubscriptionPresentationModule.getGetUsageStatsUseCase(application)
    private val createCheckoutSessionUseCase: CreateCheckoutSessionUseCase =
        SubscriptionPresentationModule.getCreateCheckoutSessionUseCase(application)
    private val cancelSubscriptionUseCase: CancelSubscriptionUseCase =
        SubscriptionPresentationModule.getCancelSubscriptionUseCase(application)
    private val handleCheckoutSuccessUseCase: com.softfocus.features.subscription.domain.usecases.HandleCheckoutSuccessUseCase =
        SubscriptionPresentationModule.getHandleCheckoutSuccessUseCase(application)

    private val localUserDataSource = LocalUserDataSource(application)

    private val _uiState = MutableStateFlow<SubscriptionUiState>(SubscriptionUiState.Loading)
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    private val _usageStats = MutableStateFlow<UsageStats?>(null)
    val usageStats: StateFlow<UsageStats?> = _usageStats.asStateFlow()

    private val _checkoutUrl = MutableStateFlow<String?>(null)
    val checkoutUrl: StateFlow<String?> = _checkoutUrl.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val initializeSubscriptionUseCase: com.softfocus.features.subscription.domain.usecases.InitializeSubscriptionUseCase =
        SubscriptionPresentationModule.getInitializeSubscriptionUseCase(application)

    init {
        loadSubscription()
    }

    fun loadSubscription() {
        viewModelScope.launch {
            _uiState.value = SubscriptionUiState.Loading

            val currentUser = SessionManager.getCurrentUser(getApplication())
            if (currentUser == null) {
                _uiState.value = SubscriptionUiState.Error("Usuario no autenticado")
                return@launch
            }

            val isPatient = currentUser.userType == UserType.GENERAL &&
                    localUserDataSource.hasTherapeuticRelationship()

            if (isPatient) {
                _uiState.value = SubscriptionUiState.IsPatient
                return@launch
            }

            getMySubscriptionUseCase().onSuccess { subscription ->
                _uiState.value = SubscriptionUiState.Success(subscription)
                loadUsageStats()
            }.onFailure { error ->
                android.util.Log.w("SubscriptionViewModel", "No subscription found, initializing...")
                initializeSubscriptionAndRetry()
            }
        }
    }

    private fun initializeSubscriptionAndRetry() {
        viewModelScope.launch {
            initializeSubscriptionUseCase().onSuccess { subscription ->
                _uiState.value = SubscriptionUiState.Success(subscription)
                loadUsageStats()
            }.onFailure { error ->
                _uiState.value = SubscriptionUiState.Error(error.message ?: "Error al cargar suscripción")
            }
        }
    }

    private fun loadUsageStats() {
        viewModelScope.launch {
            getUsageStatsUseCase().onSuccess { stats ->
                _usageStats.value = stats
            }.onFailure {
            }
        }
    }

    fun upgradeToPro(successUrl: String, cancelUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            createCheckoutSessionUseCase(successUrl, cancelUrl)
                .onSuccess { checkoutSession ->
                    _checkoutUrl.value = checkoutSession.checkoutUrl
                }
                .onFailure { error ->
                    val errorMsg = error.message ?: "Error al crear sesión de pago"
                    android.util.Log.e("SubscriptionViewModel", "Error upgrade: $errorMsg", error)
                    _errorMessage.value = errorMsg
                }

            _isLoading.value = false
        }
    }

    fun cancelSubscription(immediate: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            cancelSubscriptionUseCase(immediate)
                .onSuccess { updatedSubscription ->
                    _uiState.value = SubscriptionUiState.Success(updatedSubscription)
                    _errorMessage.value = "Suscripción cancelada exitosamente"
                }
                .onFailure { error ->
                    _errorMessage.value = error.message ?: "Error al cancelar suscripción"
                }

            _isLoading.value = false
        }
    }

    fun clearCheckoutUrl() {
        _checkoutUrl.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun handleCheckoutSuccess(sessionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            android.util.Log.d("SubscriptionViewModel", "Processing checkout success with sessionId: $sessionId")

            handleCheckoutSuccessUseCase(sessionId)
                .onSuccess { updatedSubscription ->
                    android.util.Log.d("SubscriptionViewModel", "Payment processed successfully. New plan: ${updatedSubscription.plan}, Status: ${updatedSubscription.status}")

                    // Update UI state with new subscription
                    _uiState.value = SubscriptionUiState.Success(updatedSubscription)

                    // Close WebView
                    _checkoutUrl.value = null

                    // Show success message
                    _errorMessage.value = "¡Pago exitoso! Ahora tienes el Plan Pro"

                    android.util.Log.d("SubscriptionViewModel", "Subscription updated successfully to: ${updatedSubscription.plan}")
                }
                .onFailure { error ->
                    android.util.Log.e("SubscriptionViewModel", "Error processing payment: ${error.message}", error)
                    _errorMessage.value = error.message ?: "Error al procesar pago"

                    // Close WebView even on error so user isn't stuck
                    _checkoutUrl.value = null
                }

            _isLoading.value = false
        }
    }
}

sealed class SubscriptionUiState {
    object Loading : SubscriptionUiState()
    data class Success(val subscription: Subscription) : SubscriptionUiState()
    object IsPatient : SubscriptionUiState()
    data class Error(val message: String) : SubscriptionUiState()
}
