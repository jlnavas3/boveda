/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PantallaAyudaClavesSsh(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Claves SSH y servidores",
        alVolver = alVolver,
        textoAccion = "Crear entrada de Servidor",
        iconoAccion = Icons.Filled.Dns,
        alEjecutarAccion = { vm.ir(Pantalla.Editar(id = null, tipoInicial = TipoEntrada.SERVIDOR)) }
    ) {
        TarjetaInformativaAyuda(titulo = "Generación de pares de claves criptográficas") {
            Text(
                text = "Bóveda Local integra un generador criptográfico offline para credenciales de acceso a infraestructura remota, servidores Linux y repositorios Git. Permite generar pares de claves públicas y privadas en milisegundos con entropía segura del dispositivo.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Algoritmos y estándares compatibles") {
            Text(
                text = "• Ed25519 (Recomendado): Claves de curva elíptica de 256 bits, máxima velocidad y máxima seguridad moderna contra ataques de canal lateral.\n\n• RSA (2048 y 4096 bits): Compatible con sistemas heredados y servidores corporativos que exigen criptografía basada en factorización.\n\n• ECDSA P-256: Algoritmo de curva elíptica estandarizado por NIST.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Formatos y huella digital (Fingerprint)") {
            Text(
                text = "• Clave pública OpenSSH: En formato de una sola línea (ej. 'ssh-ed25519 AAAAC3Nza...'), lista para añadir al archivo ~/.ssh/authorized_keys del servidor.\n\n• Clave privada PEM PKCS#8: Bloque de texto estándar listo para clientes SSH y terminales.\n\n• Huella digital SHA-256: Muestra la huella (ej. 'SHA256:abc123xyz...') para verificar la autenticidad de la clave al conectar por primera vez.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.sp
            )
        }
    }
}
