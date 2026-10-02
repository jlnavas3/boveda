package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TarjetaHistorialDetalle(
    entrada: Entrada,
    vm: VaultViewModel? = null,
    haptica: Haptica,
    alCopiarClave: ((String) -> Unit)? = null,
    alGuardarEntrada: ((Entrada) -> Unit)? = null
) {
    val historialUnico = remember(entrada.historialContrasenas, entrada.contrasena) {
        entrada.historialContrasenas
            .distinctBy { it.contrasena }
            .filterNot { it.contrasena == entrada.contrasena }
    }
    if (historialUnico.isEmpty()) return

    val reveladas = remember { mutableStateMapOf<String, Boolean>() }
    var claveCopiadaReciente by remember { mutableStateOf<String?>(null) }
    var claveARestaurar by remember { mutableStateOf<String?>(null) }
    var claveAEliminar by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(claveCopiadaReciente) {
        if (claveCopiadaReciente != null) {
            delay(1500)
            claveCopiadaReciente = null
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Contraseñas anteriores (${historialUnico.size})")

        historialUnico.forEachIndexed { index, cambio ->
            if (index > 0) {
                Spacer(Modifier.height(EspaciadoDetalle))
            }
            val estaRevelada = reveladas[cambio.contrasena] == true
            val fueCopiada = claveCopiadaReciente == cambio.contrasena

            FilaHistorialContrasena(
                cambio = cambio,
                revelada = estaRevelada,
                copiado = fueCopiada,
                onToggleRevelar = {
                    haptica.toque()
                    reveladas[cambio.contrasena] = !estaRevelada
                },
                onCopiar = {
                    haptica.exito()
                    vm?.copiar("Contraseña anterior", cambio.contrasena, sensible = true)
                    alCopiarClave?.invoke(cambio.contrasena)
                    claveCopiadaReciente = cambio.contrasena
                },
                onEliminar = {
                    haptica.toque()
                    claveAEliminar = cambio.contrasena
                },
                onSolicitarRestaurar = {
                    haptica.toque()
                    claveARestaurar = cambio.contrasena
                }
            )
        }
    }

    if (claveARestaurar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Restaurar esta contraseña?",
            mensaje = "Esta contraseña pasará a ser la contraseña activa de \"${entrada.titulo}\". La que tienes actualmente no se perderá: se conservará en este mismo historial.",
            textoConfirmar = "Restaurar",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.Restore,
            alConfirmar = {
                val nuevaClave = claveARestaurar
                claveARestaurar = null
                if (nuevaClave != null) {
                    haptica.exito()
                    val actualizada = entrada.copy(contrasena = nuevaClave)
                    vm?.guardar(actualizada)
                    alGuardarEntrada?.invoke(actualizada)
                }
            },
            alDescartar = { claveARestaurar = null }
        )
    }

    if (claveAEliminar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar esta contraseña del historial?",
            mensaje = "Esta contraseña anterior se eliminará permanentemente. Esta acción no se puede deshacer.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                val clave = claveAEliminar
                claveAEliminar = null
                if (clave != null) {
                    haptica.error()
                    val nuevoHistorial = entrada.historialContrasenas.filterNot { it.contrasena == clave }
                    val actualizada = entrada.copy(historialContrasenas = nuevoHistorial)
                    vm?.guardar(actualizada)
                    alGuardarEntrada?.invoke(actualizada)
                }
            },
            alDescartar = { claveAEliminar = null }
        )
    }
}


@Composable
private fun FilaHistorialContrasena(
    cambio: CambioContrasena,
    revelada: Boolean,
    copiado: Boolean,
    onToggleRevelar: () -> Unit,
    onCopiar: () -> Unit,
    onEliminar: () -> Unit,
    onSolicitarRestaurar: () -> Unit
) {
    TarjetaDatoDetalle(colorBorde = ColorDatosContrasena) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formatearFechaDetalle(cambio.cambiadaEn),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${cambio.contrasena.length} caracteres",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextoSecundario.copy(alpha = 0.8f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BotonIconoDetalle(
                        icono = Icons.Filled.Delete,
                        descripcion = "Eliminar contraseña anterior",
                        alPulsar = onEliminar,
                        tint = Peligro.copy(alpha = 0.7f)
                    )
                    BotonIconoDetalle(
                        icono = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        descripcion = if (revelada) "Ocultar" else "Ver contraseña",
                        alPulsar = onToggleRevelar
                    )
                    BotonCopiarDetalle(
                        copiado = copiado,
                        alPulsar = onCopiar
                    )
                    BotonIconoDetalle(
                        icono = Icons.Filled.Restore,
                        descripcion = "Restaurar como activa",
                        alPulsar = onSolicitarRestaurar,
                        tint = Ambar
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            if (revelada) {
                Text(
                    text = contrasenaColoreada(cambio.contrasena),
                    style = EstiloMono,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = "•".repeat(cambio.contrasena.length.coerceIn(8, 20)),
                    style = EstiloMonoGrande.copy(letterSpacing = 2.sp),
                    color = TextoSecundario,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun formatearFechaDetalle(milisegundos: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        sdf.format(Date(milisegundos))
    } catch (e: Exception) {
        "Fecha desconocida"
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaHistorialDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val contexto = androidx.compose.ui.platform.LocalContext.current
        TarjetaHistorialDetalle(
            entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo.copy(
                historialContrasenas = listOf(
                    CambioContrasena(contrasena = "ViejaClave123!", cambiadaEn = System.currentTimeMillis() - 86400000L * 15),
                    CambioContrasena(contrasena = "AnteriorSegura2023", cambiadaEn = System.currentTimeMillis() - 86400000L * 90)
                )
            ),
            haptica = remember { Haptica(contexto) }
        )
    }
}

