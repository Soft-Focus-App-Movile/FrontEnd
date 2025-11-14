package com.softfocus.features.therapy.data.remote

import com.softfocus.core.networking.ApiConstants
import com.softfocus.features.therapy.data.models.request.ConnectWithPsychologistRequestDto
import com.softfocus.features.therapy.data.models.request.SendChatMessageRequestDto
import com.softfocus.features.therapy.data.models.response.ConnectResponseDto
import com.softfocus.features.therapy.data.models.response.GetRelationshipWithPatientResponseDto
import com.softfocus.features.therapy.data.models.response.MyRelationshipResponseDto
import com.softfocus.features.therapy.data.models.response.PatientDirectoryResponseDto
import com.softfocus.features.therapy.data.models.response.PatientProfileResponseDto
import com.softfocus.features.therapy.data.models.response.SendChatMessageResponseDto
import com.softfocus.features.therapy.data.models.response.TherapyChatResponseDto
import com.softfocus.features.tracking.data.model.CheckInsHistoryApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TherapyService {
    @GET(ApiConstants.Therapy.MY_RELATIONSHIP)
    suspend fun getMyRelationship(
        @Header("Authorization") token: String
    ): MyRelationshipResponseDto

    @GET(ApiConstants.Therapy.GET_PATIENT_RELATIONSHIP)
    suspend fun getRelationshipWithPatient(
        @Header("Authorization") token: String,
        @Path("patientId") patientId: String
    ): GetRelationshipWithPatientResponseDto

    @POST(ApiConstants.Therapy.CONNECT)
    suspend fun connectWithPsychologist(
        @Header("Authorization") token: String,
        @Body request: ConnectWithPsychologistRequestDto
    ): ConnectResponseDto

    @GET(ApiConstants.Therapy.PATIENTS)
    suspend fun getMyPatients(
        @Header("Authorization") token: String
    ): List<PatientDirectoryResponseDto>

    @GET(ApiConstants.Chat.HISTORY)
    suspend fun getChatHistory(
        @Header("Authorization") token: String,
        @Query("relationshipId") relationshipId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<TherapyChatResponseDto>>

    @POST(ApiConstants.Chat.SEND)
    suspend fun sendChatMessage(
        @Header("Authorization") token: String,
        @Body request: SendChatMessageRequestDto
    ): Response<SendChatMessageResponseDto>

    @GET(ApiConstants.Chat.LAST_MESSAGE)
    suspend fun getLastReceivedMessage(
        @Header("Authorization") token: String
    ): Response<TherapyChatResponseDto>

    @GET(ApiConstants.Users.PSYCHOLOGIST_PATIENT)
    suspend fun getPatientDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): PatientProfileResponseDto

    @GET(ApiConstants.Tracking.PATIENT_CHECK_INS_HISTORY)
    suspend fun getPatientCheckIns(
        @Header("Authorization") token: String,
        @Path("userId") patientId: String,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): CheckInsHistoryApiResponse

    @DELETE(ApiConstants.Therapy.DISCONNECT)
    suspend fun disconnectRelationship(
        @Header("Authorization") token: String,
        @Path("relationshipId") relationshipId: String
    )

}
