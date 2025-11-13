package com.softfocus.features.search.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.softfocus.features.search.domain.models.Psychologist
import com.softfocus.features.search.presentation.components.ContactDialog
import com.softfocus.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsychologistDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: PsychologistDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showContactDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Green37
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Green49)
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.error ?: "Error desconocido",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray787
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(containerColor = Green49)
                        ) {
                            Text("Reintentar", color = Color.White)
                        }
                    }
                }
            }
            state.psychologist != null -> {
                PsychologistDetailContent(
                    psychologist = state.psychologist!!,
                    onContactClick = { showContactDialog = true },
                    modifier = Modifier.padding(paddingValues)
                )

                if (showContactDialog) {
                    ContactDialog(
                        psychologist = state.psychologist!!,
                        onDismiss = { showContactDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun PsychologistDetailContent(
    psychologist: Psychologist,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header con foto
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Cuadro verde con la foto ocupando todo el espacio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Green49),
                contentAlignment = Alignment.Center
            ) {
                // Foto de perfil que ocupa todo el espacio verde
                if (psychologist.profileImageUrl != null) {
                    AsyncImage(
                        model = psychologist.profileImageUrl,
                        contentDescription = psychologist.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Si no hay foto, mostrar icono centrado
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre a la izquierda
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = psychologist.fullName,
                    style = CrimsonSemiBold,
                    fontSize = 24.sp,
                    color = Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Rating
                if (psychologist.averageRating != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = YellowCB9D,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", psychologist.averageRating),
                            style = SourceSansSemiBold,
                            fontSize = 18.sp,
                            color = Black
                        )
                        Text(
                            text = " (${psychologist.totalReviews} reseñas)",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray787
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Información básica
            InfoCard {
                InfoRow(label = "Años de experiencia", value = "${psychologist.yearsOfExperience} años")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Ciudad", value = psychologist.city ?: "No especificado")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(
                    label = "Disponibilidad",
                    value = if (psychologist.isAcceptingNewPatients) "Disponible" else "No disponible",
                    valueColor = if (psychologist.isAcceptingNewPatients) Green49 else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Formación académica
            SectionTitle("Formación Académica")
            Spacer(modifier = Modifier.height(8.dp))
            InfoCard {
                InfoRow(label = "Universidad", value = psychologist.university ?: "No especificado")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Grado", value = psychologist.degree ?: "No especificado")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Año de graduación", value = psychologist.graduationYear?.toString() ?: "No especificado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información profesional
            SectionTitle("Colegiatura")
            Spacer(modifier = Modifier.height(8.dp))
            InfoCard {
                InfoRow(label = "Número de colegiatura", value = psychologist.licenseNumber ?: "No especificado")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Colegio profesional", value = psychologist.professionalCollege ?: "No especificado")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                InfoRow(label = "Región", value = psychologist.collegeRegion ?: "No especificado")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Especialidades
            SectionTitle("Especialidades")
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (psychologist.specialties.isNotEmpty()) {
                        psychologist.specialties.forEach { specialty ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = "•",
                                    style = SourceSansSemiBold,
                                    fontSize = 14.sp,
                                    color = Green49
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = specialty,
                                    style = SourceSansRegular,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No se han especificado especialidades",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray787
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bio profesional
            SectionTitle("Acerca de")
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = psychologist.professionalBio ?: "El psicólogo no ha proporcionado información adicional.",
                    style = SourceSansRegular,
                    fontSize = 14.sp,
                    color = if (psychologist.professionalBio != null) Color.Black else Gray787,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Justify,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Idiomas
            SectionTitle("Idiomas")
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (!psychologist.languages.isNullOrEmpty()) {
                        psychologist.languages.joinToString(", ")
                    } else {
                        "No especificado"
                    },
                    style = SourceSansRegular,
                    fontSize = 14.sp,
                    color = if (!psychologist.languages.isNullOrEmpty()) Color.Black else Gray787,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de contacto
            Button(
                onClick = onContactClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = YellowCB9D),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Contactar",
                    style = SourceSansSemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color = Color.Black) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = SourceSansRegular,
            fontSize = 14.sp,
            color = Gray787
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = SourceSansSemiBold,
            fontSize = 14.sp,
            color = valueColor
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = CrimsonSemiBold,
        fontSize = 18.sp,
        color = Green65
    )
}

@Preview(showBackground = true)
@Composable
fun PsychologistDetailContentPreview() {
    PsychologistDetailContent(
        psychologist = Psychologist(
            id = "1",
            fullName = "Dra. Patricia Sanchez",
            profileImageUrl = null,
            professionalBio = null,
            specialties = listOf(),
            yearsOfExperience = 8,
            city = null,
            languages = null,
            isAcceptingNewPatients = true,
            averageRating = null,
            totalReviews = 0,
            allowsDirectMessages = true,
            targetAudience = listOf("Adultos", "Adolescentes"),
            email = "patricia.sanchez@example.com",
            phone = null,
            whatsApp = null,
            corporateEmail = null,
            university = null,
            graduationYear = null,
            degree = null,
            licenseNumber = null,
            professionalCollege = null,
            collegeRegion = null
        ),
        onContactClick = {}
    )
}
