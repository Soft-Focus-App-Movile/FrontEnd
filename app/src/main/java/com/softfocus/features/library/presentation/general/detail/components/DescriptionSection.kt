package com.softfocus.features.library.presentation.general.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.ui.theme.CrimsonBold
import com.softfocus.ui.theme.Gray828
import com.softfocus.ui.theme.Green29
import com.softfocus.ui.theme.SourceSansRegular

/**
 * Sección de descripción del contenido
 *
 * @param description Texto de la descripción
 * @param modifier Modificador opcional
 */
@Composable
fun DescriptionSection(
    description: String?,
    modifier: Modifier = Modifier
) {
    if (description != null) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título de la sección
            Text(
                text = "Descripción",
                style = CrimsonBold.copy(fontSize = 18.sp),
                color = Green29
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = description,
                style = SourceSansRegular.copy(fontSize = 14.sp),
                color = Gray828,
                lineHeight = 20.sp
            )
        }
    }
}
