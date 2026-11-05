/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun TarjetaCamposDetalle(
    campos: List<CampoPersonalizado>,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit,
    alCopiarValor: ((String, String, Boolean) -> Unit)? = null
) {
    if (campos.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Campos personalizados (${campos.size})")

        campos.forEachIndexed { index, campo ->
            if (index > 0) {
                Spacer(Modifier.height(EspaciadoDetalle))
            }
            if (campo.tipo == TipoCampo.BOOLEANO) {
                FilaCampoBooleanoDetalle(campo = campo)
            } else {
                FilaCampoPersonalizadoDetalle(
                    campo = campo,
                    vm = vm,
                    ajustes = ajustes,
                    haptica = haptica,
                    copiado = ultimaCopia == "campo_${campo.id}",
                    alCopiar = { alCopiarCampo("campo_${campo.id}") },
                    alCopiarValor = alCopiarValor
                )
            }
        }
    }
}

@Composable
private fun FilaCampoPersonalizadoDetalle(
    campo: CampoPersonalizado,
    vm: VaultViewModel?,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    copiado: Boolean,
    alCopiar: () -> Unit,
    alCopiarValor: ((String, String, Boolean) -> Unit)? = null
) {
    var revelado by remember { mutableStateOf(false) }
    val esSensible = campo.esSensibleEfectivo
    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true && ajustes.ocultarCampos
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    if (esSensible) {
        TemporizadorAutoOcultar(
            revelado = revelado,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            revelado = false
        }
    }

    val valorFormateado = remember(campo.valor, campo.tipo, campo.formato, ajustes) {
        if (ajustes == null || campo.valor.isBlank()) campo.valor
        else when {
            campo.tipo == TipoCampo.FECHA -> FormateadorCampos.normalizarYFormatearFecha(campo.valor, ajustes.formatoFecha)
            campo.tipo == TipoCampo.HORA -> {
                val p = FormateadorCampos.parsearHora(campo.valor, ajustes.formatoHora)
                if (p != null) FormateadorCampos.formatearHora(p.first, p.second, ajustes.formatoHora)
                else campo.valor
            }
            campo.tipo == TipoCampo.TELEFONO -> FormateadorCampos.aplicarMascaraTelefono(campo.valor, ajustes.formatoTelefono)
            campo.tipo == TipoCampo.DECIMAL -> FormateadorCampos.sanitizarDecimal(campo.valor, ajustes.separadorDecimal)
            campo.formato == "TARJETA_CREDITO" -> FormateadorCampos.aplicarMascaraTarjeta(campo.valor, ajustes.formatoTarjeta)
            campo.formato in listOf("MM/AA", "VENCIMIENTO") -> FormateadorCampos.aplicarMascaraExpiracion(campo.valor, ajustes.formatoExpiracionTarjeta)
            campo.formato == "IBAN" -> FormateadorCampos.aplicarMascaraIban(campo.valor, ajustes.formatoIban)
            else -> campo.valor
        }
    }

    val colorBordeCampo = remember(campo.tipo, esSensible) {
        when {
            @Suppress("DEPRECATION")
            (esSensible || campo.tipo == TipoCampo.PIN || campo.tipo == TipoCampo.OCULTO) -> ColorDatosContrasena
            campo.tipo == TipoCampo.EMAIL -> ColorDatosUsuario
            campo.tipo == TipoCampo.URL -> ColorDatosWeb
            campo.tipo in listOf(TipoCampo.FECHA, TipoCampo.HORA) -> ColorDatos2FA
            else -> ColorSeparadorAjustes
        }
    }

    TarjetaDatoDetalle(colorBorde = colorBordeCampo) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = if (esSensible && seguridadVisualActiva && !revelado) Alignment.CenterVertically else Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        campo.etiqueta.ifBlank { "Campo adicional" },
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoSecundario
                    )
                    if (esSensible) {
                        Box(
                            modifier = Modifier
                                .clip(FormaPequena)
                                .background(ColorCampoAjustes)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Security,
                                    contentDescription = "Sensible",
                                    tint = TextoSecundario,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(3.dp))
                                Text(
                                    "Sensible",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = TextoSecundario
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(ColorCampoAjustes)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            campo.tipo.etiqueta,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = TextoSecundario
                        )
                    }
                }

                Spacer(Modifier.height(2.dp))

                if (esSensible && seguridadVisualActiva) {
                    TextoSeguroVisual(
                        texto = valorFormateado,
                        oculto = !revelado,
                        estilo = estiloOcultamiento,
                        estiloTexto = if (!revelado) EstiloMono.copy(letterSpacing = 2.sp) else EstiloMono,
                        colorTexto = if (!revelado) TextoSecundario else TextoPrincipal,
                        maxLines = if (!revelado) 1 else Int.MAX_VALUE,
                        overflow = TextOverflow.Clip,
                        modifier = if (!revelado) Modifier.horizontalScroll(rememberScrollState()) else Modifier
                    )
                } else {
                    Text(
                        text = valorFormateado,
                        style = if (esSensible) EstiloMono else MaterialTheme.typography.bodyLarge,
                        color = TextoPrincipal,
                        maxLines = 1,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (esSensible && seguridadVisualActiva) {
                    BotonIconoDetalle(
                        icono = if (revelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        descripcion = if (revelado) "Ocultar" else "Revelar",
                        alPulsar = {
                            haptica.toque()
                            revelado = !revelado
                            if (revelado) {
                                Diagnostico.apuntar("seguridad", "Dato sensible revelado (${campo.etiqueta.ifBlank { "campo personalizado" }})")
                            }
                        }
                    )
                }
                BotonCopiarDetalle(
                    copiado = copiado,
                    alPulsar = {
                        haptica.exito()
                        val eti = campo.etiqueta.ifBlank { "Campo personalizado" }
                        vm?.copiar(eti, campo.valor, sensible = esSensible)
                        alCopiarValor?.invoke(eti, campo.valor, esSensible)
                        alCopiar()
                    }
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaCamposDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val contexto = androidx.compose.ui.platform.LocalContext.current
        TarjetaCamposDetalle(
            campos = listOf(
                CampoPersonalizado(etiqueta = "PIN de Acceso", valor = "8492", tipo = TipoCampo.PIN, esSensible = true),
                CampoPersonalizado(etiqueta = "Número de Cuenta", valor = "ES91 2100 0418 4502 0005 1234", tipo = TipoCampo.TEXTO, esSensible = false)
            ),
            haptica = remember { Haptica(contexto) },
            ultimaCopia = null,
            alCopiarCampo = {}
        )
    }
}

