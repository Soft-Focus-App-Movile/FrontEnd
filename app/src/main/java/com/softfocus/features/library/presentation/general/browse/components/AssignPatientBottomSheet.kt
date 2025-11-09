package com.softfocus.features.library.presentation.general.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.softfocus.features.library.domain.models.MockPatientsData
import com.softfocus.features.library.domain.models.Patient
import com.softfocus.ui.theme.Green49
import com.softfocus.ui.theme.Gray828
import com.softfocus.ui.theme.SourceSansSemiBold
import com.softfocus.ui.theme.SourceSansRegular

/**
 * Bottom Sheet para seleccionar un paciente al que asignar contenido
 * Solo visible para psicólogos
 *
 * @param selectedCount Cantidad de contenido seleccionado
 * @param onPatientSelected Callback cuando se selecciona un paciente (patient, patientId, patientName)
 * @param onDismiss Callback para cerrar el bottom sheet
 * @param modifier Modificador opcional
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignPatientBottomSheet(
    selectedCount: Int,
    onPatientSelected: (patientId: String, patientName: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val patients = remember { MockPatientsData.patients }

    // Filtrar pacientes por búsqueda
    val filteredPatients = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            patients
        } else {
            patients.filter { patient ->
                patient.name.contains(searchQuery, ignoreCase = true) ||
                patient.email.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1C1C1C),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Seleccione el Paciente",
                        style = SourceSansSemiBold.copy(fontSize = 20.sp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$selectedCount ${if (selectedCount == 1) "contenido seleccionado" else "contenidos seleccionados"}",
                        style = SourceSansRegular.copy(fontSize = 14.sp),
                        color = Gray828
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar...",
                        style = SourceSansRegular.copy(fontSize = 14.sp),
                        color = Gray828
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Gray828
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar",
                                tint = Gray828
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Green49,
                    unfocusedBorderColor = Gray828,
                    cursorColor = Green49
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de pacientes
            if (filteredPatients.isEmpty()) {
                // Estado vacío
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron pacientes",
                        style = SourceSansRegular.copy(fontSize = 14.sp),
                        color = Gray828
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredPatients) { patient ->
                        PatientItem(
                            patient = patient,
                            onClick = {
                                onPatientSelected(patient.id, patient.name)
                                onDismiss()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Item individual de paciente en la lista
 */
@Composable
private fun PatientItem(
    patient: Patient,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF2D2D2D)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto del paciente
            if (patient.photoUrl != null) {
                AsyncImage(
                    model = patient.photoUrl,
                    contentDescription = patient.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Gray828)
                )
            } else {
                // Inicial si no hay foto
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Green49),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = patient.name.first().uppercase(),
                        style = SourceSansSemiBold.copy(fontSize = 20.sp),
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del paciente
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = patient.name,
                    style = SourceSansSemiBold.copy(fontSize = 16.sp),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = patient.email,
                    style = SourceSansRegular.copy(fontSize = 13.sp),
                    color = Gray828,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
