package me.crivos.app.ui.screens.sim

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.components.CrivosSegmentedTabs
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

@Composable
fun SimScreen() {
    val c = LocalCrivosColors.current
    val subTabs = listOf("QUESTÕES OBJETIVAS", "SIMULADOS DISCURSIVOS", "CADERNO DE ERROS")
    var selectedSubTab by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var answered by remember { mutableStateOf(false) }
    val correctOption = 1 // Letra B

    val alternatives = listOf(
        "A) O mandado de segurança coletivo pode ser impetrado por partido político sem representação no Congresso Nacional.",
        "B) Conceder-se-á mandado de segurança para proteger direito líquido e certo, não amparado por habeas corpus ou habeas data.",
        "C) O prazo para impetração do mandado de segurança é prescricional de 180 dias.",
        "D) É cabível mandado de segurança contra lei em tese e atos interna corporis irrestritamente."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CrivosUnifiedShell(
            title = "06 · BATERIA DE QUESTÕES & SIMULADOS",
            headerTrailing = {
                CrivosTag(text = "ACURÁCIA: 79%", style = CrivosTagStyle.GREEN_OK)
            }
        ) {
            CrivosSegmentedTabs(
                tabs = subTabs,
                selectedIndex = selectedSubTab,
                onTabSelected = { selectedSubTab = it }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CrivosTag(text = "BANCA FGV · 2025", style = CrivosTagStyle.SUBTLE_AR)
                    CrivosTag(text = "DIR. CONSTITUCIONAL", style = CrivosTagStyle.BLUE_REV)
                }
                CrivosTag(text = "ESSENCIAL", style = CrivosTagStyle.RED_SOLID)
            }

            Text(
                text = "Acerca dos remédios constitucionais previstos no Art. 5º da Constituição Federal de 1988 e da jurisprudência do Supremo Tribunal Federal, assinale a afirmativa correta:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = c.inkPrimary,
                lineHeight = 20.sp
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                alternatives.forEachIndexed { idx, alt ->
                    val isSelected = selectedOption == idx
                    val borderCol = when {
                        answered && idx == correctOption -> c.green
                        answered && isSelected && idx != correctOption -> CrivosRed
                        isSelected -> CrivosRed
                        else -> c.borderSubtle
                    }
                    val bgCol = when {
                        answered && idx == correctOption -> c.green.copy(alpha = 0.12f)
                        answered && isSelected && idx != correctOption -> CrivosRed.copy(alpha = 0.12f)
                        isSelected -> c.bgElevated
                        else -> c.bgSubtle
                    }

                    Surface(
                        onClick = {
                            if (!answered) selectedOption = idx
                        },
                        color = bgCol,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, borderCol),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = alt,
                            fontSize = 13.sp,
                            color = c.inkPrimary,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            if (answered) {
                Surface(
                    color = c.bgSubtle,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, c.border),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CrivosTag(
                            text = if (selectedOption == correctOption) "✓ RESPOSTA CORRETA · GABARITO B" else "✖ INCORRETA · GABARITO B",
                            style = if (selectedOption == correctOption) CrivosTagStyle.GREEN_OK else CrivosTagStyle.RED_SOLID
                        )
                        Text(
                            text = "Fundamentação: Art. 5º, LXIX da CF/88. O prazo de 120 dias é decadencial (Súmula 632/STF) e exige partido com representação no Congresso Nacional.",
                            fontSize = 12.5.sp,
                            color = c.inkPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (selectedOption != null) answered = true
                    },
                    enabled = selectedOption != null && !answered,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrivosRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                ) {
                    Text("CONFIRMAR RESPOSTA", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold)
                }

                if (answered) {
                    OutlinedButton(
                        onClick = {
                            answered = false
                            selectedOption = null
                        },
                        border = BorderStroke(1.dp, c.border),
                        shape = RoundedCornerShape(9.dp),
                        modifier = Modifier.height(42.dp)
                    ) {
                        Text("PRÓXIMA →", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = c.inkPrimary)
                    }
                }
            }
        }
    }
}
