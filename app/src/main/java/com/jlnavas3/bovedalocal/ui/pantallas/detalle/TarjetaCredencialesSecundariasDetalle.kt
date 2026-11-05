/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TotpSecundario
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun TarjetaCredencialesSecundariasDetalle(
    entrada: Entrada,
    vm: VaultViewModel? = null,
    haptica: Haptica,
    ajustes: AjustesApp? = null,
    alCopiarClave: ((String) -> Unit)? = null,
    alCopiarTotp: ((String) -> Unit)? = null,
    alGuardarEntrada: ((Entrada) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val historialUnico = remember(entrada.historialContrasenas, entrada.contrasena) {
        entrada.historialContrasenas
            .distinctBy { it.contrasena }
            .filterNot { it.contrasena == entrada.contrasena }
    }
    val passkeysSecundarias = remember(entrada.passkeysSecundarias, entrada.passkey) {
        entrada.passkeysSecundarias
            .filterNot { com.jlnavas3.bovedalocal.data.FusionadorEntradas.sonMismaPasskey(it, entrada.passkey) }
            .distinctBy { it.credId.ifBlank { it.clavePrivada } }
    }
    val totpsSecundarios = remember(entrada.totpsSecundarios, entrada.secretoTotp) {
        val secPrincipal = entrada.secretoTotp?.trim()
        entrada.totpsSecundarios
            .filterNot { secPrincipal != null && it.secreto.trim().equals(secPrincipal, ignoreCase = true) }
            .distinctBy { it.secreto.trim() }
    }

    if (historialUnico.isEmpty() && passkeysSecundarias.isEmpty() && totpsSecundarios.isEmpty()) {
        return
    }

    var expandido by rememberSaveable { mutableStateOf(false) }

    val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
    val tiempoAutoOcultar = if (seguridadVisualActiva) ajustes.tiempoAutoOcultarSegundos else 0

    val reveladas = remember { mutableStateMapOf<String, Boolean>() }
    var claveCopiadaReciente by remember { mutableStateOf<String?>(null) }

    // Estados para confirmación
    var claveARestaurar by remember { mutableStateOf<String?>(null) }
    var claveAEliminar by remember { mutableStateOf<String?>(null) }

    var passkeyARestaurar by remember { mutableStateOf<DatosPasskey?>(null) }
    var passkeyAEliminar by remember { mutableStateOf<DatosPasskey?>(null) }

    var totpARestaurar by remember { mutableStateOf<TotpSecundario?>(null) }
    var totpAEliminar by remember { mutableStateOf<TotpSecundario?>(null) }

    LaunchedEffect(claveCopiadaReciente) {
        if (claveCopiadaReciente != null) {
            delay(1500)
            claveCopiadaReciente = null
        }
    }

    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    if (totpsSecundarios.isNotEmpty() && expandido) {
        LaunchedEffect(Unit) {
            while (true) {
                ahora = System.currentTimeMillis() / 1000
                delay(500)
            }
        }
    }

    val forma = FormaCampo
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.4f)
    }
    val grosor = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorCampoAjustes)
            .border(grosor, colorBorde, forma)
    ) {
        // Encabezado colapsable con ícono circular, título y etiquetas en 2da fila
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptica.toque()
                    expandido = !expandido
                }
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(TextoSecundario.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LockReset,
                    contentDescription = null,
                    tint = TextoSecundario,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(11.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Credenciales alternativas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(3.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (historialUnico.isNotEmpty()) {
                        BadgeConteoAlternativas("${historialUnico.size} ${if (historialUnico.size == 1) "clave" else "claves"}")
                    }
                    if (passkeysSecundarias.isNotEmpty()) {
                        BadgeConteoAlternativas("${passkeysSecundarias.size} ${if (passkeysSecundarias.size == 1) "llave de acceso" else "llaves de acceso"}")
                    }
                    if (totpsSecundarios.isNotEmpty()) {
                        BadgeConteoAlternativas("${totpsSecundarios.size} 2FA")
                    }
                }
            }

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = if (expandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (expandido) "Colapsar credenciales alternativas" else "Desplegar credenciales alternativas",
                tint = TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        AnimatedVisibility(
            visible = expandido,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(ColorSeparadorAjustes.copy(alpha = 0.45f))
                )
                Spacer(Modifier.height(10.dp))

                // Subsección 1: Contraseñas anteriores
                if (historialUnico.isNotEmpty()) {
                    if (passkeysSecundarias.isNotEmpty() || totpsSecundarios.isNotEmpty()) {
                        SubetiquetaAlternativa("Contraseñas anteriores (${historialUnico.size})")
                    }
                    historialUnico.forEachIndexed { index, cambio ->
                        if (index > 0) {
                            Spacer(Modifier.height(EspaciadoDetalle))
                        }
                        val estaRevelada = reveladas[cambio.contrasena] == true
                        val fueCopiada = claveCopiadaReciente == cambio.contrasena

                        TemporizadorAutoOcultar(
                            revelado = estaRevelada && seguridadVisualActiva,
                            tiempoSegundos = tiempoAutoOcultar,
                            alAutoOcultar = { reveladas[cambio.contrasena] = false }
                        )

                        FilaHistorialContrasenaDetalle(
                            cambio = cambio,
                            revelada = estaRevelada,
                            copiado = fueCopiada,
                            onToggleRevelar = {
                                haptica.toque()
                                reveladas[cambio.contrasena] = !estaRevelada
                            },
                            onCopiar = {
                                haptica.exito()
                                vm?.copiar("Contraseña anterior", cambio.contrasena, sensible = true)
                                alCopiarClave?.invoke(cambio.contrasena)
                                claveCopiadaReciente = cambio.contrasena
                            },
                            onEliminar = {
                                haptica.toque()
                                claveAEliminar = cambio.contrasena
                            },
                            onSolicitarRestaurar = {
                                haptica.toque()
                                claveARestaurar = cambio.contrasena
                            },
                            seguridadVisualActiva = seguridadVisualActiva,
                            estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"
                        )
                    }
                }

                // Subsección 2: Passkeys secundarias
                if (passkeysSecundarias.isNotEmpty()) {
                    if (historialUnico.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                    }
                    SubetiquetaAlternativa("Llaves de acceso (${passkeysSecundarias.size})")
                    passkeysSecundarias.forEachIndexed { index, passkey ->
                        if (index > 0) {
                            Spacer(Modifier.height(EspaciadoDetalle))
                        }
                        FilaPasskeySecundariaDetalle(
                            passkey = passkey,
                            onEliminar = {
                                haptica.toque()
                                passkeyAEliminar = passkey
                            },
                            onSolicitarRestaurar = {
                                haptica.toque()
                                passkeyARestaurar = passkey
                            },
                            fechaEntradaFallback = entrada.creadaEn
                        )
                    }
                }

                // Subsección 3: Códigos 2FA secundarios
                if (totpsSecundarios.isNotEmpty()) {
                    if (historialUnico.isNotEmpty() || passkeysSecundarias.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                    }
                    SubetiquetaAlternativa("Códigos 2FA (${totpsSecundarios.size})")
                    totpsSecundarios.forEachIndexed { index, totp ->
                        if (index > 0) {
                            Spacer(Modifier.height(EspaciadoDetalle))
                        }
                        FilaTotpSecundarioDetalle(
                            totp = totp,
                            ajustes = ajustes,
                            haptica = haptica,
                            ahora = ahora,
                            alCopiarTotp = { codigo ->
                                vm?.copiar("Código 2FA", codigo, sensible = true)
                                alCopiarTotp?.invoke(codigo)
                            },
                            onSolicitarRestaurar = {
                                haptica.toque()
                                totpARestaurar = totp
                            },
                            onEliminar = {
                                haptica.toque()
                                totpAEliminar = totp
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogos de confirmación: Contraseña
    if (claveARestaurar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Restaurar esta contraseña?",
            mensaje = "Esta contraseña pasará a ser la contraseña activa de \"${entrada.titulo}\". La que tienes actualmente no se perderá: se conservará en este mismo historial.",
            textoConfirmar = "Restaurar",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.Restore,
            alConfirmar = {
                val nuevaClave = claveARestaurar
                claveARestaurar = null
                if (nuevaClave != null) {
                    haptica.exito()
                    val actualizada = entrada.copy(contrasena = nuevaClave)
                    vm?.guardar(actualizada)
                    alGuardarEntrada?.invoke(actualizada)
                }
            },
            alDescartar = { claveARestaurar = null }
        )
    }

    if (claveAEliminar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar esta contraseña del historial?",
            mensaje = "Esta contraseña anterior se eliminará permanentemente. Esta acción no se puede deshacer.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                val clave = claveAEliminar
                claveAEliminar = null
                if (clave != null) {
                    haptica.error()
                    val nuevoHistorial = entrada.historialContrasenas.filterNot { it.contrasena == clave }
                    val actualizada = entrada.copy(historialContrasenas = nuevoHistorial)
                    vm?.guardar(actualizada)
                    alGuardarEntrada?.invoke(actualizada)
                }
            },
            alDescartar = { claveAEliminar = null }
        )
    }

    // Diálogos de confirmación: Passkey
    if (passkeyARestaurar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Establecer como llave de acceso principal?",
            mensaje = "Esta llave de acceso pasará a ser la principal activa de \"${entrada.titulo}\". La llave de acceso actual no se perderá: se conservará como alternativa.",
            textoConfirmar = "Restaurar",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.Restore,
            alConfirmar = {
                val target = passkeyARestaurar
                passkeyARestaurar = null
                if (target != null) {
                    haptica.exito()
                    if (vm != null) {
                        vm.intercambiarPasskeyPrincipal(entrada.id, target)
                    } else {
                        val principalAnterior = entrada.passkey
                        val nuevas = entrada.passkeysSecundarias.filterNot { it.credId == target.credId }.toMutableList()
                        if (principalAnterior != null && principalAnterior.credId != target.credId) {
                            nuevas.add(0, principalAnterior)
                        }
                        val actualizada = entrada.copy(passkey = target, passkeysSecundarias = nuevas)
                        alGuardarEntrada?.invoke(actualizada)
                    }
                }
            },
            alDescartar = { passkeyARestaurar = null }
        )
    }

    if (passkeyAEliminar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar llave de acceso secundaria?",
            mensaje = "Esta llave de acceso alternativa se eliminará permanentemente de la entrada.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                val target = passkeyAEliminar
                passkeyAEliminar = null
                if (target != null) {
                    haptica.error()
                    if (vm != null) {
                        vm.eliminarPasskeySecundaria(entrada.id, target)
                    } else {
                        val nuevas = entrada.passkeysSecundarias.filterNot { it.credId == target.credId }
                        val actualizada = entrada.copy(passkeysSecundarias = nuevas)
                        alGuardarEntrada?.invoke(actualizada)
                    }
                }
            },
            alDescartar = { passkeyAEliminar = null }
        )
    }

    // Diálogos de confirmación: TOTP
    if (totpARestaurar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Establecer como código 2FA principal?",
            mensaje = "Este código 2FA pasará a ser el principal activo de \"${entrada.titulo}\". El código 2FA actual no se perderá: se conservará como alternativo.",
            textoConfirmar = "Restaurar",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.Restore,
            alConfirmar = {
                val target = totpARestaurar
                totpARestaurar = null
                if (target != null) {
                    haptica.exito()
                    if (vm != null) {
                        vm.intercambiarTotpPrincipal(entrada.id, target)
                    } else {
                        val nuevas = entrada.totpsSecundarios.filterNot { it.id == target.id }.toMutableList()
                        if (entrada.tieneTotp) {
                            nuevas.add(
                                0,
                                TotpSecundario(
                                    id = java.util.UUID.randomUUID().toString(),
                                    secreto = entrada.secretoTotp ?: "",
                                    emisor = entrada.totpEmisor.ifBlank { entrada.titulo },
                                    periodo = entrada.totpPeriodo,
                                    digitos = entrada.totpDigitos,
                                    algoritmo = entrada.totpAlgoritmo
                                )
                            )
                        }
                        val actualizada = entrada.copy(
                            secretoTotp = target.secreto,
                            totpEmisor = target.emisor,
                            totpPeriodo = target.periodo,
                            totpDigitos = target.digitos,
                            totpAlgoritmo = target.algoritmo,
                            totpsSecundarios = nuevas
                        )
                        alGuardarEntrada?.invoke(actualizada)
                    }
                }
            },
            alDescartar = { totpARestaurar = null }
        )
    }

    if (totpAEliminar != null) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar código 2FA secundario?",
            mensaje = "Este código 2FA alternativo se eliminará permanentemente de la entrada.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = {
                val target = totpAEliminar
                totpAEliminar = null
                if (target != null) {
                    haptica.error()
                    if (vm != null) {
                        vm.eliminarTotpSecundario(entrada.id, target.id)
                    } else {
                        val nuevas = entrada.totpsSecundarios.filterNot { it.id == target.id }
                        val actualizada = entrada.copy(totpsSecundarios = nuevas)
                        alGuardarEntrada?.invoke(actualizada)
                    }
                }
            },
            alDescartar = { totpAEliminar = null }
        )
    }
}

@Composable
private fun BadgeConteoAlternativas(texto: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(TextoSecundario.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold),
            color = TextoSecundario
        )
    }
}

@Composable
private fun SubetiquetaAlternativa(texto: String) {
    Text(
        text = texto.uppercase(),
        color = ColorAjusteGris,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.8.sp
        ),
        modifier = Modifier.padding(start = 2.dp, bottom = 6.dp, top = 2.dp)
    )
}
