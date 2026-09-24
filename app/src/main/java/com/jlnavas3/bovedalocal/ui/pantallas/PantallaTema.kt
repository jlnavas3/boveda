package com.jlnavas3.bovedalocal.ui.pantallas

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableIntStateOf
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import kotlin.math.roundToInt
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjuste
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaOpcionRadio
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorFila
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorCamara
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.Haptica




@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaTema(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoConfirmarIcono by remember { mutableStateOf<PaletaAcento?>(null) }

    val reqDinamico = remember { BringIntoViewRequester() }
    val reqAcento = remember { BringIntoViewRequester() }
    val reqLauncher = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "09.2.2" -> reqDinamico.bringIntoView()
                seccionDestino == "09.2.3" -> reqAcento.bringIntoView()
                seccionDestino == "09.2.8" -> reqLauncher.bringIntoView()
                seccionDestino.startsWith("09.2.") && seccionDestino != "09.2" -> reqAcento.bringIntoView()
            }
        }
    }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Tema y colores",
                idEtiqueta = "03.2",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Modo de tema, paleta visual y colores semánticos por módulo")
                Spacer(Modifier.height(10.dp))

                // 0. Modo de tema (Sistema, Claro, Oscuro)
                ComponenteGrupo(
                    etiqueta = "Modo de tema",
                    idGrupo = "03.2.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Sigue la configuración de Android o fija un aspecto específico"
                ) {
                    ComponenteRadio(
                        titulo = "Automático (sistema)",
                        icono = Icons.Filled.BrightnessAuto,
                        colorIcono = Color(0xFFFB8C00),
                        seleccionado = ajustes.temaApp == "sistema",
                        idFila = "03.2.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            vm.ajustarTema("sistema")
                        }
                    )
                    ComponenteSeparador()
                    ComponenteRadio(
                        titulo = "Modo claro",
                        icono = Icons.Filled.LightMode,
                        colorIcono = Color(0xFFFFA000),
                        seleccionado = ajustes.temaApp == "claro",
                        idFila = "03.2.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            vm.ajustarTema("claro")
                        }
                    )
                    ComponenteSeparador()
                    ComponenteRadio(
                        titulo = "Modo oscuro",
                        icono = Icons.Filled.DarkMode,
                        colorIcono = Color(0xFF3F51B5),
                        seleccionado = ajustes.temaApp == "oscuro",
                        idFila = "03.2.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            vm.ajustarTema("oscuro")
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Animación de pantalla bloqueada
                ComponenteGrupo(
                    etiqueta = "Animación de pantalla bloqueada",
                    idGrupo = "03.2.G2",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteRadio(
                        titulo = "Mecanismo de engranajes",
                        icono = Icons.Filled.Memory,
                        colorIcono = Color(0xFF5C6BC0),
                        seleccionado = ajustes.animacionDesbloqueo == "engranajes",
                        idFila = "03.2.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarAnimacionDesbloqueo("engranajes")
                        }
                    )
                    ComponenteSeparador()
                    ComponenteRadio(
                        titulo = "Puerta de bóveda",
                        icono = Icons.Filled.Security,
                        colorIcono = Color(0xFF1E88E5),
                        seleccionado = ajustes.animacionDesbloqueo != "engranajes",
                        idFila = "03.2.5",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarAnimacionDesbloqueo("puerta")
                        }
                    )
                    ComponenteSeparador()
                    ComponenteBotonFila(
                        titulo = "Calibración",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorIconosInternos,
                        idFila = "03.2.6",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.CalibracionAnimacion())
                        }
                    )
            }

            Spacer(Modifier.height(18.dp))

            // 1. Color dinámico del sistema (Material You - Android 12+)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                Spacer(Modifier.height(18.dp))
                ComponenteGrupo(
                    etiqueta = "Color dinámico del sistema",
                    idGrupo = "03.2.G3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Adapta los colores automáticamente al fondo de pantalla de Android",
                    modifier = Modifier.bringIntoViewRequester(reqDinamico)
                ) {
                    ComponenteSwitch(
                        titulo = "Material You",
                        icono = Icons.Filled.AutoAwesome,
                        colorIcono = Color(0xFF00897B),
                        activo = ajustes.colorDinamicoSistema,
                        idFila = "03.2.7",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.alternarColorDinamicoSistema(it)
                        }
                    )
                }
            }

            // Colores de campos y datos
            Spacer(Modifier.height(18.dp))
            ComponenteGrupo(
                etiqueta = "Colores de campos y datos",
                idGrupo = "03.2.G3.5",
                mostrarId = ajustes.mostrarIdsAjustes,
                descripcion = "Personaliza los colores individuales para Usuario, Contraseña, 2FA, Passkey, Web y Apps"
            ) {
                ComponenteNavegacion(
                    titulo = "Colores de campos y datos",
                    icono = Icons.Filled.Palette,
                    colorIcono = ColorAcento,
                    idFila = "03.2.1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alPulsar = { vm.ir(Pantalla.ColoresDatos()) }
                )
            }

            // 2. Color de acento principal
            Spacer(Modifier.height(18.dp))
            ComponenteGrupo(
                etiqueta = "Color de acento principal",
                idGrupo = "03.2.G4",
                mostrarId = ajustes.mostrarIdsAjustes,
                descripcion = if (ajustes.colorDinamicoSistema) {
                    "Material You está activo. Seleccionar un color manual desactivará el color dinámico del sistema."
                } else {
                    "Afecta a los botones destacados, elementos activos y selectores"
                },
                modifier = Modifier.bringIntoViewRequester(reqAcento)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SelectorColorEnTiempoReal(
                        colorInicial = ColorAcento,
                        titulo = "Acento"
                    ) { nuevoColor ->
                        vm.ajustarColorAcento(nuevoColor.aHex())
                    }
                }
                ComponenteSeparador()
                ComponenteBotonFila(
                    titulo = "Restablecer",
                    alPulsar = {
                        vm.ajustarColorAcento("ambar")
                    }
                )
            }

            // 4. Ícono de la app en el Launcher
            Spacer(Modifier.height(18.dp))
            ComponenteGrupo(
                etiqueta = "Ícono de la app en el launcher",
                idGrupo = "03.2.G6",
                mostrarId = ajustes.mostrarIdsAjustes,
                descripcion = "Variante de icono para la pantalla de inicio y cajón de aplicaciones",
                modifier = Modifier.bringIntoViewRequester(reqLauncher)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Android exige cambiar el icono del launcher mediante variantes del sistema. Al seleccionarlo, la app se reiniciará brevemente.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PaletaAcento.entries.forEach { paleta ->
                            val seleccionado = ajustes.iconoLauncher == paleta.clave
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(paleta.base)
                                    .border(
                                        width = if (seleccionado) 3.dp else 1.dp,
                                        color = if (seleccionado) TextoPrincipal else Borde,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        if (ajustes.iconoLauncher != paleta.clave) {
                                            haptica.tic()
                                            dialogoConfirmarIcono = paleta
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = paleta.etiqueta,
                                        tint = colorContraste(paleta.base),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                ComponenteSeparador()
                ComponenteBotonFila(
                    titulo = "Restablecer",
                    alPulsar = {
                        if (ajustes.iconoLauncher != "ambar") {
                            dialogoConfirmarIcono = PaletaAcento.AMBAR
                        }
                    }
                )
            }

            // 9. Botones de acción inferiores con iconos normalizados
            Spacer(Modifier.height(18.dp))
            ComponenteGrupo {
                ComponenteBotonFila(
                    titulo = "Restablecer módulo",
                    alPulsar = {
                        vm.restablecerColoresTema()
                    }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

    dialogoConfirmarIcono?.let { paleta ->
        val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
        val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { dialogoConfirmarIcono = null },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Cambiar icono a \"${paleta.etiqueta}\"", color = TextoPrincipal) },
            text = {
                Text(
                    "Android cerrará la aplicación por un instante para que el launcher actualice su icono. Ya quedará guardado antes de cerrarse.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val elegido = paleta
                    dialogoConfirmarIcono = null
                    vm.ajustarIconoLauncher(elegido.clave)
                    actividad?.finishAffinity()
                }) { Text("Cambiar y cerrar", color = ColorAcento) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoConfirmarIcono = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}


