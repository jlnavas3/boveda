package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.ElementoRetornoSubmenu
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

private enum class SubmenuLista {
    PRINCIPAL,
    IMPORTAR,
    EXPORTAR
}

/**
 * Menú desplegable contextual para la barra superior de PantallaLista: ordenación, filtros, importación y exportación.
 */
@Composable
fun MenuOpcionesLista(
    expandido: Boolean,
    soloFavoritos: Boolean,
    agruparPorSitio: Boolean,
    mostrarIndicadoresContenido: Boolean,
    hayFiltrosParaRestablecer: Boolean,
    alDescartar: () -> Unit,
    alMostrarOrdenacion: () -> Unit,
    alMostrarFiltros: () -> Unit,
    alAlternarSoloFavoritos: () -> Unit,
    alIrOrganizacionGrupo: () -> Unit,
    alIrOrganizacionIndicadores: () -> Unit,
    alIrExportarSelectivo: () -> Unit,
    alIrCopiaSeguridadManual: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alIrCsvGoogle: () -> Unit,
    alImportarDirectoCxf: () -> Unit,
    alExportarDirectoCxf: () -> Unit,
    alRestablecerFiltros: () -> Unit
) {
    var submenuActivo by remember(expandido) { mutableStateOf(SubmenuLista.PRINCIPAL) }

    MenuDesplegableBoveda(
        expanded = expandido,
        onDismissRequest = {
            submenuActivo = SubmenuLista.PRINCIPAL
            alDescartar()
        },
        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
    ) {
        when (submenuActivo) {
            SubmenuLista.PRINCIPAL -> {
                ElementoMenuCompacto(
                    texto = "Ordenar por...",
                    icono = Icons.AutoMirrored.Filled.Sort,
                    onClick = {
                        alDescartar()
                        alMostrarOrdenacion()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Filtrar por tipo...",
                    icono = Icons.Filled.Tune,
                    onClick = {
                        alDescartar()
                        alMostrarFiltros()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                    icono = Icons.Filled.Star,
                    colorIcono = if (soloFavoritos) ColorAcento else ColorIconosInternos,
                    colorTexto = if (soloFavoritos) ColorAcento else TextoPrincipal,
                    onClick = {
                        alDescartar()
                        alAlternarSoloFavoritos()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = if (agruparPorSitio) "Ajustes de agrupación..." else "Agrupar cuentas...",
                    icono = Icons.Filled.Layers,
                    colorIcono = ColorAcento,
                    onClick = {
                        alDescartar()
                        alIrOrganizacionGrupo()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = if (mostrarIndicadoresContenido) "Ajustes de indicadores..." else "Mostrar indicadores...",
                    icono = Icons.Filled.Tune,
                    colorIcono = ColorAcento,
                    onClick = {
                        alDescartar()
                        alIrOrganizacionIndicadores()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Importar...",
                    icono = Icons.Filled.FileDownload,
                    colorIcono = ColorAcento,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.IMPORTAR }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Exportar...",
                    icono = Icons.Filled.FileUpload,
                    colorIcono = ColorAcento,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.EXPORTAR }
                )
                if (hayFiltrosParaRestablecer) {
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Restablecer filtros",
                        icono = Icons.Filled.Close,
                        colorIcono = Peligro,
                        colorTexto = Peligro,
                        onClick = {
                            alDescartar()
                            alRestablecerFiltros()
                        }
                    )
                }
            }
            SubmenuLista.IMPORTAR -> {
                ElementoRetornoSubmenu(
                    titulo = "Importar",
                    alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Copia de seguridad (.bvda)",
                    icono = Icons.Filled.FileDownload,
                    onClick = {
                        alDescartar()
                        alIrCopiaSeguridad()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Contraseñas de Google (.csv)",
                    icono = Icons.Filled.FileDownload,
                    onClick = {
                        alDescartar()
                        alIrCsvGoogle()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Importación directa de llaves de paso y contraseñas",
                    icono = Icons.Filled.VpnKey,
                    onClick = {
                        alDescartar()
                        alImportarDirectoCxf()
                    }
                )
            }
            SubmenuLista.EXPORTAR -> {
                ElementoRetornoSubmenu(
                    titulo = "Exportar",
                    alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Copia de seguridad completa (.bvda)",
                    icono = Icons.Filled.Backup,
                    onClick = {
                        alDescartar()
                        alIrCopiaSeguridadManual()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Exportación selectiva (.bvda)",
                    icono = Icons.Filled.FileUpload,
                    onClick = {
                        alDescartar()
                        alIrExportarSelectivo()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Exportación directa de llaves de paso y contraseñas",
                    icono = Icons.Filled.VpnKey,
                    onClick = {
                        alDescartar()
                        alExportarDirectoCxf()
                    }
                )
            }
        }
    }
}
