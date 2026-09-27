package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.util.DatosAuditoria

@Composable
fun GrupoHardwareSistema(
    datos: DatosAuditoria,
    modifier: Modifier = Modifier
) {
    GrupoAjustes(
        etiqueta = "Hardware y sistema operativo",
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
