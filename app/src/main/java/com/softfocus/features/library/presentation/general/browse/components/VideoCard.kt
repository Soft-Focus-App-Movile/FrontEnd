package com.softfocus.features.library.presentation.general.browse.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
 * @param isSelected Si el contenido está seleccionado (modo psicólogo)
 * @param isSelectionMode Si está en modo selección (mostrar overlay)
 * @param onViewClick Callback al hacer clic en el botón "Ver"
 * @param onClick Callback al hacer clic en el card (para selección)
 * @param onLongClick Callback al mantener presionado el card
 * @param modifier Modificador opcional
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoCard(
    content: ContentItem,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    onViewClick: () -> Unit = {},
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelectionMode && isSelected) {
                Color(0xFF1C1C1C).copy(alpha = 0.7f)
            } else {
                Color(0xFF1C1C1C)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Thumbnail del video con indicador de selección
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

                // Overlay de selección cuando está en modo selección
                if (isSelectionMode && isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Green49.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Seleccionado",
                            tint = Green49,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = content.title,
                        style = SourceSansSemiBold.copy(fontSize = 14.sp),
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )

                    content.channelName?.let { channel ->
                        Text(
                            text = channel,
                            style = SourceSansRegular.copy(fontSize = 12.sp),
                            color = Color.White.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    content.getFormattedDuration()?.let { duration ->
                        Text(
                            text = duration,
                            style = SourceSansLight.copy(fontSize = 11.sp),
                            color = Color.White.copy(alpha = 0.6f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (!isSelectionMode) {
                    Button(
                        onClick = onViewClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green65
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        modifier = Modifier.align(Alignment.End).height(32.dp)
                    ) {
                        Text(
                            text = "Ver",
                            style = SourceSansSemiBold.copy(fontSize = 13.sp),
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
