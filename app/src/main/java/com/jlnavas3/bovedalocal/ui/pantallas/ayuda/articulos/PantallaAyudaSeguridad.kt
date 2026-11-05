/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaAdvertenciaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda exhaustivo sobre políticas de acceso, autenticación biométrica,
 * bloqueo de aplicación y protección contra ataques de fuerza bruta.
 */
@Composable
fun PantallaAyudaSeguridad(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Bloqueo y biometría",
        alVolver = alVolver,
        textoAccion = "Abrir Bloqueo y biometría",
        iconoAccion = Icons.Filled.Lock,
        alEjecutarAccion = { vm.ir(Pantalla.BloqueoBiometria()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Pantalla unificada de acceso y protección") {
            Text(
                text = "En Ajustes > Seguridad > Bloqueo y biometría (01-SEG-BIO-BLO) se centralizan de forma directa y sin menús dispersos los tres pilares esenciales de custodia local de tu bóveda:\n\n1. Biometría (01-SEG-BIO-G01)\n2. Bloqueo de aplicación (01-SEG-BIO-G02)\n3. Protección contra fuerza bruta (01-SEG-BIO-G05)\n\nPuedes navegar rápidamente entre ellos pulsando el menú de 3 puntos en la barra superior.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "1. Biometría: Modos Fuerte vs Compatible") {
            Text(
                text = "Tu contraseña maestra nunca se almacena en texto claro en memoria ni en disco. Al activar el desbloqueo con huella dactilar, se genera una clave criptográfica de desbloqueo:\n\n• Modo Fuerte (Recomendado): Emplea el chip de seguridad seguro del móvil (Android Keystore / TEE / StrongBox). La clave solo se libera tras una autenticación biométrica positiva respaldada por hardware (Class 3 Biometric).\n\n• Modo Compatible: Diseñado específicamente para dispositivos con ROMs personalizadas (LineageOS, Pixel Experience, etc.) o capas de fabricante con fallos de implementación en el Keystore. Mantiene la seguridad de la clave permitiendo que el sensor físico autentique sin provocar bloqueos del sistema.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "2. Bloqueo de aplicación y defensas en primer plano") {
            Text(
                text = "• Temporizadores de inactividad: Configura cuándo debe bloquearse la sesión automáticamente al estar en segundo plano. Puedes elegir: Inmediato (al salir), 15s, 30s, 1 minuto, 2 minutos, 5 minutos, 10 minutos o Desactivado.\n\n• Protección contra Tapjacking: Mecanismo de defensa que descarta cualquier toque táctil sobre la pantalla si otra aplicación superpone burbujas flotantes, filtros de pantalla o ventanas invisibles (toques oscurecidos/occluded), evitando que malware engañe al usuario.\n\n• FLAG_SECURE (Seguridad visual): Impide que aplicaciones espía o el propio sistema tomen capturas de pantalla de tus claves y oculta el contenido de la bóveda en la vista de aplicaciones recientes de Android.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "3. Freno contra fuerza bruta") {
            Text(
                text = "Protege tu bóveda ante intentos de adivinación automatizada de contraseña o PIN si alguien toma posesión física de tu móvil:\n\n• Intentos permitidos: Número de fallos tolerados antes de imponer un tiempo de espera (3, 5 o 10 intentos).\n\n• Espera exponencial: Tras exceder el límite, cada error sucesivo multiplica el tiempo de bloqueo (2s, 4s, 8s, 16s...).\n\n• Tope máximo de espera: Límite acumulable configurable (hasta 60s, 300s o ilimitado) para balancear seguridad y evitar que un descuido impida el acceso prolongado.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Consejo de seguridad en el portapapeles",
            texto = "En Ajustes > Seguridad > Portapapeles puedes activar la limpieza automática de contraseñas copiadas (30s o 60s). Esto garantiza que ninguna otra aplicación en segundo plano pueda inspeccionar tu portapapeles más tarde."
        )

        TarjetaAdvertenciaAyuda(
            titulo = "Huella no sustituye la clave maestra",
            texto = "La biometría es una comodidad de desbloqueo rápido. Tras reiniciar el móvil o tras actualizaciones mayores de Android, siempre se requerirá tu clave maestra. Asegúrate de memorizarla o guardarla en tu Kit de Emergencia."
        )
    }
}
