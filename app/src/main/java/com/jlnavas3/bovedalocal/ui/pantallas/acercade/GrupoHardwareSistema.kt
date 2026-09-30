package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.util.DatosAuditoria

@Composable
fun GrupoHardwareSistema(
    datos: DatosAuditoria,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false
) {
    ComponenteGrupo(
        etiqueta = "Hardware y sistema operativo",
        icono = Icons.Filled.Smartphone,
        colorIcono = Color(0xFF607D8B),
        idGrupo = "06-SIS-DGN-G03",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ItemMetrica("Dispositivo", "${datos.fabricante} ${datos.modelo} (${datos.dispositivo})")
            ItemMetrica("SoC / Procesador", if (datos.soc != null) "${datos.soc} (${datos.placa})" else datos.placa)
            ItemMetrica("Arquitectura CPU", "${datos.abis} · ${datos.nucleosCpu} núcleos")
            ItemMetrica("Versión Android", "Android ${datos.versionAndroid} (API ${datos.apiSdk})")
            ItemMetrica("Parche de seguridad", datos.parcheSeguridad)
            ItemMetrica("Compilación", datos.compilacion)
        }
    }
}
