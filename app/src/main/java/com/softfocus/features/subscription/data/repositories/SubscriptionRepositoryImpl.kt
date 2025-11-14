package com.softfocus.features.subscription.data.repositories

import com.softfocus.features.subscription.data.models.request.CancelSubscriptionRequestDto
import com.softfocus.features.subscription.data.models.request.CreateCheckoutSessionRequestDto
import com.softfocus.features.subscription.data.models.request.TrackFeatureUsageRequestDto
import com.softfocus.features.subscription.data.models.response.FeatureUsageDto
import com.softfocus.features.subscription.data.models.response.SubscriptionResponseDto
import com.softfocus.features.subscription.data.models.response.UsageLimitsDto
import com.softfocus.features.subscription.data.models.response.UsageStatsResponseDto
import com.softfocus.features.subscription.data.remote.SubscriptionService
import com.softfocus.features.subscription.domain.models.CheckoutSession
import com.softfocus.features.subscription.domain.models.FeatureAccess
import com.softfocus.features.subscription.domain.models.FeatureType
import com.softfocus.features.subscription.domain.models.FeatureUsage
import com.softfocus.features.subscription.domain.models.Subscription
import com.softfocus.features.subscription.domain.models.SubscriptionPlan
import com.softfocus.features.subscription.domain.models.SubscriptionStatus
import com.softfocus.features.subscription.domain.models.UsageLimits
import com.softfocus.features.subscription.domain.models.UsageStats
import com.softfocus.features.subscription.domain.repositories.SubscriptionRepository

