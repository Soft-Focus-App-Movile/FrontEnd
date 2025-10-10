package com.softfocus.features.auth.presentation.accountreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.R
import com.softfocus.core.ui.theme.SoftFocusTheme
import com.softfocus.ui.theme.CrimsonSemiBold
import com.softfocus.ui.theme.SourceSansRegular

@Composable
fun AccountReviewScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD4E4D4), // Verde claro arriba
                        Color(0xFF8FB89C)  // Verde más oscuro abajo
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "Tu cuenta está\nsiendo revisada",
                style = CrimsonSemiBold,
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            // Imagen del panda
            Image(
                painter = painterResource(id = R.drawable.soft_panda_black),
                contentDescription = "Panda Soft Focus",
                modifier = Modifier
                    .size(280.dp)
                    .padding(vertical = 32.dp)
            )

            // Texto "soft focus" (opcional, puedes ajustarlo según el diseño)
            Text(
                text = "soft focus",
                style = SourceSansRegular,
                fontSize = 24.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountReviewScreenPreview() {
    SoftFocusTheme {
        AccountReviewScreen()
    }
}
