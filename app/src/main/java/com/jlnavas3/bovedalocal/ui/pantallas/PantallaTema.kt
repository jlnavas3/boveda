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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
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

private data class InfoSeccionFuncional(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val icono: ImageVector,
    val colorActual: Color,
    val colorPorDefecto: Color,
    val mutador: (Color) -> Unit
)

@Composable
fun PantallaTema(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoConfirmarIcono by remember { mutableStateOf<PaletaAcento?>(null) }
    var seccionElegida by remember { mutableStateOf(0) }
    var dropdownSeccionesAbierto by remember { mutableStateOf(false) }

    val seccionesFuncionales = listOf(
        InfoSeccionFuncional(
            id = "seguridad",
            nombre = "Seguridad y Diagnóstico",
            descripcion = "Auditoría de seguridad, informes y cifrado militar",
            icono = Icons.Filled.Security,
            colorActual = ColorSeguridad,
            colorPorDefecto = Color(0xFF0284C7),
            mutador = { vm.ajustarColorSeguridad(it.aHex()) }
        ),
        InfoSeccionFuncional(
            id = "generador",
            nombre = "Generador de Contraseñas",
            descripcion = "Generación de claves seguras, entropía y dados",
            icono = Icons.Filled.AutoAwesome,
            colorActual = ColorGenerador,
            colorPorDefecto = Color(0xFF0D9488),
            mutador = { vm.ajustarColorGenerador(it.aHex()) }
        ),
        InfoSeccionFuncional(
            id = "2fa",
            nombre = "Autenticador 2FA / TOTP",
            descripcion = "Códigos temporales y sincronización de reloj",
            icono = Icons.Filled.Timer,
            colorActual = Color2FA,
            colorPorDefecto = Color(0xFFF97316),
            mutador = { vm.ajustarColor2FA(it.aHex()) }
        ),
        InfoSeccionFuncional(
            id = "passkeys",
            nombre = "Passkeys WebAuthn",
            descripcion = "Credenciales FIDO2 y llaves de paso criptográficas",
            icono = Icons.Filled.Fingerprint,
            colorActual = ColorPasskeys,
            colorPorDefecto = Color(0xFF8B5CF6),
            mutador = { vm.ajustarColorPasskeys(it.aHex()) }
        ),
        InfoSeccionFuncional(
            id = "salud",
            nombre = "Salud de la Bóveda",
            descripcion = "Detección de claves duplicadas, débiles y caducadas",
            icono = Icons.Filled.HealthAndSafety,
            colorActual = ColorSalud,
            colorPorDefecto = Color(0xFF10B981),
            mutador = { vm.ajustarColorSalud(it.aHex()) }
        ),
        InfoSeccionFuncional(
            id = "papelera",
            nombre = "Papelera de Reciclaje",
            descripcion = "Cuentas eliminadas y purga definitiva de entradas",
            icono = Icons.Filled.Delete,
            colorActual = ColorPapelera,
            colorPorDefecto = Color(0xFFEF4444),
            mutador = { vm.ajustarColorPapelera(it.aHex()) }
        ),
        InfoSeccionFuncional(
            id = "exportacion",
            nombre = "Copia y Exportación",
            descripcion = "Archivos de respaldo cifrados y kit de emergencia",
            icono = Icons.Filled.Backup,
            colorActual = ColorExportacion,
            colorPorDefecto = Color(0xFF6366F1),
            mutador = { vm.ajustarColorExportacion(it.aHex()) }
        )
    )

    ContenedorPrincipal(conScroll = true, espaciado = 14.dp) {
        CabeceraPantalla(
            titulo = "Personalizar Colores",
            subtitulo = "Paleta visual de la app y colores semánticos por módulo",
            alVolver = { vm.volverAtras() }
        )

        // 1. Tarjeta de previsualización en tiempo real
        TarjetaBovedaDesplegable(
            titulo = "Vista previa en tiempo real",
            descripcion = "Observa cómo interactúan tus colores en vivo",
            icono = Icons.Filled.Visibility,
            colorIcono = ColorAcento,
            inicialmenteAbierta = true
        ) {
            ContenedorTarjeta(
                colorFondo = ColorTarjetas,
                paddingInterno = 18.dp
            ) {
                Text(
                    text = "Título de Sección de Ejemplo",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Este es un texto sobre la tarjeta personalizada. El contraste se calcula automáticamente.",
                    color = ColorSobreTarjetas.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
                ContenedorFila(
                    titulo = "Elemento de prueba",
                    subtitulo = "El ícono usa el color asignado",
                    icono = Icons.Filled.Key,
                    colorIcono = ColorIconosInternos
                )
                Spacer(Modifier.height(6.dp))
                BotonAmbar(
                    texto = "Botón con Color de Acento",
                    icono = Icons.Filled.Check
                ) {}
            }
        }

        // 2. Color dinámico del sistema (Material You - Android 12+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            TarjetaBovedaDesplegable(
                titulo = "Color dinámico del sistema",
                descripcion = "Sincroniza la paleta con tu fondo de pantalla (Material You)",
                icono = Icons.Filled.AutoAwesome,
                colorIcono = ColorAcento,
                inicialmenteAbierta = false
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Material You (Monet)", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (ajustes.colorDinamicoSistema) "Activo: usando colores del fondo de pantalla" else "Desactivado: usando paleta elegida",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = ajustes.colorDinamicoSistema,
                        onCheckedChange = {
                            haptica.tic()
                            vm.alternarColorDinamicoSistema(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ColorSobreAcento,
                            checkedTrackColor = ColorAcento,
                            checkedBorderColor = ColorAcento,
                            uncheckedThumbColor = TextoSecundario,
                            uncheckedTrackColor = SuperficieAlta,
                            uncheckedBorderColor = TextoSecundario
                        )
                    )
                }
            }
        }

        // 3. Sección: Color de Acento (con Picker en tiempo real)
        TarjetaBovedaDesplegable(
            titulo = "Color de acento principal",
            descripcion = "Afecta a los botones destacados, elementos activos y selector flotante",
            icono = Icons.Filled.Palette,
            colorIcono = ColorAcento,
            inicialmenteAbierta = true
        ) {
            SelectorColorEnTiempoReal(
                colorInicial = ColorAcento,
                titulo = "Acento"
            ) { nuevoColor ->
                vm.ajustarColorAcento(nuevoColor.aHex())
            }
        }

        // 4. Sección: Color de Íconos Internos (independiente)
        TarjetaBovedaDesplegable(
            titulo = "Color de íconos internos",
            descripcion = "Personaliza los íconos de la lista, menú y botones sin alterar el acento general",
            icono = Icons.Filled.Security,
            colorIcono = ColorIconosInternos,
            inicialmenteAbierta = false
        ) {
            val esAdaptativo = ajustes.colorIconosInternos.isBlank()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Color adaptativo del tema", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                BotonBorde(
                    texto = if (esAdaptativo) "Activo" else "Restablecer",
                    icono = Icons.Filled.Refresh,
                    modifier = Modifier.width(140.dp)
                ) {
                    haptica.tic()
                    vm.ajustarColorIconosInternos("")
                }
            }
            Spacer(Modifier.height(8.dp))
            SelectorColorEnTiempoReal(
                colorInicial = ColorIconosInternos,
                titulo = "Íconos"
            ) { nuevoColor ->
                vm.ajustarColorIconosInternos(nuevoColor.aHex())
            }
        }

        // 5. Sección: Color de Títulos y Cabeceras
        TarjetaBovedaDesplegable(
            titulo = "Color de títulos y cabeceras",
            descripcion = "Color destacado para las categorías de la lista y títulos de sección",
            icono = Icons.Filled.TextFields,
            colorIcono = ColorTitulos,
            inicialmenteAbierta = false
        ) {
            val esAdaptativo = ajustes.colorTitulos.isBlank()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Color adaptativo del tema", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                BotonBorde(
                    texto = if (esAdaptativo) "Activo" else "Restablecer",
                    icono = Icons.Filled.Refresh,
                    modifier = Modifier.width(140.dp)
                ) {
                    haptica.tic()
                    vm.ajustarColorTitulos("")
                }
            }
            Spacer(Modifier.height(8.dp))
            SelectorColorEnTiempoReal(
                colorInicial = ColorTitulos,
                titulo = "Títulos"
            ) { nuevoColor ->
                vm.ajustarColorTitulos(nuevoColor.aHex())
            }
        }

        // 6. Sección: Color de Tarjetas
        TarjetaBovedaDesplegable(
            titulo = "Color de tarjetas y superficies",
            descripcion = "Personaliza el fondo de las tarjetas y paneles de contenido",
            icono = Icons.Filled.Layers,
            colorIcono = ColorTarjetas,
            inicialmenteAbierta = false
        ) {
            val esAdaptativo = ajustes.colorTarjetas.isBlank()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Fondo adaptativo del tema", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                BotonBorde(
                    texto = if (esAdaptativo) "Activo" else "Restablecer",
                    icono = Icons.Filled.Refresh,
                    modifier = Modifier.width(140.dp)
                ) {
                    haptica.tic()
                    vm.ajustarColorTarjetas("")
                }
            }
            Spacer(Modifier.height(8.dp))
            SelectorColorEnTiempoReal(
                colorInicial = ColorTarjetas,
                titulo = "Tarjetas"
            ) { nuevoColor ->
                vm.ajustarColorTarjetas(nuevoColor.aHex())
            }
        }

        // 7. Sección: Colores por Sección Funcional con Menú Dropdown
        val seccionActual = seccionesFuncionales[seccionElegida.coerceIn(0, seccionesFuncionales.size - 1)]
        TarjetaBovedaDesplegable(
            titulo = "Colores por Sección Funcional",
            descripcion = "Color semántico para cada módulo clave de la app",
            icono = Icons.Filled.ColorLens,
            colorIcono = seccionActual.colorActual,
            inicialmenteAbierta = true
        ) {
            Text(
                text = "Selecciona un módulo en el menú desplegable para personalizar su identidad visual:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(4.dp))

            // Selector Dropdown de Sección Funcional
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(Superficie)
                        .then(
                            if (GrosorBorde > 0.dp) {
                                Modifier.border(
                                    GrosorBorde,
                                    if (dropdownSeccionesAbierto) ColorTitulos else ColorBordeActual,
                                    FormaCampo
                                )
                            } else {
                                Modifier
                            }
                        )
                        .clickable { dropdownSeccionesAbierto = true }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge del ícono con fondo suave estilo barra lateral
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(FormaPequena)
                            .background(seccionActual.colorActual.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = seccionActual.icono,
                            contentDescription = null,
                            tint = seccionActual.colorActual,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = seccionActual.nombre,
                            color = ColorTitulos,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = seccionActual.descripcion,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    // Muestra de color circular
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(seccionActual.colorActual)
                            .border(1.5.dp, TextoPrincipal.copy(alpha = 0.3f), CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector = if (dropdownSeccionesAbierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = "Desplegar secciones funcionales",
                        tint = TextoSecundario,
                        modifier = Modifier.size(22.dp)
                    )
                }

                MenuDesplegableBoveda(
                    expanded = dropdownSeccionesAbierto,
                    onDismissRequest = { dropdownSeccionesAbierto = false }
                ) {
                    seccionesFuncionales.forEachIndexed { i, seccion ->
                        if (i > 0) {
                            SeparadorOpcionMenu()
                        }
                        val esElegida = seccionElegida == i
                        DropdownMenuItem(
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(FormaPequena)
                                        .background(seccion.colorActual.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = seccion.icono,
                                        contentDescription = null,
                                        tint = seccion.colorActual,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            },
                            text = {
                                Column {
                                    Text(
                                        text = seccion.nombre,
                                        color = if (esElegida) ColorTitulos else TextoPrincipal,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (esElegida) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                    Text(
                                        text = seccion.descripcion,
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .clip(CircleShape)
                                            .background(seccion.colorActual)
                                            .border(1.dp, TextoPrincipal.copy(alpha = 0.25f), CircleShape)
                                    )
                                    if (esElegida) {
                                        Spacer(Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Seleccionado",
                                            tint = ColorTitulos,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                haptica.tic()
                                seccionElegida = i
                                dropdownSeccionesAbierto = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // Barra de acción para restablecer este color individual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Color actual: ${seccionActual.colorActual.aHex()}",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
                BotonBorde(
                    texto = "Restablecer este módulo",
                    icono = Icons.Filled.Refresh,
                    modifier = Modifier.width(180.dp)
                ) {
                    haptica.tic()
                    seccionActual.mutador(seccionActual.colorPorDefecto)
                }
            }

            Spacer(Modifier.height(8.dp))

            // Selector en tiempo real para el módulo seleccionado
            SelectorColorEnTiempoReal(
                colorInicial = seccionActual.colorActual,
                titulo = seccionActual.nombre
            ) { nuevoColor ->
                seccionActual.mutador(nuevoColor)
            }
        }

        // 8. Sección: Ícono del Launcher (Fossify)
        TarjetaBovedaDesplegable(
            titulo = "Ícono de la app en el Launcher",
            descripcion = "Variantes del launcher del sistema (requiere reinicio rápido)",
            icono = Icons.Filled.AppShortcut,
            colorIcono = ColorAcento,
            inicialmenteAbierta = false
        ) {
            Text(
                "Android exige cambiar el icono del launcher mediante variantes del sistema. Al seleccionarlo, la app se reiniciará brevemente.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
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

        // 9. Botones de acción inferiores con iconos normalizados
        Spacer(Modifier.height(4.dp))
        BotonBorde(
            texto = "Restablecer todos los colores",
            icono = Icons.Filled.Refresh,
            color = ColorTitulos
        ) {
            haptica.tic()
            vm.restablecerColoresTema()
        }

        BotonColorido(
            texto = "Listo",
            icono = Icons.Filled.Check,
            color = ColorAcento
        ) {
            haptica.exito()
            vm.volverAtras()
        }

        Spacer(Modifier.height(24.dp))
    }

    dialogoConfirmarIcono?.let { paleta ->
        AlertDialog(
            onDismissRequest = { dialogoConfirmarIcono = null },
            containerColor = SuperficieAlta,
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
