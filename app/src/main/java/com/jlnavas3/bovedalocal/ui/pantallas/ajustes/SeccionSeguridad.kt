package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionSeguridad(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    contexto: Context,
    haptica: Haptica,
    flujo: FlujoBiometria,
    modoActivo: BiometricKeyStore.Modo?,
    nivel: Biometria.Nivel,
    capacidad: Biometria.Capacidad,
    activarFuerte: () -> Unit,
    ofrecerCompatible: (String) -> Unit
) {
    val esSenuelo = vm.repositorio.esModoSenuelo

    TarjetaAjuste(
        titulo = "Seguridad",
        icono = Icons.Filled.Security,
        descripcion = if (esSenuelo) "Bloqueo automático y borrado del portapapeles." else "Huella, bloqueo automático y borrado del portapapeles."
    ) {
        if (!esSenuelo) {
            Spacer(Modifier.height(10.dp))
            FilaAjuste(
                titulo = "Abrir con huella",
                descripcion = when {
                    modoActivo != null -> "Activa en modo ${modoActivo.etiqueta}."
                    nivel == Biometria.Nivel.FUERTE ->
                        "La clave maestra se guarda envuelta por el Keystore, atada a tu huella de Clase 3."
                    nivel == Biometria.Nivel.COMPATIBLE ->
                        "${Biometria.explicarFaltaDeFuerte(capacidad)} Hay un modo compatible: Android comprueba la huella o el PIN y la app abre la bóveda."
                    else ->
                        "Sin huella ni PIN utilizables ahora mismo: ${Biometria.explicar(capacidad.compatible)}."
                },
                activo = ajustes.biometriaActiva,
                habilitado = nivel != Biometria.Nivel.NINGUNO || ajustes.biometriaActiva,
                alCambiar = { activar ->
                    if (activar) {
                        when (nivel) {
                            Biometria.Nivel.FUERTE -> activarFuerte()
                            Biometria.Nivel.COMPATIBLE -> ofrecerCompatible(Biometria.explicarFaltaDeFuerte(capacidad))
                            Biometria.Nivel.NINGUNO -> vm.avisar("Este móvil no ofrece huella ni PIN utilizables ahora mismo")
                        }
                    } else {
                        flujo.desactivar()
                        haptica.tic()
                        vm.avisar("Huella desactivada")
                    }
                }
            )
            if (capacidad.fuerte == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED &&
                capacidad.debil != BiometricManager.BIOMETRIC_SUCCESS
            ) {
                Spacer(Modifier.height(10.dp))
                BotonColorido(
                    texto = "Registrar una huella en Android",
                    color = ColorSeguridad,
                    icono = Icons.Filled.Fingerprint
                ) {
                    if (!AjustesSistema.abrirRegistroHuella(contexto)) vm.avisar("No encuentro esa pantalla en este móvil")
                }
            }
            when {
                ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.FUERTE && Biometria.hayCompatible(capacidad) ->
                    EnlaceAjuste("Cambiar a modo compatible") {
                        ofrecerCompatible("Si la huella te falla en este móvil aunque Android la acepte, el modo compatible suele funcionar.")
                    }
                ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.COMPATIBLE && Biometria.hayFuerte(capacidad) ->
                    EnlaceAjuste("Volver al modo fuerte") { activarFuerte() }
                !ajustes.biometriaActiva && nivel == Biometria.Nivel.FUERTE && Biometria.hayCompatible(capacidad) ->
                    EnlaceAjuste("Activar en modo compatible") {
                        ofrecerCompatible("Para quien ya sabe que la huella de Clase 3 le falla en este móvil.")
                    }
            }
            Spacer(Modifier.height(8.dp))
        } else {
            Spacer(Modifier.height(10.dp))
        }
        Surface(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
            shape = FormaTarjeta,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Protección de pantalla permanente (FLAG_SECURE)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "La bóveda bloquea permanentemente las capturas de pantalla y oculta la vista previa en aplicaciones recientes de Android para garantizar la máxima privacidad.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        SelectorAjuste(
            titulo = "Bloqueo automático",
            icono = Icons.Filled.Lock,
            seleccionado = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.firstOrNull { it.first == ajustes.autoBloqueoSegundos }?.second ?: "30 segundos",
            opciones = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.map { (valor, etiqueta) ->
                OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Lock)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarAutoBloqueo(valor.toInt()) }
        )
        Spacer(Modifier.height(14.dp))
        SelectorAjuste(
            titulo = "Borrado del portapapeles",
            icono = Icons.Filled.Backup,
            seleccionado = AlmacenAjustes.OPCIONES_PORTAPAPELES.first { it.first == ajustes.portapapelesSegundos }.second,
            opciones = AlmacenAjustes.OPCIONES_PORTAPAPELES.map { (valor, etiqueta) ->
                OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Timer)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarPortapapeles(valor.toInt()) }
        )
        if (!esSenuelo) {
            Spacer(Modifier.height(14.dp))
            BotonColorido(
                texto = "Bóveda señuelo (PIN de coacción)",
                color = ColorSeguridad,
                icono = Icons.Filled.Shield
            ) {
                haptica.toque()
                vm.ir(Pantalla.AjustesSenuelo)
            }
        }
    }
}
