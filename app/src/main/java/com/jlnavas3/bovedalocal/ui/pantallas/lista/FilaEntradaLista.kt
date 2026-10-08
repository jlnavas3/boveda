package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila visual de una credencial o entrada dentro del listado principal de la bóveda.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FilaEntrada(
    entrada: Entrada,
    seleccionActiva: Boolean,
    seleccionado: Boolean,
    alAbrir: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    alFavorito: () -> Unit,
    alCopiarCodigo: (String) -> Unit = {},
    alPulsarLargo: () -> Unit,
    alAlternarSeleccion: () -> Unit,
    segundosUnix: Long = System.currentTimeMillis() / 1000,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    separarDigitosTotp: Boolean = false,
    mostrarIndicadores: Boolean = true,
    enGrupo: Boolean = false,
    esUltimoEnGrupo: Boolean = false,
    ocultarUsuario: Boolean = false,
    ocultarTotp: Boolean = false,
    estiloOcultamiento: String = "desenfoque",
    identidadAsociada: com.jlnavas3.bovedalocal.data.Identidad? = null,
    ocultarEmailIdentidad: Boolean = false,
    mostrarChipIdentidad: Boolean = true
) {
    val compacta = alturaFila.value <= 44f
    val forma = if (enGrupo) {
        if (esUltimoEnGrupo) RoundedCornerShape(bottomStart = CurvaturaEsquinas, bottomEnd = CurvaturaEsquinas) else RoundedCornerShape(0.dp)
    } else {
        RoundedCornerShape(CurvaturaEsquinas)
    }
    val tamanoIcono = if (compacta) 28 else if (alturaFila.value <= 56f) 32 else 38

    val contenidoFila: @Composable () -> Unit = {
        val secreto = entrada.secretoTotp
        val tieneTotp = !seleccionActiva && !secreto.isNullOrBlank()

        val contexto = LocalContext.current
        val paquete = remember(entrada.id, entrada.modificadaEn) {
            GestorAppsInstaladas.resolverPaqueteApp(contexto, entrada)
        }
        val nombreApp = remember(paquete) {
            if (paquete != null) {
                GestorAppsInstaladas.obtenerNombreApp(contexto, paquete)
            } else null
        }
        val domPaquete = remember(paquete) { if (paquete != null) Dominios.dominioDePaquete(paquete) else null }
        val marcaPaquete = remember(domPaquete) { if (domPaquete != null) Dominios.marca(domPaquete) else null }
        val tituloMostrar = remember(entrada.titulo, nombreApp, domPaquete, marcaPaquete, paquete) {
            val tit = entrada.titulo.trim()
            when {
                tit.isNotBlank() && !com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.esTituloTecnico(tit, entrada.urls) -> tit
                !nombreApp.isNullOrBlank() -> nombreApp
                tit.isNotBlank() -> tit
                else -> "Sin título"
            }
        }

        val fondoFila = if (seleccionado) {
            ColorAcento.copy(alpha = 0.22f)
        } else if (resaltado) {
            ColorAcento.copy(alpha = 0.16f)
        } else if (enGrupo) {
            Color.Transparent
        } else {
            ColorTarjetaAjustes
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .clip(forma)
                .background(fondoFila)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && !enGrupo) Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    else Modifier
                )
                .combinedClickable(
                    onClick = { if (seleccionActiva) alAlternarSeleccion() else alAbrir() },
                    onLongClick = { if (!seleccionActiva) alPulsarLargo() }
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
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
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
                            Icon(Icons.Filled.Check, contentDescription = null, tint = ColorSobreAcento, modifier = Modifier.size(15.dp))
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }

                // Avatar Squircle / Icono representativo a la izquierda
                IconoTipoEntrada(
                    entrada = entrada,
                    tamanoIcono = tamanoIcono
                )

                Spacer(Modifier.width(12.dp))

                // Columna central: Título con TOTP y Subtítulo
                ColumnaDetallesFilaEntrada(
                    entrada = entrada,
                    tituloMostrar = tituloMostrar,
                    resaltado = resaltado,
                    compacta = compacta,
                    tieneTotp = tieneTotp,
                    secreto = secreto,
                    segundosUnix = segundosUnix,
                    separarDigitosTotp = separarDigitosTotp,
                    alCopiarCodigo = alCopiarCodigo,
                    ocultarUsuario = ocultarUsuario,
                    ocultarTotp = ocultarTotp,
                    estiloOcultamiento = estiloOcultamiento,
                    identidadAsociada = identidadAsociada,
                    ocultarEmailIdentidad = ocultarEmailIdentidad,
                    mostrarChipIdentidad = mostrarChipIdentidad,
                    modifier = Modifier.weight(1f)
                )

                // Bloque derecho: solo botón de favorito cuando la entrada es favorita (si no está en selección)
                if (!seleccionActiva && entrada.favorito) {
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable(onClick = alFavorito),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favorito",
                            tint = ColorAcento,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (seleccionActiva) {
        contenidoFila()
    } else {
        ContenedorDeslizamientoFila(
            entradaId = entrada.id,
            alturaFila = alturaFila,
            forma = forma,
            enGrupo = enGrupo,
            alCopiarUsuario = alCopiarUsuario,
            alCopiarContrasena = alCopiarContrasena,
            contenido = contenidoFila
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun FilaEntradaListaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
        ) {
            FilaEntrada(
                entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                seleccionActiva = false,
                seleccionado = false,
                alAbrir = {},
                alCopiarUsuario = {},
                alCopiarContrasena = {},
                alFavorito = {},
                alPulsarLargo = {},
                alAlternarSeleccion = {}
            )

            FilaEntrada(
                entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaBancaria,
                seleccionActiva = true,
                seleccionado = true,
                alAbrir = {},
                alCopiarUsuario = {},
                alCopiarContrasena = {},
                alFavorito = {},
                alPulsarLargo = {},
                alAlternarSeleccion = {}
            )
        }
    }
}

