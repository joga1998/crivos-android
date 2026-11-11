package me.crivos.app.ui.screens.legis

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.components.CrivosSegmentedTabs
import me.crivos.app.ui.components.CrivosTag
import me.crivos.app.ui.components.CrivosTagStyle
import me.crivos.app.ui.components.CrivosUnifiedShell
import me.crivos.app.ui.theme.CrivosRed
import me.crivos.app.ui.theme.LocalCrivosColors

data class Artigo(
    val diploma: String,
    val numero: String,
    val incidencia: String,
    val texto: String
)

@Composable
fun LegisScreen() {
    val c = LocalCrivosColors.current
    val diplomas = listOf("CF/88", "LEI 14.133", "LEI 9.784", "LEI 8.112", "CP")
    var selectedDiploma by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val artigos = remember {
        listOf(
            Artigo(
                "CF/88",
                "Art. 5º, XI",
                "INCIDÊNCIA CRÍTICA",
                "A casa é asilo inviolável do indivíduo, ninguém nela podendo penetrar sem consentimento do morador, salvo em caso de flagrante delito ou desastre, ou para prestar socorro, ou, durante o dia, por determinação judicial."
            ),
            Artigo(
                "CF/88",
                "Art. 37, § 6º",
                "INCIDÊNCIA CRÍTICA",
                "As pessoas jurídicas de direito público e as de direito privado prestadoras de serviços públicos responderão pelos danos que seus agentes, nessa qualidade, causarem a terceiros, assegurado o direito de regresso contra o responsável nos casos de dolo ou culpa."
            ),
            Artigo(
                "LEI 14.133",
                "Art. 75",
                "ALTA INCIDÊNCIA",
                "É dispensável a licitação para contratação que envolva valores inferiores a R$ 100.000,00 (cem mil reais), no caso de obras e serviços de engenharia ou de serviços de manutenção de veículos automotores."
            ),
            Artigo(
                "LEI 9.784",
                "Art. 54",
                "ESSENCIAL",
                "O direito da Administração de anular os atos administrativos de que decorram efeitos favoráveis para os destinatários decai em cinco anos, contados da data em que foram praticados, salvo comprovada má-fé."
            )
        )
    }

    val filtered = artigos.filter {
        (selectedDiploma == 0 || it.diploma == diplomas[selectedDiploma]) &&
        (searchQuery.isBlank() || it.numero.contains(searchQuery, ignoreCase = true) || it.texto.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(c.bgPaper)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CrivosUnifiedShell(
            title = "04 · LEGISLAÇÃO SECA GRIFADA",
            headerTrailing = {
                CrivosTag(text = "VADE MECUM TÁTICO", style = CrivosTagStyle.RED_SOLID)
            }
        ) {
            CrivosSegmentedTabs(
                tabs = diplomas,
                selectedIndex = selectedDiploma,
                onTabSelected = { selectedDiploma = it }
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por artigo ou palavra-chave...", fontSize = 13.sp, color = c.inkMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CrivosRed) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CrivosRed,
                    unfocusedBorderColor = c.borderSubtle,
                    focusedContainerColor = c.bgSubtle,
                    unfocusedContainerColor = c.bgSubtle,
                    focusedTextColor = c.inkPrimary,
                    unfocusedTextColor = c.inkPrimary
                ),
                shape = RoundedCornerShape(9.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { artigo ->
                Surface(
                    color = c.bgCard,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, c.borderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CrivosTag(text = artigo.diploma, style = CrivosTagStyle.SUBTLE_AR)
                                Text(
                                    text = artigo.numero,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.5.sp,
                                    color = CrivosRed
                                )
                            }
                            CrivosTag(text = artigo.incidencia, style = CrivosTagStyle.RED_SOLID)
                        }

                        Text(
                            text = artigo.texto,
                            fontSize = 14.sp,
                            color = c.inkPrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
