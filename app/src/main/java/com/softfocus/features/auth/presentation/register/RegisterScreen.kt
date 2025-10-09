package com.softfocus.features.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.R
import com.softfocus.core.ui.theme.SoftFocusTheme
import com.softfocus.features.auth.domain.models.UserType
import com.softfocus.features.auth.presentation.di.PresentationModule
import com.softfocus.ui.theme.CrimsonSemiBold
import com.softfocus.ui.theme.Gray828
import com.softfocus.ui.theme.GrayE0
import com.softfocus.ui.theme.Green37
import com.softfocus.ui.theme.Green49
import com.softfocus.ui.theme.SourceSansRegular
import com.softfocus.ui.theme.SourceSansBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val userType by viewModel.userType.collectAsState()
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // Campos adicionales para Psicólogo
    var licenseNumber by remember { mutableStateOf("") }
    var yearsOfExperience by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var specialtiesExpanded by remember { mutableStateOf(false) }
    var selectedSpecialties by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var graduationYear by remember { mutableStateOf("") }

    // Navigate on success
    user?.let {
        onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        // Title
        Text(
            text = "Regístrate",
            style = CrimsonSemiBold,
            fontSize = 40.sp,
            color = Green49,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(1.dp))

        // Panda logo
        Image(
            painter = painterResource(id = R.drawable.soft_panda_black),
            contentDescription = "Soft Focus Logo",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // First name and Last name in row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Green37,
                    unfocusedIndicatorColor = GrayE0
                ),
                placeholder = {
                    Text(
                        text = "Nombre",
                        style = SourceSansRegular,
                        color = Gray828
                    )
                },
                singleLine = true,
                enabled = !isLoading
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Green37,
                    unfocusedIndicatorColor = GrayE0
                ),
                placeholder = {
                    Text(
                        text = "Apellido",
                        style = SourceSansRegular,
                        color = Gray828
                    )
                },
                singleLine = true,
                enabled = !isLoading
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Green37,
                unfocusedIndicatorColor = GrayE0
            ),
            placeholder = {
                Text(
                    text = "Correo",
                    style = SourceSansRegular,
                    color = Gray828
                )
            },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Green37,
                unfocusedIndicatorColor = GrayE0
            ),
            placeholder = {
                Text(
                    text = "Contraseña",
                    style = SourceSansRegular,
                    color = Gray828
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Green37
                )
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (isPasswordVisible) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        tint = Green37
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Green37,
                unfocusedIndicatorColor = GrayE0
            ),
            placeholder = {
                Text(
                    text = "Confirme su contraseña",
                    style = SourceSansRegular,
                    color = Gray828
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Green37
                )
            },
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (isConfirmPasswordVisible) {
                            "Ocultar contraseña"
                        } else {
                            "Mostrar contraseña"
                        },
                        tint = Green37
                    )
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Type Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (!isLoading) expanded = it }
        ) {
            OutlinedTextField(
                value = when (userType) {
                    UserType.GENERAL -> "Usuario General"
                    UserType.PATIENT -> "Paciente"
                    UserType.PSYCHOLOGIST -> "Psicólogo"
                    UserType.ADMIN -> "Administrador"
                    null -> ""
                },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Green37,
                    unfocusedIndicatorColor = GrayE0
                ),
                placeholder = {
                    Text(
                        text = "Tipo de usuario",
                        style = SourceSansRegular,
                        color = Gray828
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Usuario General") },
                    onClick = {
                        viewModel.updateUserType(UserType.GENERAL)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Psicólogo") },
                    onClick = {
                        viewModel.updateUserType(UserType.PSYCHOLOGIST)
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos adicionales para Psicólogo
        if (userType == UserType.PSYCHOLOGIST) {
            // Número de licencia y Años de experiencia en fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = licenseNumber,
                    onValueChange = { licenseNumber = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Green37,
                        unfocusedIndicatorColor = GrayE0
                    ),
                    placeholder = {
                        Text(
                            text = "Número de licencia",
                            style = SourceSansRegular,
                            color = Gray828
                        )
                    },
                    singleLine = true,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = yearsOfExperience,
                    onValueChange = { yearsOfExperience = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Green37,
                        unfocusedIndicatorColor = GrayE0
                    ),
                    placeholder = {
                        Text(
                            text = "Años de experiencia",
                            style = SourceSansRegular,
                            color = Gray828
                        )
                    },
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Región de colegiatura
            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Green37,
                    unfocusedIndicatorColor = GrayE0
                ),
                placeholder = {
                    Text(
                        text = "Región de colegiatura",
                        style = SourceSansRegular,
                        color = Gray828
                    )
                },
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Especialidades (Dropdown)
            ExposedDropdownMenuBox(
                expanded = specialtiesExpanded,
                onExpandedChange = { specialtiesExpanded = !specialtiesExpanded && !isLoading }
            ) {
                OutlinedTextField(
                    value = selectedSpecialties.ifEmpty { "" },
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Green37,
                        unfocusedIndicatorColor = GrayE0
                    ),
                    placeholder = {
                        Text(
                            text = "Especialidades",
                            style = SourceSansRegular,
                            color = Gray828
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = specialtiesExpanded) },
                    enabled = !isLoading
                )

                ExposedDropdownMenu(
                    expanded = specialtiesExpanded,
                    onDismissRequest = { specialtiesExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Clínica") },
                        onClick = {
                            selectedSpecialties = "Clínica"
                            specialtiesExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Educativa") },
                        onClick = {
                            selectedSpecialties = "Educativa"
                            specialtiesExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Organizacional") },
                        onClick = {
                            selectedSpecialties = "Organizacional"
                            specialtiesExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Universidad y Año de graduación en fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = university,
                    onValueChange = { university = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Green37,
                        unfocusedIndicatorColor = GrayE0
                    ),
                    placeholder = {
                        Text(
                            text = "Universidad",
                            style = SourceSansRegular,
                            color = Gray828
                        )
                    },
                    singleLine = true,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = graduationYear,
                    onValueChange = { graduationYear = it },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Green37,
                        unfocusedIndicatorColor = GrayE0
                    ),
                    placeholder = {
                        Text(
                            text = "Año de graduación",
                            style = SourceSansRegular,
                            color = Gray828
                        )
                    },
                    singleLine = true,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // TODO: Aquí irían los botones para subir documentos (Licencia, Diploma, DNI, Certificaciones)
            Text(
                text = "Documentos (pendiente de implementar)",
                style = SourceSansRegular,
                color = Gray828,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Terms checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it },
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Acepto la política de privacidad",
                style = SourceSansRegular,
                fontSize = 14.sp,
                color = Gray828
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Register button
        Button(
            onClick = {
                val fullName = "$firstName $lastName".trim()
                viewModel.register(fullName)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green49
            ),
            enabled = !isLoading &&
                    firstName.isNotEmpty() &&
                    lastName.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    confirmPassword.isNotEmpty() &&
                    acceptedTerms
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Iniciar Sesión",
                    style = SourceSansBold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Login link
        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "Ya tienes cuenta? ",
                color = Color.Gray
            )
            Text(
                text = "Iniciar Sesión",
                color = Green49,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(13.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val viewModel = PresentationModule.getRegisterViewModel()
    SoftFocusTheme {
        RegisterScreen(
            viewModel = viewModel,
            onRegisterSuccess = {},
            onNavigateToLogin = {}
        )
    }
}
