package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel

/**
 * Representa un ítem granular en el catálogo global de búsqueda de Ajustes.
 */
data class ItemBusquedaAjustes(
    val titulo: String,
    val subtitulo: String,
    val ruta: String,
    val icono: ImageVector,
    val colorIcono: Color,
    val idEtiqueta: String,
    val palabrasClave: String,
    val alPulsar: () -> Unit
)

/**
 * Construye el índice exhaustivo de búsqueda que contiene todas las opciones
 * principales y sub-configuraciones internas de Bóveda Local conectadas al catálogo [MapaAjustes].
 */
fun crearIndiceBusquedaAjustes(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    alAbrirNombreBoveda: () -> Unit,
    alAbrirProveedorPasskeys: () -> Unit,
    alAbrirCambioMaestra: () -> Unit
): List<ItemBusquedaAjustes> {
    val items = mutableListOf<ItemBusquedaAjustes>()
    val idsRegistrados = mutableSetOf<String>()

    // 1. Integrar todos los nodos registrados en MapaAjustes (N1, N2, N3)
    MapaAjustes.TODOS_LOS_NODOS.forEach { nodo ->
        val (icono, color) = MapaAjustes.resolverIconoYColor(nodo)
        val accion: () -> Unit = when {
            nodo.accionEspecial == AccionEspecialAjuste.CAMBIAR_MAESTRA -> alAbrirCambioMaestra
            nodo.accionEspecial == AccionEspecialAjuste.NOMBRE_BOVEDA -> alAbrirNombreBoveda
            nodo.accionEspecial == AccionEspecialAjuste.PROVEEDOR_PASSKEYS -> alAbrirProveedorPasskeys
            nodo.pantallaDestino != null -> { { vm.ir(nodo.pantallaDestino) } }
            else -> { { vm.irPorId(nodo.id) } }
        }
        items += ItemBusquedaAjustes(
            titulo = nodo.titulo,
            subtitulo = nodo.subtitulo,
            ruta = nodo.ruta,
            icono = icono,
            colorIcono = color,
            idEtiqueta = nodo.id,
            palabrasClave = "${nodo.palabrasClave.joinToString(" ")} ${nodo.id} ${nodo.grupo}",
            alPulsar = accion
        )
        idsRegistrados += nodo.id
    }

    // 2. Acciones y atajos granulares profundos adicionales
    fun agregarGranular(
        id: String,
        titulo: String,
        subtitulo: String,
        ruta: String,
        icono: ImageVector,
        colorIcono: Color,
        palabrasClave: String,
        alPulsar: () -> Unit
    ) {
        if (!idsRegistrados.contains(id)) {
            items += ItemBusquedaAjustes(
                titulo = titulo,
                subtitulo = subtitulo,
                ruta = ruta,
                icono = icono,
                colorIcono = colorIcono,
                idEtiqueta = id,
                palabrasClave = palabrasClave,
                alPulsar = alPulsar
            )
            idsRegistrados += id
        }
    }

    // Seguridad granular
    agregarGranular(
        id = "01-SEG-BIO-INA",
        titulo = "Bloqueo por inactividad",
        subtitulo = "Tiempo de espera antes de requerir clave (1m, 5m, 10m, 20m, 30m)",
        ruta = "Seguridad > Biometría y seguridad > Bloqueo y biometría",
        icono = Icons.Filled.Timer,
        colorIcono = Color(0xFF1E88E5),
        palabrasClave = "inactividad bloqueo tiempo minutos espera automatico cerrar sesion temporizador",
        alPulsar = { vm.ir(Pantalla.BloqueoBiometria("01-SEG-BIO-BLO")) }
    )
    agregarGranular(
        id = "01-SEG-BIO-BRU",
        titulo = "Protección contra fuerza bruta",
        subtitulo = "Límite de intentos fallidos antes del bloqueo temporal progresivo",
        ruta = "Seguridad > Biometría y seguridad > Bloqueo y biometría",
        icono = Icons.Filled.Security,
        colorIcono = Color(0xFFE53935),
        palabrasClave = "fuerza bruta intentos fallidos bloqueo penalizacion tiempo maximo espera reintentos",
        alPulsar = { vm.ir(Pantalla.BloqueoBiometria("01-SEG-BIO-BLO")) }
    )
    agregarGranular(
        id = "01-SEG-BIO-SEC",
        titulo = "Ocultar contenido en apps recientes",
        subtitulo = "FLAG_SECURE para evitar capturas de pantalla y vista previa",
        ruta = "Seguridad > Biometría y seguridad > Seguridad visual",
        icono = Icons.Filled.Security,
        colorIcono = Color(0xFF1E88E5),
        palabrasClave = "flag secure capturas pantalla recientes ocultar privacidad vista previa",
        alPulsar = { vm.ir(Pantalla.SeguridadVisual("01-SEG-BIO-VIS")) }
    )
    agregarGranular(
        id = "01-SEG-BIO-CLP-SUB",
        titulo = "Limpieza de portapapeles",
        subtitulo = "Borrado automático de contraseñas copiadas de la memoria",
        ruta = "Seguridad > Biometría y seguridad > Portapapeles",
        icono = Icons.Filled.Timer,
        colorIcono = Color(0xFF1E88E5),
        palabrasClave = "portapapeles copiar pegar limpiar borrado tiempo segundos memoria",
        alPulsar = { vm.ir(Pantalla.Portapapeles("01-SEG-BIO-CLP")) }
    )

    // Apariencia granular
    agregarGranular(
        id = "02-APA-THM-GLW",
        titulo = "Efecto de alumbrado y glow",
        subtitulo = "Configurar destellos luminosos al resaltar opciones y navegar",
        ruta = "Sistema > Opciones avanzadas > Alumbrado y resaltado",
        icono = Icons.Filled.AutoAwesome,
        colorIcono = Color(0xFF8E24AA),
        palabrasClave = "glow destello alumbrado luz resaltar brillo intensidad repeticiones duracion",
        alPulsar = { vm.ir(Pantalla.AvanzadaAlumbrado("06-SIS-AVZ-LGT")) }
    )

    // Lista y reglas granulares
    agregarGranular(
        id = "03-LST-RGL",
        titulo = "Reglas de URLs y redes",
        subtitulo = "Subdominios filtrados, extensiones, marcas personalizadas y RFC 1918",
        ruta = "Lista de cuentas > Diseño de lista > Indicadores",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFF00ACC1),
        palabrasClave = "reglas urls redes subdominios homelab puertos rfc 1918 ip privadas marcas servicios",
        alPulsar = { vm.ir(Pantalla.ReglasNormalizacion) }
    )
    agregarGranular(
        id = "03-LST-RGL-PRT",
        titulo = "Puertos de servicio y homelab",
        subtitulo = "Detectar servicios por puerto (:8080, :8443, :9000, etc.)",
        ruta = "Lista de cuentas > Asistente de títulos > Reglas",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFF00ACC1),
        palabrasClave = "puertos homelab servicio servidor local ip docker portainer proxmox",
        alPulsar = { vm.ir(Pantalla.ReglasNormalizacion) }
    )
    agregarGranular(
        id = "03-LST-RGL-RFC",
        titulo = "Redes privadas e IPC (RFC 1918)",
        subtitulo = "Normalización de rangos IP 192.168.x.x, 10.x.x.x y 172.16.x.x",
        ruta = "Lista de cuentas > Asistente de títulos > Reglas",
        icono = Icons.Filled.Tune,
        colorIcono = Color(0xFF00ACC1),
        palabrasClave = "redes privadas rfc 1918 ip router local intranet subnet octeto",
        alPulsar = { vm.ir(Pantalla.ReglasNormalizacion) }
    )
    agregarGranular(
        id = "03-LST-CAT",
        titulo = "Categorías de cuentas",
        subtitulo = "Organizar entradas por carpetas, etiquetas y grupos",
        ruta = "Lista de cuentas",
        icono = Icons.Filled.Folder,
        colorIcono = Color(0xFF10B981),
        palabrasClave = "categorias carpetas etiquetas clasificar grupos organizar cuentas",
        alPulsar = { vm.ir(Pantalla.Categorias()) }
    )

    return items
}
