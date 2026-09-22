package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Menú lateral rediseñado estilo MagicOS / Samsung One UI:
 * - Tarjetas agrupadas con esquinas redondeadas y sin divisores duros.
 * - Iconos en contenedores redondeados con colores temáticos por sección.
 * - Badges numéricos modernos con píldoras de contraste suave.
 * - Acción de bloqueo dedicada y pie de página con badges de seguridad.
 */
@Composable
fun MenuLateral(
    nombreApp: String,
    totalEntradas: Int,
    totalPapelera: Int,
    totalDuplicadas: Int = 0,
    perfilArgon2: PerfilArgon2 = PerfilArgon2.ESTANDAR,
    mostrarIds: Boolean = false,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit = {}
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val topAjustado = (topInset - 24.dp).coerceAtLeast(8.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(start = 14.dp, end = 14.dp, top = topAjustado, bottom = 10.dp)
    ) {
        // Cabecera destacada estilo MagicOS / One UI
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = nombreApp,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = ColorTitulos,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (esOscuroActivo) Color(0xFF19271E) else Color(0xFFE8F5E9))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (esOscuroActivo) Color(0xFF4CAF50) else Color(0xFF2E7D32))
                    )
                    Text(
                        text = "Bóveda cifrada",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = if (esOscuroActivo) Color(0xFF81C784) else Color(0xFF1B5E20),
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Contenido scrolleable agrupado en tarjetas suaves
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Grupo 1: Herramientas
            GrupoMenuLateral(titulo = "Herramientas") {
                ItemMenu(
                    texto = "Generar contraseñas",
                    icono = Icons.Filled.AutoAwesome,
                    colorIcono = ColorGenerador,
                    idEtiqueta = "04.4",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.Generador) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Historial de contraseñas",
                    icono = Icons.Filled.History,
                    colorIcono = ColorGenerador,
                    idEtiqueta = "04.5",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.HistorialClaves) }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    SeparadorItemMenu()
                    ItemMenu(
                        texto = "Passkeys",
                        icono = Icons.Filled.Fingerprint,
                        colorIcono = ColorPasskeys,
                        idEtiqueta = "04.6",
                        mostrarId = mostrarIds
                    ) { alIr(Pantalla.Passkeys) }
                }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Autenticador 2FA",
                    icono = Icons.Filled.Timer,
                    colorIcono = Color2FA,
                    idEtiqueta = "04.7",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.Autenticador) }
            }

            // Grupo 2: Organización y Auditoría
            GrupoMenuLateral(titulo = "Organización y auditoría") {
                ItemMenu(
                    texto = "Salud de la bóveda",
                    icono = Icons.Filled.HealthAndSafety,
                    colorIcono = ColorSalud,
                    idEtiqueta = "02.4",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.SaludBoveda) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Limpiar duplicados",
                    icono = Icons.Filled.ContentCopy,
                    colorIcono = if (totalDuplicadas > 0) Peligro else ColorIconosInternos,
                    badge = if (totalDuplicadas > 0) totalDuplicadas.toString() else null,
                    colorBadge = if (totalDuplicadas > 0) Peligro else ColorIconosInternos,
                    idEtiqueta = "02.5",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.Duplicados) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Papelera",
                    icono = Icons.Filled.Delete,
                    colorIcono = if (totalPapelera > 0) ColorPapelera else ColorIconosInternos,
                    badge = if (totalPapelera > 0) totalPapelera.toString() else null,
                    colorBadge = ColorPapelera,
                    idEtiqueta = "02.6",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.Papelera) }
            }

            // Grupo 3: Sistema
            GrupoMenuLateral(titulo = "Sistema") {
                ItemMenu(
                    texto = "Ajustes",
                    icono = Icons.Filled.Settings,
                    colorIcono = Color(0xFF546E7A),
                    idEtiqueta = "06.0",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.Ajustes) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Registro de eventos",
                    icono = Icons.Filled.History,
                    colorIcono = ColorExportacion,
                    idEtiqueta = "05.2",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.Registro) }

                SeparadorItemMenu()

                ItemMenu(
                    texto = "Diagnóstico de seguridad",
                    icono = Icons.Filled.Security,
                    colorIcono = ColorSeguridad,
                    idEtiqueta = "05.3",
                    mostrarId = mostrarIds
                ) { alIr(Pantalla.AcercaDe) }
            }
        }

        Spacer(Modifier.height(10.dp))

        // Pie de Menú: Cápsulas de seguridad y versión
        val fondoVerdeInsignia = if (esOscuroActivo) Color(0xFF14291B) else Color(0xFFE8F5E9)
        val textoVerdeInsignia = if (esOscuroActivo) Color(0xFF81C784) else Color(0xFF1B5E20)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(fondoVerdeInsignia)
                        .padding(horizontal = 7.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = "Argon2id · ${perfilArgon2.memoriaKiB / 1024}M",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 9.sp),
                        color = textoVerdeInsignia,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(fondoVerdeInsignia)
                        .padding(horizontal = 7.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = "AES-256",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 9.sp),
                        color = textoVerdeInsignia,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(fondoVerdeInsignia)
                        .padding(horizontal = 7.dp, vertical = 2.5.dp)
                ) {
                    Text(
                        text = "100% Offline",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 9.sp),
                        color = textoVerdeInsignia,
                        maxLines = 1
                    )
                }
            }

            Text(
                text = "Bóveda Local · v${BuildConfig.VERSION_NAME} · by: jlnavas3",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                color = ColorAjusteGris.copy(alpha = 0.85f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun GrupoMenuLateral(
    titulo: String,
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = titulo.uppercase(),
            color = ColorAjusteGris,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                fontSize = 11.sp
            ),
            modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ColorTarjetaAjustes)
        ) {
            contenido()
        }
    }
}

@Composable
fun ItemMenu(
    texto: String,
    icono: ImageVector,
    colorIcono: Color = ColorIconosInternos,
    colorTexto: Color = ColorTextoAjustes,
    badge: String? = null,
    colorBadge: Color = Ambar,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    mostrarChevron: Boolean = true,
    alPulsar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alPulsar()
            }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = colorTexto,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema(colorIcono))
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = idEtiqueta,
                        style = com.jlnavas3.bovedalocal.ui.theme.EstiloMono.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema(colorIcono)
                    )
                }
            }
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorBadge)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp
                    ),
                    color = Color.White
                )
            }
            Spacer(Modifier.width(4.dp))
        }

        if (mostrarChevron) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun SeparadorItemMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 61.dp)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}
