package me.crivos.app.ui.screens.crono

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.components.CrivosSegmentedTabs
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

data class CronoDayMission(
    val id: String,
    val data: String,
    val diaSemana: String,
    val fase: String,
    val disciplina: String,
    val topico: String,
    val horas: String,
    val status: String // pending, done, partial, skip
)

@Composable
fun CronoScreen() {
    val c = LocalCrivosColors.current
    val filtros = listOf("TODOS", "HOJE", "PENDENTES", "CONCLUÍDOS")
    var selectedFilter by remember { mutableStateOf(0) }

    var dias by remember {
        mutableStateOf(
            listOf(
                CronoDayMission("1", "09/11", "SEG", "CICLO 1 · TEORIA", "DIR. CONSTITUCIONAL", "Direitos e Garantias Individuais (Art. 5º CF/88)", "3,5h", "done"),
                CronoDayMission("2", "10/11", "TER", "CICLO 1 · LEI SECA", "DIR. ADMINISTRATIVO", "Poderes Administrativos e Atos Administrativos", "3,5h", "done"),
                CronoDayMission("3", "11/11", "QUA", "CICLO 1 · PRÁTICA", "PORTUGUÊS", "Sintaxe de Concordância, Regência e Crase (Banca FGV)", "3,0h", "pending"),
                CronoDayMission("4", "12/11", "QUI", "CICLO 1 · TEORIA", "DIR. PENAL", "Teoria Geral do Crime: Tipicidade e Excludentes", "3,5h", "pending"),
                CronoDayMission("5", "13/11", "SEX", "CICLO 1 · REVISÃO", "REVISÃO SM-2 + QUESTÕES", "Bateria de 50 Questões Comentadas + Fila SM-2", "4,0h", "pending")
            )
        )
    }

    val doneCount = dias.count { it.status == "done" }
    val pct = if (dias.isNotEmpty()) (doneCount * 100) / dias.size else 0

    val filtered = dias.filter {
        when (selectedFilter) {
            1 -> it.data == "10/11"
            2 -> it.status == "pending" || it.status == "partial"
            3 -> it.status == "done"
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CrivosUnifiedShell(
            title = "02 · CRONOGRAMA TÁTICO",
            headerTrailing = {
                CrivosTag(text = "$pct% CONCLUÍDO ($doneCount/${dias.size})", style = CrivosTagStyle.RED_SOLID)
            }
        ) {
            LinearProgressIndicator(
                progress = pct / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CrivosRed,
                trackColor = c.borderSubtle
            )

            CrivosSegmentedTabs(
                tabs = filtros,
                selectedIndex = selectedFilter,
                onTabSelected = { selectedFilter = it }
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { item ->
                val stripeColor = when (item.status) {
                    "done" -> c.green
                    "partial" -> c.yellow
                    else -> CrivosRed
                }

                Surface(
                    color = c.bgCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, c.borderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRect(
                                    color = stripeColor,
                                    topLeft = Offset.Zero,
                                    size = Size(4.dp.toPx(), size.height)
                                )
                            }
                            .padding(start = 15.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${item.data} (${item.diaSemana})",
                                        color = c.inkPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    CrivosTag(text = item.fase, style = CrivosTagStyle.SUBTLE_AR)
                                }
                                Text(
                                    text = "META: ${item.horas}",
                                    color = c.inkMuted,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CrivosTag(text = item.disciplina, style = CrivosTagStyle.BLUE_REV)
                            }

                            Text(
                                text = item.topico,
                                color = if (item.status == "done") c.inkMuted else c.inkPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                val (btnLabel, btnColor) = when (item.status) {
                                    "done" -> "✓ CONCLUÍDO" to c.green
                                    "partial" -> "½ PARCIAL" to c.yellow
                                    else -> "○ MARCAR FEITO" to c.inkPrimary
                                }
                                OutlinedButton(
                                    onClick = {
                                        dias = dias.map {
                                            if (it.id == item.id) {
                                                val nextSt = when (it.status) {
                                                    "pending" -> "done"
                                                    "done" -> "partial"
                                                    else -> "pending"
                                                }
                                                it.copy(status = nextSt)
                                            } else it
                                        }
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = c.bgSubtle,
                                        contentColor = btnColor
                                    ),
                                    border = BorderStroke(1.dp, btnColor.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(
                                        text = btnLabel,
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.ExtraBold
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
