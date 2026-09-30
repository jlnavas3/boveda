package com.jlnavas3.bovedalocal.ui.pantallas.tile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun GrupoConfiguracionTile(
    mostrarIdsAjustes: Boolean,
    tileModo: String,
    tileLongitud: Int,
    tilePatron: String,
    tileCopiarPortapapeles: Boolean,
    tileMostrarToast: Boolean,
    tileHaptica: Boolean,
    tileHapticaIntensidad: Float,
    haptica: Haptica,
    alCambiarTileModo: (String) -> Unit,
    alCambiarTileLongitud: (Int) -> Unit,
    alCambiarTilePatron: (String) -> Unit,
    alCambiarTileCopiarPortapapeles: (Boolean) -> Unit,
    alCambiarTileMostrarToast: (Boolean) -> Unit,
    alCambiarTileHaptica: (Boolean) -> Unit,
    alCambiarTileHapticaIntensidad: (Float) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null
) {
    val opcionesModo = remember {
        listOf(
            OpcionSelectorModal("longitud", "Por longitud", "Longitud de caracteres", "Genera una contraseña aleatoria de longitud fija", Icons.Filled.Key),
            OpcionSelectorModal("diceware", "Frase Diceware", "Frase de palabras Diceware", "Genera una frase de palabras legibles", Icons.Filled.Key),
            OpcionSelectorModal("patron", "Por patrón", "Por patrón personalizado", "Genera según máscara de caracteres (XXXX-XXXX)", Icons.Filled.Tune)
        )
    }

    val opcionesSeparador = remember {
        listOf(
            OpcionSelectorModal("-", "Guion (-)", "Guion (-)", "Separador con guion estándar", Icons.Filled.Key),
            OpcionSelectorModal(".", "Punto (.)", "Punto (.)", "Separador con punto", Icons.Filled.Key),
            OpcionSelectorModal("_", "Guion bajo (_)", "Guion bajo (_)", "Separador con guion bajo", Icons.Filled.Key),
            OpcionSelectorModal(" ", "Espacio ( )", "Espacio ( )", "Separador con espacio en blanco", Icons.Filled.Key)
        )
    }

    ComponenteGrupo(
        etiqueta = "Generación rápida",
        icono = Icons.Filled.DashboardCustomize,
        colorIcono = ColorGenerador,
        alRestablecer = alRestablecerGrupo,
        idGrupo = "04-HER-MSK-G01",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Añade el mosaico en la barra rápida de Android para generar con un toque",
        modifier = modifier
    ) {
        ComponenteSelectorModal(
            titulo = "Modo de generación",
            descripcionModal = "Elige la estrategia de generación al pulsar el mosaico del sistema",
            icono = null,
            idFila = "04-HER-MSK-MOD",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = tileModo,
            opciones = opcionesModo,
            alSeleccionar = alCambiarTileModo
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        if (tileModo == "longitud") {
            val opcionesLongitud = remember {
                AlmacenAjustes.OPCIONES_TILE_LONGITUD.map { (valor, etiqueta) ->
                    OpcionSelectorModal(
                        valor = valor,
                        etiquetaFila = "$valor car.",
                        etiquetaModal = etiqueta,
                        descripcionModal = "Clave de $valor caracteres aleatorios",
                        icono = Icons.Filled.Key
                    )
                }
            }
            ComponenteSelectorModal(
                titulo = "Longitud de la clave",
                descripcionModal = "Cantidad de caracteres generados para la nueva clave",
                icono = null,
                idFila = "04-HER-MSK-LEN",
                mostrarId = mostrarIdsAjustes,
                valorSeleccionado = tileLongitud,
                opciones = opcionesLongitud,
                alSeleccionar = alCambiarTileLongitud
            )

            if (ajustes != null && vm != null) {
                ComponenteSeparador(sangriaInicio = 16.dp)
                Box(modifier = Modifier.padding(16.dp)) {
                    ComponenteCampoTexto(
                        valor = ajustes.tileSimbolos,
                        etiqueta = "Símbolos permitidos",
                        alCambiar = { vm.ajustarTileSimbolos(it) },
                        monoespaciada = true
                    )
                }
            }
        } else if (tileModo == "diceware") {
            if (ajustes != null && vm != null) {
                ComponenteSlider(
                    titulo = "Cantidad de palabras",
                    icono = null,
                    valor = ajustes.tileDicewarePalabras.toFloat(),
                    valorTexto = "${ajustes.tileDicewarePalabras} palabras",
                    rango = 3f..12f,
                    pasos = 8,
                    idFila = "04-HER-MSK-WRD",
                    mostrarId = mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarTileDicewarePalabras(AjustesDefaults.Tile.DICEWARE_PALABRAS)
                    },
                    alCambiar = {
                        vm.ajustarTileDicewarePalabras(it.roundToInt())
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSelectorModal(
                    titulo = "Separador de palabras",
                    icono = null,
                    valorSeleccionado = ajustes.tileDicewareSeparador,
                    opciones = opcionesSeparador,
                    idFila = "04-HER-MSK-SEP",
                    mostrarId = mostrarIdsAjustes,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarTileDicewareSeparador(it)
                    }
                )
            }
        } else {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Column {
                    ComponenteCampoTexto(
                        valor = tilePatron,
                        etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX)",
                        alCambiar = alCambiarTilePatron,
                        monoespaciada = true
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "X: alfanum. mayúscula | A: letra mayúscula | a: minúscula | 9: dígito | w: palabra Diceware",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Copiar al portapapeles",
            icono = null,
            idFila = "04-HER-MSK-CLP",
            mostrarId = mostrarIdsAjustes,
            activo = tileCopiarPortapapeles,
            alCambiar = alCambiarTileCopiarPortapapeles
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Aviso emergente (Toast)",
            icono = null,
            idFila = "04-HER-MSK-TST",
            mostrarId = mostrarIdsAjustes,
            activo = tileMostrarToast,
            alCambiar = alCambiarTileMostrarToast
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Vibración táctil",
            icono = null,
            idFila = "04-HER-MSK-HAP",
            mostrarId = mostrarIdsAjustes,
            activo = tileHaptica,
            alCambiar = alCambiarTileHaptica
        )

        if (tileHaptica) {
            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSlider(
                titulo = "Intensidad de vibración",
                valor = tileHapticaIntensidad,
                valorTexto = "${(tileHapticaIntensidad * 100).roundToInt()}%",
                rango = 0.01f..1.0f,
                pasos = 99,
                etiquetaMin = "1% (Mínima)",
                etiquetaMax = "100%",
                idFila = "04-HER-MSK-HIN",
                mostrarId = mostrarIdsAjustes,
                icono = null,
                alRestablecer = {
                    alCambiarTileHapticaIntensidad(AjustesDefaults.Tile.HAPTICA_INTENSIDAD)
                    haptica.probar(AjustesDefaults.Tile.HAPTICA_INTENSIDAD)
                },
                alCambiar = {
                    alCambiarTileHapticaIntensidad(it)
                    haptica.probar(it)
                }
            )
        }
    }
}
