package me.crivos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.data.remote.SupabaseRepository
import me.crivos.app.ui.components.CrivosMoreBottomSheet
import me.crivos.app.ui.components.CrivosTopBar
import me.crivos.app.ui.components.SheetModuleItem
import me.crivos.app.ui.screens.auth.LoginScreen
import me.crivos.app.ui.screens.cards.CardsScreen
import me.crivos.app.ui.screens.crono.CronoScreen
import me.crivos.app.ui.screens.dados.DadosScreen
import me.crivos.app.ui.screens.home.HomeScreen
import me.crivos.app.ui.screens.legis.LegisScreen
import me.crivos.app.ui.screens.perfil.PerfilScreen
import me.crivos.app.ui.screens.pomo.PomoScreen
import me.crivos.app.ui.screens.sim.SimScreen
import me.crivos.app.ui.screens.textos.AssuntosScreen
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.CrivosTheme
import me.crivos.app.ui.theme.LocalCrivosColors

/**
 * 5 abas primárias inferiores fiéis ao `#bottom-nav` do `app.crivos.me`:
 * Início (`home`), Crono (`crono`), Assuntos (`textos`), Prática (`cards`), Mais (`more`).
 */
data class PrimaryTab(
    val id: String,
    val label: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
    private val supabaseRepo = SupabaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            var isLoggedIn by remember { mutableStateOf(supabaseRepo.isAuthenticated) }
            var activeScreen by remember { mutableStateOf("home") }
            var isMoreSheetOpen by remember { mutableStateOf(false) }

            val primaryTabs = remember {
                listOf(
                    PrimaryTab("home", "INÍCIO", Icons.Default.Home),
                    PrimaryTab("crono", "CRONO", Icons.Default.CalendarToday),
                    PrimaryTab("textos", "ASSUNTOS", Icons.Default.Description),
                    PrimaryTab("cards", "PRÁTICA", Icons.Default.Style),
                    PrimaryTab("more", "MAIS", Icons.Default.GridView)
                )
            }

            val sheetModules = remember {
                listOf(
                    SheetModuleItem("legis", "04", "Legislação", Icons.Default.MenuBook),
                    SheetModuleItem("pomo", "05", "Pomodoro", Icons.Default.Timer),
                    SheetModuleItem("sim", "06", "Simulado", Icons.Default.Quiz),
                    SheetModuleItem("dados", "10", "Diagnóstico", Icons.Default.BarChart),
                    SheetModuleItem("cards", "03", "Flashcards", Icons.Default.Style),
                    SheetModuleItem("textos", "02", "Edital", Icons.Default.Description),
                    SheetModuleItem("crono", "01", "Agenda", Icons.Default.CalendarToday),
                    SheetModuleItem("perfil", "12", "Perfil", Icons.Default.Person)
                )
            }

            CrivosTheme(darkTheme = isDarkTheme) {
                val c = LocalCrivosColors.current

                if (!isLoggedIn) {
                    LoginScreen(
                        supabaseRepo = supabaseRepo,
                        onLoginSuccess = { isLoggedIn = true },
                        onDemoMode = { isLoggedIn = true }
                    )
                } else {
                    val userEmail = supabaseRepo.currentSession?.user?.email ?: "candidato@crivos.me"
                    val userName = userEmail.substringBefore("@").ifBlank { "CANDIDATO" }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = c.bgPaper,
                        topBar = {
                            CrivosTopBar(
                                concursoTitle = "ENGENHARIA DE EDITAIS",
                                onBrandClick = { activeScreen = "home" },
                                onDatesToggle = { activeScreen = "crono" }
                            )
                        },
                        bottomBar = {
                            Surface(
                                color = c.bgCard,
                                border = BorderStroke(0.5.dp, c.borderSubtle)
                            ) {
                                NavigationBar(
                                    containerColor = c.bgCard,
                                    tonalElevation = 0.dp
                                ) {
                                    val practiceScreens = setOf("cards", "legis", "pomo", "sim")
                                    val primaryScreenIds = setOf("home", "crono", "textos")

                                    primaryTabs.forEach { tab ->
                                        val isSelected = when (tab.id) {
                                            "more" -> isMoreSheetOpen || (activeScreen !in primaryScreenIds && activeScreen !in practiceScreens)
                                            "cards" -> activeScreen in practiceScreens && !isMoreSheetOpen
                                            else -> activeScreen == tab.id && !isMoreSheetOpen
                                        }

                                        NavigationBarItem(
                                            selected = isSelected,
                                            onClick = {
                                                if (tab.id == "more") {
                                                    isMoreSheetOpen = !isMoreSheetOpen
                                                } else {
                                                    isMoreSheetOpen = false
                                                    activeScreen = tab.id
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = tab.icon,
                                                    contentDescription = tab.label,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            },
                                            label = {
                                                Text(
                                                    text = tab.label,
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 0.4.sp
                                                )
                                            },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = CrivosRed,
                                                selectedTextColor = c.inkPrimary,
                                                unselectedIconColor = c.inkMuted,
                                                unselectedTextColor = c.inkMuted,
                                                indicatorColor = CrivosRed.copy(
                                                    alpha = if (c.isDark) 0.20f else 0.16f
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            color = c.bgPaper
                        ) {
                            when (activeScreen) {
                                "home" -> HomeScreen(onNavigate = { dest -> activeScreen = dest })
                                "crono" -> CronoScreen()
                                "textos" -> AssuntosScreen()
                                "cards" -> CardsScreen()
                                "pomo" -> PomoScreen()
                                "legis" -> LegisScreen()
                                "sim" -> SimScreen()
                                "dados" -> DadosScreen()
                                "perfil" -> PerfilScreen(
                                    userEmail = userEmail,
                                    userName = userName,
                                    isDarkTheme = isDarkTheme,
                                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                                    onLogout = {
                                        supabaseRepo.logout()
                                        isLoggedIn = false
                                    }
                                )
                                else -> HomeScreen(onNavigate = { dest -> activeScreen = dest })
                            }
                        }

                        if (isMoreSheetOpen) {
                            CrivosMoreBottomSheet(
                                userEmail = userEmail,
                                userName = userName,
                                isDarkTheme = isDarkTheme,
                                currentModuleId = activeScreen,
                                modules = sheetModules,
                                onSelectModule = { modId ->
                                    activeScreen = modId
                                    isMoreSheetOpen = false
                                },
                                onToggleTheme = { isDarkTheme = !isDarkTheme },
                                onOpenProfile = {
                                    activeScreen = "perfil"
                                    isMoreSheetOpen = false
                                },
                                onDismiss = { isMoreSheetOpen = false }
                            )
                        }
                    }
                }
            }
        }
    }
}
