package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.GestorBackupAutomatico
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoPiePagina
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
            TextoSubtitulo(
                texto = "Se exportarán $cantidad ${if (cantidad == 1) "entrada cifrada" else "entradas cifradas"}. Configura el nombre del archivo y la clave de cifrado:"
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
            TextoPiePagina(
                texto = "Nombre final: $nombreFinalResuelto",
                color = ColorAcento,
                monoespaciada = true
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
                    TextoCuerpo(
                        texto = "Guardar en carpeta de copias automáticas",
                        tamano = TamanoCuerpo.PEQUENO
                    )
                    TextoPiePagina(
                        texto = if (guardarEnDirectorioAuto) "Guardará directamente en Descargas/BovedaLocal/Backups" else "Solicitará ruta con el explorador de archivos"
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
                    TextoCuerpo(
                        texto = "Usar clave de copia automática",
                        tamano = TamanoCuerpo.PEQUENO,
                        color = if (tieneAutoPassword) TextoPrincipal else TextoSecundario.copy(alpha = 0.5f)
                    )
                    if (!tieneAutoPassword) {
                        TextoPiePagina(
                            texto = "No configurada en Ajustes"
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
