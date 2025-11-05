package com.softfocus.features.library.presentation.general.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softfocus.features.library.domain.models.ContentItem
import com.softfocus.features.library.domain.models.ContentType
import com.softfocus.features.library.domain.models.EmotionalTag
import com.softfocus.features.library.domain.repositories.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de biblioteca general
 *
 * Maneja el estado de la pantalla principal de biblioteca con tabs de:
 * - Películas/Series
 * - Música
 * - Videos
 * - Lugares
 */
class GeneralLibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GeneralLibraryUiState>(GeneralLibraryUiState.Loading)
    val uiState: StateFlow<GeneralLibraryUiState> = _uiState.asStateFlow()

    private val _selectedType = MutableStateFlow(ContentType.Movie)
    val selectedType: StateFlow<ContentType> = _selectedType.asStateFlow()

    private val _selectedEmotion = MutableStateFlow<EmotionalTag?>(null)
    val selectedEmotion: StateFlow<EmotionalTag?> = _selectedEmotion.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _favoritesMap = MutableStateFlow<Map<String, String>>(emptyMap())

    init {
        loadAllContent()
        loadFavorites()
    }

    /**
     * Carga contenido recomendado para todos los tipos
     */
    private fun loadAllContent() {
        viewModelScope.launch {
            _uiState.value = GeneralLibraryUiState.Loading

            try {
                val contentMap = mutableMapOf<ContentType, List<ContentItem>>()

                // Cargar contenido para cada tipo
                ContentType.values().forEach { type ->
                    repository.getRecommendedContent(
                        contentType = type,
                        limit = 20
                    ).onSuccess { content ->
                        contentMap[type] = content
                    }
                }

                _uiState.value = GeneralLibraryUiState.Success(
                    contentByType = contentMap,
                    selectedType = _selectedType.value
                )
            } catch (e: Exception) {
                _uiState.value = GeneralLibraryUiState.Error(
                    e.message ?: "Error al cargar contenido"
                )
            }
        }
    }

    /**
     * Carga contenido filtrado por emoción
     */
    fun loadContentByEmotion(emotion: EmotionalTag) {
        viewModelScope.launch {
            _selectedEmotion.value = emotion
            _uiState.value = GeneralLibraryUiState.Loading

            try {
                val contentMap = mutableMapOf<ContentType, List<ContentItem>>()

                // Cargar contenido para cada tipo con filtro de emoción
                ContentType.values().forEach { type ->
                    repository.getRecommendedByEmotion(
                        emotion = emotion,
                        contentType = type,
                        limit = 20
                    ).onSuccess { content ->
                        contentMap[type] = content
                    }
                }

                _uiState.value = GeneralLibraryUiState.Success(
                    contentByType = contentMap,
                    selectedType = _selectedType.value
                )
            } catch (e: Exception) {
                _uiState.value = GeneralLibraryUiState.Error(
                    e.message ?: "Error al cargar contenido"
                )
            }
        }
    }

    /**
     * Limpia el filtro de emoción y recarga todo el contenido
     */
    fun clearEmotionFilter() {
        _selectedEmotion.value = null
        loadAllContent()
    }

    fun searchContent(query: String) {
        if (query.isBlank()) {
            if (_selectedEmotion.value != null) {
                loadContentByEmotion(_selectedEmotion.value!!)
            } else {
                loadAllContent()
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = GeneralLibraryUiState.Loading

            try {
                repository.searchContent(
                    query = query,
                    contentType = _selectedType.value,
                    emotionFilter = _selectedEmotion.value,
                    limit = 20
                ).onSuccess { results ->
                    val contentMap = mapOf(_selectedType.value to results)
                    _uiState.value = GeneralLibraryUiState.Success(
                        contentByType = contentMap,
                        selectedType = _selectedType.value
                    )
                }.onFailure { error ->
                    _uiState.value = GeneralLibraryUiState.Error(
                        error.message ?: "Error en la búsqueda"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = GeneralLibraryUiState.Error(
                    e.message ?: "Error en la búsqueda"
                )
            }
        }
    }

    /**
     * Cambia el tipo de contenido seleccionado
     */
    fun selectContentType(type: ContentType) {
        _selectedType.value = type
        // Actualizar el estado con el nuevo tipo seleccionado
        val currentState = _uiState.value
        if (currentState is GeneralLibraryUiState.Success) {
            _uiState.value = currentState.copy(selectedType = type)
        }
    }

    /**
     * Carga los IDs de favoritos del usuario
     */
    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().onSuccess { favorites ->
                _favoriteIds.value = favorites.map { it.content.externalId }.toSet()
                _favoritesMap.value = favorites.associate { it.content.externalId to it.id }
            }
        }
    }

    /**
     * Alterna el estado de favorito de un contenido
     */
    fun toggleFavorite(content: ContentItem) {
        viewModelScope.launch {
            val isFavorite = _favoriteIds.value.contains(content.externalId)

            if (isFavorite) {
                val favoriteId = _favoritesMap.value[content.externalId]
                favoriteId?.let {
                    repository.deleteFavorite(it).onSuccess {
                        _favoriteIds.value = _favoriteIds.value - content.externalId
                        _favoritesMap.value = _favoritesMap.value - content.externalId
                    }
                }
            } else {
                repository.addFavorite(
                    contentId = content.externalId,
                    contentType = content.type
                ).onSuccess { favorite ->
                    _favoriteIds.value = _favoriteIds.value + content.externalId
                    _favoritesMap.value = _favoritesMap.value + (content.externalId to favorite.id)
                }
            }
        }
    }

    /**
     * Reintenta cargar el contenido
     */
    fun retry() {
        if (_selectedEmotion.value != null) {
            loadContentByEmotion(_selectedEmotion.value!!)
        } else {
            loadAllContent()
        }
    }
}
