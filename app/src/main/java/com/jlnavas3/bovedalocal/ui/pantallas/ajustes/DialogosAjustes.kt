package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

private val colorDialogo: Color @Composable get() = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF)

@Composable
fun DialogoModoCompatible(
    motivo: String,
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "Modo compatible",
        mensaje = motivo + "\n\nEn este modo la huella o el PIN los comprueba Android y la app abre la bóveda. " +
            "La clave maestra sigue envuelta por el Keystore y no sale del móvil, pero no queda atada " +
            "al chip como en el modo fuerte: es algo más débil. Tu contraseña maestra sigue siendo la " +
            "única llave real, y puedes volver al modo fuerte cuando quieras.",
        textoConfirmar = "Activar modo compatible",
        textoCancelar = "Ahora no",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}

@Composable
fun DialogoCambioMaestra(
    alDescartar: () -> Unit,
    alConfirmar: (actual: String, nueva: String) -> Unit
) {
    var actualMaestra by remember { mutableStateOf("") }
    var nuevaMaestra by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = alDescartar,
        shape = RoundedCornerShape(20.dp),
        containerColor = colorDialogo,
        tonalElevation = 0.dp,
        title = { Text("Cambiar contraseña maestra", color = TextoPrincipal) },
        text = {
            Column {
                CampoBoveda(valor = actualMaestra, etiqueta = "Contraseña actual", alCambiar = { actualMaestra = it }, esContrasena = true)
                Spacer(Modifier.height(10.dp))
                CampoBoveda(valor = nuevaMaestra, etiqueta = "Nueva contraseña", alCambiar = { nuevaMaestra = it }, esContrasena = true)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Se vuelve a cifrar toda la bóveda y se desactiva la huella.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = nuevaMaestra.length >= 10 && actualMaestra.isNotEmpty(),
                onClick = {
                    alConfirmar(actualMaestra, nuevaMaestra)
                }
            ) { Text("Cambiar", color = ColorAcento) }
        },
        dismissButton = { TextButton(onClick = alDescartar) { Text("Cancelar") } }
    )
}

@Composable
fun DialogoImportarCsv(
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "Importar CSV",
        mensaje = "El CSV que exportan Google, Chrome, Bitwarden o LastPass va sin cifrar: cualquiera que " +
            "lo abra ve las contraseñas en claro. Elige el archivo, se cifra al entrar en la bóveda, " +
            "y después borra ese CSV de donde lo tengas guardado.",
        textoConfirmar = "Elegir archivo",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}

@Composable
fun DialogoBorradoManualCsv(
    rutaArchivo: String,
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "Eliminar archivo sin cifrar",
        mensaje = "Por directivas de seguridad de Android, la aplicación no cuenta con permisos directos del sistema de archivos para borrar el documento automáticamente.\n\n" +
            "Te recomendamos FUERTEMENTE abrir la app 'Archivos' o 'Descargas' de tu teléfono y eliminar manualmente el archivo:\n\n" +
            "📁 $rutaArchivo\n\n" +
            "Este archivo contiene todas tus contraseñas de Google en texto plano y no debe permanecer en el almacenamiento del dispositivo.",
        textoConfirmar = "Ya lo he eliminado / Entendido",
        textoCancelar = "Cerrar",
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        iconoHeader = Icons.Filled.Warning,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}

@Composable
fun DialogoBorrarBoveda(
    alDescartar: () -> Unit,
    alConfirmar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¿Borrar la bóveda entera?",
        mensaje = "Se elimina el archivo cifrado y la clave de la huella. Si no tienes copia, no hay vuelta atrás.",
        textoConfirmar = "Borrar todo",
        textoCancelar = "Cancelar",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.DeleteForever,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}

@Composable
fun DialogoContrasena(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    alConfirmar: (String) -> Unit,
    alCancelar: () -> Unit
) {
    var valor by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    val esOscuro = isSystemInDarkTheme()
    val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
    AlertDialog(
        onDismissRequest = alCancelar,
        containerColor = colorDialogo,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(20.dp),
        title = { Text(titulo, color = TextoPrincipal) },
        text = {
            Column {
                Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                CampoBoveda(
                    valor = valor,
                    etiqueta = "Contraseña",
                    alCambiar = { valor = it },
                    esContrasena = true,
                    mostrarContrasena = mostrarContrasena,
                    alAlternarMostrarContrasena = { mostrarContrasena = !mostrarContrasena }
                )
            }
        },
        confirmButton = {
            TextButton(enabled = valor.length >= 8, onClick = { alConfirmar(valor) }) {
                Text(textoBoton, color = ColorAcento)
            }
        },
        dismissButton = { TextButton(onClick = alCancelar) { Text("Cancelar") } }
    )
}
