package me.crivos.app.ui.screens.crono

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.crivos.app.ui.theme.*

data class CronoItem(
    val id: String,
    val materia: String,
    val topico: String,
    val duracaoMinutos: Int,
    val concluido: Boolean
)

@Composable
fun CronoScreen() {
    var items by remember {
        mutableStateOf(
            listOf(
                CronoItem("1", "Direito Constitucional", "Art. 5º - Direitos Individuais e Coletivos", 60, true),
                CronoItem("2", "Direito Administrativo", "Atos Administrativos - Requisitos e Atributos", 45, true),
                CronoItem("3", "Língua Portuguesa", "Crase e Regência Verbal (Banca FGV)", 50, false),
                CronoItem("4", "Raciocínio Lógico", "Tautologia, Contradição e Equivalências", 40, false)
            )
        )
    }

    val totalMinutos = items.sumOf { it.duracaoMinutos }
    val concluidosMinutos = items.filter { it.concluido }.sumOf { it.duracaoMinutos }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "CRONOGRAMA DO DIA",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = EmeraldPrimary,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "${concluidosMinutos / 60}h${concluidosMinutos % 60}m concluídos de ${totalMinutos / 60}h${totalMinutos % 60}m planejados",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            items = items.map {
                                if (it.id == item.id) it.copy(concluido = !it.concluido) else it
                            }
                        },
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (item.concluido) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = null,
                            tint = if (item.concluido) EmeraldPrimary else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.materia,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldPrimary
                            )
                            Text(
                                text = item.topico,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (item.concluido) TextMuted else TextPrimary
                            )
                        }
                        Text(
                            text = "${item.duracaoMinutos} min",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
