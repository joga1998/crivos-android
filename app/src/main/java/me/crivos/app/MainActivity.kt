package me.crivos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import me.crivos.app.data.remote.SupabaseRepository
import me.crivos.app.ui.screens.auth.LoginScreen
import me.crivos.app.ui.screens.cards.CardsScreen
import me.crivos.app.ui.screens.crono.CronoScreen
import me.crivos.app.ui.screens.home.HomeScreen
import me.crivos.app.ui.screens.legis.LegisScreen
import me.crivos.app.ui.screens.pomo.PomoScreen
import me.crivos.app.ui.theme.AmoledBackground
import me.crivos.app.ui.theme.CrivosTheme
import me.crivos.app.ui.theme.EmeraldPrimary

enum class AppDestination(val label: String, val icon: ImageVector) {
    HOME("Início", Icons.Default.Home),
    CRONO("Crono", Icons.Default.CalendarToday),
    CARDS("Flashcards", Icons.Default.FitnessCenter),
    POMO("Pomodoro", Icons.Default.Timer),
    LEGIS("Legislação", Icons.Default.MenuBook)
}

class MainActivity : ComponentActivity() {
    private val supabaseRepo = SupabaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrivosTheme {
                var isLoggedIn by remember { mutableStateOf(supabaseRepo.isAuthenticated) }
                var currentDestination by remember { mutableStateOf(AppDestination.HOME) }

                if (!isLoggedIn) {
                    LoginScreen(
                        supabaseRepo = supabaseRepo,
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = AmoledBackground,
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                AppDestination.values().forEach { destination ->
                                    NavigationBarItem(
                                        selected = currentDestination == destination,
                                        onClick = { currentDestination = destination },
                                        icon = {
                                            Icon(
                                                imageVector = destination.icon,
                                                contentDescription = destination.label
                                            )
                                        },
                                        label = { Text(destination.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = EmeraldPrimary,
                                            selectedTextColor = EmeraldPrimary,
                                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            color = AmoledBackground
                        ) {
                            when (currentDestination) {
                                AppDestination.HOME -> HomeScreen()
                                AppDestination.CRONO -> CronoScreen()
                                AppDestination.CARDS -> CardsScreen()
                                AppDestination.POMO -> PomoScreen()
                                AppDestination.LEGIS -> LegisScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
