package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Warning
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesSenuelo(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var datosSenuelo by remember { mutableStateOf(BovedaSenuelo.cargar(contexto)) }
    var activo by remember { mutableStateOf(datosSenuelo.activo) }
    var pinCoaccion by remember { mutableStateOf("") }
    var verPin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidiana)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraPantalla(
            titulo = "Bóveda Señuelo (Coacción)",
            subtitulo = "Protección anti-extorsión y apertura señuelo transparente",
            alVolver = { vm.volverAtras() }
        )

        // Tarjeta Explicativa de Seguridad
        ContenedorTarjeta(paddingInterno = 14.dp) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Security,
                    contentDescription = null,
                    tint = ColorSeguridad,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.size(12.dp))
                Column {
                    Text(
                        text = "¿Cómo funciona?",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Si alguien te obliga a desbloquear la app, introduce tu PIN de coacción en la pantalla principal. Bóveda Local se abrirá normalmente pero mostrará solo cuentas señuelo inofensivas. Tu bóveda real permanece 100% cifrada e inaccesible.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Configuración del PIN de Coacción
        ContenedorTarjeta(paddingInterno = 14.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = "Activar Bóveda Señuelo",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (activo) "Habilitada (se activa con su propio PIN al desbloquear)" else "Desactivada",
                        color = if (activo) ColorSeguridad else TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = activo,
                    onCheckedChange = { nuevoEstado ->
                        haptica.tic()
                        activo = nuevoEstado
                        if (!nuevoEstado) {
                            BovedaSenuelo.desactivar(contexto)
                            datosSenuelo = BovedaSenuelo.cargar(contexto)
                            vm.avisar("Bóveda señuelo desactivada")
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ColorSobreAcento,
                        checkedTrackColor = ColorSeguridad,
                        checkedBorderColor = ColorSeguridad,
                        uncheckedThumbColor = TextoSecundario,
                        uncheckedTrackColor = SuperficieAlta,
                        uncheckedBorderColor = TextoSecundario
                    )
                )
            }

            if (activo) {
                Spacer(Modifier.height(14.dp))

                Text(
                    text = if (datosSenuelo.hashHex.isNotBlank()) "Cambiar PIN / Contraseña de Coacción" else "Definir PIN de Coacción",
                    color = ColorAcento,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(6.dp))

                CampoBoveda(
                    valor = pinCoaccion,
                    etiqueta = "PIN o clave de coacción (ej. 1984)",
                    alCambiar = { pinCoaccion = it },
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = { verPin = !verPin }
                )

                Spacer(Modifier.height(12.dp))

                BotonColorido(
                    texto = "Guardar PIN de coacción",
                    color = ColorSeguridad,
                    icono = Icons.Filled.Check
                ) {
                    if (pinCoaccion.isBlank()) {
                        haptica.error()
                        vm.avisar("Introduce un PIN de coacción válido")
                    } else {
                        haptica.exito()
                        BovedaSenuelo.configurar(contexto, pinCoaccion, poblarCuentasEjemplo = datosSenuelo.entradas.isEmpty())
                        datosSenuelo = BovedaSenuelo.cargar(contexto)
                        pinCoaccion = ""
                        vm.avisar("Bóveda señuelo configurada correctamente")
                    }
                }
            }
        }

        // Alerta de Biometría si la bóveda señuelo está activada
        if (activo) {
            Spacer(Modifier.height(14.dp))
            if (ajustes.biometriaActiva) {
                androidx.compose.material3.Surface(
                    color = Peligro.copy(alpha = 0.10f),
                    shape = com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Peligro.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Warning,
                                contentDescription = null,
                                tint = Peligro,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Riesgo de coacción: Huella activada",
                                color = Peligro,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "El sensor biométrico de Android abrirá SIEMPRE tu bóveda ORIGINAL (el hardware del móvil no distingue situaciones de coacción física). Si un atacante te fuerza a poner el dedo, se revelarán tus claves reales.\n\nPara garantizar la máxima protección de la Bóveda Señuelo, te recomendamos encarecidamente desactivar el acceso por huella.",
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(12.dp))
                        BotonColorido(
                            texto = "Desactivar acceso por huella dactilar",
                            color = Peligro,
                            icono = Icons.Filled.Fingerprint
                        ) {
                            haptica.exito()
                            vm.repositorio.desactivarBiometria()
                            vm.avisar("Acceso por huella desactivado")
                        }
                    }
                }
            } else {
                androidx.compose.material3.Surface(
                    color = ColorSeguridad.copy(alpha = 0.08f),
                    shape = com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ColorSeguridad.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = ColorSeguridad,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Huella desactivada: Seguridad óptima. Solo se puede acceder mediante contraseña/PIN.",
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Estado y Cuentas Señuelo
        if (activo) {
            ContenedorTarjeta(paddingInterno = 14.dp) {
                Text(
                    text = "Cuentas Simuladas en Bóveda Señuelo",
                    color = ColorAcento,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Actualmente hay ${datosSenuelo.entradas.size} cuentas simuladas almacenadas en la bóveda señuelo.",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(12.dp))

                BotonColorido(
                    texto = "Restablecer cuentas de ejemplo",
                    color = Ambar,
                    icono = Icons.Filled.Refresh
                ) {
                    haptica.toque()
                    val ejemplos = BovedaSenuelo.cuentasEjemplo()
                    BovedaSenuelo.guardarEntradasSenuelo(contexto, ejemplos)
                    datosSenuelo = BovedaSenuelo.cargar(contexto)
                    vm.avisar("Cuentas de ejemplo restablecidas (${ejemplos.size})")
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
