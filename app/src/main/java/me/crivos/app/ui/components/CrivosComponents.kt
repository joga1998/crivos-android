package me.crivos.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

/**
 * TopAppBar Tática Mobile do CRIVOS (Fiel a `.crivos-headbar` do mobile.css).
 * Sempre escura (#000000 / #111111) com o quadrado vermelho `.sq-red` + marca `CRIVOS`,
 * chip do concurso e botão de marcos temporais.
 */
@Composable
fun CrivosTopBar(
    concursoTitle: String = "ENGENHARIA DE EDITAIS",
    onBrandClick: () -> Unit,
    onDatesToggle: () -> Unit = {}
) {
    val c = LocalCrivosColors.current
    Surface(
        color = Color(0xFF000000),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        border = BorderStroke(0.5.dp, Color(0xFF1F1F24))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Lado esquerdo: Quadrado vermelho Bauhaus + CRIVOS + Chip do Concurso
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onBrandClick() }
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(11.dp)
                            .background(CrivosRed)
                    )
                    Text(
                        text = "CRIVOS",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.2).sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(1.dp)
                        .background(Color(0xFF2A2A2E))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = concursoTitle.uppercase(),
                    color = Color(0xFFA3A39E),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Lado direito: Botão "+" de Marcos Temporais do Edital (44dp touch target)
            IconButton(
                onClick = onDatesToggle,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Marcos Temporais do Edital",
                    tint = Color(0xFFF4F4F0),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

/**
 * Tag Semântica CRIVOS (`.tag`, `.tag.ok`, `.tag.ar`, `.tag.rev`, `.tag.esc`).
 */
enum class CrivosTagStyle {
    DEFAULT, RED_SOLID, GREEN_OK, BLUE_REV, YELLOW_WARN, SUBTLE_AR
}

@Composable
fun CrivosTag(
    text: String,
    style: CrivosTagStyle = CrivosTagStyle.DEFAULT,
    modifier: Modifier = Modifier
) {
    val c = LocalCrivosColors.current
    val (bg, fg, borderColor) = when (style) {
        CrivosTagStyle.RED_SOLID -> Triple(c.red, Color.White, c.red)
        CrivosTagStyle.GREEN_OK -> Triple(c.green.copy(alpha = 0.15f), c.green, c.green.copy(alpha = 0.4f))
        CrivosTagStyle.BLUE_REV -> Triple(Color.Transparent, c.blue, c.blue)
        CrivosTagStyle.YELLOW_WARN -> Triple(c.yellow, Color(0xFF111111), c.yellow)
        CrivosTagStyle.SUBTLE_AR -> Triple(c.bgElevated, c.inkPrimary, c.border)
        CrivosTagStyle.DEFAULT -> Triple(c.bgSubtle, c.inkMuted, c.borderSubtle)
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Text(
            text = text.uppercase(),
            color = fg,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.4.sp,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
        )
    }
}

/**
 * Contêiner de Tela Unificada (`.app-unified-shell` com `.shell-header` e `.shell-body`).
 */
@Composable
fun CrivosUnifiedShell(
    title: String,
    modifier: Modifier = Modifier,
    headerTrailing: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val c = LocalCrivosColors.current
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = c.bgCard),
        border = BorderStroke(1.dp, c.borderSubtle)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // .shell-header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.bgCard)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    color = c.inkPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                    modifier = Modifier.weight(1f)
                )
                if (headerTrailing != null) {
                    headerTrailing()
                }
            }
            HorizontalDivider(color = c.borderSubtle, thickness = 1.dp)
            // .shell-body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(c.bgCard)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                content = content
            )
        }
    }
}

/**
 * Abas Segmentadas Horizontais (`.morph-group` e `.morph-pill` no mobile.css):
 * Rola horizontalmente com sublinhado inferior `2dp` em Vermelho Crivos (`#D92B2B`) na aba ativa.
 */
@Composable
fun CrivosSegmentedTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val c = LocalCrivosColors.current
    Column(modifier = modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.Transparent,
            contentColor = c.inkPrimary,
            edgePadding = 0.dp,
            divider = {
                HorizontalDivider(color = c.borderSubtle, thickness = 1.dp)
            },
            indicator = { tabPositions ->
                if (selectedIndex in tabPositions.indices) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                        height = 2.dp,
                        color = CrivosRed
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, label ->
                val active = selectedIndex == index
                Tab(
                    selected = active,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = label.uppercase(),
                        fontSize = 10.5.sp,
                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (active) c.inkPrimary else c.inkMuted,
                        letterSpacing = 0.3.sp,
                        modifier = Modifier.padding(horizontal = 11.dp)
                    )
                }
            }
        }
    }
}

/**
 * Item do Grid de Módulos Secundários na Gaveta "Mais" (`.sheet-grid-item`).
 */
data class SheetModuleItem(
    val id: String,
    val num: String,
    val label: String,
    val icon: ImageVector,
    val isEliteOnly: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrivosMoreBottomSheet(
    userEmail: String,
    userName: String,
    isDarkTheme: Boolean,
    currentModuleId: String,
    modules: List<SheetModuleItem>,
    onSelectModule: (String) -> Unit,
    onToggleTheme: () -> Unit,
    onOpenProfile: () -> Unit,
    onDismiss: () -> Unit
) {
    val c = LocalCrivosColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = c.bgCard,
        scrimColor = Color.Black.copy(alpha = 0.58f),
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(c.border)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 28.dp)
        ) {
            // Botão fechar no canto direito (.bottom-sheet-header)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(c.bgSubtle)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = c.inkMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Linha de Perfil + Botão de Tema (.bottom-sheet-user-row)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onOpenProfile() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(c.bgSubtle)
                            .border(1.dp, c.border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName.take(1).uppercase(),
                            color = c.inkPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userName.uppercase(),
                            color = c.inkPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = userEmail,
                            color = c.inkMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Botão Alternar Tema (.sheet-theme-toggle)
                Surface(
                    onClick = onToggleTheme,
                    color = c.bgSubtle,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, c.borderSubtle),
                    modifier = Modifier.height(40.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Alternar Tema",
                            tint = c.inkPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isDarkTheme) "CLARO" else "AMOLED",
                            color = c.inkPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }

            HorizontalDivider(
                color = c.borderSubtle,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Grid 4x2 de Módulos Secundários (.bottom-sheet-grid)
            val rows = modules.chunked(4)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                rows.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { item ->
                            val isActive = item.id == currentModuleId
                            val bg = if (isActive) CrivosRed else c.bgSubtle
                            val fg = if (isActive) Color.White else c.inkPrimary
                            val bd = if (isActive) CrivosRed else c.borderSubtle

                            Surface(
                                onClick = { onSelectModule(item.id) },
                                color = bg,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, bd),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(78.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 4.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        tint = fg,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.label.uppercase(),
                                        color = fg,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        // Preenche células vazias caso a linha tenha menos de 4 itens
                        repeat(4 - rowItems.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
