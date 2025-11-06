package com.softfocus.features.library.presentation.general.detail.components

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage

/**
 * Extrae el video ID de una URL de YouTube
 * Soporta formatos: youtube.com/watch?v=ID y youtu.be/ID
 */
private fun extractYoutubeVideoId(url: String): String? {
    return try {
        when {
            url.contains("youtube.com/watch?v=") -> {
                url.substringAfter("v=").substringBefore("&")
            }
            url.contains("youtu.be/") -> {
                url.substringAfter("youtu.be/").substringBefore("?")
            }
            else -> null
        }
    } catch (e: Exception) {
        null
    }
}

/**
 * Imagen hero del contenido (imagen grande superior)
 *
 * @param imageUrl URL de la imagen
 * @param contentDescription Descripción del contenido
 * @param onBackClick Callback al hacer clic en el botón de volver
 * @param showVideoPlayer Si debe mostrar el reproductor de video en lugar de la imagen
 * @param trailerUrl URL del trailer de YouTube
 * @param modifier Modificador opcional
 */
@Composable
fun ContentHeroImage(
    imageUrl: String?,
    contentDescription: String,
    onBackClick: () -> Unit = {},
    showVideoPlayer: Boolean = false,
    trailerUrl: String? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (showVideoPlayer && trailerUrl != null) {
            // Reproductor de YouTube
            val videoId = extractYoutubeVideoId(trailerUrl)
            if (videoId != null) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            webViewClient = WebViewClient()

                            val embedUrl = "https://www.youtube.com/embed/$videoId?autoplay=1&rel=0"
                            loadUrl(embedUrl)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Si no se puede extraer el video ID, mostrar la imagen
                AsyncImage(
                    model = imageUrl,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Imagen de fondo normal
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente oscuro en la parte inferior para mejor legibilidad
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 100f
                        )
                    )
            )
        }

        // Botón de volver (arriba a la izquierda)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(50)
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )
        }
    }
}
