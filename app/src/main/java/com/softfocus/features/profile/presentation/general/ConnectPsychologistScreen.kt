package com.softfocus.features.profile.presentation.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.R
import com.softfocus.features.therapy.presentation.connect.ConnectPsychologistViewModel
import com.softfocus.features.therapy.presentation.connect.ConnectUiState
import com.softfocus.features.therapy.presentation.di.TherapyPresentationModule
import com.softfocus.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectPsychologistScreen(
    viewModel: ConnectPsychologistViewModel,
    onNavigateBack: () -> Unit,
    onConnectionSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var code by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is ConnectUiState.Success) {
            onConnectionSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Conectar con Psicólogo",
                        style = CrimsonSemiBold,
                        fontSize = 20.sp,
                        color = Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            // Card con borde gradiente
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                YellowE8,
                                YellowCB9C
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    // Imagen de jirafa sobresaliendo a la izquierda DENTRO de la card
                    Image(
                        painter = painterResource(id = R.drawable.jiraff_focus),
                        contentDescription = "Jirafa",
                        modifier = Modifier
                            .size(200.dp)
                            .offset(x = (-30).dp)
                    )

                    // Contenido a la derecha
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Texto "Tienes código de tu psicólogo?"
                        Text(
                            text = "Tienes código de tu psicólogo?",
                            style = SourceSansRegular,
                            fontSize = 12.sp,
                            color = Yellow7E,
                            textAlign = TextAlign.Center,
                            lineHeight = 14.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Campo de texto para el código
                        OutlinedTextField(
                            value = code,
                            onValueChange = { code = it.uppercase().take(8) },
                            placeholder = {
                                Text(
                                    text = "Ingresa código aquí",
                                    style = SourceSansRegular,
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedContainerColor = YellowB5,
                                focusedContainerColor = YellowB5,
                                unfocusedTextColor = Color.White,
                                focusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            textStyle = SourceSansRegular.copy(fontSize = 11.sp),
                            enabled = uiState !is ConnectUiState.Loading,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Botón Conectar
                        Button(
                            onClick = {
                                if (code.isNotBlank()) {
                                    viewModel.connectWithPsychologist(code)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(28.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = YellowD8,
                                disabledContainerColor = GrayD9
                            ),
                            enabled = code.isNotBlank() && uiState !is ConnectUiState.Loading,
                            contentPadding = PaddingValues(vertical = 0.dp)
                        ) {
                            if (uiState is ConnectUiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Black,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Conectar",
                                    style = SourceSansRegular,
                                    fontSize = 12.sp,
                                    color = Black
                                )
                            }
                        }

                        if (uiState is ConnectUiState.Error) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = (uiState as ConnectUiState.Error).message,
                                style = SourceSansRegular,
                                fontSize = 10.sp,
                                color = Color.Red,
                                textAlign = TextAlign.Center,
                                lineHeight = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Link "¿No tienes código? Busca psicólogos aquí"
                        TextButton(
                            onClick = { /* TODO: Navigate to search */ },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text(
                                text = "¿No tienes código? Busca psicólogos aquí",
                                style = SourceSansRegular,
                                fontSize = 10.sp,
                                color = Yellow7E,
                                textAlign = TextAlign.Center,
                                lineHeight = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectPsychologistScreenPreview() {
    val context = LocalContext.current
    val viewModel = TherapyPresentationModule.getConnectPsychologistViewModel(context)
    ConnectPsychologistScreen(
        viewModel = viewModel,
        onNavigateBack = {},
        onConnectionSuccess = {}
    )
}
