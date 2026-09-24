package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SeccionCopiaSeguridad(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    alExportar: () -> Unit,
    alImportar: () -> Unit,
    inicialmenteAbierta: Boolean = false,
    seccionDestino: String? = null
) {
    var mostrarPassBackupAuto by remember { mutableStateOf(false) }

    TarjetaAjuste(
        titulo = "Copia de seguridad",
        icono = Icons.Filled.Backup,
        descripcion = "Exporta, importa y configura avisos y copias automáticas locales.",
        colorIcono = ColorExportacion,
        inicialmenteAbierta = inicialmenteAbierta || (seccionDestino != null && seccionDestino.startsWith("06")),
        idEtiqueta = "06",
        mostrarId = ajustes.mostrarIdsAjustes
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
            texto = "Exportación selectiva",
            color = ColorExportacion,
            icono = Icons.Filled.FileDownload
        ) { vm.ir(Pantalla.ExportarSelectivo("todos")) }
        Spacer(Modifier.height(10.dp))
        BotonColorido(
            texto = "Importar bóveda cifrada",
            color = ColorExportacion,
            icono = Icons.Filled.FileUpload
        ) { alImportar() }

        Spacer(Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Copia automática local rotativa",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            if (ajustes.mostrarIdsAjustes) {
                Box(
                    modifier = Modifier
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema(ColorExportacion))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "06.1",
                        style = com.jlnavas3.bovedalocal.ui.theme.EstiloMono.copy(fontSize = 8.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema(ColorExportacion)
                    )
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Guarda copias cifradas periódicas en la carpeta de Descargas de este móvil y conserva automáticamente las más recientes.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(10.dp))

        val seleccionadoFrecuencia = AlmacenAjustes.OPCIONES_FRECUENCIA_BACKUP_AUTO
            .find { it.first == ajustes.backupAutoFrecuenciaDias }?.second
            ?: "Nunca"

        val opcionesFrecuencia = AlmacenAjustes.OPCIONES_FRECUENCIA_BACKUP_AUTO.map { (dias, etiqueta) ->
            val icono = when (dias) {
                0 -> Icons.Filled.Block
                1 -> Icons.Filled.Today
                7 -> Icons.Filled.DateRange
                14 -> Icons.Filled.CalendarMonth
                30 -> Icons.Filled.EventRepeat
                60 -> Icons.Filled.Event
                else -> Icons.Filled.CalendarToday
            }
            OpcionAjuste(dias.toString(), etiqueta, icono)
        }

        SelectorAjuste(
            titulo = "Frecuencia de copia automática",
            icono = Icons.Filled.Autorenew,
            seleccionado = seleccionadoFrecuencia,
            opciones = opcionesFrecuencia,
            alSeleccionar = { valor ->
                haptica.tic()
                vm.ajustarBackupAutoFrecuenciaDias(valor.toInt())
            }
        )

        if (ajustes.backupAutoFrecuenciaDias > 0) {
            val seleccionadoMaxCopias = AlmacenAjustes.OPCIONES_MAX_COPIAS_BACKUP_AUTO
                .find { it.first == ajustes.backupAutoMaxCopias }?.second
                ?: "${ajustes.backupAutoMaxCopias} copias"

            val opcionesMaxCopias = AlmacenAjustes.OPCIONES_MAX_COPIAS_BACKUP_AUTO.map { (max, etiqueta) ->
                OpcionAjuste(max.toString(), etiqueta, Icons.Filled.Backup)
            }

            SelectorAjuste(
                titulo = "Copias a respaldar",
                icono = Icons.Filled.Backup,
                seleccionado = seleccionadoMaxCopias,
                opciones = opcionesMaxCopias,
                alSeleccionar = { valor ->
                    haptica.tic()
                    vm.ajustarBackupAutoMaxCopias(valor.toInt())
                }
            )

            com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto(
                valor = ajustes.backupAutoPasswordCifrado,
                etiqueta = "Contraseña para la copia automática",
                alCambiar = { vm.ajustarBackupAutoPasswordCifrado(it) },
                tipo = com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto.CONTRASENA,
                mostrarContrasena = mostrarPassBackupAuto,
                alAlternarMostrarContrasena = { mostrarPassBackupAuto = !mostrarPassBackupAuto }
            )
            Spacer(Modifier.height(8.dp))
            com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto(
                valor = ajustes.backupAutoPatronNombre,
                etiqueta = "Patrón del nombre del archivo",
                alCambiar = { vm.ajustarBackupAutoPatronNombre(it) },
                monoespaciada = true
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Usa {99} para número secuencial (01, 02...) y {FECHA} para fecha y hora (ej. {99}-boveda-auto-{FECHA}). Se guarda como .bvda (compatible con .boveda).",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(12.dp))

            // Info de carpeta y última ejecución
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(Superficie)
                    .then(
                        if (GrosorBorde > 0.dp && ColorBordeActual != androidx.compose.ui.graphics.Color.Transparent)
                            Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo)
                        else Modifier
                    )
                    .padding(12.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Folder,
                            contentDescription = null,
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Descargas/BovedaLocal/Backups/",
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    val textoUltima = if (ajustes.backupAutoUltimaEjecucion > 0L) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        "Última copia: ${sdf.format(Date(ajustes.backupAutoUltimaEjecucion))}"
                    } else {
                        "Última copia: Aún no realizada"
                    }
                    val textoRotacion = if (ajustes.backupAutoMaxCopias <= 0) {
                        "Rotación: Infinitas"
                    } else {
                        "Rotación máxima: ${ajustes.backupAutoMaxCopias} copias"
                    }
                    Text(
                        "$textoUltima ($textoRotacion)",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Ejecutar copia automática ahora",
                color = ColorAcento,
                icono = Icons.Filled.PlayArrow
            ) {
                haptica.toque()
                vm.ejecutarBackupAutomatico(manual = true)
            }
        }

        BotonRestablecerItem {
            haptica.toque()
            vm.ajustarBackupAutoFrecuenciaDias(0)
            vm.ajustarBackupAutoMaxCopias(5)
            vm.ajustarBackupAutoPasswordCifrado("")
            vm.ajustarBackupAutoPatronNombre("{99}-backup-{FECHA}")
        }

        Spacer(Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recordarme hacer una copia",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            if (ajustes.mostrarIdsAjustes) {
                Box(
                    modifier = Modifier
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                        .background(com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema(ColorExportacion))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "06.2",
                        style = com.jlnavas3.bovedalocal.ui.theme.EstiloMono.copy(fontSize = 8.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema(ColorExportacion)
                    )
                }
            }
        }
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
            seleccionado = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.firstOrNull { it.first == ajustes.recordatorioExportacionDias }?.second
                ?: if (ajustes.recordatorioExportacionDias == -30) "Cada 30 minutos (prueba)" else "${ajustes.recordatorioExportacionDias} días",
            opciones = AlmacenAjustes.OPCIONES_RECORDATORIO_EXPORTACION.map { (valor, etiqueta) ->
                OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Backup)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarRecordatorioExportacion(valor.toInt()) }
        )
        BotonRestablecerItem {
            vm.ajustarRecordatorioExportacion(30)
        }
        Spacer(Modifier.height(14.dp))
        BotonColorido(
            texto = "Kit de emergencia físico (Imprimir / PDF)",
            color = ColorAcento,
            icono = Icons.Filled.Print
        ) {
            haptica.toque()
            vm.irPorId("06.3")
        }
    }
}
