package com.softfocus.features.home.presentation.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.res.painterResource
import com.softfocus.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.softfocus.core.data.local.UserSession
import com.softfocus.core.utils.LocationHelper
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softfocus.ui.theme.CrimsonSemiBold
import com.softfocus.ui.theme.Gray828
import com.softfocus.ui.theme.Green49
import com.softfocus.ui.theme.Green65
import com.softfocus.ui.theme.GreenEC
import com.softfocus.ui.theme.SourceSansRegular
import com.softfocus.ui.theme.YellowCB9D
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.softfocus.features.notifications.presentation.list.NotificationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralHomeScreen(
    onNavigateToNotifications: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var locationText by remember { mutableStateOf("Lima, Peru") }

    // Obtener información del usuario
    val userSession = remember { UserSession(context) }
    val currentUser = remember { userSession.getUser() }
    val userName = remember {
        currentUser?.fullName?.split(" ")?.firstOrNull() ?: "Usuario"
    }
    val notificationsViewModel: NotificationsViewModel = hiltViewModel()
    val notificationsState by notificationsViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        scope.launch {
            if (LocationHelper.hasLocationPermission(context)) {
                val location = LocationHelper.getCurrentLocation(context)
                locationText = if (location != null) {
                    LocationHelper.getCityAndCountry(context, location)
                } else {
                    "Lima, Peru"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location_pin),
                            contentDescription = null,
                            tint = Green49,
                            modifier = Modifier.size(23.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = locationText,
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (notificationsState.unreadCount > 0) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = if (notificationsState.unreadCount > 99) "99+"
                                        else notificationsState.unreadCount.toString(),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNavigateToNotifications) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_notification_bell),
                                contentDescription = "Notificaciones",
                                tint = Green49,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text(
                            text = "Necesitas ayuda profesional?",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .shadow(4.dp, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF6B8E6F))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filtros",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hola $userName ,",
                style = CrimsonSemiBold,
                fontSize = 24.sp,
                color = Green65,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "¿Cómo te sientes hoy?",
                style = CrimsonSemiBold,
                fontSize = 24.sp,
                color = Green65,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GreenEC),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .padding(end = 80.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Registra tu estado de ánimo",
                                style = CrimsonSemiBold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = YellowCB9D
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Registrar ahora",
                                    style = SourceSansRegular,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                // Koala image overlapping the card
                Image(
                    painter = painterResource(id = R.drawable.koala_focus),
                    contentDescription = "Koala",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = 35.dp, y = (-30).dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recomendaciones",
                    style = CrimsonSemiBold,
                    fontSize = 20.sp,
                    color = Gray828
                )
                TextButton(onClick = { }) {
                    Text(
                        text = "ver todas",
                        style = SourceSansRegular,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(mockRecommendations) { recommendation ->
                    RecommendationCard(recommendation)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFC5D9A4))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Has registrado 4 días\nesta semana",
                        style = SourceSansRegular,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF5F5F5))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Llevas 3 días sintiéndote mal,",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray828
                        )
                        Text(
                            text = "¿necesitas ayuda?",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray828
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC5D9A4)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Hablar con IA",
                                style = SourceSansRegular,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            )
                        ) {
                            Text(
                                text = "Buscar Psicólogo",
                                style = SourceSansRegular,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RecommendationCard(recommendation: Recommendation) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recommendation.action,
                style = SourceSansRegular,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray828
            )
            Text(
                text = recommendation.category,
                style = SourceSansRegular,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

data class Recommendation(
    val action: String,
    val category: String
)

val mockRecommendations = listOf(
    Recommendation("Ver", "Soul"),
    Recommendation("Escuchar", "Música Acústica"),
    Recommendation("Meditar", "Prana")
)

@Preview(showBackground = true)
@Composable
fun GeneralHomeScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Saludo
        Text(
            text = "Hola Laura ,",
            style = CrimsonSemiBold,
            fontSize = 24.sp,
            color = Green65
        )
        Text(
            text = "¿Cómo te sientes hoy?",
            style = CrimsonSemiBold,
            fontSize = 24.sp,
            color = Green65
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card de registro de ánimo
        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GreenEC),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .padding(end = 30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Registra tu estado de ánimo",
                            style = CrimsonSemiBold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = YellowCB9D
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Registrar ahora",
                                style = SourceSansRegular,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Koala image overlapping the card
            Image(
                painter = painterResource(id = R.drawable.koala_focus),
                contentDescription = "Koala",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 35.dp, y = (-30).dp)
            )
        }
    }
}
