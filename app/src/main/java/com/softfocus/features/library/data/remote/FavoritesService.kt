package com.softfocus.features.library.data.remote

import com.softfocus.core.networking.ApiConstants
import com.softfocus.features.library.data.models.request.FavoriteRequestDto
import com.softfocus.features.library.data.models.response.FavoriteResponseDto
import com.softfocus.features.library.data.models.response.FavoritesListResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Servicio Retrofit para gestión de favoritos
 * Solo disponible para usuarios General y Patient (NO para Psychologist)
 */
interface FavoritesService {

    /**
     * Obtiene todos los favoritos del usuario autenticado
     *
     * @param token Token de autenticación Bearer
     * @param contentType Filtro opcional por tipo de contenido (Movie, Music, Video, Place)
     * @param emotionFilter Filtro opcional por emoción (Happy, Sad, Anxious, Calm, Energetic)
     * @return Objeto con lista de favoritos y total
     *
     * Ejemplo de uso:
     * ```
     * // Obtener todos los favoritos
     * val response = getFavorites("Bearer $token")
     * val allFavorites = response.favorites
     *
     * // Filtrar por tipo
     * val movieResponse = getFavorites("Bearer $token", contentType = "Movie")
     *
     * // Filtrar por emoción
     * val calmResponse = getFavorites("Bearer $token", emotionFilter = "Calm")
     * ```
     */
    @GET(ApiConstants.Library.FAVORITES)
    suspend fun getFavorites(
        @Header("Authorization") token: String,
        @Query("contentType") contentType: String? = null,
        @Query("emotionFilter") emotionFilter: String? = null
    ): FavoritesListResponseDto

    /**
     * Agrega un contenido a favoritos
     *
     * @param token Token de autenticación Bearer
     * @param request Datos del contenido a favoritear (contentId, contentType)
     * @return El favorito creado con su ID
     *
     * Nota: El contenido debe existir en cache (haber sido buscado previamente)
     *
     * Ejemplo de uso:
     * ```
     * val request = FavoriteRequestDto(
     *     contentId = "tmdb-movie-27205",
     *     contentType = "Movie"
     * )
     * val favorite = addFavorite("Bearer $token", request)
     * ```
     */
    @POST(ApiConstants.Library.FAVORITES)
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequestDto
    ): FavoriteResponseDto

    @DELETE(ApiConstants.Library.FAVORITE_BY_ID)
    suspend fun deleteFavorite(
        @Header("Authorization") token: String,
        @Path("favoriteId") favoriteId: String
    )
}
