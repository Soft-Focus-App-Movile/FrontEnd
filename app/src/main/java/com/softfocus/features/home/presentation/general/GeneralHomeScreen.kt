package com.softfocus.features.home.presentation.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
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
import com.softfocus.ui.theme.Gray787
import com.softfocus.ui.theme.Gray828
import com.softfocus.ui.theme.Green29
import com.softfocus.ui.theme.Green49
import com.softfocus.ui.theme.Green65
import com.softfocus.ui.theme.GreenEC
import com.softfocus.ui.theme.SourceSansRegular
import com.softfocus.ui.theme.SourceSansSemiBold
import com.softfocus.ui.theme.YellowCB9D
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.request.crossfade
import com.softfocus.features.notifications.presentation.list.NotificationsViewModel
import com.softfocus.features.library.domain.models.ContentItem
import com.softfocus.features.library.presentation.di.libraryViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralHomeScreen(
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToLibrary: () -> Unit = {},
    onNavigateToContentDetail: (String) -> Unit = {},
    viewModel: GeneralHomeViewModel = libraryViewModel { GeneralHomeViewModel(it) }
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

    // Estado de las recomendaciones
    val recommendationsState by viewModel.recommendationsState.collectAsState()

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
                    .padding(horizontal = 24.dp)
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

            Spacer(modifier = Modifier.height(10.dp))

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
                    color = Green65
                )
                TextButton(onClick = onNavigateToLibrary) {
                    Text(
                        text = "ver todas",
                        style = SourceSansSemiBold,
                        fontSize = 14.sp,
                        color = Gray787
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Contenido de recomendaciones según el estado
            when (val state = recommendationsState) {
                is RecommendationsState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Green49)
                    }
                }
                is RecommendationsState.Success -> {
                    RecommendationsCarousel(
                        recommendations = state.recommendations,
                        onContentClick = onNavigateToContentDetail
                    )
                }
                is RecommendationsState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay recomendaciones disponibles",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray787
                        )
                    }
                }
                is RecommendationsState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error al cargar recomendaciones",
                            style = SourceSansRegular,
                            fontSize = 14.sp,
                            color = Gray787
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { viewModel.retry() }) {
                            Text(
                                text = "Reintentar",
                                style = SourceSansSemiBold,
                                fontSize = 14.sp,
                                color = Green49
                            )
                        }
                    }
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

/**
 * Carrusel de recomendaciones con auto-scroll continuo y suave
 */
@Composable
fun RecommendationsCarousel(
    recommendations: List<ContentItem>,
    onContentClick: (String) -> Unit
) {
    val context = LocalContext.current

    // Estado para controlar el scroll
    val lazyListState = androidx.compose.foundation.lazy.rememberLazyListState()

    // Detectar si el usuario está haciendo scroll manual
    val isUserScrolling = lazyListState.isScrollInProgress
    var lastUserInteraction by remember { mutableStateOf(0L) }

    // Detectar interacción del usuario
    LaunchedEffect(isUserScrolling) {
        if (isUserScrolling) {
            lastUserInteraction = System.currentTimeMillis()
        }
    }

    // Auto-scroll continuo muy lento y suave
    LaunchedEffect(recommendations) {
        if (recommendations.isNotEmpty()) {
            var totalScrollOffset = 0

            while (true) {
                delay(30L) // Delay para suavidad (30ms entre cada movimiento)

                try {
                    // Pausar auto-scroll si el usuario interactuó recientemente (últimos 3 segundos)
                    val timeSinceInteraction = System.currentTimeMillis() - lastUserInteraction
                    if (timeSinceInteraction > 3000 && !isUserScrolling) {
                        totalScrollOffset += 1

                        // Calcular índice y offset basados en el scroll total
                        val itemWidth = 152 // 140 (ancho card) + 12 (spacing)
                        val currentIndex = (totalScrollOffset / itemWidth) % recommendations.size
                        val currentOffset = totalScrollOffset % itemWidth

                        // Scroll suave sin saltos
                        lazyListState.scrollToItem(currentIndex, currentOffset)
                    }
                } catch (e: Exception) {
                    // Ignorar errores de scroll
                }
            }
        }
    }

    LazyRow(
        state = lazyListState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(recommendations) { content ->
            RecommendationCard(
                content = content,
                onClick = { onContentClick(content.externalId) },
                context = context
            )
        }
    }
}

/**
 * Card de recomendación con imagen real y botón Ver
 */
@Composable
fun RecommendationCard(
    content: ContentItem,
    onClick: () -> Unit,
    context: android.content.Context
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Imagen del contenido
                val imageUrl = content.posterUrl ?: content.backdropUrl ?: content.thumbnailUrl ?: content.photoUrl

                if (imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = content.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Título del contenido
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = content.title,
                        style = SourceSansRegular,
                        fontSize = if (content.title.length > 30) 10.sp else 12.sp,
                        color = Green29,
                        maxLines = 2,
                        lineHeight = if (content.title.length > 30) 12.sp else 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Botón Ver
                    Button(
                        onClick = onClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = YellowCB9D
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Ver",
                            style = SourceSansRegular,
                            fontSize = 11.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
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
