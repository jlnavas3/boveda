package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionCopiaSeguridad(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    alExportar: () -> Unit,
    alImportar: () -> Unit
) {
    TarjetaAjuste(
        titulo = "Copia de seguridad",
        icono = Icons.Filled.Backup,
        descripcion = "Exporta, importa y configura avisos para no olvidar tus copias.",
        colorIcono = ColorExportacion
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "El archivo exportado va cifrado con su propia contraseña y con Argon2id. Sin esa contraseña es ruido.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        BotonColorido(
            texto = "Exportar bóveda cifrada",
            color = ColorExportacion,
            icono = Icons.Filled.FileDownload
        ) { alExportar() }
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Importar bóveda cifrada",
            color = ColorExportacion,
            icono = Icons.Filled.FileUpload
        ) { alImportar() }
        Spacer(Modifier.height(14.dp))
        Text(
            "Recordarme hacer una copia",
            color = TextoPrincipal,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Un aviso arriba de la lista si pasa este tiempo sin exportar. Todo se calcula en el móvil.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))
        SelectorAjuste(
            titulo = "Frecuencia del recordatorio",
            icono = Icons.Filled.Backup,
            seleccionado = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.first { it.first == ajustes.recordatorioExportacionDias }.second,
            opciones = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.map { (valor, etiqueta) ->
                OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Backup)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarRecordatorioExportacion(valor.toInt()) }
        )
        Spacer(Modifier.height(14.dp))
        BotonColorido(
            texto = "Kit de emergencia físico (Imprimir / PDF)",
            color = ColorAcento,
            icono = Icons.Filled.Print
        ) {
            haptica.toque()
            vm.ir(Pantalla.KitEmergencia)
        }
    }
}
