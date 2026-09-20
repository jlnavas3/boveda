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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaKitEmergencia(
    vm: VaultViewModel,
    actividad: Activity,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val reqBanner = remember { BringIntoViewRequester() }
    val reqOpciones = remember { BringIntoViewRequester() }
    val reqPreview = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "02.3.1" -> reqOpciones.bringIntoView()
                seccionDestino.startsWith("02.3.") && seccionDestino != "02.3" -> reqOpciones.bringIntoView()
            }
        }
    }

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

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Kit de emergencia",
                idEtiqueta = "02.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Hoja de respaldo físico para imprimir y guardar en caja fuerte")
                Spacer(Modifier.height(10.dp))

                // Banner informativo
                ComponenteGrupo(
                    etiqueta = "Copia física de seguridad",
                    idGrupo = "02.3.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Respaldo 100% offline sin servidores ni telemetría"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                tint = ColorSeguridad,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Respaldo 100% desconectado",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Genera un documento que puedes imprimir en papel o guardar en PDF localmente. Bóveda Local opera 100% offline sin servidores en la nube ni telemetría.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Opciones de configuración
                ComponenteGrupo(
                    etiqueta = "Opciones del documento",
                    idGrupo = "02.3.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Configuración del contenido que se incluirá en el PDF/impresión"
                ) {
                    ComponenteSwitch(
                        titulo = "Incluir contraseñas en claro",
                        icono = Icons.Filled.Shield,
                        colorIcono = if (incluirContrasenas) Peligro else ColorAcento,
                        activo = incluirContrasenas,
                        idFila = "02.3.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        colorActivo = Peligro,
                        alCambiar = {
                            haptica.tic()
                            incluirContrasenas = it
                        }
                    )
                    ComponenteSeparador()
                    ComponenteSwitch(
                        titulo = "Solo cuentas favoritas / esenciales",
                        icono = Icons.Filled.Description,
                        colorIcono = ColorSeguridad,
                        activo = soloFavoritos,
                        idFila = "02.3.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            soloFavoritos = it
                        }
                    )
                    ComponenteSeparador()
                    ComponenteSwitch(
                        titulo = "Incluir notas seguras",
                        icono = Icons.Filled.Description,
                        colorIcono = Ambar,
                        activo = incluirNotas,
                        idFila = "02.3.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            incluirNotas = it
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Botones de acción principales
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

                // Vista previa del documento
                ComponenteGrupo(
                    etiqueta = "Vista previa del documento",
                    idGrupo = "02.3.G3",
                    mostrarId = false,
                    descripcion = "Previsualización del formato de texto"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Previsualización del texto impreso",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
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
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
