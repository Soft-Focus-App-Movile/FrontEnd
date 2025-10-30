package com.softfocus.ui.components.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Announcement
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PersonalInjury
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.softfocus.R
import com.softfocus.core.navigation.Route
import com.softfocus.ui.theme.Green29
import com.softfocus.ui.theme.SourceSansRegular

@Composable
fun PsychologistBottomNav(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedTab by remember { mutableStateOf("home") }

    NavigationBar(
        containerColor = Color.White,
        contentColor = Green29
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(
                        id = if (currentRoute == Route.Home.path || selectedTab == "home")
                            R.drawable.ic_home_rounded_filled
                        else
                            R.drawable.ic_home_rounded_outlined
                    ),
                    contentDescription = "Inicio",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Inicio", fontSize = 12.sp, style = SourceSansRegular) },
            selected = currentRoute == Route.Home.path || selectedTab == "home",
            onClick = {
                selectedTab = "home"
                if (currentRoute != Route.Home.path) {
                    navController.navigate(Route.Home.path) {
                        popUpTo(Route.Home.path) { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Green29,
                selectedTextColor = Green29,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Green29.copy(alpha = 0.12f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.PersonalInjury,
                    contentDescription = "Pacientes",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Pacientes", fontSize = 12.sp, style = SourceSansRegular) },
            selected = selectedTab == "pacientes",
            onClick = { selectedTab = "pacientes" },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Green29,
                selectedTextColor = Green29,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Green29.copy(alpha = 0.12f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Announcement,
                    contentDescription = "Alertas",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Alertas", fontSize = 12.sp, style = SourceSansRegular) },
            selected = selectedTab == "alertas",
            onClick = { selectedTab = "alertas" },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Green29,
                selectedTextColor = Green29,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Green29.copy(alpha = 0.12f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Bookmarks,
                    contentDescription = "Biblioteca",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Biblioteca", fontSize = 12.sp, style = SourceSansRegular) },
            selected = selectedTab == "biblioteca",
            onClick = { selectedTab = "biblioteca" },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Green29,
                selectedTextColor = Green29,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Green29.copy(alpha = 0.12f)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == Route.Profile.path || selectedTab == "perfil") Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("Perfil", fontSize = 12.sp, style = SourceSansRegular) },
            selected = currentRoute == Route.Profile.path || selectedTab == "perfil",
            onClick = {
                selectedTab = "perfil"
                if (currentRoute != Route.Profile.path) {
                    navController.navigate(Route.Profile.path)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Green29,
                selectedTextColor = Green29,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Green29.copy(alpha = 0.12f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PsychologistBottomNavPreview() {
    val navController = rememberNavController()
    PsychologistBottomNav(navController)
}
