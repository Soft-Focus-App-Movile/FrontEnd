package com.softfocus.features.profile.presentation.psychologist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.softfocus.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonalInfoScreen(
    onNavigateBack: () -> Unit,
    viewModel: PsychologistProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Track original values to detect changes
    var originalFirstName by remember { mutableStateOf("") }
    var originalAge by remember { mutableStateOf("") }
    var originalBio by remember { mutableStateOf("") }

    // Initialize with profile data
    LaunchedEffect(profile) {
        profile?.let {
            val name = it.firstName ?: it.fullName.substringBefore(" ")
            val calculatedAge = calculateAge(it.dateOfBirth ?: "")?.toString() ?: ""
            val description = it.bio ?: it.professionalBio ?: ""

            firstName = name
            age = calculatedAge
            bio = description
            email = it.email
            phone = it.phone ?: ""

            // Store original values
            originalFirstName = name
            originalAge = calculatedAge
            originalBio = description
        }
    }

    // Check if there are changes
    val hasChanges = firstName != originalFirstName ||
                     age != originalAge ||
                     bio != originalBio

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar información Personal",
                        style = CrimsonSemiBold,
                        color = Green37,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 48.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Image (placeholder)
            Surface(
                shape = RoundedCornerShape(50),
                color = GreenEB2,
                modifier = Modifier.size(100.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nombre",
                    style = SourceSansRegular,
                    fontSize = 16.sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = GreenEB2,
                        focusedContainerColor = GreenEB2,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Green37
                    ),
                    textStyle = SourceSansRegular,
                    singleLine = true
                )
            }

            // Age Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Edad",
                    style = SourceSansRegular,
                    fontSize = 16.sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = GreenEB2,
                        focusedContainerColor = GreenEB2,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Green37
                    ),
                    textStyle = SourceSansRegular,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Description Field
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Descripción",
                    style = SourceSansRegular,
                    fontSize = 16.sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = GreenEB2,
                        focusedContainerColor = GreenEB2,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Green37
                    ),
                    textStyle = SourceSansRegular,
                    maxLines = 6
                )
            }

            // Contact Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Contacto",
                    style = SourceSansRegular,
                    fontSize = 16.sp,
                    color = Yellow7E
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Email
                ContactItem(
                    icon = Icons.Default.Email,
                    text = email
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Phone
                ContactItem(
                    icon = Icons.Default.Phone,
                    text = phone
                )

                Spacer(modifier = Modifier.height(8.dp))

                // WhatsApp
                ContactItem(
                    icon = Icons.Default.Chat,
                    text = "Whatsapp"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel Button
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Black,
                        containerColor = GreenEB2
                    ),
                    border = null,
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        style = SourceSansRegular,
                        fontSize = 14.sp
                    )
                }

                // Save Button
                Button(
                    onClick = {
                        // TODO: Implement save functionality
                        onNavigateBack()
                    },
                    enabled = hasChanges,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowCB9C,
                        contentColor = Black,
                        disabledContainerColor = GrayD9,
                        disabledContentColor = Gray767
                    ),
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "Guardar",
                        style = SourceSansRegular,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Yellow7E,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            style = SourceSansRegular,
            fontSize = 14.sp,
            color = Yellow7E
        )
    }
}

private fun calculateAge(dateOfBirth: String): Int? {
    return try {
        val formatter = java.time.format.DateTimeFormatter.ISO_DATE_TIME
        val birthDate = java.time.LocalDate.parse(dateOfBirth, formatter)
        val currentDate = java.time.LocalDate.now()
        java.time.Period.between(birthDate, currentDate).years
    } catch (e: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun EditPersonalInfoScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 48.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Image
        Surface(
            shape = RoundedCornerShape(50),
            color = GreenEB2,
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    tint = White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Name Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Nombre",
                style = SourceSansRegular,
                fontSize = 16.sp,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Juan",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GreenEB2,
                    focusedContainerColor = GreenEB2,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Green37
                ),
                textStyle = SourceSansRegular,
                singleLine = true
            )
        }

        // Age Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Edad",
                style = SourceSansRegular,
                fontSize = 16.sp,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "35",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GreenEB2,
                    focusedContainerColor = GreenEB2,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Green37
                ),
                textStyle = SourceSansRegular,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        // Description Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Descripción",
                style = SourceSansRegular,
                fontSize = 16.sp,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = "Psicólogo clínico especializado en terapia cognitivo-conductual...",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GreenEB2,
                    focusedContainerColor = GreenEB2,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Green37
                ),
                textStyle = SourceSansRegular,
                maxLines = 6
            )
        }

        // Contact Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contacto",
                style = SourceSansRegular,
                fontSize = 16.sp,
                color = Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            ContactItem(
                icon = Icons.Default.Email,
                text = "drjuan@email.com"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactItem(
                icon = Icons.Default.Phone,
                text = "+51 987 654 321"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactItem(
                icon = Icons.Default.Chat,
                text = "Whatsapp"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Black,
                    containerColor = GreenEB2
                ),
                border = null,
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Cancelar",
                    style = SourceSansRegular,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = { },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YellowCB9C,
                    contentColor = Black
                ),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Guardar",
                    style = SourceSansRegular,
                    fontSize = 14.sp
                )
            }
        }
    }
}
