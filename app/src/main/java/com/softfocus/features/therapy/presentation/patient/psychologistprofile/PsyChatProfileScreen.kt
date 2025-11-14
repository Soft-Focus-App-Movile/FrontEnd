package com.softfocus.features.therapy.presentation.patient.psychologistprofile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.softfocus.R
import com.softfocus.ui.theme.Black
import com.softfocus.ui.theme.CrimsonSemiBold
import com.softfocus.ui.theme.Green49
import com.softfocus.ui.theme.RedE8
import com.softfocus.ui.theme.SoftFocusMobileTheme
import com.softfocus.ui.theme.SourceSansBold
import com.softfocus.ui.theme.SourceSansRegular
import com.softfocus.ui.theme.SourceSansSemiBold
import com.softfocus.ui.theme.White

val buttonRed = Color(0xFFC65252)

@Composable
fun PsyChatProfileScreen(
    onBackClicked: () -> Unit,
    viewModel: PsyChatProfileViewModel
) {
    val summaryState by viewModel.summaryState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFCFEFC))
    ) {
        // Sección de imagen superior y botón de regreso
        item {
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                // Placeholder para la imagen de cabecera
                AsyncImage(
                    model = summaryState.profilePhotoUrl,
                    contentDescription = "Foto de perfil de psicologo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.ic_profile_user),
                    error = painterResource(id = R.drawable.ic_profile_user)
                )
                // Botón de regreso
                IconButton(
                    onClick = onBackClicked,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }
            }
        }

        // Contenido principal del perfil
        item {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = summaryState.psychologistName,
                    style = CrimsonSemiBold.copy(fontSize = 30.sp),
                    color = Color(0xFF657142)
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (summaryState.specialties.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        summaryState.specialties.forEach { specialty ->
                            ProfileTag(text = specialty)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray)

                // Título y Universidad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = summaryState.degree,
                        style = CrimsonSemiBold.copy(fontSize = 18.sp),
                    )
                    Text(
                        text = summaryState.university,
                        style = SourceSansSemiBold.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray)

                // Biografía
                Text(
                    text = summaryState.bio,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Justify
                )
                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray)

                // Contacto externo


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Contacto externo",
                            style = CrimsonSemiBold.copy(fontSize = 24.sp),
                            color = Color(0xFF37593F)
                        )
                        ContactRow(
                            icon = Icons.Default.Email,
                            text = summaryState.email
                        )
                        ContactRow(
                            icon = Icons.Default.Phone,
                            text = summaryState.phoneNumber
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.bunny_softfocus),
                        contentDescription = "Conejo",
                        modifier = Modifier
                            .height(150.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray)

            }
        }
    }
}

@Composable
fun ProfileTag(text: String) {
    Surface(
        color = Color(0xFF657142),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF657142),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = SourceSansRegular.copy(fontSize = 14.sp),
            color = Color.DarkGray
        )
    }
}
