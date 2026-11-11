package me.crivos.app.ui.screens.pomo

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.crivos.app.ui.theme.*

@Composable
fun PomoScreen() {
    val totalTimeSeconds = 25 * 60
    var timeLeftSeconds by remember { mutableStateOf(totalTimeSeconds) }
    var isRunning by remember { mutableStateOf(false) }

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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "POMODORO CRIVOS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "Bloco de Foco Líquido",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        // Indicador circular de progresso
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Trilho de fundo
                drawArc(
                    color = SurfaceBorder,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
                // Arco de progresso
                drawArc(
                    color = EmeraldPrimary,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedTime,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isRunning) "EM EXECUÇÃO" else "PAUSADO",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRunning) EmeraldPrimary else TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Controles de Play / Pause / Reset
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    isRunning = false
                    timeLeftSeconds = totalTimeSeconds
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Resetar",
                    tint = TextSecondary
                )
            }

            Button(
                onClick = { isRunning = !isRunning },
                modifier = Modifier
                    .height(56.dp)
                    .width(160.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) CrimsonDanger else EmeraldPrimary
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = AmoledBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRunning) "Pausar" else "Iniciar",
                    color = AmoledBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
