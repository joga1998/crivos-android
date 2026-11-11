package me.crivos.app.ui.screens.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun PerfilScreen(
    userEmail: String,
    userName: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit
) {
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
            title = "PERFIL & CREDENCIAIS DO ALUNO",
            headerTrailing = {
                CrivosTag(text = "// SISTEMA ELITE", style = CrivosTagStyle.RED_SOLID)
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("CANDIDATO CREDENCIADO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = c.inkMuted)
                Text(userName.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = c.inkPrimary)
                Text(userEmail, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = c.inkMuted)
            }

            HorizontalDivider(color = c.borderSubtle)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onToggleTheme,
                    border = BorderStroke(1.dp, c.border),
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                ) {
                    Text(
                        text = if (isDarkTheme) "MODO PAPEL CLARO" else "MODO AMOLED PURO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = c.inkPrimary
                    )
                }

                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrivosRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(9.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                ) {
                    Text("ENCERRAR SESSÃO", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}
