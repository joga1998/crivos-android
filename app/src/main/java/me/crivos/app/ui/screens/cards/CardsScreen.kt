package me.crivos.app.ui.screens.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.domain.algorithm.SM2Engine
import me.crivos.app.domain.algorithm.SM2State
import me.crivos.app.ui.components.CrivosSegmentedTabs
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

data class FlashcardDeckItem(
    val id: String,
    val disciplina: String,
    val frente: String,
    val verso: String
)

@Composable
fun CardsScreen() {
    val c = LocalCrivosColors.current
    val modes = listOf("FILA SM-2 HOJE", "TODOS OS CARDS", "CRIAR CARD")
    var selectedMode by remember { mutableStateOf(0) }

    val deck = remember {
        listOf(
            FlashcardDeckItem(
                "1",
                "DIR. CONSTITUCIONAL",
                "Qual é o prazo decadencial para impetração de Mandado de Segurança?",
                "120 dias, contados da ciência, pelo interessado, do ato impugnado (Art. 23 da Lei 12.016/2009). Natureza decadencial (não se suspende nem se interrompe)."
            ),
            FlashcardDeckItem(
                "2",
                "DIR. ADMINISTRATIVO",
                "Quais são os 5 elementos/requisitos de validade do Ato Administrativo?",
                "CO-FI-FO-MO-OB: Competência, Finalidade, Forma, Motivo e Objeto. Os três primeiros são sempre vinculados; Motivo e Objeto formam o mérito nos atos discricionários."
            ),
            FlashcardDeckItem(
                "3",
                "PORTUGUÊS · FGV",
                "Quando o uso da crase antes de pronomes possessivos femininos é facultativo?",
                "Quando o pronome possessivo feminino estiver no singular e exercer função adjetiva (acompanhando um substantivo explícito): 'Refiro-me a/à minha tese'."
            )
        )
    }

    var currentIdx by remember { mutableStateOf(0) }
    var cardStates by remember {
        mutableStateOf(deck.associate { it.id to SM2State(ef = 2.5, n = 0, iv = 0) })
    }
    var isRevealed by remember { mutableStateOf(false) }

    val currentCard = deck[currentIdx % deck.size]
    val currentState = cardStates[currentCard.id] ?: SM2State()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CrivosUnifiedShell(
            title = "04 · REPETIÇÃO ESPAÇADA SM-2",
            headerTrailing = {
                CrivosTag(text = "FILA: ${deck.size} CARDS", style = CrivosTagStyle.RED_SOLID)
            }
        ) {
            CrivosSegmentedTabs(
                tabs = modes,
                selectedIndex = selectedMode,
                onTabSelected = { selectedMode = it }
            )

            // Telemetria do card atual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CARD ${currentIdx + 1}/${deck.size}",
                    fontSize = 10.5.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = c.inkMuted
                )
                Text(
                    text = "REP: ${currentState.n} · INT: ${currentState.iv}D · EF: ${currentState.ef}",
                    fontSize = 10.5.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = c.inkPrimary
                )
            }
        }

        // Card de Estudo Interativo (.fc)
        Surface(
            color = c.bgCard,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, if (isRevealed) CrivosRed else c.borderSubtle),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { isRevealed = !isRevealed }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CrivosTag(text = currentCard.disciplina, style = CrivosTagStyle.BLUE_REV)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentCard.frente,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = c.inkPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                if (isRevealed) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 18.dp),
                        color = c.borderSubtle
                    )
                    CrivosTag(text = "GABARITO / VERSO", style = CrivosTagStyle.GREEN_OK)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentCard.verso,
                        fontSize = 14.5.sp,
                        color = c.inkPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 21.sp
                    )
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "TOQUE PARA REVELAR O VERSO",
                        fontSize = 10.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = c.inkMuted,
                        letterSpacing = 0.8.sp
                    )
                }
            }
        }

        // Dock de Classificação SM-2 (.shell-dock)
        if (isRevealed) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val gradeCard: (Int) -> Unit = { q ->
                    val res = SM2Engine.nextSM2(currentState, q)
                    cardStates = cardStates + (currentCard.id to SM2State(ef = res.ef, n = res.n, iv = res.iv))
                    isRevealed = false
                    currentIdx = (currentIdx + 1) % deck.size
                }

                SM2GradeButton(
                    modifier = Modifier.weight(1f),
                    label = "ERREI",
                    sub = "1d",
                    color = CrivosRed,
                    onClick = { gradeCard(1) }
                )
                SM2GradeButton(
                    modifier = Modifier.weight(1f),
                    label = "DIFÍCIL",
                    sub = "3d",
                    color = c.yellow,
                    onClick = { gradeCard(3) }
                )
                SM2GradeButton(
                    modifier = Modifier.weight(1f),
                    label = "BOM",
                    sub = "6d",
                    color = c.green,
                    onClick = { gradeCard(4) }
                )
                SM2GradeButton(
                    modifier = Modifier.weight(1f),
                    label = "FÁCIL",
                    sub = "15d",
                    color = c.blue,
                    onClick = { gradeCard(5) }
                )
            }
        } else {
            Button(
                onClick = { isRevealed = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CrivosRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "REVELAR RESPOSTA",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    letterSpacing = 0.6.sp
                )
            }
        }
    }
}

@Composable
private fun SM2GradeButton(
    modifier: Modifier = Modifier,
    label: String,
    sub: String,
    color: Color,
    onClick: () -> Unit
) {
    val c = LocalCrivosColors.current
    Surface(
        onClick = onClick,
        color = c.bgCard,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, color),
        modifier = modifier.height(48.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
            Text(
                text = sub,
                fontSize = 9.5.sp,
                fontFamily = FontFamily.Monospace,
                color = c.inkMuted
            )
        }
    }
}
