package me.crivos.app.ui.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.crivos.app.data.remote.SupabaseRepository
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

@Composable
fun LoginScreen(
    supabaseRepo: SupabaseRepository,
    onLoginSuccess: () -> Unit,
    onDemoMode: () -> Unit = {}
) {
    val c = LocalCrivosColors.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = c.bgCard,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, c.borderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Marca CRIVOS com quadrado vermelho Bauhaus
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(CrivosRed)
                    )
                    Text(
                        text = "CRIVOS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = c.inkPrimary,
                        letterSpacing = (-0.4).sp
                    )
                    CrivosTag(text = "// SISTEMA OPERACIONAL", style = CrivosTagStyle.SUBTLE_AR)
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "ACESSO AO PAINEL TÁTICO",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = c.inkPrimary
                    )
                    Text(
                        text = "Autentique-se com suas credenciais de aluno (app.crivos.me)",
                        fontSize = 12.5.sp,
                        color = c.inkMuted
                    )
                }

                // Campo de E-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorMessage = null },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("E-MAIL DO CANDIDATO", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                    leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, tint = CrivosRed) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrivosRed,
                        unfocusedBorderColor = c.border,
                        focusedContainerColor = c.bgSubtle,
                        unfocusedContainerColor = c.bgSubtle,
                        focusedTextColor = c.inkPrimary,
                        unfocusedTextColor = c.inkPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                // Campo de Senha
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("SENHA DE ACESSO", fontSize = 11.sp, fontFamily = FontFamily.Monospace) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CrivosRed) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrivosRed,
                        unfocusedBorderColor = c.border,
                        focusedContainerColor = c.bgSubtle,
                        unfocusedContainerColor = c.bgSubtle,
                        focusedTextColor = c.inkPrimary,
                        unfocusedTextColor = c.inkPrimary
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage.orEmpty(),
                        color = CrivosRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Informe seu e-mail e senha de acesso."
                            return@Button
                        }
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            val result = supabaseRepo.loginWithPassword(email.trim(), password)
                            isLoading = false
                            result.onSuccess {
                                onLoginSuccess()
                            }.onFailure { ex ->
                                errorMessage = ex.message ?: "Falha na autenticação. Verifique suas credenciais."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrivosRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "LIBERAR PAINEL TÁTICO →",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.5.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = onDemoMode,
                    border = BorderStroke(1.dp, c.border),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                ) {
                    Text(
                        text = "ACESSAR EM MODO DEMONSTRAÇÃO",
                        color = c.inkMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
