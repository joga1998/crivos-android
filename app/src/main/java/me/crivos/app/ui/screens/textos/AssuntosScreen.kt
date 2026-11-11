package me.crivos.app.ui.screens.textos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.components.CrivosSegmentedTabs
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

data class AssuntoItem(
    val id: String,
    val disciplina: String,
    val titulo: String,
    val prioridade: String, // ESSENCIAL, IMPORTANTE, SECUNDÁRIO
    val status: String // pendente, lendo, revisado, dominado
)

@Composable
fun AssuntosScreen() {
    val c = LocalCrivosColors.current
    val disciplinas = listOf("TODAS", "DIR. CONSTITUCIONAL", "DIR. ADMINISTRATIVO", "PORTUGUÊS", "DIR. PENAL")
    var selectedDiscIdx by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    var assuntos by remember {
        mutableStateOf(
            listOf(
                AssuntoItem("1", "DIR. CONSTITUCIONAL", "Direitos e Garantias Fundamentais (Art. 5º ao 17)", "ESSENCIAL", "dominado"),
                AssuntoItem("2", "DIR. CONSTITUCIONAL", "Controle de Constitucionalidade (ADI, ADC, ADPF)", "ESSENCIAL", "revisado"),
                AssuntoItem("3", "DIR. ADMINISTRATIVO", "Atos Administrativos: Requisitos, Atributos e Extinção", "ESSENCIAL", "lendo"),
                AssuntoItem("4", "DIR. ADMINISTRATIVO", "Nova Lei de Licitações e Contratos (Lei 14.133/2021)", "ESSENCIAL", "pendente"),
                AssuntoItem("5", "PORTUGUÊS", "Reescrita de Frases, Regência e Crase (Padrão FGV/Cebraspe)", "ESSENCIAL", "dominado"),
                AssuntoItem("6", "DIR. PENAL", "Teoria Geral do Crime: Tipicidade, Ilicitude e Culpabilidade", "IMPORTANTE", "revisado")
            )
        )
    }

    val filtered = assuntos.filter { item ->
        val matchesDisc = selectedDiscIdx == 0 || item.disciplina == disciplinas[selectedDiscIdx]
        val matchesQuery = searchQuery.isBlank() || item.titulo.contains(searchQuery, ignoreCase = true)
        matchesDisc && matchesQuery
    }

    val dominados = assuntos.count { it.status == "dominado" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CrivosUnifiedShell(
            title = "03 · MATRIZ DE ASSUNTOS DO EDITAL",
            headerTrailing = {
                CrivosTag(
                    text = "$dominados/${assuntos.size} DOMINADOS",
                    style = CrivosTagStyle.BLUE_REV
                )
            }
        ) {
            // Abas segmentadas com sublinhado vermelho (.morph-group)
            CrivosSegmentedTabs(
                tabs = disciplinas,
                selectedIndex = selectedDiscIdx,
                onTabSelected = { selectedDiscIdx = it }
            )

            // Busca rápida compacta
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Filtrar tópico do edital...", fontSize = 13.sp, color = c.inkMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CrivosRed) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CrivosRed,
                    unfocusedBorderColor = c.borderSubtle,
                    focusedContainerColor = c.bgSubtle,
                    unfocusedContainerColor = c.bgSubtle,
                    focusedTextColor = c.inkPrimary,
                    unfocusedTextColor = c.inkPrimary
                ),
                shape = RoundedCornerShape(9.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { item ->
                Surface(
                    color = c.bgCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, c.borderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CrivosTag(text = item.disciplina, style = CrivosTagStyle.SUBTLE_AR)
                            CrivosTag(
                                text = item.prioridade,
                                style = when (item.prioridade) {
                                    "ESSENCIAL" -> CrivosTagStyle.RED_SOLID
                                    "IMPORTANTE" -> CrivosTagStyle.YELLOW_WARN
                                    else -> CrivosTagStyle.DEFAULT
                                }
                            )
                        }

                        Text(
                            text = item.titulo,
                            color = c.inkPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 19.sp
                        )

                        // Seletor de Status (.morph-group.is-status)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val statuses = listOf(
                                "pendente" to "PENDENTE",
                                "lendo" to "EM ESTUDO",
                                "revisado" to "REVISADO",
                                "dominado" to "DOMINADO"
                            )
                            statuses.forEach { (stKey, stLabel) ->
                                val isActive = item.status == stKey
                                Surface(
                                    onClick = {
                                        assuntos = assuntos.map {
                                            if (it.id == item.id) it.copy(status = stKey) else it
                                        }
                                    },
                                    color = if (isActive) CrivosRed.copy(alpha = 0.16f) else c.bgSubtle,
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isActive) CrivosRed else c.borderSubtle
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)
                                    ) {
                                        Text(
                                            text = stLabel,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (isActive) CrivosRed else c.inkMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
