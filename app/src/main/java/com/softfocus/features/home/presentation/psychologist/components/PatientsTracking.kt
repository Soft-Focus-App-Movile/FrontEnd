package com.softfocus.features.home.presentation.psychologist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.ui.components.ProfileAvatar
import com.softfocus.ui.theme.Black
import com.softfocus.ui.theme.CrimsonSemiBold
import com.softfocus.ui.theme.GrayA2
import com.softfocus.ui.theme.Green49
import com.softfocus.ui.theme.Green65
import com.softfocus.ui.theme.GreenF2
import com.softfocus.ui.theme.SourceSansRegular
import com.softfocus.ui.theme.White

data class PatientActivity(
    val id: String,
    val fullName: String,
    val profileImageUrl: String?,
    val activityStatus: String,
    val activityTime: String
)

@Composable
fun PatientsTracking(
    patients: List<PatientActivity> = mockPatients,
    onPatientClick: (String) -> Unit = {},
    onViewAllClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pacientes con actividad reciente",
                style = CrimsonSemiBold,
                fontSize = 18.sp,
                color = Green65
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Lista de pacientes (máximo 4)
        patients.take(4).forEach { patient ->
            PatientActivityCard(
                patient = patient,
                onClick = { onPatientClick(patient.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón "Ver todos"
        TextButton(
            onClick = onViewAllClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "Ver todos",
                style = SourceSansRegular,
                fontSize = 14.sp,
                color = Green49
            )
        }
    }
}

@Composable
fun PatientActivityCard(
    patient: PatientActivity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GreenF2),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar del paciente (forma cuadrada redondeada)
            ProfileAvatar(
                imageUrl = patient.profileImageUrl,
                fullName = patient.fullName,
                size = 56.dp,
                fontSize = 20.sp,
                backgroundColor = Color(0xFFE8F5E9),
                textColor = Green49,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Información del paciente
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = patient.fullName,
                    style = CrimsonSemiBold,
                    fontSize = 16.sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = patient.activityStatus,
                    style = SourceSansRegular,
                    fontSize = 13.sp,
                    color = GrayA2
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = patient.activityTime,
                    style = SourceSansRegular,
                    fontSize = 12.sp,
                    color = GrayA2
                )
            }
        }
    }
}

// Mock data
private val mockPatients = listOf(
    PatientActivity(
        id = "1",
        fullName = "Ana García",
        profileImageUrl = null,
        activityStatus = "Completó registro",
        activityTime = "Hace 3h"
    ),
    PatientActivity(
        id = "2",
        fullName = "Luis Torres",
        profileImageUrl = null,
        activityStatus = "No realizó registro",
        activityTime = "Hoy"
    ),
    PatientActivity(
        id = "3",
        fullName = "María López",
        profileImageUrl = null,
        activityStatus = "Completó registro",
        activityTime = "Ayer"
    ),
    PatientActivity(
        id = "4",
        fullName = "Carlos Martínez",
        profileImageUrl = null,
        activityStatus = "Completó registro",
        activityTime = "Hace 2 días"
    )
)