class SubscriptionRepositoryImpl(
    private val subscriptionService: SubscriptionService
) : SubscriptionRepository {

    override suspend fun getMySubscription(): Result<Subscription> {
        return try {
            val response = subscriptionService.getMySubscription()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al obtener suscripción: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun getUsageStats(): Result<UsageStats> {
        return try {
            val response = subscriptionService.getUsageStats()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al obtener estadísticas: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun checkFeatureAccess(featureType: FeatureType): Result<FeatureAccess> {
        return try {
            val featureTypeStr = featureType.toBackendString()
            val response = subscriptionService.checkFeatureAccess(featureTypeStr)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(
                    FeatureAccess(
                        hasAccess = body.hasAccess,
                        denialReason = body.denialReason,
                        currentUsage = body.currentUsage,
                        limit = body.limit,
                        upgradeMessage = body.upgradeMessage
                    )
                )
            } else {
                Result.failure(Exception("Error al verificar acceso: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun createCheckoutSession(successUrl: String, cancelUrl: String): Result<CheckoutSession> {
        return try {
            val request = CreateCheckoutSessionRequestDto(
                successUrl = successUrl,
                cancelUrl = cancelUrl
            )
            val response = subscriptionService.createCheckoutSession(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(
                    CheckoutSession(
                        sessionId = body.sessionId,
                        checkoutUrl = body.checkoutUrl
                    )
                )
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Solicitud inválida. Por favor, intenta de nuevo."
                    403 -> "Ya tienes una suscripción activa."
                    500 -> "Error del servidor. Por favor, intenta más tarde."
                    else -> "Error al crear sesión de pago: ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun cancelSubscription(cancelImmediately: Boolean): Result<Subscription> {
        return try {
            val request = CancelSubscriptionRequestDto(cancelImmediately)
            val response = subscriptionService.cancelSubscription(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "No puedes cancelar esta suscripción."
                    404 -> "Suscripción no encontrada."
                    500 -> "Error del servidor. Por favor, intenta más tarde."
                    else -> "Error al cancelar suscripción: ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun trackUsage(userId: String, featureType: FeatureType): Result<Unit> {
        return try {
            val request = TrackFeatureUsageRequestDto(
                userId = userId,
                featureType = featureType.toBackendString()
            )
            val response = subscriptionService.trackUsage(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al rastrear uso: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun initializeSubscription(): Result<Subscription> {
        return try {
            val response = subscriptionService.initializeSubscription()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Error al inicializar suscripción: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    override suspend fun handleCheckoutSuccess(sessionId: String): Result<Subscription> {
        return try {
            val response = subscriptionService.handleCheckoutSuccess(sessionId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Sesión de pago inválida."
                    404 -> "Sesión de pago no encontrada."
                    500 -> "Error del servidor. Por favor, intenta más tarde."
                    else -> "Error al procesar pago: ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }


    private fun SubscriptionResponseDto.toDomain(): Subscription {
        return Subscription(
            id = id,
            userId = userId,
            userType = userType,
            plan = parsePlan(plan),
            status = parseStatus(status),
            currentPeriodStart = currentPeriodStart,
            currentPeriodEnd = currentPeriodEnd,
            cancelAtPeriodEnd = cancelAtPeriodEnd,
            cancelledAt = cancelledAt,
            isActive = isActive,
            usageLimits = usageLimits.toDomain(),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun UsageLimitsDto.toDomain(): UsageLimits {
        return UsageLimits(
            aiChatMessagesPerDay = aiChatMessagesPerDay,
            facialAnalysisPerWeek = facialAnalysisPerWeek,
            contentRecommendationsPerWeek = contentRecommendationsPerWeek,
            checkInsPerDay = checkInsPerDay,
            maxPatientConnections = maxPatientConnections,
            contentAssignmentsPerWeek = contentAssignmentsPerWeek
        )
    }

    private fun UsageStatsResponseDto.toDomain(): UsageStats {
        return UsageStats(
            plan = parsePlan(plan),
            featureUsages = featureUsages.map { it.toDomain() },
            generatedAt = generatedAt
        )
    }

    private fun FeatureUsageDto.toDomain(): FeatureUsage {
        return FeatureUsage(
            featureType = parseFeatureType(featureType),
            currentUsage = currentUsage,
            limit = limit,
            isUnlimited = isUnlimited,
            limitReached = limitReached,
            remaining = remaining,
            periodStart = periodStart,
            periodEnd = periodEnd,
            lastUsedAt = lastUsedAt
        )
    }

    private fun parsePlan(planStr: String): SubscriptionPlan {
        return when (planStr.uppercase()) {
            "BASIC" -> SubscriptionPlan.BASIC
            "PRO" -> SubscriptionPlan.PRO
            else -> SubscriptionPlan.BASIC
        }
    }

    private fun parseStatus(statusStr: String): SubscriptionStatus {
        return when (statusStr.uppercase()) {
            "ACTIVE" -> SubscriptionStatus.ACTIVE
            "CANCELLED" -> SubscriptionStatus.CANCELLED
            "PAST_DUE" -> SubscriptionStatus.PAST_DUE
            "TRIALING" -> SubscriptionStatus.TRIALING
            else -> SubscriptionStatus.ACTIVE
        }
    }

    private fun parseFeatureType(featureTypeStr: String): FeatureType {
        return when (featureTypeStr) {
            "AiChatMessage" -> FeatureType.AI_CHAT_MESSAGE
            "FacialAnalysis" -> FeatureType.FACIAL_ANALYSIS
            "ContentRecommendation" -> FeatureType.CONTENT_RECOMMENDATION
            "CheckIn" -> FeatureType.CHECK_IN
            "PatientConnection" -> FeatureType.PATIENT_CONNECTION
            "ContentAssignment" -> FeatureType.CONTENT_ASSIGNMENT
            else -> FeatureType.AI_CHAT_MESSAGE
        }
    }

    private fun FeatureType.toBackendString(): String {
        return when (this) {
            FeatureType.AI_CHAT_MESSAGE -> "AiChatMessage"
            FeatureType.FACIAL_ANALYSIS -> "FacialAnalysis"
            FeatureType.CONTENT_RECOMMENDATION -> "ContentRecommendation"
            FeatureType.CHECK_IN -> "CheckIn"
            FeatureType.PATIENT_CONNECTION -> "PatientConnection"
            FeatureType.CONTENT_ASSIGNMENT -> "ContentAssignment"
        }
    }
}
