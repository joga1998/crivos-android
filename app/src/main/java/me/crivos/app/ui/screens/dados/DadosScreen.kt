package me.crivos.app.ui.screens.dados

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

@Composable
fun DadosScreen() {
    val c = LocalCrivosColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CrivosUnifiedShell(
            title = "10 · DIAGNÓSTICO & PROTOCOLO 80/20",
            headerTrailing = {
                CrivosTag(text = "SCORE: 84%", style = CrivosTagStyle.GREEN_OK)
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DiagKpiBox(
                    modifier = Modifier.weight(1f),
                    label = "SCORE PROJETADO",
                    value = "84%",
                    sub = "+6% ACIMA DO CORTE",
                    color = c.green
                )
                DiagKpiBox(
                    modifier = Modifier.weight(1f),
                    label = "CORTE ESTIMADO",
                    value = "78%",
                    sub = "BANCA FGV · ELITE",
                    color = CrivosRed
                )
            }

            HorizontalDivider(color = c.borderSubtle)

            Text(
                text = "DESEMPENHO POR DISCIPLINA (INCIDÊNCIA PARETO):",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = c.inkMuted,
                letterSpacing = 0.4.sp
            )

            DisciplineBar("DIREITO CONSTITUCIONAL", 0.88f, "88% ACERTO")
            DisciplineBar("DIREITO ADMINISTRATIVO", 0.82f, "82% ACERTO")
            DisciplineBar("LÍNGUA PORTUGUESA (FGV)", 0.74f, "74% ACERTO")
            DisciplineBar("DIREITO PENAL & PROCESSUAL", 0.91f, "91% ACERTO")
        }
    }
}

@Composable
private fun DiagKpiBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    sub: String,
    color: androidx.compose.ui.graphics.Color
) {
    val c = LocalCrivosColors.current
    Surface(
        color = c.bgSubtle,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, c.border),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = c.inkMuted)
            Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(text = sub, fontSize = 9.5.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = c.inkPrimary)
        }
    }
}

@Composable
private fun DisciplineBar(name: String, progress: Float, label: String) {
    val c = LocalCrivosColors.current
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = c.inkPrimary)
            Text(text = label, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = CrivosRed)
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (progress >= 0.8f) c.green else CrivosRed,
            trackColor = c.borderSubtle
        )
    }
}
