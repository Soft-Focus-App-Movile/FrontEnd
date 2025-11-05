package com.softfocus.features.library.presentation.general.browse

import com.softfocus.features.library.domain.models.ContentItem
import com.softfocus.features.library.domain.models.ContentType

/**
 * Estados de UI para la pantalla de biblioteca general
 */
sealed class GeneralLibraryUiState {
    /**
     * Estado inicial - cargando contenido
     */
    object Loading : GeneralLibraryUiState()

    /**
     * Estado exitoso - contenido cargado
     *
     * @property contentByType Mapa de tipo de contenido a lista de items
     * @property selectedType Tipo de contenido seleccionado actualmente
     */
    data class Success(
        val contentByType: Map<ContentType, List<ContentItem>>,
        val selectedType: ContentType = ContentType.Movie
    ) : GeneralLibraryUiState() {
        /**
         * Obtiene el contenido del tipo seleccionado
         */
        fun getSelectedContent(): List<ContentItem> = contentByType[selectedType] ?: emptyList()
    }

    /**
     * Estado de error
     *
     * @property message Mensaje de error
     */
    data class Error(val message: String) : GeneralLibraryUiState()
}
