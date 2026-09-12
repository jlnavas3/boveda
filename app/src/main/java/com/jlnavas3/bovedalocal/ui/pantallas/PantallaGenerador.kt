package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.crypto.Wordlist
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.ContrasenaSlotMachine
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegablePepo
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSeparadorDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaGenerador(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var opciones by remember { mutableStateOf(OpcionesGenerador()) }
    var generada by remember { mutableStateOf("") }
    var generacion by remember { mutableIntStateOf(0) }

    fun regenerar() {
        generada = PasswordGenerator.generar(opciones)
        generacion++
    }

    LaunchedEffect(opciones) { regenerar() }

    val bits = PasswordGenerator.entropiaBits(opciones)
    val espaciado = EspaciadoComponentes.coerceAtLeast(10.dp)

    val modoActual = when {
        opciones.modoFrase -> "Frase Diceware"
        opciones.modoPatron -> "Por Patrón"
        else -> "Aleatoria"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        CabeceraPantalla(
            titulo = "Generador",
            subtitulo = "Aleatoriedad criptográfica de alta entropía (CSPRNG)",
            alVolver = { vm.volverAtras() }
        )

        Spacer(Modifier.height(8.dp))

        // 1. Tarjeta: Resultado y Entropía
        TarjetaPepo {
            EtiquetaSeccion("Resultado")
            Spacer(Modifier.height(10.dp))
            ContrasenaSlotMachine(objetivo = generada, generacion = generacion, haptica = haptica)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(Menta.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("${bits.roundToInt()} bits", color = Menta, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Text(
                        PasswordGenerator.tiempoDeCrackeo(bits),
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(Modifier.height(espaciado))

        // 2. Tarjeta: Configuración de Parámetros
        TarjetaPepo {
            EtiquetaSeccion("Configuración del Generador")
            Spacer(Modifier.height(10.dp))

            // Selector desplegable para el Modo de Generación
            SelectorModoGenerador(
                modoActual = modoActual,
                alSeleccionarModo = { nuevoModo ->
                    haptica.tic()
                    opciones = when (nuevoModo) {
                        "Frase Diceware" -> opciones.copy(modoFrase = true, modoPatron = false)
                        "Por Patrón" -> opciones.copy(modoFrase = false, modoPatron = true)
                        else -> opciones.copy(modoFrase = false, modoPatron = false)
                    }
                }
            )

            Spacer(Modifier.height(14.dp))

            when {
                opciones.modoFrase -> {
                    EtiquetaSeccion("Número de palabras: ${opciones.palabras}")
                    Slider(
                        value = opciones.palabras.toFloat(),
                        onValueChange = {
                            val nuevo = it.roundToInt().coerceIn(3, 12)
                            if (nuevo != opciones.palabras) {
                                haptica.tic()
                                opciones = opciones.copy(palabras = nuevo)
                            }
                        },
                        valueRange = 3f..12f,
                        steps = 8,
                        colors = coloresSlider()
                    )
                    Spacer(Modifier.height(10.dp))
                    EtiquetaSeccion("Separador")
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("-" to "Guion (-)", " " to "Espacio", "." to "Punto (.)", "_" to "Guion bajo (_)", "" to "Sin separador").forEach { (sep, label) ->
                            val activo = opciones.separadorFrase == sep
                            Box(
                                modifier = Modifier
                                    .clip(FormaCampo)
                                    .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
                                    .then(
                                        if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                            Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, FormaCampo)
                                        else Modifier
                                    )
                                    .clickable {
                                        haptica.tic()
                                        opciones = opciones.copy(separadorFrase = sep)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (activo) ColorSobreAcento else TextoPrincipal
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Diccionario local de ${Wordlist.TAMANO} palabras en español, integrado dentro de la app sin conexión.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                opciones.modoPatron -> {
                    // Selector desplegable para Plantillas Rápidas
                    SelectorPlantillaPatron(
                        patronActual = opciones.patron,
                        alSeleccionarPlantilla = { nuevaPlantilla ->
                            haptica.tic()
                            opciones = opciones.copy(patron = nuevaPlantilla)
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    CampoPepo(
                        valor = opciones.patron,
                        etiqueta = "Máscara / Patrón personalizado",
                        alCambiar = { opciones = opciones.copy(patron = it) },
                        monoespaciada = true
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        "X: alfanumérica (A-Z, 0-9) | A: mayúscula (A-Z) | a: minúscula (a-z) | 9 o d: dígito (0-9) | w: palabra Diceware",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                else -> {
                    // Modo Aleatorio clásico
                    EtiquetaSeccion("Longitud: ${opciones.longitud} caracteres")
                    Slider(
                        value = opciones.longitud.toFloat(),
                        onValueChange = {
                            val nuevo = it.roundToInt().coerceIn(8, 64)
                            if (nuevo != opciones.longitud) {
                                haptica.tic()
                                opciones = opciones.copy(longitud = nuevo)
                            }
                        },
                        valueRange = 8f..64f,
                        colors = coloresSlider()
                    )

                    Spacer(Modifier.height(8.dp))
                    EtiquetaSeccion("Caracteres incluidos")
                    Spacer(Modifier.height(8.dp))

                    // Fila unificada con los 4 conmutadores (Mayúsculas, minúsculas, dígitos y símbolos)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaCampo)
                            .background(Superficie)
                            .then(
                                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                    Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                                else Modifier
                            )
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ColumnaInterruptor(
                            simbolo = "A-Z",
                            etiqueta = "Mayús",
                            activo = opciones.mayusculas,
                            alCambiar = {
                                haptica.tic()
                                opciones = opciones.copy(mayusculas = it)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(ColorSeparadorDropdown))
                        ColumnaInterruptor(
                            simbolo = "a-z",
                            etiqueta = "Minús",
                            activo = opciones.minusculas,
                            alCambiar = {
                                haptica.tic()
                                opciones = opciones.copy(minusculas = it)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(ColorSeparadorDropdown))
                        ColumnaInterruptor(
                            simbolo = "0-9",
                            etiqueta = "Núm",
                            activo = opciones.digitos,
                            alCambiar = {
                                haptica.tic()
                                opciones = opciones.copy(digitos = it)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(ColorSeparadorDropdown))
                        ColumnaInterruptor(
                            simbolo = "#$!",
                            etiqueta = "Símb",
                            activo = opciones.simbolos,
                            alCambiar = {
                                haptica.tic()
                                opciones = opciones.copy(simbolos = it)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(espaciado))

        // 3. Botones de acción inferiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonColorido(
                texto = "Regenerar",
                color = ColorGenerador,
                icono = Icons.Filled.Refresh,
                modifier = Modifier.weight(1f)
            ) {
                haptica.toque()
                regenerar()
            }

            BotonColorido(
                texto = "Copiar",
                color = ColorSeguridad,
                icono = Icons.Filled.ContentCopy,
                modifier = Modifier.weight(1f)
            ) {
                haptica.exito()
                vm.copiar("Contraseña", generada, sensible = true)
            }
        }

        Spacer(Modifier.height(10.dp))

        BotonBorde(
            texto = "Crear nueva entrada",
            color = ColorTitulos,
            icono = Icons.Filled.Add
        ) {
            vm.ir(Pantalla.Editar(null, generada))
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SelectorModoGenerador(
    modoActual: String,
    alSeleccionarModo: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo

    val icono = when (modoActual) {
        "Frase Diceware" -> Icons.Filled.MenuBook
        "Por Patrón" -> Icons.Filled.Pattern
        else -> Icons.Filled.Shuffle
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && (abierto || ColorBordeActual != Color.Transparent))
                        Modifier.border(GrosorBorde, if (abierto) ColorTitulos else ColorBordeActual, forma)
                    else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icono,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Modo de generación", color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelSmall)
                Text(modoActual, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir modos de generación",
                tint = TextoSecundario
            )
        }

        MenuDesplegablePepo(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            val opcionesModo = listOf(
                Triple("Aleatoria", "Caracteres alfanuméricos y símbolos", Icons.Filled.Shuffle),
                Triple("Frase Diceware", "Palabras memorables de alta entropía", Icons.Filled.MenuBook),
                Triple("Por Patrón", "Estructura y plantillas personalizadas", Icons.Filled.Pattern)
            )

            opcionesModo.forEachIndexed { index, (modo, desc, ic) ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val seleccionado = modoActual == modo
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            ic,
                            contentDescription = null,
                            tint = if (seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Column {
                            Text(
                                modo,
                                color = if (seleccionado) ColorTitulos else TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
                            )
                            Text(
                                desc,
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    trailingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp)) }
                    } else null,
                    onClick = {
                        alSeleccionarModo(modo)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SelectorPlantillaPatron(
    patronActual: String,
    alSeleccionarPlantilla: (String) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaCampo

    val plantillas = listOf(
        "Clave Windows" to "XXXXX-XXXXX-XXXXX-XXXXX-XXXXX",
        "PIN 4 dígitos" to "9999",
        "PIN 6 dígitos" to "999999",
        "Token 16" to "XXXX-XXXX-XXXX-XXXX",
        "Frase + Dígitos" to "w-9999"
    )

    val nombreSeleccionado = plantillas.firstOrNull { it.second == patronActual }?.first ?: "Plantilla personalizada"

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp && (abierto || ColorBordeActual != Color.Transparent))
                        Modifier.border(GrosorBorde, if (abierto) ColorTitulos else ColorBordeActual, forma)
                    else Modifier
                )
                .clickable { abierto = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = if (abierto) ColorIconosInternos else TextoSecundario,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Plantilla rápida", color = if (abierto) ColorTitulos else TextoSecundario, style = MaterialTheme.typography.labelSmall)
                Text(nombreSeleccionado, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), maxLines = 1)
            }
            Icon(
                if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir plantillas rápidas",
                tint = TextoSecundario
            )
        }

        MenuDesplegablePepo(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            plantillas.forEachIndexed { index, (nombre, patron) ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val seleccionado = patronActual == patron
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Pattern,
                            contentDescription = null,
                            tint = if (seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    text = {
                        Column {
                            Text(
                                nombre,
                                color = if (seleccionado) ColorTitulos else TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
                            )
                            Text(
                                patron,
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    trailingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Check, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(18.dp)) }
                    } else null,
                    onClick = {
                        alSeleccionarPlantilla(patron)
                        abierto = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ColumnaInterruptor(
    simbolo: String,
    etiqueta: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { alCambiar(!activo) }
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Text(
            text = simbolo,
            color = if (activo) ColorTitulos else TextoSecundario,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = etiqueta,
            color = if (activo) TextoPrincipal else TextoSecundario,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(Modifier.height(4.dp))
        Switch(
            checked = activo,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorSobreAcento,
                checkedTrackColor = Ambar,
                checkedBorderColor = Ambar,
                uncheckedThumbColor = TextoSecundario,
                uncheckedTrackColor = SuperficieAlta,
                uncheckedBorderColor = TextoSecundario
            )
        )
    }
}

@Composable
private fun coloresSlider() = SliderDefaults.colors(
    thumbColor = Ambar,
    activeTrackColor = Ambar,
    inactiveTrackColor = Borde
)
