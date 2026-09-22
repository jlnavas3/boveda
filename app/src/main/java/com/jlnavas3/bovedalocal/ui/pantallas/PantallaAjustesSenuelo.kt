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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaAjustesSenuelo(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var datosSenuelo by remember { mutableStateOf(BovedaSenuelo.cargar(contexto)) }
    var activo by remember { mutableStateOf(datosSenuelo.activo) }
    var pinCoaccion by remember { mutableStateOf("") }
    var verPin by remember { mutableStateOf(false) }

    val reqExplicacion = remember { BringIntoViewRequester() }
    val reqPin = remember { BringIntoViewRequester() }
    val reqCuentas = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "01.1.1" -> reqExplicacion.bringIntoView()
                seccionDestino == "01.1.2" -> reqPin.bringIntoView()
                seccionDestino == "01.1.3" -> reqCuentas.bringIntoView()
                seccionDestino.startsWith("01.1.") && seccionDestino != "01.1" -> reqPin.bringIntoView()
            }
        }
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Bóveda señuelo",
                idEtiqueta = "01.2",
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
                DescripcionPantalla(subtitulo = "Protección anti-extorsión y apertura señuelo transparente")
                Spacer(Modifier.height(10.dp))

                // Tarjeta Explicativa
                ComponenteGrupo(
                    etiqueta = "¿Cómo funciona?",
                    idGrupo = "01.2.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Apertura ficticia transparente ante coacción o amenaza física"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = ColorSeguridad,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Protección anti-extorsión física",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Si alguien te obliga a desbloquear la app, introduce tu PIN de coacción en la pantalla principal. Bóveda Local se abrirá normalmente pero mostrará solo cuentas señuelo inofensivas. Tu bóveda real permanece 100% cifrada e inaccesible.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Configuración del PIN de Coacción
                ComponenteGrupo(
                    etiqueta = "Configuración del PIN de coacción",
                    idGrupo = "01.2.G2",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Activar Bóveda Señuelo",
                        icono = Icons.Filled.Security,
                        colorIcono = ColorSeguridad,
                        activo = activo,
                        idFila = "01.2.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        colorActivo = ColorSeguridad,
                        alCambiar = { nuevoEstado ->
                            haptica.tic()
                            activo = nuevoEstado
                            if (!nuevoEstado) {
                                BovedaSenuelo.desactivar(contexto)
                                datosSenuelo = BovedaSenuelo.cargar(contexto)
                                vm.avisar("Bóveda señuelo desactivada")
                            }
                        }
                    )

                    if (activo) {
                        SeparadorFilaSimple()
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (datosSenuelo.hashHex.isNotBlank()) "Cambiar PIN / Clave de coacción" else "Definir PIN de coacción",
                                color = ColorAcento,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(Modifier.height(8.dp))

                            ComponenteCampoTexto(
                                valor = pinCoaccion,
                                etiqueta = "PIN o clave de coacción (ej. 1984)",
                                alCambiar = { pinCoaccion = it },
                                tipo = TipoCampoTexto.NUMERICO,
                                esContrasena = true,
                                mostrarContrasena = verPin,
                                alAlternarMostrarContrasena = { verPin = !verPin },
                                mostrarIcono = true
                            )
                        }

                        SeparadorFilaSimple()

                        ComponenteBotonFila(
                            titulo = "Guardar PIN de coacción",
                            icono = Icons.Filled.Check,
                            colorIcono = ColorSeguridad,
                            idFila = "01.2.2",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = {
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
                        )
                    }
                }

                // Alerta de Biometría si la bóveda señuelo está activada
                if (activo) {
                    Spacer(Modifier.height(16.dp))
                    if (ajustes.biometriaActiva) {
                        ComponenteAlerta(
                            tipo = TipoAlerta.DANGER,
                            titulo = "Riesgo de coacción: Huella activada",
                            mensaje = "El sensor biométrico de Android abrirá SIEMPRE tu bóveda ORIGINAL (el hardware del móvil no distingue situaciones de coacción física). Si un atacante te fuerza a poner el dedo, se revelarán tus claves reales.\n\nPara garantizar la máxima protección de la Bóveda Señuelo, te recomendamos desactivar el acceso por huella.",
                            icono = Icons.Filled.Warning,
                            accion = {
                                ComponenteBotonFila(
                                    titulo = "Desactivar acceso por huella dactilar",
                                    icono = Icons.Filled.Fingerprint,
                                    colorIcono = Peligro,
                                    alPulsar = {
                                        haptica.exito()
                                        vm.repositorio.desactivarBiometria()
                                        vm.avisar("Acceso por huella desactivado")
                                    }
                                )
                            }
                        )
                    } else {
                        ComponenteAlerta(
                            tipo = TipoAlerta.SUCCESS,
                            titulo = "Huella desactivada: Seguridad óptima",
                            mensaje = "Solo se puede acceder mediante contraseña/PIN. La bóveda señuelo protegerá tus datos reales frente a coacción física.",
                            icono = Icons.Filled.CheckCircle
                        )
                    }
                }

                // Estado y Cuentas Señuelo
                if (activo) {
                    Spacer(Modifier.height(16.dp))
                    ComponenteGrupo(
                        etiqueta = "Cuentas simuladas en señuelo",
                        idGrupo = "01.2.G3",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Actualmente hay ${datosSenuelo.entradas.size} cuentas simuladas almacenadas en la bóveda señuelo.",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Estas cuentas se muestran al ingresar el PIN de coacción en el desbloqueo.",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        SeparadorFilaSimple()

                        ComponenteBotonFila(
                            titulo = "Restablecer cuentas de ejemplo",
                            icono = Icons.Filled.Refresh,
                            colorIcono = ColorIconosInternos,
                            idFila = "01.2.3",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = {
                                haptica.toque()
                                val ejemplos = BovedaSenuelo.cuentasEjemplo()
                                BovedaSenuelo.guardarEntradasSenuelo(contexto, ejemplos)
                                datosSenuelo = BovedaSenuelo.cargar(contexto)
                                vm.avisar("Cuentas de ejemplo restablecidas (${ejemplos.size})")
                            }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
