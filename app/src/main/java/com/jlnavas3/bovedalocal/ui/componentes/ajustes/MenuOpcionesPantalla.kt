package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

data class AccionSaltoGrupo(
    val idGrupo: String,
    val titulo: String
)

@Composable
fun BotonMenuOpcionesPantalla(
    modifier: Modifier = Modifier,
    grupos: List<AccionSaltoGrupo> = emptyList(),
    alRestablecerPantalla: (() -> Unit)? = null,
    textoRestablecer: String = "Restablecer pantalla",
    mensajeToastRestablecer: String? = null
) {
    val contexto = LocalContext.current
    var menuAbierto by remember { mutableStateOf(false) }
    val coordinador = LocalCoordinadorResaltado.current

    if (grupos.isEmpty() && alRestablecerPantalla == null) return

    Box(modifier = modifier) {
        BotonIconoCabecera(
            onClick = { menuAbierto = true },
            icono = Icons.Default.MoreVert,
            descripcion = "Más opciones"
        )

        MenuDesplegableBoveda(
            expanded = menuAbierto,
            onDismissRequest = { menuAbierto = false },
            modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
        ) {
            grupos.forEach { grupo ->
                com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                    texto = grupo.titulo,
                    icono = Icons.AutoMirrored.Filled.ArrowForward,
                    colorIcono = ColorIconosInternos,
                    colorTexto = TextoPrincipal,
                    onClick = {
                        menuAbierto = false
                        coordinador?.resaltarAjuste(grupo.idGrupo)
                    }
                )
            }

            if (grupos.isNotEmpty() && alRestablecerPantalla != null) {
                SeparadorOpcionMenu()
            }

            if (alRestablecerPantalla != null) {
                com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                    texto = textoRestablecer,
                    icono = Icons.Filled.RestartAlt,
                    colorIcono = Peligro,
                    colorTexto = Peligro,
                    onClick = {
                        menuAbierto = false
                        Haptica(contexto).tic()
                        alRestablecerPantalla()
                        val mensaje = mensajeToastRestablecer ?: "Ajustes de pantalla restablecidos"
                        Toast.makeText(contexto, mensaje, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
