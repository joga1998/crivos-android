package me.crivos.app.ui.screens.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.domain.algorithm.SM2Engine
import me.crivos.app.domain.algorithm.SM2State
import me.crivos.app.ui.theme.*

@Composable
fun CardsScreen() {
    var cardState by remember { mutableStateOf(SM2State(ef = 2.5, n = 0, iv = 0)) }
    var isRevealed by remember { mutableStateOf(false) }
    var lastFeedback by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Topo com métricas do card
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "REPETIÇÕES: ${cardState.n}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
            Text(
                text = "INTERVALO: ${cardState.iv}d",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary
            )
            Text(
                text = "EASE: ${cardState.ef}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }

        // Card central interativo (Frente / Verso)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 24.dp)
                .clickable { isRevealed = !isRevealed },
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Direito Constitucional",
                    fontSize = 12.sp,
                    color = EmeraldPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Qual é o prazo constitucional para impetração de Mandado de Segurança?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                if (isRevealed) {
                    Divider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = SurfaceBorder
                    )
                    Text(
                        text = "120 dias, a contar da ciência formal do ato impugnado (Art. 23 da Lei nº 12.016/2009).",
                        fontSize = 16.sp,
                        color = GoldAccent,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Toque para ver a resposta",
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Ações de classificação SM-2
        if (isRevealed) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Como foi a resposta?",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RatingButton(
                        modifier = Modifier.weight(1f),
                        text = "Errei",
                        color = CrimsonDanger,
                        onClick = {
                            val res = SM2Engine.nextSM2(cardState, 1)
                            cardState = SM2State(ef = res.ef, n = res.n, iv = res.iv)
                            isRevealed = false
                            lastFeedback = "Card agendado para amanhã."
                        }
                    )
                    RatingButton(
                        modifier = Modifier.weight(1f),
                        text = "Difícil",
                        color = GoldAccent,
                        onClick = {
                            val res = SM2Engine.nextSM2(cardState, 3)
                            cardState = SM2State(ef = res.ef, n = res.n, iv = res.iv)
                            isRevealed = false
                            lastFeedback = "Próxima revisão em ${res.iv} dias."
                        }
                    )
                    RatingButton(
                        modifier = Modifier.weight(1f),
                        text = "Bom",
                        color = EmeraldPrimary,
                        onClick = {
                            val res = SM2Engine.nextSM2(cardState, 4)
                            cardState = SM2State(ef = res.ef, n = res.n, iv = res.iv)
                            isRevealed = false
                            lastFeedback = "Próxima revisão em ${res.iv} dias."
                        }
                    )
                    RatingButton(
                        modifier = Modifier.weight(1f),
                        text = "Fácil",
                        color = BlueInfo,
                        onClick = {
                            val res = SM2Engine.nextSM2(cardState, 5)
                            cardState = SM2State(ef = res.ef, n = res.n, iv = res.iv)
                            isRevealed = false
                            lastFeedback = "Próxima revisão em ${res.iv} dias."
                        }
                    )
                }
            }
        } else {
            Button(
                onClick = { isRevealed = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Virar Card", color = AmoledBackground, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun RatingButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
        border = androidx.compose.foundation.BorderStroke(1.dp, color),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
