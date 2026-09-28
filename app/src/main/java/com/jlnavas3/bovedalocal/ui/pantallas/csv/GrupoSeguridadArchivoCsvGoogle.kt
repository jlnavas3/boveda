package com.jlnavas3.bovedalocal.ui.pantallas.csv

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun GrupoSeguridadArchivoCsvGoogle(
    mostrarId: Boolean,
    csvGoogleEliminado: Boolean,
    csvGoogleCuentas: Int,
    alSolicitarBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Seguridad del archivo CSV",
        icono = Icons.Filled.Security,
        colorIcono = Peligro,
        idGrupo = "05-COP-CSV-G02",
        mostrarId = mostrarId,
        descripcion = "Estado de protección contra fugas de texto claro",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (csvGoogleEliminado) {
                ComponenteAlerta(
                    tipo = TipoAlerta.SUCCESS,
                    titulo = "Archivo CSV eliminado de forma segura",
                    mensaje = "Se importaron $csvGoogleCuentas cuentas.",
                    icono = Icons.Filled.CheckCircle
                )
            } else {
                ComponenteAlerta(
                    tipo = TipoAlerta.DANGER,
                    titulo = "¡Atención: archivo CSV sin cifrar!",
                    mensaje = "El archivo descargado contiene todas tus claves en texto claro y cualquier aplicación con permiso de almacenamiento podría leerlo.",
                    icono = Icons.Filled.Security,
                    accion = {
                        ComponenteBotonFila(
                            titulo = "Eliminar archivo CSV original",
                            colorIcono = Peligro,
                            icono = Icons.Filled.Delete,
                            alPulsar = alSolicitarBorrado
                        )
                    }
                )
            }
        }
    }
}
