package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionTileRapido(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    contexto: Context,
    haptica: Haptica
) {
    TarjetaAjuste(
        "Atajo en Ajustes Rápidos (Tile)",
        Icons.Filled.Key,
        "Genera credenciales seguras al instante desde la cortina de notificaciones de Android."
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "Añade el tile 'Generador Rápido' editando los botones de la barra de notificaciones para generar contraseñas con un solo toque sin abrir la bóveda.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(14.dp))
        SelectorAjuste(
            titulo = "Modo de generación",
            icono = Icons.Filled.Tune,
            seleccionado = if (ajustes.tileModo == "patron") "Por patrón personalizado" else "Longitud de caracteres",
            opciones = listOf(
                OpcionAjuste("longitud", "Longitud de caracteres", Icons.Filled.Key),
                OpcionAjuste("patron", "Por patrón personalizado", Icons.Filled.Tune)
            ),
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarTileModo(valor) }
        )
        Spacer(Modifier.height(12.dp))
        if (ajustes.tileModo == "longitud") {
            SelectorAjuste(
                titulo = "Longitud de la clave",
                icono = Icons.Filled.Key,
                seleccionado = "${ajustes.tileLongitud} caracteres",
                opciones = AlmacenAjustes.OPCIONES_TILE_LONGITUD.map { (valor, etiqueta) ->
                    OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Key)
                },
                alSeleccionar = { valor -> haptica.tic(); vm.ajustarTileLongitud(valor.toInt()) }
            )
        } else {
            CampoPepo(
                valor = ajustes.tilePatron,
                etiqueta = "Patrón (ej. XXXXX-XXXXX-XXXXX-XXXXX)",
                alCambiar = { vm.ajustarTilePatron(it) },
                monoespaciada = true
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "X: alfanum. mayúscula | A: letra mayúscula | a: minúscula | 9: dígito | w: palabra Diceware",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(Modifier.height(12.dp))
        FilaAjuste(
            titulo = "Copiar al portapapeles",
            descripcion = "Copia la clave generada directamente al pulsar el tile",
            activo = ajustes.tileCopiarPortapapeles,
            alCambiar = {
                haptica.toque()
                vm.ajustarTileCopiarPortapapeles(it)
                Toast.makeText(
                    contexto,
                    if (it) "Copia al portapapeles activada para el tile" else "Copia al portapapeles desactivada para el tile",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        Spacer(Modifier.height(10.dp))
        FilaAjuste(
            titulo = "Mostrar aviso emergente (Toast)",
            descripcion = "Muestra una confirmación rápida al generar",
            activo = ajustes.tileMostrarToast,
            alCambiar = {
                haptica.toque()
                vm.ajustarTileMostrarToast(it)
                Toast.makeText(
                    contexto,
                    if (it) "Avisos Toast activados para el tile" else "Avisos Toast desactivados para el tile",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        Spacer(Modifier.height(10.dp))
        FilaAjuste(
            titulo = "Microvibración háptica",
            descripcion = "Confirma con una vibración táctil al generar",
            activo = ajustes.tileHaptica,
            alCambiar = {
                haptica.toque()
                vm.ajustarTileHaptica(it)
                Toast.makeText(
                    contexto,
                    if (it) "Microvibración háptica activada para el tile" else "Microvibración háptica desactivada para el tile",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}
