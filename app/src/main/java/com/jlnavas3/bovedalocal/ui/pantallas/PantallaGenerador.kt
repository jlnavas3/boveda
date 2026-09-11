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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.crypto.Wordlist
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.ContrasenaSlotMachine
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Generador",
            subtitulo = "Aleatoriedad de alta entropía (CSPRNG del sistema)",
            alVolver = { vm.volverAtras() }
        )

        TarjetaPepo {
            EtiquetaSeccion("Resultado")
            Spacer(Modifier.height(12.dp))
            ContrasenaSlotMachine(objetivo = generada, generacion = generacion, haptica = haptica)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                Text("${bits.roundToInt()} bits", color = Menta, style = MaterialTheme.typography.labelLarge)
                Text(
                    "resistiría ${PasswordGenerator.tiempoDeCrackeo(bits)}",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ModoChip("Aleatoria", !opciones.modoFrase && !opciones.modoPatron) {
                opciones = opciones.copy(modoFrase = false, modoPatron = false)
            }
            ModoChip("Frase Diceware (3-12)", opciones.modoFrase) {
                opciones = opciones.copy(modoFrase = true, modoPatron = false)
            }
            ModoChip("Por Patrón", opciones.modoPatron) {
                opciones = opciones.copy(modoFrase = false, modoPatron = true)
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            opciones.modoFrase -> {
                TarjetaPepo {
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
                    Spacer(Modifier.height(8.dp))
                    EtiquetaSeccion("Separador")
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("-" to "Guion (-)", " " to "Espacio", "." to "Punto (.)", "_" to "Guion bajo (_)", "" to "Sin separador").forEach { (sep, label) ->
                            val activo = opciones.separadorFrase == sep
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(SuperficieAlta, SuperficieAlta)))
                                    .clickable {
                                        haptica.tic()
                                        opciones = opciones.copy(separadorFrase = sep)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (activo) ColorSobreAcento else TextoSecundario
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Diccionario español de ${Wordlist.TAMANO} palabras, todo dentro del APK.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            opciones.modoPatron -> {
                TarjetaPepo {
                    EtiquetaSeccion("Plantillas rápidas")
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val plantillas = listOf(
                            "Clave Windows" to "XXXXX-XXXXX-XXXXX-XXXXX-XXXXX",
                            "PIN 4 dígitos" to "9999",
                            "PIN 6 dígitos" to "999999",
                            "Token 16" to "XXXX-XXXX-XXXX-XXXX",
                            "Palabra + Dígitos" to "w-9999"
                        )
                        plantillas.forEach { (nombre, pat) ->
                            val activa = opciones.patron == pat
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(if (activa) DegradadoAmbar else Brush.horizontalGradient(listOf(SuperficieAlta, SuperficieAlta)))
                                    .clickable {
                                        haptica.tic()
                                        opciones = opciones.copy(patron = pat)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    nombre,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (activa) ColorSobreAcento else TextoSecundario
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    CampoPepo(
                        valor = opciones.patron,
                        etiqueta = "Plantilla / Patrón personalizado",
                        alCambiar = { opciones = opciones.copy(patron = it) },
                        monoespaciada = true
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "X: alfanum. mayúscula (A-Z, 0-9) | A: letra mayúscula (A-Z) | a: minúscula (a-z) | 9 o d: dígito (0-9) | w: palabra Diceware",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            else -> {
                TarjetaPepo {
                    EtiquetaSeccion("Longitud: ${opciones.longitud}")
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
                    FilaInterruptor("Mayúsculas", opciones.mayusculas) { opciones = opciones.copy(mayusculas = it) }
                    FilaInterruptor("Minúsculas", opciones.minusculas) { opciones = opciones.copy(minusculas = it) }
                    FilaInterruptor("Dígitos", opciones.digitos) { opciones = opciones.copy(digitos = it) }
                    FilaInterruptor("Símbolos", opciones.simbolos) { opciones = opciones.copy(simbolos = it) }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        BotonColorido(
            texto = "Generar otra",
            color = ColorGenerador,
            icono = Icons.Filled.Refresh
        ) {
            haptica.toque()
            regenerar()
        }
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Copiar contraseña",
            color = ColorSeguridad,
            icono = Icons.Filled.ContentCopy
        ) {
            haptica.exito()
            vm.copiar("Contraseña", generada, sensible = true)
        }
        Spacer(Modifier.height(10.dp))
        BotonBorde("Usar en una entrada nueva") {
            vm.ir(Pantalla.Editar(null, generada))
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun coloresSlider() = SliderDefaults.colors(
    thumbColor = Ambar,
    activeTrackColor = Ambar,
    inactiveTrackColor = Borde
)

@Composable
private fun FilaInterruptor(texto: String, activo: Boolean, alCambiar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(texto, color = TextoPrincipal, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
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
private fun ModoChip(texto: String, activo: Boolean, alPulsar: () -> Unit) {
    val forma = FormaBoton
    Box(
        modifier = Modifier
            .clip(forma)
            .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alPulsar() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(texto, color = if (activo) ColorSobreAcento else TextoSecundario, style = MaterialTheme.typography.bodyMedium)
    }
}
