/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
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
 * Artículo de ayuda exhaustivo sobre el normalizador de títulos, reglas de URLs, TLDs y servicios de red local.
 */
@Composable
fun PantallaAyudaNormalizadorTitulos(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Normalización de títulos y URLs",
        alVolver = alVolver,
        textoAccion = "Reglas de Normalización",
        iconoAccion = Icons.Filled.AutoFixHigh,
        alEjecutarAccion = { vm.ir(Pantalla.ReglasNormalizacion()) }
    ) {
        TarjetaInformativaAyuda(titulo = "De URLs caóticas a títulos impecables") {
            Text(
                text = "Al importar credenciales desde Chrome, Firefox u otros gestores, es habitual encontrarse con títulos caóticos como 'https://accounts.google.com/signin/v2/identifier?flowName=Glif' o 'https://auth.bbva.es/es/login'.\n\nEl Normalizador de Títulos de Bóveda Local transforma automáticamente esas direcciones en nombres claros, concisos y elegantes ('Google' o 'BBVA'), eliminando el desorden visual de tu lista de cuentas.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "El Asistente interactivo de Normalización") {
            Text(
                text = "Desde Ajustes > Lista de cuentas > Normalizador de títulos puedes ejecutar una limpieza guiada con previsualización en vivo (Dry-Run):\n\n• Lista comparativa: Revisa lado a lado el título actual frente a la sugerencia limpia antes de guardar nada en la base de datos.\n• Selección individual: Puedes desmarcar las entradas que prefieras conservar intactas o editar manualmente la propuesta sugerida con un solo toque.\n• Modos de reemplazo: Elige si deseas normalizar únicamente las entradas cuyos títulos sean URLs puras o también nombres genéricos repetitivos.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Reglas dinámicas de URLs (03-LST-RGL)") {
            Text(
                text = "En Ajustes > Reglas de Normalización puedes personalizar los parámetros del motor de limpieza:\n\n• Extensiones TLD descartables: Lista de extensiones (.com, .org, .net, .es, .io) y dominios compuestos (.com.ar, .co.uk) que se remueven del título para dejar solo la marca principal.\n\n• Limpieza de prefijos: Descarta automáticamente 'www.', 'm.', 'login.', 'auth.', 'app.', 'sso.' y rutas de redirección.\n\n• Marcas reconocidas personalizadas: Asocia dominios específicos a nombres oficiales y distintivos (ej: 'github.com' → 'GitHub', 'mercadolibre.com.ar' → 'Mercado Libre').",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Redes locales, Routers y Homelab (Subrutas dinámicas)") {
            Text(
                text = "Bóveda Local cuenta con soporte especializado para administradores de sistemas y entusiastas del auto-alojamiento (Homelab):\n\n• Detección de IPs privadas: Reconoce rangos locales (192.168.x.x, 10.x.x.x, 172.16.x.x, 127.0.0.1, localhost) y puertos comunes (8080, 8443, 9000, 8006).\n\n• Subrutas dinámicas (/ruta → Servicio): Asocia rutas específicas de un mismo servidor a títulos claros. Por ejemplo:\n  - 192.168.1.1/admin → 'Router Principal'\n  - 192.168.1.50/pihole → 'Pi-hole DNS'\n  - 192.168.1.100:8006 → 'Proxmox VE'\n  - 192.168.1.100:9000 → 'Portainer Docker'\n  - 192.168.1.200/truenas → 'TrueNAS Storage'\n\nAl ingresar la URL en una entrada de servidor o login, el título amigable se asigna de forma instantánea.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaConsejoAyuda(
            titulo = "Agrega tus propias subrutas en segundos",
            texto = "En Ajustes > Reglas de Normalización > Rutas de Red Local puedes tocar '+' y agregar cualquier subruta que utilices en tu red casera o de oficina con el nombre del servicio deseado."
        )
    }
}
