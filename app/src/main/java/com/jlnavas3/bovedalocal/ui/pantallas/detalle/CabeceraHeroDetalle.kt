package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada

@Composable
fun CabeceraHeroDetalle(
    entrada: Entrada,
    alMostrarQr: () -> Unit,
    alActualizarTitulo: ((String) -> Unit)? = null
) {
    val contexto = LocalContext.current
    val iconoApp = rememberIconoAppInstalada(entrada)

    val paquete = remember(entrada, contexto) {
        GestorAppsInstaladas.resolverPaqueteApp(contexto, entrada)
    }
    val nombreApp = remember(paquete, contexto) {
        if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            LanzadorEnlaces.obtenerNombreApp(contexto, paquete)
        } else null
    }
    val domPaquete = remember(paquete) { if (paquete != null) Dominios.dominioDePaquete(paquete) else null }
    val marcaPaquete = remember(domPaquete) { if (domPaquete != null) Dominios.marca(domPaquete) else null }
    val esTituloTecnico = remember(entrada.titulo, domPaquete, marcaPaquete, paquete, nombreApp) {
        val tit = entrada.titulo.trim()
        val titDominio = tit.removePrefix("https://").removePrefix("http://").removePrefix("www.").trimEnd('/')
        !nombreApp.isNullOrBlank() && !tit.equals(nombreApp, ignoreCase = true) && (
            tit.isBlank() ||
                tit == "Nueva entrada" ||
                (domPaquete != null && (tit.equals(domPaquete, ignoreCase = true) || titDominio.equals(domPaquete, ignoreCase = true))) ||
                (paquete != null && tit.equals(paquete, ignoreCase = true)) ||
                com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.esTituloTecnico(tit, entrada.urls)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (iconoApp != null) {
            IconoAppCircular(
                bitmap = iconoApp,
                descripcion = entrada.titulo,
                tamanoDp = 60.dp
            )
        } else {
            Monograma(
                titulo = entrada.titulo.ifBlank { "?" },
                semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                tamano = 60
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = entrada.titulo.ifBlank { "Sin título" },
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = ColorTitulos,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(FormaPequena)
                    .background(ColorAcento.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(
                    text = entrada.tipo.etiqueta,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = ColorAcento
                )
            }
            if (entrada.ignoradaEnSalud) {
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(Peligro.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Peligro,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Ignorada",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = Peligro
                        )
                    }
                }
            }
        }
        if (esTituloTecnico && nombreApp != null && alActualizarTitulo != null) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorAcento.copy(alpha = 0.15f))
                    .clickable { alActualizarTitulo(nombreApp) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoFixHigh,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Actualizar a \"$nombreApp\"",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAcento,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    if (entrada.tipo == TipoEntrada.WIFI) {
        Spacer(Modifier.height(8.dp))
        BotonColorido(
            texto = "Compartir Wi-Fi por código QR",
            color = ColorAcento,
            icono = Icons.Filled.QrCode,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = alMostrarQr
        )
    }
}
