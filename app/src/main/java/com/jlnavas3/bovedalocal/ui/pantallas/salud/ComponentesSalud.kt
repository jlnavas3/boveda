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
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import java.util.concurrent.TimeUnit

const val DIAS_AVISO_ANTIGUEDAD = 180L

enum class PestanaSalud(val titulo: String) {
    REPETIDAS("Repetidas"),
    COMUNES("Filtradas"),
    DEBILES("Débiles"),
    ANTIGUAS("Antiguas"),
    IGNORADAS("Ignoradas")
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilaProblemaAgil(
    entrada: Entrada,
    etiquetaDetalle: String,
    colorDetalle: Color,
    alCambiarRapido: () -> Unit,
    alVerDetalle: () -> Unit,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    alIgnorar: () -> Unit = {},
    mostrarIndicadores: Boolean = false,
    enGrupo: Boolean = true,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    alAlternarSeleccion: (() -> Unit)? = null,
    alPulsarLargo: (() -> Unit)? = null
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
    val fondo = if (seleccionado) ColorAcento.copy(alpha = 0.22f) else if (enGrupo) androidx.compose.ui.graphics.Color.Transparent else ColorTarjetaAjustes
    
    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val ocultarUsuario = seguridadVisualActiva && (ajustes?.ocultarUsuario == true)
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
    val tiempoAutoOcultar = ajustes?.tiempoAutoOcultarSegundos ?: 10

    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }

    TemporizadorAutoOcultar(
        revelado = mostrarContrasena,
        tiempoSegundos = if (seguridadVisualActiva) tiempoAutoOcultar else 0
    ) {
        mostrarContrasena = false
    }

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        modifier = modifier,
        forma = forma,
        enGrupo = enGrupo,
        accionIzquierda = if (seleccionActiva) null else AccionDeslizamiento(
            texto = "Cambiar\nClave",
            icono = Icons.Filled.AutoFixHigh,
            color = Menta,
            alEjecutar = alCambiarRapido
        ),
        accionDerecha = if (seleccionActiva) null else AccionDeslizamiento(
            texto = if (entrada.ignoradaEnSalud) "Restaurar" else "Ignorar",
            icono = if (entrada.ignoradaEnSalud) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            color = if (entrada.ignoradaEnSalud) Menta else Peligro,
            alEjecutar = alIgnorar
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(fondo)
                .then(
                    if (seleccionado) Modifier.border(1.dp, ColorAcento.copy(alpha = 0.5f), forma)
                    else if (!enGrupo && GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
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
                    .combinedClickable(
                        onClick = {
                            if (seleccionActiva) {
                                alAlternarSeleccion?.invoke()
                            } else {
                                alVerDetalle()
                            }
                        },
                        onLongClick = {
                            if (!seleccionActiva && alPulsarLargo != null) {
                                alPulsarLargo()
                            }
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (seleccionActiva) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (seleccionado) ColorAcento else Borde),
                        contentAlignment = Alignment.Center
                    ) {
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorSobreAcento,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }

                val iconoApp = rememberIconoAppInstalada(entrada)
                if (iconoApp != null) {
                    IconoAppCircular(
                        bitmap = iconoApp,
                        descripcion = entrada.titulo,
                        tamanoDp = 36.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(FormaPequena)
                            .background(ColorCampoAjustes)
                            .border(0.8.dp, ColorSeparadorAjustes, FormaPequena),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoTipo,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Fila 1: Título y Badge de diagnóstico
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entrada.titulo.ifBlank { "Sin título" },
                            color = ColorTextoAjustes,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(FormaPequena)
                                .background(ColorCampoAjustes)
                                .border(0.8.dp, ColorSeparadorAjustes, FormaPequena)
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = etiquetaDetalle,
                                color = ColorAcento,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    // Fila 2: Usuario / Correo
                    val usuarioTexto = entrada.usuario.ifBlank { "Sin usuario" }
                    if (entrada.usuario.isNotBlank() && ocultarUsuario) {
                        TextoSeguroVisual(
                            texto = entrada.usuario,
                            oculto = true,
                            estilo = estiloOcultamiento,
                            estiloTexto = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            colorTexto = ColorTextoAjustes.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = usuarioTexto,
                            color = if (entrada.usuario.isNotBlank()) ColorTextoAjustes.copy(alpha = 0.85f) else ColorAjusteGris,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    // Fila 3: Contraseña (oculta por puntos + ojo)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (entrada.contrasena.isBlank()) {
                            Text(
                                text = "Sin contraseña",
                                color = ColorAjusteGris,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        } else {
                            TextoSeguroVisual(
                                texto = entrada.contrasena,
                                oculto = !mostrarContrasena,
                                estilo = if (seguridadVisualActiva) estiloOcultamiento else "puntos_reales",
                                estiloTexto = if (mostrarContrasena) {
                                    MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp
                                    )
                                } else {
                                    MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                                },
                                colorTexto = ColorTextoAjustes.copy(alpha = 0.9f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                        if (entrada.contrasena.isNotBlank()) {
                            IconButton(
                                onClick = { mostrarContrasena = !mostrarContrasena },
                                modifier = Modifier.size(22.dp)
                            ) {
                                Icon(
                                    imageVector = if (mostrarContrasena) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (mostrarContrasena) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = ColorAjusteGris,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    // Fila 4+: Enlaces web (un enlace por línea si hay más de uno)
                    val urlsLimpias = remember(entrada.urls) { entrada.urls.filter { it.isNotBlank() } }
                    if (urlsLimpias.isNotEmpty()) {
                        Spacer(Modifier.height(2.dp))
                        urlsLimpias.forEach { url ->
                            Text(
                                text = url,
                                color = ColorAjusteGris,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
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
    ajustes: AjustesApp? = null,
    mostrarIndicadores: Boolean = false,
    colapsable: Boolean = true,
    expandido: Boolean = false,
    alAlternar: () -> Unit = {},
    alIgnorar: (Entrada) -> Unit = {},
    seleccionActiva: Boolean = false,
    seleccionados: Set<String> = emptySet(),
    alAlternarSeleccion: ((String) -> Unit)? = null,
    alPulsarLargo: ((String) -> Unit)? = null
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
                ajustes = ajustes,
                alIgnorar = { alIgnorar(entrada) },
                mostrarIndicadores = mostrarIndicadores,
                enGrupo = true,
                seleccionActiva = seleccionActiva,
                seleccionado = seleccionados.contains(entrada.id),
                alAlternarSeleccion = { alAlternarSeleccion?.invoke(entrada.id) },
                alPulsarLargo = { alPulsarLargo?.invoke(entrada.id) }
            )
        }
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun ComponentesSaludPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaGrupoRepetido(
            grupo = listOf(
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo.copy(id = "mock-salud-2", titulo = "Twitter / X")
            ),
            expandido = true,
            alCambiarClave = {},
            alVerDetalle = {}
        )
    }
}

