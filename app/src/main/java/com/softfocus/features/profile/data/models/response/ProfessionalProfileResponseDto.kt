package com.softfocus.features.profile.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * DTO for Professional Profile API response
 * Matches backend PsychologistProfessionalResource structure
 * Used for GET and PUT /api/v1/users/psychologist/professional endpoint responses
 */
data class ProfessionalProfileResponseDto(
    @SerializedName("professionalBio")
    val professionalBio: String? = null,

    @SerializedName("isAcceptingNewPatients")
    val isAcceptingNewPatients: Boolean? = null,

    @SerializedName("maxPatientsCapacity")
    val maxPatientsCapacity: Int? = null,

    @SerializedName("targetAudience")
    val targetAudience: List<String>? = null,

    @SerializedName("languages")
    val languages: List<String>? = null,

    @SerializedName("businessName")
    val businessName: String? = null,

    @SerializedName("businessAddress")
    val businessAddress: String? = null,

    @SerializedName("bankAccount")
    val bankAccount: String? = null,

    @SerializedName("paymentMethods")
    val paymentMethods: String? = null,

    @SerializedName("isProfileVisibleInDirectory")
    val isProfileVisibleInDirectory: Boolean? = null,

    @SerializedName("allowsDirectMessages")
    val allowsDirectMessages: Boolean? = null
)
