package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.Check
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TarjetaCuentaTotp(
    entrada: Entrada,
    ahora: Long,
    separarDigitos: Boolean,
    haptica: Haptica,
    alCopiarCodigo: (String) -> Unit,
    alAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    alPulsarLargo: (() -> Unit)? = null,
    alAlternarSeleccion: (() -> Unit)? = null,
    mostrarIndicadores: Boolean = false,
    alVerDetalle: () -> Unit = {}
) {
    val secreto = entrada.secretoTotp ?: return
    val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
    val segundosRestantes = Totp.segundosRestantes(ahora, periodo)
    val codigo = remember(ahora / periodo, secreto, entrada.totpDigitos) {
        try {
            Totp.codigo(
                secreto = Base32.decodificar(secreto),
                segundosUnix = ahora,
                digitos = entrada.totpDigitos,
                periodo = periodo
            )
        } catch (e: Exception) {
            "------"
        }
    }
    val codigoVisible = if (separarDigitos && codigo.length == 6) {
        "${codigo.take(3)} ${codigo.drop(3)}"
    } else {
        codigo
    }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val ocultarUsuario = seguridadVisualActiva && (ajustes?.ocultarUsuario == true)
    val ocultarTotp = seguridadVisualActiva && (ajustes?.ocultarTotp == true)
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    var codigoRevelado by rememberSaveable { mutableStateOf(false) }

    if (ocultarTotp) {
        TemporizadorAutoOcultar(
            revelado = codigoRevelado,
            tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
        ) {
            codigoRevelado = false
        }
    }

    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val fondoFila = if (seleccionado) {
        ColorAcento.copy(alpha = 0.22f)
    } else {
        ColorTarjetaAjustes
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondoFila)
            .then(
                if (seleccionado) Modifier.border(1.dp, ColorAcento.copy(alpha = 0.5f), forma)
                else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
    ) {
        if (mostrarIndicadores) {
            IndicadorContenidoTarjeta(
                entrada = entrada,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(ColorDatos2FA)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (seleccionActiva) {
                            haptica.tic()
                            alAlternarSeleccion?.invoke()
                        } else {
                            alVerDetalle()
                        }
                    },
                    onLongClick = {
                        if (!seleccionActiva && alPulsarLargo != null) {
                            haptica.toque()
                            alPulsarLargo()
                        }
                    }
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (seleccionActiva) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) ColorAcento else Borde),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entrada.totpEmisor.ifBlank { entrada.titulo },
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entrada.usuario.isNotBlank()) {
                    if (ocultarUsuario) {
                        TextoSeguroVisual(
                            texto = entrada.usuario,
                            oculto = true,
                            estilo = estiloOcultamiento,
                            estiloTexto = MaterialTheme.typography.bodySmall,
                            colorTexto = TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = entrada.usuario,
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                VisualizadorCodigoTotp(
                    codigo = codigo,
                    codigoVisible = codigoVisible,
                    segundosRestantes = segundosRestantes,
                    periodo = periodo,
                    ocultarTotp = ocultarTotp,
                    codigoRevelado = codigoRevelado,
                    estiloOcultamiento = estiloOcultamiento,
                    seleccionActiva = seleccionActiva,
                    haptica = haptica,
                    alCopiarCodigo = alCopiarCodigo,
                    alAlternarSeleccion = alAlternarSeleccion,
                    alAlternarRevelado = { codigoRevelado = !codigoRevelado }
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = if (seleccionActiva) "$segundosRestantes s restantes" else "Toca el código para copiar · $segundosRestantes s",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (!seleccionActiva && entrada.favorito) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable {
                            haptica.tic()
                            alAlternarFavorito()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Quitar de favoritos",
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaCuentaTotpPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val contexto = androidx.compose.ui.platform.LocalContext.current
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            TarjetaCuentaTotp(
                entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                ahora = System.currentTimeMillis() / 1000,
                separarDigitos = true,
                haptica = remember { Haptica(contexto) },
                alCopiarCodigo = {},
                alAlternarFavorito = {}
            )
            TarjetaCuentaTotp(
                entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                ahora = System.currentTimeMillis() / 1000,
                separarDigitos = true,
                haptica = remember { Haptica(contexto) },
                alCopiarCodigo = {},
                alAlternarFavorito = {},
                seleccionActiva = true,
                seleccionado = true
            )
        }
    }
}

