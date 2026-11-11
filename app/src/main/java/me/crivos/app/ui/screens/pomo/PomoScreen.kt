package me.crivos.app.ui.screens.pomo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.crivos.app.ui.components.CrivosSegmentedTabs
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

@Composable
fun PomoScreen() {
    val c = LocalCrivosColors.current
    val phases = listOf("FOCO · 25M", "PAUSA CURTA · 5M", "PAUSA LONGA · 15M")
    var selectedPhase by remember { mutableStateOf(0) }

    val phaseDurations = listOf(25 * 60, 5 * 60, 15 * 60)
    var timeLeftSeconds by remember(selectedPhase) { mutableStateOf(phaseDurations[selectedPhase]) }
    var isRunning by remember { mutableStateOf(false) }

    val totalTimeSeconds = phaseDurations[selectedPhase]

    LaunchedEffect(isRunning, timeLeftSeconds) {
        if (isRunning && timeLeftSeconds > 0) {
            delay(1000L)
            timeLeftSeconds -= 1
        } else if (timeLeftSeconds == 0) {
            isRunning = false
        }
    }

    val minutes = timeLeftSeconds / 60
    val seconds = timeLeftSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val progress = (totalTimeSeconds - timeLeftSeconds).toFloat() / totalTimeSeconds.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        CrivosUnifiedShell(
            title = "05 · CRONÔMETRO POMODORO TÁTICO",
            headerTrailing = {
                CrivosTag(
                    text = if (isRunning) "EM EXECUÇÃO" else "AGUARDANDO",
                    style = if (isRunning) CrivosTagStyle.RED_SOLID else CrivosTagStyle.DEFAULT
                )
            }
        ) {
            CrivosSegmentedTabs(
                tabs = phases,
                selectedIndex = selectedPhase,
                onTabSelected = {
                    isRunning = false
                    selectedPhase = it
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 22.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = c.borderSubtle,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Square)
                        )
                        drawArc(
                            color = CrivosRed,
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Square)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // .pomo-headline-tag
                        CrivosTag(
                            text = when (selectedPhase) {
                                0 -> "BLOCO DE FOCO"
                                1 -> "RECUPERAÇÃO"
                                else -> "CICLO COMPLETO"
                            },
                            style = CrivosTagStyle.RED_SOLID
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // #tm.timer (48sp font-mono)
                        Text(
                            text = formattedTime,
                            fontSize = 48.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            color = c.inkPrimary,
                            letterSpacing = (-1.5).sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier
                        .weight(0.7f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrivosRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) "PAUSAR CICLO" else "INICIAR FOCO",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }

                OutlinedButton(
                    onClick = {
                        isRunning = false
                        timeLeftSeconds = totalTimeSeconds
                    },
                    modifier = Modifier
                        .weight(0.3f)
                        .height(44.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = c.bgSubtle,
                        contentColor = c.inkPrimary
                    ),
                    border = BorderStroke(1.dp, c.border),
                    shape = RoundedCornerShape(9.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reiniciar",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
