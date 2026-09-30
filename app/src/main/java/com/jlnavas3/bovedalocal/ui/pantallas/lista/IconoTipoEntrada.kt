package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/**
 * Avatar circular / Icono representativo a la izquierda de la fila según el tipo de entrada o monograma.
 */
@Composable
fun IconoTipoEntrada(
    entrada: Entrada,
    tamanoIcono: Int,
    modifier: Modifier = Modifier
) {
    val iconoApp = rememberIconoAppInstalada(entrada)

    Box(
        modifier = modifier.size(tamanoIcono.dp),
        contentAlignment = Alignment.Center
    ) {
        if (iconoApp != null) {
            IconoAppCircular(
                bitmap = iconoApp,
                descripcion = entrada.titulo,
                tamanoDp = tamanoIcono.dp
            )
        } else {
            when (entrada.tipo) {
                TipoEntrada.PASSKEY -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(ColorPasskeys),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Fingerprint,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.NOTA -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF0288D1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.TARJETA -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFFE91E63)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CreditCard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.WIFI -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF00897B)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Wifi,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.CUENTA_BANCARIA -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF3949AB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountBalance,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.SERVIDOR -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF546E7A)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Dns,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.WALLET -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFFFFB300)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            TipoEntrada.IDENTIDAD -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF00ACC1)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Badge,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }
            }
            else -> {
                Monograma(
                    titulo = entrada.titulo.ifBlank { "?" },
                    semilla = entrada.urls.firstOrNull() ?: entrada.usuario.ifBlank { entrada.titulo },
                    tamano = tamanoIcono
                )
            }
        }
    }
}
}
