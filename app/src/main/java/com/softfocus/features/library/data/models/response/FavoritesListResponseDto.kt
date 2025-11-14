package com.softfocus.features.library.data.models.response

import com.google.gson.annotations.SerializedName

/**
 * DTO para respuesta que contiene lista de favoritos del backend
 *
 * Esta clase envuelve la lista de favoritos junto con estadísticas
 * sobre el total de favoritos.
 */
data class FavoritesListResponseDto(
    @SerializedName("favorites")
    val favorites: List<FavoriteResponseDto>,

    @SerializedName("total")
    val total: Int? = null
)
