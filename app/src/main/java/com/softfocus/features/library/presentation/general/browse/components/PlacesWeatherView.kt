package com.softfocus.features.library.presentation.general.browse.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.R
import com.softfocus.features.library.domain.models.WeatherCondition
import com.softfocus.ui.theme.*

/**
 * Vista de clima con zorrito mascota
 *
 * @param weather Condiciones climáticas actuales
 * @param modifier Modificador opcional
 */
@Composable
fun PlacesWeatherView(
    weather: WeatherCondition,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Fila horizontal centrada: Zorrito + Clima
        Row(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .offset(x = (-20).dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zorrito a la izquierda
            Image(
                painter = painterResource(id = R.drawable.fox_image),
                contentDescription = "Mascota",
                modifier = Modifier.size(152.dp)
            )

            // Información del clima a la derecha
            WeatherHeader(weather = weather)
        }

        // Recomendación basada en el clima con fondo oscuro semi-transparente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = weather.getRecommendation(),
                style = SourceSansRegular.copy(fontSize = 14.sp),
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

/**
 * Header con información del clima
 */
@Composable
private fun WeatherHeader(
    weather: WeatherCondition,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Nombre de la ciudad
        Text(
            text = weather.cityName,
            style = SourceSansSemiBold.copy(fontSize = 34.sp),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Temperatura principal
        Text(
            text = "${weather.temperature.toInt()}°",
            style = CrimsonBold.copy(fontSize = 66.sp),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Descripción del clima
        Text(
            text = weather.description.replaceFirstChar { it.uppercase() },
            style = SourceSansRegular.copy(fontSize = 18.sp),
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        // Temperaturas alta y baja (aproximadas)
        // Como el backend solo retorna temperatura actual, mostramos estimaciones
        val highTemp = (weather.temperature + 5).toInt()
        val lowTemp = (weather.temperature - 5).toInt()

        Text(
            text = "H:${highTemp}° L:${lowTemp}°",
            style = SourceSansRegular.copy(fontSize = 15.sp),
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}
