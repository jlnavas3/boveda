/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.ElementoRetornoSubmenu
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

private enum class SubmenuLista {
    PRINCIPAL,
    CREAR_NUEVO,
    ORDENAR,
    FILTRAR,
    IMPORTAR,
    EXPORTAR,
    APARIENCIA_DISENO
}

/**
 * Menú desplegable contextual para la barra superior de PantallaLista: ordenación, filtros, importación,
 * exportación, otros ajustes contextuales y ayuda.
 */
@Composable
fun MenuOpcionesLista(
    expandido: Boolean,
    soloFavoritos: Boolean,
    hayFiltrosParaRestablecer: Boolean,
    criterioOrdenacionActual: CriterioOrdenacion,
    filtroTipoActual: TipoEntrada?,
    alDescartar: () -> Unit,
    alSeleccionarCriterioOrdenacion: (CriterioOrdenacion) -> Unit,
    alSeleccionarFiltroTipo: (TipoEntrada?) -> Unit,
    alAlternarSoloFavoritos: () -> Unit,
    alIrNormalizadorTitulos: () -> Unit,
    alIrExportarSelectivo: () -> Unit,
    alIrCopiaSeguridadManual: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alIrCsvGoogle: () -> Unit,
    alImportarGoogleAuthenticator: () -> Unit = {},
    alImportarDirectoCxf: () -> Unit,
    alImportarContactos: () -> Unit = {},
    alExportarDirectoCxf: () -> Unit,
    alIrTema: () -> Unit,
    alIrFormas: () -> Unit,
    alIrTipografia: () -> Unit,
    alIrOrganizacionGrupo: () -> Unit,
    alIrOrganizacionIndicadores: () -> Unit,
    alIrIndiceAz: () -> Unit,
    alIrTransiciones: () -> Unit = {},
    alIrSeguridadDatos: () -> Unit,
    alIrAjustesGenerales: () -> Unit,
    alAbrirAyuda: () -> Unit,
    alRestablecerFiltros: () -> Unit,
    alCrearTipo: (TipoEntrada) -> Unit = {}
) {
    var submenuActivo by remember(expandido) { mutableStateOf(SubmenuLista.PRINCIPAL) }

    val tiposFiltro = remember {
        listOf(
            null to ("Todo" to Icons.Filled.SelectAll),
            TipoEntrada.LOGIN to ("Claves / Logins" to Icons.Filled.Lock),
            TipoEntrada.PASSKEY to ("Llaves de acceso" to Icons.Filled.Fingerprint),
            TipoEntrada.NOTA to ("Notas seguras" to Icons.Filled.Description),
            TipoEntrada.TARJETA to ("Tarjetas bancarias" to Icons.Filled.CreditCard),
            TipoEntrada.CONTACTO to ("Contactos" to Icons.Filled.Person),
            TipoEntrada.WIFI to ("Redes Wi-Fi" to Icons.Filled.Wifi),
            TipoEntrada.CUENTA_BANCARIA to ("Cuentas bancarias" to Icons.Filled.AccountBalance),
            TipoEntrada.IDENTIDAD to ("Identidad" to Icons.Filled.Badge),
            TipoEntrada.SERVIDOR to ("Servidores" to Icons.Filled.Dns),
            TipoEntrada.WALLET to ("Cripto Wallets" to Icons.Filled.AccountBalanceWallet)
        )
    }

    MenuDesplegableBoveda(
        expanded = expandido,
        onDismissRequest = {
            submenuActivo = SubmenuLista.PRINCIPAL
            alDescartar()
        },
        modifier = Modifier.widthIn(min = 220.dp, max = 305.dp)
    ) {
        when (submenuActivo) {
            SubmenuLista.PRINCIPAL -> {
                ElementoMenuCompacto(
                    texto = "Crear nuevo...",
                    icono = Icons.Filled.AddCircle,
                    colorIcono = ColorAcento,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.CREAR_NUEVO }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Ordenar por...",
                    icono = Icons.AutoMirrored.Filled.Sort,
                    colorIcono = ColorIconosInternos,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.ORDENAR }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Filtrar por tipo...",
                    icono = Icons.Filled.Tune,
                    colorIcono = ColorIconosInternos,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.FILTRAR }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                    icono = Icons.Filled.Star,
                    colorIcono = ColorIconosInternos,
                    colorTexto = TextoPrincipal,
                    onClick = {
                        alDescartar()
                        alAlternarSoloFavoritos()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Normalizar títulos...",
                    icono = Icons.Filled.Spellcheck,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrNormalizadorTitulos()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Importar...",
                    icono = Icons.Filled.FileDownload,
                    colorIcono = ColorIconosInternos,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.IMPORTAR }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Exportar...",
                    icono = Icons.Filled.FileUpload,
                    colorIcono = ColorIconosInternos,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.EXPORTAR }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Apariencia y diseño...",
                    icono = Icons.Filled.Palette,
                    colorIcono = ColorIconosInternos,
                    iconoFinal = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = { submenuActivo = SubmenuLista.APARIENCIA_DISENO }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Ajustes generales...",
                    icono = Icons.Filled.Settings,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrAjustesGenerales()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Ayuda y manual...",
                    icono = Icons.AutoMirrored.Filled.HelpOutline,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alAbrirAyuda()
                    }
                )
                if (hayFiltrosParaRestablecer) {
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Restablecer filtros",
                        icono = Icons.Filled.Close,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            alDescartar()
                            alRestablecerFiltros()
                        }
                    )
                }
            }
            SubmenuLista.CREAR_NUEVO -> {
                ElementoRetornoSubmenu(
                    titulo = "Crear nuevo",
                    alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                )
                val tiposCreacion = remember {
                    listOf(
                        TipoEntrada.LOGIN to ("Contraseña / Login" to Icons.Filled.Lock),
                        TipoEntrada.PASSKEY to ("Llave de acceso" to Icons.Filled.Fingerprint),
                        TipoEntrada.NOTA to ("Nota segura" to Icons.Filled.Description),
                        TipoEntrada.TARJETA to ("Tarjeta bancaria" to Icons.Filled.CreditCard),
                        TipoEntrada.WIFI to ("Red Wi-Fi" to Icons.Filled.Wifi),
                        TipoEntrada.CUENTA_BANCARIA to ("Cuenta bancaria" to Icons.Filled.AccountBalance),
                        TipoEntrada.IDENTIDAD to ("Identidad" to Icons.Filled.Badge),
                        TipoEntrada.SERVIDOR to ("Servidor SSH" to Icons.Filled.Dns),
                        TipoEntrada.WALLET to ("Cripto Wallet" to Icons.Filled.AccountBalanceWallet),
                        TipoEntrada.CONTACTO to ("Contacto" to Icons.Filled.Person)
                    )
                }
                tiposCreacion.forEach { (tipo, par) ->
                    val (etiqueta, icono) = par
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = etiqueta,
                        icono = icono,
                        colorIcono = ColorIconosInternos,
                        onClick = {
                            submenuActivo = SubmenuLista.PRINCIPAL
                            alDescartar()
                            alCrearTipo(tipo)
                        }
                    )
                }
            }
            SubmenuLista.ORDENAR -> {
                ElementoRetornoSubmenu(
                    titulo = "Ordenar por",
                    alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                )
                CriterioOrdenacion.entries.forEach { criterio ->
                    SeparadorOpcionMenu()
                    val seleccionado = criterio == criterioOrdenacionActual
                    ElementoMenuCompacto(
                        texto = criterio.etiqueta,
                        icono = Icons.AutoMirrored.Filled.Sort,
                        colorIcono = ColorIconosInternos,
                        colorTexto = if (seleccionado) ColorAcento else TextoPrincipal,
                        iconoFinal = if (seleccionado) Icons.Filled.Check else null,
                        colorIconoFinal = ColorAcento,
                        onClick = {
                            alSeleccionarCriterioOrdenacion(criterio)
                            alDescartar()
                        }
                    )
                }
            }
            SubmenuLista.FILTRAR -> {
                ElementoRetornoSubmenu(
                    titulo = "Filtrar por tipo",
                    alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                )
                tiposFiltro.forEach { (tipo, par) ->
                    val (etiqueta, icono) = par
                    val seleccionado = tipo == filtroTipoActual
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = etiqueta,
                        icono = icono,
                        colorIcono = ColorIconosInternos,
                        colorTexto = if (seleccionado) ColorAcento else TextoPrincipal,
                        iconoFinal = if (seleccionado) Icons.Filled.Check else null,
                        colorIconoFinal = ColorAcento,
                        onClick = {
                            alSeleccionarFiltroTipo(tipo)
                            alDescartar()
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
                    texto = "Respaldo (.bvda)",
                    icono = Icons.Filled.FileDownload,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrCopiaSeguridad()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Google Passwords (.csv)",
                    icono = Icons.Filled.FileDownload,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrCsvGoogle()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Google Authenticator (QR)",
                    icono = Icons.Filled.QrCodeScanner,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alImportarGoogleAuthenticator()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Llaves y contraseñas (CXF)",
                    icono = Icons.Filled.VpnKey,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alImportarDirectoCxf()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Contactos del teléfono",
                    icono = Icons.Filled.ContactPhone,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alImportarContactos()
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
                    texto = "Respaldo completo (.bvda)",
                    icono = Icons.Filled.Backup,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrCopiaSeguridadManual()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Respaldo selectivo (.bvda)",
                    icono = Icons.Filled.FileUpload,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrExportarSelectivo()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Llaves y contraseñas (CXF)",
                    icono = Icons.Filled.VpnKey,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alExportarDirectoCxf()
                    }
                )
            }
            SubmenuLista.APARIENCIA_DISENO -> {
                ElementoRetornoSubmenu(
                    titulo = "Apariencia y diseño",
                    alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Tema y colores...",
                    icono = Icons.Filled.Palette,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrTema()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Formas y bordes...",
                    icono = Icons.Filled.SquareFoot,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrFormas()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Tipografía...",
                    icono = Icons.Filled.TextFields,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrTipografia()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Densidad y lista...",
                    icono = Icons.Filled.Layers,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrOrganizacionGrupo()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Indicadores de tarjetas...",
                    icono = Icons.Filled.Tune,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrOrganizacionIndicadores()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Índice lateral A-Z...",
                    icono = Icons.AutoMirrored.Filled.Sort,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrIndiceAz()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Transiciones...",
                    icono = Icons.Filled.AutoAwesome,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrTransiciones()
                    }
                )
                SeparadorOpcionMenu()
                ElementoMenuCompacto(
                    texto = "Seguridad visual...",
                    icono = Icons.Filled.Security,
                    colorIcono = ColorIconosInternos,
                    onClick = {
                        alDescartar()
                        alIrSeguridadDatos()
                    }
                )
            }
        }
    }
}
