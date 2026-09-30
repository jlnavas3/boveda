package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.RectangleShape
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import java.util.concurrent.TimeUnit

const val DIAS_AVISO_ANTIGUEDAD = 180L

enum class PestanaSalud(val titulo: String) {
    REPETIDAS("Repetidas"),
    COMUNES("Filtradas"),
    DEBILES("Débiles"),
    ANTIGUAS("Antiguas")
}

fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))

@Composable
fun FilaMetricaSalud(etiqueta: String, valor: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = ColorTextoAjustes, style = MaterialTheme.typography.bodyMedium)
        Text(valor, color = color, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun MensajeExitoPestana(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    colorIcono: Color = Menta
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(colorIcono.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorTextoAjustes,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitulo,
            style = MaterialTheme.typography.bodyMedium,
            color = ColorAjusteGris,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FilaProblemaAgil(
    entrada: Entrada,
    etiquetaDetalle: String,
    colorDetalle: Color,
    alCambiarRapido: () -> Unit,
    alVerDetalle: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarIndicadores: Boolean = false,
    enGrupo: Boolean = true
) {
    val (iconoTipo, colorTipo) = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock to ColorSeguridad
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint to ColorPasskeys
        TipoEntrada.NOTA -> Icons.Filled.Description to ColorAcento
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard to ColorGenerador
        TipoEntrada.WIFI -> Icons.Filled.Wifi to ColorSalud
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance to ColorSeguridad
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge to ColorExportacion
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns to ColorIconosInternos
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet to ColorAcento
    }
    val colorLegible = colorLegibleParaTema(colorTipo)
    val forma = if (enGrupo) RectangleShape else RoundedCornerShape(CurvaturaEsquinas)
    val fondo = if (enGrupo) androidx.compose.ui.graphics.Color.Transparent else ColorTarjetaAjustes

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        modifier = modifier,
        forma = forma,
        enGrupo = enGrupo,
        accionIzquierda = AccionDeslizamiento(
            texto = "Cambiar\nClave",
            icono = Icons.Filled.AutoFixHigh,
            color = Menta,
            alEjecutar = alCambiarRapido
        ),
        accionDerecha = AccionDeslizamiento(
            texto = "Ver\nDetalle",
            icono = Icons.AutoMirrored.Filled.ArrowForwardIos,
            color = ColorAcento,
            alEjecutar = alVerDetalle
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(fondo)
                .then(
                    if (!enGrupo && GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
        ) {
            if (mostrarIndicadores) {
                IndicadorContenidoTarjeta(
                    entrada = entrada,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { alVerDetalle() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val iconoApp = rememberIconoAppInstalada(entrada)
                if (iconoApp != null) {
                    IconoAppCircular(
                        bitmap = iconoApp,
                        descripcion = entrada.titulo,
                        tamanoDp = 38.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(colorTipo)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoTipo,
                            contentDescription = null,
                            tint = colorLegible,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = entrada.titulo.ifBlank { "Sin título" },
                        color = ColorTextoAjustes,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val subtitulo = entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                    Text(
                        text = subtitulo,
                        color = ColorAjusteGris,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(fondoBadgeParaTema(colorDetalle))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = etiquetaDetalle,
                        color = colorLegibleParaTema(colorDetalle),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaGrupoRepetido(
    grupo: List<Entrada>,
    totalEnGrupo: Int = grupo.size,
    alCambiarClave: (Entrada) -> Unit,
    alVerDetalle: (String) -> Unit,
    mostrarIndicadores: Boolean = false,
    colapsable: Boolean = true,
    expandido: Boolean = false,
    alAlternar: () -> Unit = {}
) {
    val claveTitulo = grupo.firstOrNull()?.titulo?.ifBlank { "Clave repetida" } ?: "Clave repetida"
    com.jlnavas3.bovedalocal.ui.pantallas.lista.ComponenteGrupoLista(
        clave = claveTitulo,
        entradas = grupo,
        expandido = expandido,
        alAlternar = alAlternar,
        contenidoEntrada = { entrada, _, _ ->
            FilaProblemaAgil(
                entrada = entrada,
                etiquetaDetalle = "Repetida",
                colorDetalle = Peligro,
                alCambiarRapido = { alCambiarClave(entrada) },
                alVerDetalle = { alVerDetalle(entrada.id) },
                mostrarIndicadores = mostrarIndicadores,
                enGrupo = true
            )
        }
    )
}
