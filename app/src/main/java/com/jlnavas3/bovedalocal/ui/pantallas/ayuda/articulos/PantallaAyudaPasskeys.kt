/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda exhaustivo sobre llaves de acceso Passkeys (FIDO2 / WebAuthn) y protección anti-phishing.
 */
@Composable
fun PantallaAyudaPasskeys(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Llaves de acceso (Passkeys)",
        alVolver = alVolver,
        textoAccion = "Ver Passkeys guardadas",
        iconoAccion = Icons.Filled.Fingerprint,
        alEjecutarAccion = { vm.ir(Pantalla.Passkeys("03-LST-PAS")) }
    ) {
        TarjetaInformativaAyuda(titulo = "La revolución sin contraseñas (Passwordless)") {
            Text(
                text = "Las Passkeys (llaves de acceso) son el nuevo estándar abierto de la industria digital desarrollado por la FIDO Alliance, W3C, Google, Apple y Microsoft. Reemplazan por completo las contraseñas tradicionales y los códigos 2FA por un par de claves criptográficas asimétricas únicas para cada sitio web.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Inmunidad matemática total contra el Phishing") {
            Text(
                text = "Con las contraseñas ordinarias, si caes en un sitio web engañoso que imita a tu banco o red social, entregas tu clave al atacante. Con las Passkeys esto es matemáticamente imposible:\n\n• La firma criptográfica se vincula de forma inviolable al dominio de origen (Relying Party ID o RP ID) verificado directamente por el navegador o la app.\n• Si estás en un enlace malicioso como 'accounts-googIe.com', el sistema operativo no liberará ninguna firma para 'google.com', neutralizando el ataque por completo.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Criptografía y detalles técnicos en Bóveda Local") {
            Text(
                text = "Bóveda Local te otorga control técnico total sobre cada llave de acceso guardada:\n\n• Algoritmos soportados: ES256 (ECDSA sobre curva elíptica P-256) y EdDSA (Ed25519).\n• AAGUID: Identificador único universal del fabricante o proveedor que emitió la credencial.\n• Credential ID: Identificador único público de la llave registrado en el servidor.\n• Contador de firmas y RP ID: Verificación del número de inicios de sesión completados para prevenir ataques de repetición (replay attacks).\n• Passkeys secundarias: Guarda múltiples llaves registradas en distintos momentos o dispositivos bajo la misma cuenta.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Integración con Android 14+ (Credential Manager)") {
            Text(
                text = "En dispositivos con Android 14 o superior, Bóveda Local se registra como Proveedor de Credenciales oficial del sistema operativo. Al iniciar sesión en Chrome o en apps compatibles, el diálogo nativo de Android te ofrecerá tus passkeys locales custodiadas en tu teléfono, desbloqueándolas con tu huella sin servidores en la nube.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Interoperabilidad con el estándar CXF",
            texto = "A diferencia de las soluciones cerradas de las 'Big Tech', tus passkeys en Bóveda Local no están atrapadas: puedes exportarlas e importarlas mediante el estándar oficial CXF de la FIDO Alliance."
        )
    }
}
