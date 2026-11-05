/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda.articulos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.ContenedorArticuloDetalleAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaAdvertenciaAyuda
import com.jlnavas3.bovedalocal.ui.pantallas.ayuda.TarjetaInformativaAyuda
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Artículo de ayuda sobre los mecanismos de autodestrucción por intentos o PIN de pánico.
 */
@Composable
fun PantallaAyudaAutodestruccion(vm: VaultViewModel, alVolver: () -> Unit) {
    ContenedorArticuloDetalleAyuda(
        titulo = "Autodestrucción de emergencia",
        alVolver = alVolver,
        textoAccion = "Configurar Autodestrucción",
        iconoAccion = Icons.Filled.DeleteForever,
        alEjecutarAccion = { vm.ir(Pantalla.AjustesAutodestruccion()) }
    ) {
        TarjetaInformativaAyuda(titulo = "Contramedida radical ante intrusión física") {
            Text(
                text = "La autodestrucción es un mecanismo de defensa de última instancia diseñado para entornos hostiles, viajes a países con regímenes opresivos o riesgo de decomiso de dispositivos. Garantiza que nadie pueda acceder a tus secretos mediante ataques de fuerza bruta continuados o coacción física directa.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modalidad 1: Autodestrucción por intentos fallidos") {
            Text(
                text = "En Ajustes > Seguridad > Autodestrucción por intentos (01-SEG-DES-INT) puedes fijar un límite de claves erróneas (ej. 5 o 10 intentos):\n\n• Si un tercero o software forense intenta adivinar tu contraseña repetidamente, al alcanzar el número fijado la aplicación no impone una simple espera: borra y trunca la base de datos de manera definitiva.\n• Un contador silencioso registra los fallos y se reinicia automáticamente en cuanto se introduce la clave maestra correcta.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "Modalidad 2: PIN de pánico / emergencia") {
            Text(
                text = "En Ajustes > Seguridad > Autodestrucción por PIN (01-SEG-DES-PIN) puedes definir un código numérico de emergencia:\n\n• Si eres forzado físicamente a abrir la bóveda frente a un agresor, introduce el PIN de emergencia en lugar de tu contraseña habitual.\n• Bóveda Local simulará un fallo genérico o cerrará la aplicación mientras en milisegundos ejecuta el protocolo de destrucción en segundo plano.",
                fontSize = 13.sp,
                color = TextoPrincipal,
                lineHeight = 18.5.sp
            )
        }

        TarjetaInformativaAyuda(titulo = "El protocolo de destrucción criptográfica") {
            Text(
                text = "La autodestrucción no se limita a 'borrar archivos' ordinarios:\n\n1. Sobreescribe con ceros criptográficos y bytes aleatorios los bloques de memoria que contienen claves derivadas.\n2. Invalida y desregistra todas las credenciales de Android Keystore.\n3. Trunca y borra físicamente el archivo de la base de datos SQLCipher y los adjuntos temporales.\n4. Deja la aplicación como si recién hubiera sido instalada desde cero.",
                fontSize = 13.sp,
                color = TextoSecundario,
                lineHeight = 18.5.sp
            )
        }

        TarjetaAdvertenciaAyuda(
            titulo = "Borrado irreversible sin recuperación",
            texto = "La activación de la autodestrucción destruye todos los datos del teléfono sin posibilidad alguna de marcha atrás. Mantén siempre una copia de seguridad .bvda actualizada en un medio físico externo seguro (USB o disco offline) para poder recuperar tus datos tras un incidente."
        )
    }
}
