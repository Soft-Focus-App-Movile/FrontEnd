package com.softfocus.features.library.presentation.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.features.library.domain.models.EmotionalTag
import com.softfocus.ui.theme.Green29
import com.softfocus.ui.theme.SourceSansSemiBold

/**
 * Chip para mostrar y seleccionar emociones
 *
 * @param emotion Emoción a mostrar
 * @param selected Si el chip está seleccionado
 * @param onClick Callback al hacer clic
 * @param modifier Modificador opcional
 */
@Composable
fun EmotionChip(
    emotion: EmotionalTag,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = "${emotion.getEmoji()} ${emotion.getDisplayName()}",
                style = SourceSansSemiBold.copy(fontSize = 14.sp)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = Color.Gray,
            selectedContainerColor = Green29.copy(alpha = 0.2f),
            selectedLabelColor = Green29
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = if (selected) Green29 else Color.Gray.copy(alpha = 0.3f),
            selectedBorderColor = Green29,
            borderWidth = 1.dp
        ),
        modifier = modifier.padding(horizontal = 4.dp)
    )
}

/**
 * Obtiene el emoji para cada emoción
 */
fun EmotionalTag.getEmoji(): String = when (this) {
    EmotionalTag.Happy -> "😊"
    EmotionalTag.Sad -> "😢"
    EmotionalTag.Anxious -> "😰"
    EmotionalTag.Calm -> "😌"
    EmotionalTag.Energetic -> "⚡"
}

/**
 * Obtiene el nombre legible para cada emoción
 */
fun EmotionalTag.getDisplayName(): String = when (this) {
    EmotionalTag.Happy -> "Feliz"
    EmotionalTag.Sad -> "Triste"
    EmotionalTag.Anxious -> "Ansioso"
    EmotionalTag.Calm -> "Calmado"
    EmotionalTag.Energetic -> "Enérgico"
}
