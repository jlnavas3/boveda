package com.jlnavas3.bovedalocal.ui.pantallas

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

@Composable
fun PantallaKitEmergencia(
    vm: VaultViewModel,
    actividad: Activity
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estado by vm.estado.collectAsStateWithLifecycle()

    val entradas = remember(estado) {
        (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    }

    var incluirContrasenas by remember { mutableStateOf(false) }
    var soloFavoritos by remember { mutableStateOf(false) }
    var incluirNotas by remember { mutableStateOf(false) }

    val opciones = remember(incluirContrasenas, soloFavoritos, incluirNotas) {
        GeneradorKitEmergencia.OpcionesKit(
            incluirContrasenas = incluirContrasenas,
            soloFavoritos = soloFavoritos,
            incluirNotas = incluirNotas
        )
    }

    val textoPreview = remember(entradas, opciones) {
        GeneradorKitEmergencia.generarTexto(entradas, opciones)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidiana)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraPantalla(
            titulo = "Kit de Emergencia Físico",
            subtitulo = "Hoja de respaldo físico para imprimir y guardar en caja fuerte",
            alVolver = { vm.volverAtras() }
        )

        // Banner informativo
        TarjetaBovedaDesplegable(
            titulo = "Copia Física de Seguridad",
            descripcion = "Genera un documento impreso o PDF 100% offline para guardar en caja fuerte",
            icono = Icons.Filled.Shield,
            colorIcono = ColorSeguridad,
            inicialmenteAbierta = false
        ) {
            Text(
                text = "Genera un documento que puedes imprimir o guardar en PDF localmente. Bóveda Local opera 100% offline sin conexión a internet ni telemetría.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(14.dp))

        // Opciones de configuración
        TarjetaBovedaDesplegable(
            titulo = "Opciones del Documento",
            descripcion = "Contraseñas, favoritos y notas en el kit",
            icono = Icons.Filled.Tune,
            colorIcono = ColorAcento,
            inicialmenteAbierta = true
        ) {
            FilaOpcionKit(
                titulo = "Incluir contraseñas en claro",
                descripcion = if (incluirContrasenas) "Las contraseñas se imprimirán legibles (¡Riesgo físico!)" else "Se dejarán espacios en blanco para anotar a mano",
                activo = incluirContrasenas,
                esPeligro = incluirContrasenas,
                alCambiar = {
                    haptica.tic()
                    incluirContrasenas = it
                }
            )

            Spacer(Modifier.height(10.dp))

            FilaOpcionKit(
                titulo = "Solo cuentas favoritas / esenciales",
                descripcion = if (soloFavoritos) "Solo se incluirán las cuentas marcadas con estrella" else "Se incluirán todas las cuentas activas",
                activo = soloFavoritos,
                alCambiar = {
                    haptica.tic()
                    soloFavoritos = it
                }
            )

            Spacer(Modifier.height(10.dp))

            FilaOpcionKit(
                titulo = "Incluir notas seguras",
                descripcion = "Añade el campo de notas de cada entrada al documento",
                activo = incluirNotas,
                alCambiar = {
                    haptica.tic()
                    incluirNotas = it
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        // Botones de acción principales (52dp de altura)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonColorido(
                texto = "Imprimir / PDF",
                color = ColorAcento,
                icono = Icons.Filled.Print,
                modifier = Modifier.weight(1f)
            ) {
                haptica.exito()
                val html = GeneradorKitEmergencia.generarHtml(entradas, opciones)
                GeneradorKitEmergencia.imprimir(actividad, html)
            }

            BotonColorido(
                texto = "Copiar texto",
                color = ColorSeguridad,
                icono = Icons.Filled.ContentCopy,
                modifier = Modifier.weight(1f)
            ) {
                haptica.toque()
                Portapapeles.copiar(contexto, "Kit de Emergencia Bóveda Local", textoPreview)
                vm.avisar("Kit de emergencia copiado al portapapeles")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Vista previa del documento en Tarjeta Desplegable
        TarjetaBovedaDesplegable(
            titulo = "Vista Previa del Documento",
            descripcion = "Previsualización del texto que se imprimirá",
            icono = Icons.Filled.Description,
            colorIcono = ColorSalud,
            inicialmenteAbierta = false
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CurvaturaEsquinas))
                    .background(Superficie)
                    .then(
                        if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
                        else Modifier
                    )
                    .padding(14.dp)
            ) {
                Text(
                    text = textoPreview,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun FilaOpcionKit(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    esPeligro: Boolean = false,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = titulo,
                color = if (esPeligro && activo) Peligro else TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Switch(
            checked = activo,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorSobreAcento,
                checkedTrackColor = if (esPeligro) Peligro else Ambar,
                checkedBorderColor = if (esPeligro) Peligro else Ambar,
                uncheckedThumbColor = TextoSecundario,
                uncheckedTrackColor = SuperficieAlta,
                uncheckedBorderColor = TextoSecundario
            )
        )
    }
}
