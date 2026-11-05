/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
 * Artículo de ayuda sobre la transferencia de Passkeys bajo el estándar oficial CXF de la FIDO Alliance.
 */
@Composable
fun PantallaAyudaTransferenciaCxf(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Intercambio de Passkeys (CXF)",
        alVolver = alVolver,
        textoAccion = "Ajustes de Transferencia CXF",
        iconoAccion = Icons.Filled.SyncAlt,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesTransferenciaCxf()) }
    ) {
        TarjetaInformativaAyuda(titulo = "El estándar oficial de la FIDO Alliance") {
            Text(
                text = "Durante años, las grandes empresas tecnológicas mantuvieron las credenciales biométricas atrapadas en sus propios ecosistemas cerrados. Para resolver este cautiverio (Vendor Lock-in), la FIDO Alliance ratificó el Credential Exchange Format (CXF): una especificación abierta y universal que permite exportar e importar llaves de acceso entre gestores independientes.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Migración fluida entre plataformas") {
            Text(
                text = "Con la herramienta CXF de Bóveda Local puedes:\n\n• Traer tus passkeys: Importa de forma segura paquetes de llaves generadas previamente en Google Password Manager, Apple Keychain, Bitwarden o Dashlane.\n\n• Llevar tus passkeys contigo: Si en el futuro necesitas migrar a otra herramienta compatible, exporta tus llaves en el formato estandarizado sin trabas propietarias.\n\n• Formato estructurado: El archivo contiene las claves públicas, claves privadas (en formato COSE/JWK), identificadores de credencial (Credential ID), dominios (RP ID) y etiquetas de usuario.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Validación e inspección previa al importar") {
            Text(
                text = "Al importar un paquete CXF, Bóveda Local analiza el contenido en memoria aislada:\n\n1. Verifica la validez criptográfica de cada llave.\n2. Muestra un listado con el dominio asociado (RP ID) y el usuario de cada passkey.\n3. Si ya existe una credencial para ese mismo servicio, te ofrece asociarla automáticamente como passkey secundaria o actualizar la existente sin duplicados.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaAdvertenciaAyuda(
            titulo = "Protección del archivo de transferencia",
            texto = "Los paquetes de migración CXF contienen claves criptográficas privadas de acceso sin contraseña a tus servicios. Cifra siempre el archivo resultante con una clave robusta y bórralo del almacenamiento inmediatamente después de completar la importación."
        )

        TarjetaConsejoAyuda(
            titulo = "Verificación tras la importación",
            texto = "Tras importar tus passkeys en Bóveda Local, prueba iniciar sesión en al menos dos de tus servicios web prioritarios (ej. Google o GitHub) para verificar que la autenticación biométrica responda correctamente antes de retirar las claves del gestor de origen."
        )
    }
}
