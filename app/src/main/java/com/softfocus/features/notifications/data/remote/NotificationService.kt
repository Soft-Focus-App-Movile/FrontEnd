package com.softfocus.features.notifications.data.remote

import com.softfocus.core.networking.ApiConstants
import com.softfocus.features.notifications.data.models.request.UpdatePreferencesRequestDto
import com.softfocus.features.notifications.data.models.response.*
import retrofit2.Response
import retrofit2.http.*

interface NotificationService {

    // ========== NOTIFICATION ENDPOINTS ==========

    // GET /api/notifications - Obtener todas las notificaciones del usuario actual
    @GET(ApiConstants.Notifications.BASE)
    suspend fun getNotifications(
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<NotificationListResponseDto>

    // GET /api/notifications/{userId} - Obtener notificaciones de un usuario específico (probablemente admin)
    @GET(ApiConstants.Notifications.BY_USER_ID)
    suspend fun getNotificationsByUserId(
        @Path("userId") userId: String,
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<NotificationListResponseDto>

    // GET /api/notifications/detail/{notificationId} - Obtener una notificación específica
    @GET(ApiConstants.Notifications.DETAIL)
    suspend fun getNotificationById(
        @Path("notificationId") notificationId: String
    ): Response<NotificationResponseDto>

    // POST /api/notifications/{notificationId}/read - Marcar una notificación como leída
    @POST(ApiConstants.Notifications.MARK_AS_READ)
    suspend fun markAsRead(
        @Path("notificationId") notificationId: String
    ): Response<Unit>

    // POST /api/notifications/read-all - Marcar todas las notificaciones como leídas
    @POST(ApiConstants.Notifications.MARK_ALL_READ)
    suspend fun markAllAsRead(): Response<Unit>

    // DELETE /api/notifications/{notificationId} - Eliminar una notificación
    @DELETE(ApiConstants.Notifications.DELETE)
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: String
    ): Response<Unit>

    // GET /api/notifications/unread-count - Obtener el contador de notificaciones no leídas
    @GET(ApiConstants.Notifications.UNREAD_COUNT)
    suspend fun getUnreadCount(): Response<UnreadCountResponseDto>

    // ========== PREFERENCES ENDPOINTS ==========

    // GET /api/preferences - Obtener preferencias de notificación del usuario actual
    @GET(ApiConstants.Preferences.BASE)
    suspend fun getPreferences(): Response<PreferenceListResponseDto>

    // PUT /api/preferences - Actualizar preferencias de notificación
    @PUT(ApiConstants.Preferences.BASE)
    suspend fun updatePreferences(
        @Body request: UpdatePreferencesRequestDto
    ): Response<PreferenceListResponseDto>

    // POST /api/preferences/reset - Restaurar preferencias a valores por defecto
    @POST(ApiConstants.Preferences.RESET)
    suspend fun resetPreferences(): Response<PreferenceListResponseDto>
}