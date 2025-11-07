package com.softfocus.features.library.presentation.general.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.ui.theme.SourceSansRegular
import com.softfocus.ui.theme.YellowCB9D
import com.softfocus.ui.theme.Gray828

/**
 * Categorías de video disponibles
 */
enum class VideoCategory(val displayName: String, val queryText: String) {
    MEDITATION("Meditación", "meditación"),
    BREATHING("Respiración", "respiración"),
    RELAXATION("Relajación", "relajación")
}

/**
 * Componente que muestra los iconos de categorías de video
 *
 * @param onCategoryClick Callback cuando se selecciona una categoría
 * @param selectedCategory Categoría actualmente seleccionada
 * @param modifier Modificador opcional
 */
@Composable
fun CategoryIcons(
    onCategoryClick: (VideoCategory) -> Unit,
    selectedCategory: VideoCategory? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoCategory.entries.forEach { category ->
            CategoryIcon(
                category = category,
                isSelected = selectedCategory == category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

/**
 * Icono individual de categoría
 */
@Composable
private fun CategoryIcon(
    category: VideoCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        // Círculo con icono
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) YellowCB9D else Gray828
                ),
            contentAlignment = Alignment.Center
        ) {
            // Por ahora usamos un ícono placeholder
            // Reemplazaremos con los SVG proporcionados
            Text(
                text = when (category) {
                    VideoCategory.MEDITATION -> "🧘"
                    VideoCategory.BREATHING -> "🫁"
                    VideoCategory.RELAXATION -> "🤲"
                },
                fontSize = 32.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto de categoría
        Text(
            text = category.displayName,
            style = SourceSansRegular.copy(fontSize = 14.sp),
            color = if (isSelected) YellowCB9D else Color.White,
            textAlign = TextAlign.Center
        )
    }
}
