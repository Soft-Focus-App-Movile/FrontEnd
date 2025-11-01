package com.softfocus.features.therapy.data.remote

import com.softfocus.core.networking.ApiConstants
import com.softfocus.features.therapy.data.models.request.ConnectWithPsychologistRequestDto
import com.softfocus.features.therapy.data.models.response.ConnectResponseDto
import com.softfocus.features.therapy.data.models.response.MyRelationshipResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TherapyService {
    @GET(ApiConstants.Therapy.MY_RELATIONSHIP)
    suspend fun getMyRelationship(): MyRelationshipResponseDto

    @POST(ApiConstants.Therapy.CONNECT)
    suspend fun connectWithPsychologist(@Body request: ConnectWithPsychologistRequestDto): ConnectResponseDto
}
