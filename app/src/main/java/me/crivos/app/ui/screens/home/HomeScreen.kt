package me.crivos.app.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit = {}
) {
    val c = LocalCrivosColors.current
    val scrollState = rememberScrollState()
    var missionStatus by remember { mutableStateOf("pending") } // pending, done, partial, skip

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. WIDGET TÁTICO: TERMÔMETRO 80/20 & NOTA DE CORTE (.home-8020-banner)
        Surface(
            onClick = { onNavigate("dados") },
            color = c.bgCard,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, c.borderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CrivosTag(text = "PROTOCOLO 80/20", style = CrivosTagStyle.GREEN_OK)
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = c.inkMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)) {
                                append("SCORE PROJETADO: ")
                            }
                            withStyle(SpanStyle(color = c.inkPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold)) {
                                append("84% ")
                            }
                            withStyle(SpanStyle(color = c.inkMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)) {
                                append("· CORTE: ")
                            }
                            withStyle(SpanStyle(color = CrivosRed, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold)) {
                                append("78%")
                            }
                        }
                    )
                }

                Button(
                    onClick = { onNavigate("dados") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = c.bgSubtle,
                        contentColor = c.inkPrimary
                    ),
                    border = BorderStroke(1.dp, c.border),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Text(
                        text = "VER ALAVANCAS 80/20 →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }

        // 2. FAIXA DE TELEMETRIA MOBILE (.mobile-telemetry-strip — 6 células)
        Surface(
            color = c.bgCard,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, c.borderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TelemetryCell(
                        modifier = Modifier.weight(1f),
                        label = "CRONO",
                        value = "68%",
                        onClick = { onNavigate("crono") }
                    )
                    VerticalTelemetryDivider(c.borderSubtle)
                    TelemetryCell(
                        modifier = Modifier.weight(1f),
                        label = "ASSUNTOS",
                        value = "29/42",
                        onClick = { onNavigate("textos") }
                    )
                    VerticalTelemetryDivider(c.borderSubtle)
                    TelemetryCell(
                        modifier = Modifier.weight(1f),
                        label = "QUESTÕES",
                        value = "340/500",
                        onClick = { onNavigate("sim") }
                    )
                }
                HorizontalDivider(color = c.borderSubtle, thickness = 1.dp)
                Row(modifier = Modifier.fillMaxWidth()) {
                    TelemetryCell(
                        modifier = Modifier.weight(1f),
                        label = "SIMULADOS",
                        value = "4/6",
                        onClick = { onNavigate("sim") }
                    )
                    VerticalTelemetryDivider(c.borderSubtle)
                    TelemetryCell(
                        modifier = Modifier.weight(1f),
                        label = "SM-2 HOJE",
                        value = "18",
                        valueColor = CrivosRed,
                        onClick = { onNavigate("cards") }
                    )
                    VerticalTelemetryDivider(c.borderSubtle)
                    TelemetryCell(
                        modifier = Modifier.weight(1f),
                        label = "HORAS",
                        value = "64.5h",
                        onClick = { onNavigate("pomo") }
                    )
                }
            }
        }

        // 3. MISSÃO ATIVA DE HOJE (.home-mission-shell)
        CrivosUnifiedShell(
            title = "MISSÃO DE HOJE · 10/11",
            headerTrailing = {
                Text(
                    text = "EM DIA",
                    color = c.green,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        ) {
            // Card de missão com barra lateral esquerda vermelha (border-left: 5px solid var(--crivos-red))
            val leftStripeColor = if (missionStatus == "done") c.green else CrivosRed
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(c.bgSubtle)
                    .border(1.dp, c.border, RoundedCornerShape(10.dp))
                    .drawBehind {
                        drawRect(
                            color = leftStripeColor,
                            topLeft = Offset.Zero,
                            size = Size(5.dp.toPx(), size.height)
                        )
                    }
                    .padding(start = 16.dp, end = 14.dp, top = 14.dp, bottom = 14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Topo do Card de Missão
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CrivosTag(
                                text = "TEORIA + LEI SECA",
                                style = if (missionStatus == "done") CrivosTagStyle.GREEN_OK else CrivosTagStyle.SUBTLE_AR
                            )
                            Text(
                                text = "10/11 (TER)",
                                color = c.inkPrimary,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Text(
                            text = "META: 3,5H",
                            color = c.inkMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Checklist vertical de tópicos separados por '▪' vermelho
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        MissionBulletItem(
                            tag = "DIR. CONSTITUCIONAL",
                            text = "Art. 5º — Direitos e Garantias Individuais e Remédios Constitucionais (Mandado de Segurança e Habeas Corpus)"
                        )
                        MissionBulletItem(
                            tag = "DIR. ADMINISTRATIVO",
                            text = "Poderes da Administração Pública: Poder de Polícia, Hierárquico e Disciplinar (Jurisprudência STF/STJ)"
                        )
                    }

                    // Linha de Ação: CTA Primário (70%) + Ciclar Status (30%)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onNavigate("pomo") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrivosRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(9.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            modifier = Modifier
                                .weight(0.66f)
                                .height(42.dp)
                        ) {
                            Text(
                                text = "INICIAR NO POMODORO →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        val (statusLabel, statusColor) = when (missionStatus) {
                            "done" -> "✓ CONCLUÍDO" to c.green
                            "partial" -> "½ PARCIAL" to c.yellow
                            else -> "○ CONCLUIR" to c.inkPrimary
                        }

                        OutlinedButton(
                            onClick = {
                                missionStatus = when (missionStatus) {
                                    "pending" -> "done"
                                    "done" -> "partial"
                                    else -> "pending"
                                }
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = c.bgCard,
                                contentColor = statusColor
                            ),
                            border = BorderStroke(1.dp, statusColor.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(9.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            modifier = Modifier
                                .weight(0.34f)
                                .height(42.dp)
                        ) {
                            Text(
                                text = statusLabel,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // 4. SEQUÊNCIA IMEDIATA DO CRONOGRAMA (.home-upcoming-block)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "SEQUÊNCIA IMEDIATA DO CRONOGRAMA:",
                    color = c.inkMuted,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UpcomingMissionMiniCard(
                        modifier = Modifier.weight(1f),
                        dateLabel = "11/11 (QUA) · 3,0h",
                        topic = "Língua Portuguesa: Sintaxe de Concordância e Regência (Banca FGV)",
                        onClick = { onNavigate("crono") }
                    )
                    UpcomingMissionMiniCard(
                        modifier = Modifier.weight(1f),
                        dateLabel = "12/11 (QUI) · 3,5h",
                        topic = "Direito Penal: Teoria do Crime, Tipicidade e Excludentes de Ilicitude",
                        onClick = { onNavigate("crono") }
                    )
                }
            }
        }
    }
}

@Composable
private fun TelemetryCell(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color? = null,
    onClick: () -> Unit
) {
    val c = LocalCrivosColors.current
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = c.inkMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = valueColor ?: c.inkPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun VerticalTelemetryDivider(color: Color) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(48.dp)
            .background(color)
    )
}

@Composable
private fun MissionBulletItem(
    tag: String,
    text: String
) {
    val c = LocalCrivosColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "▪",
            color = CrivosRed,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            CrivosTag(text = tag, style = CrivosTagStyle.SUBTLE_AR)
            Text(
                text = text,
                color = c.inkPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun UpcomingMissionMiniCard(
    modifier: Modifier = Modifier,
    dateLabel: String,
    topic: String,
    onClick: () -> Unit
) {
    val c = LocalCrivosColors.current
    Surface(
        onClick = onClick,
        color = c.bgSubtle,
        shape = RoundedCornerShape(9.dp),
        border = BorderStroke(1.dp, c.border),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = dateLabel,
                color = CrivosRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = topic,
                color = c.inkPrimary,
                fontSize = 11.5.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }
    }
}
