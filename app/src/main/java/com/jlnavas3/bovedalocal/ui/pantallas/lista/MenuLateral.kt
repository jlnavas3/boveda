package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSeparadorDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Menú lateral al estilo Solid Explorer: se abre deslizando desde el borde
 * izquierdo o tocando el botón de menú.
 */
@Composable
fun MenuLateral(
    nombreApp: String,
    totalEntradas: Int,
    totalPapelera: Int,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 12.dp)
    ) {
        // Cabecera Premium
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(FormaCampo)
                        .background(SuperficieAlta)
                        .then(
                            if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeDropdown, FormaCampo)
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Ambar,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        nombreApp,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = ColorTitulos,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Menta)
                        )
                        Text(
                            "$totalEntradas ${if (totalEntradas == 1) "entrada" else "entradas"}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = TextoSecundario
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSeparadorDropdown))
        Spacer(Modifier.height(10.dp))

        // Opciones de navegación con contenedor y feedback
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ItemMenu(
                texto = "Generar contraseñas",
                icono = Icons.Filled.AutoAwesome,
                colorIcono = ColorGenerador
            ) { alIr(Pantalla.Generador) }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ItemMenu(
                    texto = "Passkeys",
                    icono = Icons.Filled.Fingerprint,
                    colorIcono = ColorPasskeys
                ) { alIr(Pantalla.Passkeys) }
            }

            ItemMenu(
                texto = "Autenticador 2FA",
                icono = Icons.Filled.Timer,
                colorIcono = Color2FA
            ) { alIr(Pantalla.Autenticador) }

            ItemMenu(
                texto = "Salud de la bóveda",
                icono = Icons.Filled.HealthAndSafety,
                colorIcono = ColorSalud
            ) { alIr(Pantalla.SaludBoveda) }

            ItemMenu(
                texto = "Papelera",
                icono = Icons.Filled.Delete,
                colorIcono = if (totalPapelera > 0) Ambar else ColorIconosInternos,
                badge = if (totalPapelera > 0) totalPapelera.toString() else null
            ) { alIr(Pantalla.Papelera) }

            ItemMenu(
                texto = "Diagnóstico de seguridad",
                icono = Icons.Filled.Security,
                colorIcono = ColorSeguridad
            ) { alIr(Pantalla.AcercaDe) }

            ItemMenu(
                texto = "Registro de eventos",
                icono = Icons.Filled.History,
                colorIcono = ColorIconosInternos
            ) { alIr(Pantalla.Registro) }

            ItemMenu(
                texto = "Configuración",
                icono = Icons.Filled.Settings,
                colorIcono = ColorIconosInternos
            ) { alIr(Pantalla.Ajustes) }

            Spacer(Modifier.height(6.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSeparadorDropdown))
            Spacer(Modifier.height(6.dp))

            ItemMenu(
                texto = "Bloquear bóveda",
                icono = Icons.Filled.Lock,
                colorIcono = Peligro,
                colorTexto = Peligro
            ) { alBloquear() }
        }

        // Pie de Menú estilizado
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ColorSeparadorDropdown))
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Bóveda local",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorTitulos
                )
                Text(
                    "v${BuildConfig.VERSION_NAME} · 100% offline",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Menta.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    "AES-256",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Menta
                )
            }
        }
    }
}

@Composable
fun ItemMenu(
    texto: String,
    icono: ImageVector,
    colorIcono: Color = ColorIconosInternos,
    colorTexto: Color = TextoPrincipal,
    badge: String? = null,
    alPulsar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(FormaPequena)
                .background(colorIcono.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
            color = colorTexto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Ambar.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Ambar
                )
            }
        }
    }
}
