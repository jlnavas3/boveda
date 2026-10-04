package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

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
