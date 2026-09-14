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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.data.PinAutodestruccion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesAutodestruccion(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var datosAutodestruccion by remember { mutableStateOf(PinAutodestruccion.cargar(contexto)) }
    var activo by remember { mutableStateOf(datosAutodestruccion.activo) }
    var pinNuevo by remember { mutableStateOf("") }
    var pinConfirmar by remember { mutableStateOf("") }
    var verPin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidiana)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraPantalla(
            titulo = "PIN de Autodestrucción",
            subtitulo = "Borrado permanente ante coerción o emergencia extrema",
            alVolver = { vm.volverAtras() }
        )

        // Banner de Alerta Crítica (Peligro)
        Surface(
            color = Peligro.copy(alpha = 0.12f),
            shape = FormaTarjeta,
            border = BorderStroke(1.dp, Peligro.copy(alpha = 0.40f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = Peligro,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AVISO CRÍTICO: ACCIÓN TOTALMENTE IRREVERSIBLE",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Peligro
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Si introduces este PIN en la pantalla de desbloqueo, la bóveda local, todas sus contraseñas, notas, passkeys y registros criptográficos serán destruidos de forma instantánea y permanente en este dispositivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoPrincipal,
                        lineHeight = 17.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "⚠️ ES IMPRESCINDIBLE TENER UN RESPALDO EXTERNO:\nAsegúrate de contar con una Copia de Seguridad Cifrada (.boveda) o un Kit de Emergencia Físico resguardado en otro lugar o dispositivo antes de habilitar esta protección.",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Peligro,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Tarjeta Explicativa Desplegable
        TarjetaBovedaDesplegable(
            titulo = "¿Cómo funciona la Autodestrucción?",
            descripcion = "Mecanismo defensivo de borrado seguro ante amenaza física",
            icono = Icons.Filled.Security,
            colorIcono = Peligro,
            inicialmenteAbierta = false
        ) {
            Text(
                text = "1. Ante una situación de coerción donde se te obligue a abrir la bóveda y no sea suficiente una Bóveda Señuelo, puedes introducir este PIN de emergencia especial.\n\n" +
                    "2. El sistema simulará procesar la solicitud pero inmediatamente sanitizará la memoria RAM (zeroizing), borrará definitivamente el archivo de la bóveda del almacenamiento y reiniciará la app al estado inicial de bienvenida (onboarding).\n\n" +
                    "3. Para un observador externo o atacante, los datos quedan completamente inaccesibles e irrecuperables en el dispositivo.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
        }

        Spacer(Modifier.height(14.dp))

        // Configuración y formulario del PIN
        ContenedorTarjeta(paddingInterno = 16.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = "Activar PIN de Autodestrucción",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (activo) "Habilitado (borrará la bóveda si se introduce al desbloquear)" else "Desactivado",
                        color = if (activo) Peligro else TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = activo,
                    onCheckedChange = { nuevoEstado ->
                        haptica.tic()
                        activo = nuevoEstado
                        if (!nuevoEstado) {
                            PinAutodestruccion.desactivar(contexto)
                            datosAutodestruccion = PinAutodestruccion.cargar(contexto)
                            vm.avisar("PIN de autodestrucción desactivado")
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ColorSobreAcento,
                        checkedTrackColor = Peligro,
                        checkedBorderColor = Peligro,
                        uncheckedThumbColor = TextoSecundario,
                        uncheckedTrackColor = SuperficieAlta,
                        uncheckedBorderColor = TextoSecundario
                    )
                )
            }

            if (activo) {
                Spacer(Modifier.height(14.dp))

                Text(
                    text = if (datosAutodestruccion.hashHex.isNotBlank()) "Cambiar PIN de Autodestrucción" else "Definir nuevo PIN de Autodestrucción",
                    color = ColorAcento,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(8.dp))

                CampoBoveda(
                    valor = pinNuevo,
                    etiqueta = "PIN de autodestrucción (mínimo 4 caracteres)",
                    alCambiar = { pinNuevo = it },
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = { verPin = !verPin }
                )

                Spacer(Modifier.height(8.dp))

                CampoBoveda(
                    valor = pinConfirmar,
                    etiqueta = "Confirmar PIN de autodestrucción",
                    alCambiar = { pinConfirmar = it },
                    esContrasena = true,
                    mostrarContrasena = verPin,
                    alAlternarMostrarContrasena = { verPin = !verPin }
                )

                Spacer(Modifier.height(12.dp))

                BotonColorido(
                    texto = "Guardar PIN de autodestrucción",
                    color = Peligro,
                    icono = Icons.Filled.DeleteForever
                ) {
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
        }

        Spacer(Modifier.height(24.dp))
    }
}
