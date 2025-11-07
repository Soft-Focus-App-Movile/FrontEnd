package com.softfocus.features.library.presentation.general.browse.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.softfocus.features.library.domain.models.ContentItem
import com.softfocus.ui.theme.*

/**
 * Card específico para videos con botón "Ver" que abre YouTube
 *
 * @param content Item de contenido de video
 * @param isFavorite Si el contenido está marcado como favorito
 * @param onFavoriteClick Callback al hacer clic en el botón de favorito
 * @param onViewClick Callback al hacer clic en el botón "Ver"
 * @param modifier Modificador opcional
 */
@Composable
fun VideoCard(
    content: ContentItem,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C1C1C)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Thumbnail del video
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(90.dp)
            ) {
                AsyncImage(
                    model = content.thumbnailUrl,
                    contentDescription = content.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )

                // Botón de favorito en esquina superior derecha del thumbnail
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (isFavorite) Green49 else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del video
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Título y metadatos
                Column {
                    // Título
                    Text(
                        text = content.title,
                        style = SourceSansSemiBold.copy(fontSize = 14.sp),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Canal/Autor
                    content.channelName?.let { channel ->
                        Text(
                            text = channel,
                            style = SourceSansRegular.copy(fontSize = 12.sp),
                            color = Color.White.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Duración
                    content.getFormattedDuration()?.let { duration ->
                        Text(
                            text = duration,
                            style = SourceSansLight.copy(fontSize = 11.sp),
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                // Botón "Ver"
                Button(
                    onClick = onViewClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green49
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Ver",
                        style = SourceSansSemiBold.copy(fontSize = 13.sp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
