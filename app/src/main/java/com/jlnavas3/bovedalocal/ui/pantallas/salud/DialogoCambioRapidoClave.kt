package com.jlnavas3.bovedalocal.ui.pantallas.salud

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun DialogoCambioRapidoClave(
    entrada: Entrada,
    alDescartar: () -> Unit,
    alGuardar: (String) -> Unit,
    alCopiar: (String) -> Unit
) {
    val contexto = LocalContext.current
    var longitud by remember { mutableIntStateOf(20) }
    var claveGenerada by remember {
        mutableStateOf(PasswordGenerator.generar(OpcionesGenerador(longitud = 20)))
    }
    val fuerza = remember(claveGenerada) { MedidorFuerza.medir(claveGenerada) }

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = RoundedCornerShape(20.dp),
        containerColor = ColorTarjetaAjustes,
        tonalElevation = 0.dp,
        title = {
            Column {
                Text(
                    text = "Actualizar contraseña rápida",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorTextoAjustes
                )
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.bodySmall,
                    color = ColorAjusteGris
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Se ha generado una clave robusta para sustituir la actual:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ColorAjusteGris
                )

                Spacer(Modifier.height(10.dp))

                val esOscuro = isSystemInDarkTheme()
                val fondoGenerada = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(fondoGenerada)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = claveGenerada,
                        style = EstiloMono,
                        color = ColorTextoAjustes,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            claveGenerada = PasswordGenerator.generar(OpcionesGenerador(longitud = longitud))
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Regenerar clave",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fortaleza: ${fuerza.etiqueta}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Menta
                    )
                    Text(
                        text = "${claveGenerada.length} caracteres",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorAjusteGris
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BotonColorido(
                        texto = "Copiar clave",
                        color = ColorAcento,
                        icono = Icons.Filled.ContentCopy,
                        modifier = Modifier.weight(1f)
                    ) {
                        alCopiar(claveGenerada)
                    }

                    val urlValida = entrada.urls.firstOrNull { it.isNotBlank() }
                    if (urlValida != null) {
                        BotonColorido(
                            texto = "Ir al sitio",
                            color = ColorSeguridad,
                            icono = Icons.AutoMirrored.Filled.OpenInNew,
                            modifier = Modifier.weight(1f)
                        ) {
                            try {
                                var u = urlValida.trim()
                                if (!u.startsWith("http://") && !u.startsWith("https://")) u = "https://$u"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(u))
                                contexto.startActivity(intent)
                            } catch (_: Exception) {}
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { alGuardar(claveGenerada) }) {
                Text("Guardar en bóveda", color = Menta, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = ColorAjusteGris)
            }
        }
    )
}
