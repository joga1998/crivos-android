package me.crivos.app.ui.screens.legis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.theme.*

data class Artigo(val numero: String, val texto: String)

@Composable
fun LegisScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val artigos = remember {
        listOf(
            Artigo(
                "Art. 1º",
                "A República Federativa do Brasil, formada pela união indissolúvel dos Estados e Municípios e do Distrito Federal, constitui-se em Estado Democrático de Direito."
            ),
            Artigo(
                "Art. 2º",
                "São Poderes da União, independentes e harmônicos entre si, o Legislativo, o Executivo e o Judiciário."
            ),
            Artigo(
                "Art. 3º",
                "Constituem objetivos fundamentais da República Federativa do Brasil: I - construir uma sociedade livre, justa e solidária; II - garantir o desenvolvimento nacional..."
            ),
            Artigo(
                "Art. 5º",
                "Todos são iguais perante a lei, sem distinção de qualquer natureza, garantindo-se aos brasileiros e aos estrangeiros residentes no País a inviolabilidade do direito à vida, à liberdade, à igualdade, à segurança e à propriedade."
            )
        )
    }

    val filtered = artigos.filter {
        it.numero.contains(searchQuery, ignoreCase = true) || it.texto.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "VADE MECUM // CF/88",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "Constituição da República",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Campo de busca
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por termo ou artigo...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = SurfaceBorder,
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { artigo ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = artigo.numero,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = GoldAccent
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = artigo.texto,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
