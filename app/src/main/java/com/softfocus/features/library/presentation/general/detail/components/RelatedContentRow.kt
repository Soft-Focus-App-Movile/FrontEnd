package com.softfocus.features.library.presentation.general.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.softfocus.features.library.domain.models.ContentItem
import com.softfocus.ui.theme.CrimsonBold
import com.softfocus.ui.theme.Green29
import com.softfocus.ui.theme.SourceSansRegular

/**
 * Fila horizontal de contenido relacionado
 *
 * @param relatedContent Lista de contenido relacionado
 * @param onContentClick Callback al hacer clic en un item
 * @param modifier Modificador opcional
 */
@Composable
fun RelatedContentRow(
    relatedContent: List<ContentItem>,
    onContentClick: (ContentItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (relatedContent.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Título de la sección
            Text(
                text = "Otros",
                style = CrimsonBold.copy(fontSize = 18.sp),
                color = Green29,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Scroll horizontal de contenido
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(relatedContent) { item ->
                    RelatedContentCard(
                        content = item,
                        onClick = { onContentClick(item) }
                    )
                }
            }
        }
    }
}

/**
 * Card pequeño para contenido relacionado
 */
@Composable
private fun RelatedContentCard(
    content: ContentItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            // Imagen
            AsyncImage(
                model = content.getMainImageUrl(),
                contentDescription = content.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            // Título
            Text(
                text = content.title,
                style = SourceSansRegular.copy(fontSize = 12.sp),
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
