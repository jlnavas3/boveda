/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaConsejoAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda sobre el generador de claves SSH, pares asimétricos Ed25519/RSA y formatos OpenSSH/PEM.
 */
@Composable
fun PantallaAyudaClavesSsh(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Claves SSH para servidores",
        alVolver = alVolver,
        textoAccion = "Crear Servidor SSH",
        iconoAccion = Icons.Filled.Key,
        alEjecutarAccion = { vm.ir(Pantalla.Editar(id = null, tipoInicial = TipoEntrada.SERVIDOR)) }
    ) {
        TarjetaInformativaAyuda(titulo = "Autenticación segura en servidores y repositorios") {
            Text(
                text = "Acceder a servidores Linux, routers domésticos, VPS o plataformas de desarrollo como GitHub/GitLab mediante contraseña tradicional es inseguro y vulnerable a ataques de fuerza bruta. El estándar moderno de la industria es el uso de pares de claves asimétricas SSH (clave pública y clave privada).",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Algoritmos criptográficos soportados") {
            Text(
                text = "Bóveda Local genera los pares de claves enteramente en el procesador del móvil sin recurrir a software externo:\n\n• Ed25519 (Altamente recomendado): Curva de Edwards sobre 255 bits. Ofrece el máximo estándar de seguridad moderno, velocidad ultrarrápida de firma y longitud compacta (ideal para copiar en terminales móviles).\n\n• RSA (2048 o 4096 bits): Algoritmo clásico universal, necesario si te conectas a servidores legados o sistemas Unix antiguos que no soporten curvas elípticas modernas.\n\n• ECDSA (P-256, P-384, P-521): Estándar NIST basado en curvas elípticas habitual en entornos corporativos o gubernamentales.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Formatos de exportación e instalación") {
            Text(
                text = "• Clave pública OpenSSH: Cadena de una sola línea (ej: 'ssh-ed25519 AAAAC3N... admin@homelab'). Es la parte que copias y pegas en el archivo ~/.ssh/authorized_keys de tu servidor o en los ajustes de GitHub/GitLab.\n\n• Clave privada PEM / PKCS#8: Bloque cifrado delimitado por '-----BEGIN OPENSSH PRIVATE KEY-----' que custodia el secreto absoluto. Nunca debes compartirla.\n\n• Huella digital SHA-256 (Fingerprint): Resumen criptográfico para verificar la autenticidad de la clave al conectarte por primera vez desde un terminal.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Vinculación directa con entradas de tipo Servidor") {
            Text(
                text = "Puedes vincular directamente la clave SSH generada dentro de la ficha de una entrada de tipo 'Servidor'. Así tendrás en una única vista la dirección IP, el puerto de conexión (22, 2222), el usuario SSH y la clave privada correspondiente.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Usa siempre Ed25519 por defecto",
            texto = "A menos que tu servidor sea muy antiguo (previo a OpenSSH 6.5 del año 2014), utiliza siempre Ed25519: es más seguro que RSA-4096, requiere menos memoria y su firma se valida de forma instantánea."
        )
    }
}
