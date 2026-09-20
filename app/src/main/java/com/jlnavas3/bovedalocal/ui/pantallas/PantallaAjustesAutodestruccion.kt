package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.jlnavas3.bovedalocal.data.PinAutodestruccion
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
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaAjustesAutodestruccion(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var datosAutodestruccion by remember { mutableStateOf(PinAutodestruccion.cargar(contexto)) }
    var activo by remember { mutableStateOf(datosAutodestruccion.activo) }
    var pinNuevo by remember { mutableStateOf("") }
    var pinConfirmar by remember { mutableStateOf("") }
    var verPin by remember { mutableStateOf(false) }

    val reqExplicacion = remember { BringIntoViewRequester() }
    val reqPin = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "01.3.1" -> reqPin.bringIntoView()
                seccionDestino.startsWith("01.3.") && seccionDestino != "01.3" -> reqPin.bringIntoView()
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
                titulo = "PIN de autodestrucción",
                idEtiqueta = "01.3",
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
                DescripcionPantalla(subtitulo = "Borrado permanente ante coerción o emergencia extrema")
                Spacer(Modifier.height(10.dp))

                // Banner de Alerta Crítica (Peligro)
                ComponenteAlerta(
                    tipo = TipoAlerta.DANGER,
                    titulo = "AVISO CRÍTICO: ACCIÓN TOTALMENTE IRREVERSIBLE",
                    icono = Icons.Filled.Warning,
                    contenido = {
                        Text(
                            text = "Si introduces este PIN en la pantalla de desbloqueo, la bóveda local, todas sus contraseñas, notas, passkeys y registros criptográficos serán destruidos de forma instantánea y permanente en este dispositivo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoPrincipal,
                            lineHeight = 17.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "⚠️ ES IMPRESCINDIBLE TENER UN RESPALDO EXTERNO:\nAsegúrate de contar con una Copia de Seguridad Cifrada (.boveda) o un Kit de Emergencia resguardado en otro lugar antes de habilitar esta opción.",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = Peligro,
                            lineHeight = 16.sp
                        )
                    }
                )

                Spacer(Modifier.height(16.dp))

                // Tarjeta Explicativa
                ComponenteGrupo(
                    etiqueta = "¿Cómo funciona la autodestrucción?",
                    idGrupo = "01.3.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Mecanismo defensivo de borrado seguro"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = Peligro,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Mecanismo defensivo de borrado seguro",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "1. Ante una situación de coerción donde se te obligue a abrir la bóveda y no sea suficiente una Bóveda Señuelo, puedes introducir este PIN de emergencia especial.\n\n" +
                                "2. El sistema simulará procesar la solicitud pero inmediatamente sanitizará la memoria RAM (zeroizing), borrará definitivamente el archivo de la bóveda del almacenamiento y reiniciará la app al estado inicial de bienvenida (onboarding).\n\n" +
                                "3. Para un observador externo o atacante, los datos quedan completamente inaccesibles e irrecuperables en el dispositivo.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Configuración y formulario del PIN
                ComponenteGrupo(
                    etiqueta = "Configuración del PIN",
                    idGrupo = "01.3.G2",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Activar PIN de Autodestrucción",
                        icono = Icons.Filled.DeleteForever,
                        colorIcono = Peligro,
                        activo = activo,
                        idFila = "01.3.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        colorActivo = Peligro,
                        alCambiar = { nuevoEstado ->
                            haptica.tic()
                            activo = nuevoEstado
                            if (!nuevoEstado) {
                                PinAutodestruccion.desactivar(contexto)
                                datosAutodestruccion = PinAutodestruccion.cargar(contexto)
                                vm.avisar("PIN de autodestrucción desactivado")
                            }
                        }
                    )

                    if (activo) {
                        SeparadorFilaSimple()
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (datosAutodestruccion.hashHex.isNotBlank()) "Cambiar PIN de Autodestrucción" else "Definir nuevo PIN de Autodestrucción",
                                color = ColorAcento,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(Modifier.height(8.dp))

                            ComponenteCampoTexto(
                                valor = pinNuevo,
                                etiqueta = "PIN de autodestrucción (mínimo 4 caracteres)",
                                alCambiar = { pinNuevo = it },
                                tipo = TipoCampoTexto.NUMERICO,
                                esContrasena = true,
                                mostrarContrasena = verPin,
                                alAlternarMostrarContrasena = { verPin = !verPin },
                                mostrarIcono = true
                            )

                            Spacer(Modifier.height(8.dp))

                            ComponenteCampoTexto(
                                valor = pinConfirmar,
                                etiqueta = "Confirmar PIN de autodestrucción",
                                alCambiar = { pinConfirmar = it },
                                tipo = TipoCampoTexto.NUMERICO,
                                esContrasena = true,
                                mostrarContrasena = verPin,
                                alAlternarMostrarContrasena = { verPin = !verPin },
                                mostrarIcono = true
                            )

                            if (datosAutodestruccion.hashHex.isNotBlank()) {
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = Peligro,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "PIN configurado y activo en el dispositivo",
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        SeparadorFilaSimple()

                        ComponenteBotonFila(
                            titulo = "Guardar PIN de autodestrucción",
                            icono = Icons.Filled.DeleteForever,
                            colorIcono = Peligro,
                            idFila = "01.3.2",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = {
                                when {
                                    pinNuevo.length < 4 -> {
                                        haptica.error()
                                        vm.avisar("El PIN debe tener al menos 4 caracteres")
                                    }
                                    pinNuevo != pinConfirmar -> {
                                        haptica.error()
                                        vm.avisar("Los PIN ingresados no coinciden")
                                    }
                                    BovedaSenuelo.esPinCoaccion(contexto, pinNuevo) -> {
                                        haptica.error()
                                        vm.avisar("El PIN no puede ser idéntico al de la Bóveda Señuelo")
                                    }
                                    else -> {
                                        haptica.exito()
                                        PinAutodestruccion.configurar(contexto, pinNuevo)
                                        datosAutodestruccion = PinAutodestruccion.cargar(contexto)
                                        pinNuevo = ""
                                        pinConfirmar = ""
                                        vm.avisar("PIN de autodestrucción configurado correctamente")
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
