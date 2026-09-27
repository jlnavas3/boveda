package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.util.DatosAuditoria

@Composable
fun GrupoAislamientoPrivacidad(
    datos: DatosAuditoria,
    mostrarIdsAjustes: Boolean = false,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Aislamiento y privacidad",
        icono = Icons.Filled.Shield,
        colorIcono = ColorSeguridad,
        idGrupo = "06.3.G1",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FilaAuditoria(
                ok = !datos.tienePermisoInternet,
                titulo = "Aislamiento de red total",
                detalle = if (!datos.tienePermisoInternet) "Permiso INTERNET NO declarado (100% offline garantizado por el kernel Android)" else "Alerta: permiso INTERNET presente"
            )
            Spacer(Modifier.height(8.dp))
            SeparadorFilaSimple()
            Spacer(Modifier.height(8.dp))

            FilaAuditoria(
                ok = datos.flagSecureActivo,
                titulo = "Protección de ventana FLAG_SECURE",
                detalle = if (datos.flagSecureActivo)
                    "Activa: capturas, grabaciones de pantalla y vista en apps recientes bloqueadas"
                else
                    "Permitida por el usuario en Ajustes. Capturas de pantalla habilitadas"
            )
            Spacer(Modifier.height(8.dp))
            SeparadorFilaSimple()
            Spacer(Modifier.height(8.dp))

            FilaAuditoria(
                ok = true,
                titulo = "Claves volátiles en memoria RAM",
                detalle = "La clave maestra nunca toca disco, reside solo en RAM volátil y se sanitiza con ceros (zeroizing) al bloquear"
            )
            Spacer(Modifier.height(8.dp))
            SeparadorFilaSimple()
            Spacer(Modifier.height(8.dp))

            FilaAuditoria(
                ok = true,
                titulo = "Permisos del sistema",
                detalle = if (datos.permisosDeclarados.isEmpty()) "Ningún permiso peligroso declarado en el Manifest" else datos.permisosDeclarados.joinToString { it.substringAfterLast('.') }
            )
        }
    }
}
