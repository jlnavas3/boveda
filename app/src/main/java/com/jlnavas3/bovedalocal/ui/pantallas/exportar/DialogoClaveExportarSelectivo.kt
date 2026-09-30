package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Diálogo modal para ingresar la contraseña de cifrado del archivo .bvda, patrón de nombre
 * y la opción de guardado directo en la carpeta de copias de seguridad automáticas.
 */
@Composable
fun DialogoClaveExportarSelectivo(
    cantidad: Int,
    autoPassword: String,
    alDescartar: () -> Unit,
    alConfirmar: (String, String, Boolean) -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var patronNombre by remember { mutableStateOf("{99}-selectivo-{FECHA}") }
    var clavePassword by remember { mutableStateOf("") }
    var mostrarClave by remember { mutableStateOf(false) }
    var usarAutoPassword by remember { mutableStateOf(false) }
    var guardarEnDirectorioAuto by remember { mutableStateOf(true) }

    val tieneAutoPassword = autoPassword.isNotBlank()
    val nombreFinalResuelto = remember(patronNombre) {
        val base = GestorBackupAutomatico.resolverNombreArchivo(patronNombre)
        if (base.endsWith(".bvda", ignoreCase = true)) base else "$base.bvda"
    }

    val claveValida = clavePassword.isNotBlank()

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Exportación selectiva (.bvda)",
        botonConfirmar = {
            BotonTextoBoveda(
                texto = "Exportar .bvda",
                alPulsar = {
                    if (claveValida) {
                        haptica.exito()
                        alConfirmar(nombreFinalResuelto, clavePassword, guardarEnDirectorioAuto)
                    }
                },
                tipo = TipoBotonTexto.PRIMARIO,
                habilitado = claveValida
            )
        },
        botonDescartar = {
            BotonTextoBoveda(
                texto = "Cancelar",
                alPulsar = alDescartar,
                tipo = TipoBotonTexto.SECUNDARIO
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Se exportarán $cantidad ${if (cantidad == 1) "entrada cifrada" else "entradas cifradas"}. Configura el nombre del archivo y la clave de cifrado:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )

                Spacer(Modifier.height(14.dp))

                // Campo Patrón de Nombre de Archivo
                ComponenteCampoTexto(
                    valor = patronNombre,
                    etiqueta = "Patrón del nombre de archivo",
                    alCambiar = { patronNombre = it },
                    placeholder = "{99}-selectivo-{FECHA}"
                )

                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Nombre final: $nombreFinalResuelto",
                    style = EstiloMono.copy(fontSize = 10.sp),
                    color = ColorAcento
                )

                Spacer(Modifier.height(12.dp))

                // Campo Contraseña
                ComponenteCampoTexto(
                    valor = clavePassword,
                    etiqueta = "Contraseña de cifrado",
                    alCambiar = {
                        clavePassword = it
                        usarAutoPassword = (it == autoPassword)
                    },
                    esContrasena = true,
                    mostrarContrasena = mostrarClave,
                    alAlternarMostrarContrasena = {
                        haptica.tic()
                        mostrarClave = !mostrarClave
                    }
                )

                Spacer(Modifier.height(10.dp))

                // Fila Switch Guardar Directamente en Carpeta de Copias Automáticas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaPequena)
                        .clickable {
                            haptica.tic()
                            guardarEnDirectorioAuto = !guardarEnDirectorioAuto
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Guardar en carpeta de copias automáticas",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = TextoPrincipal
                        )
                        Text(
                            text = if (guardarEnDirectorioAuto) "Guardará directamente en Descargas/BovedaLocalBackups" else "Solicitará ruta con el explorador de archivos",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                    }

                    SwitchBoveda(
                        checked = guardarEnDirectorioAuto,
                        onCheckedChange = { checked ->
                            haptica.tic()
                            guardarEnDirectorioAuto = checked
                        }
                    )
                }

                Spacer(Modifier.height(6.dp))

                // Fila Switch Contraseña Automática
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaPequena)
                        .clickable(enabled = tieneAutoPassword) {
                            if (tieneAutoPassword) {
                                haptica.tic()
                                usarAutoPassword = !usarAutoPassword
                                clavePassword = if (usarAutoPassword) autoPassword else ""
                            }
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Usar clave de copia automática",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = if (tieneAutoPassword) TextoPrincipal else TextoSecundario.copy(alpha = 0.5f)
                        )
                        if (!tieneAutoPassword) {
                            Text(
                                text = "No configurada en Ajustes",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextoSecundario.copy(alpha = 0.5f)
                            )
                        }
                    }

                    SwitchBoveda(
                        checked = usarAutoPassword && tieneAutoPassword,
                        onCheckedChange = { checked ->
                            if (tieneAutoPassword) {
                                haptica.tic()
                                usarAutoPassword = checked
                                clavePassword = if (checked) autoPassword else ""
                            }
                        }
                    )
                }
        }
    }
}
