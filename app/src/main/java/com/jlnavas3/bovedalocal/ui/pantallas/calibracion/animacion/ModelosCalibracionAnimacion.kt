package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

data class InfoParteEngranaje(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val colorActual: Color,
    val colorPorDefecto: Color,
    val mutador: (Color) -> Unit
)

fun crearPartesEngranajes(ajustes: AjustesApp, vm: VaultViewModel): List<InfoParteEngranaje> {
    return listOf(
        InfoParteEngranaje(
            id = "brillo",
            nombre = "Cuerpo: Brillo superior",
            descripcion = "Reflejo metálico en la parte superior del engranaje",
            colorActual = parsearColorO(ajustes.engranajesColorBrillo, Color(0xFFABA799)),
            colorPorDefecto = Color(0xFFABA799),
            mutador = { vm.ajustarEngranajesColor("brillo", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "principal",
            nombre = "Cuerpo: Color principal",
            descripcion = "Tono base dominante del cuerpo de los dientes",
            colorActual = parsearColorO(ajustes.engranajesColorPrincipal, Color(0xFF918D7E)),
            colorPorDefecto = Color(0xFF918D7E),
            mutador = { vm.ajustarEngranajesColor("principal", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "sombra_medio",
            nombre = "Cuerpo: Sombra degradado",
            descripcion = "Tono intermedio del degradado radial",
            colorActual = parsearColorO(ajustes.engranajesColorSombraMedio, Color(0xFF635C57)),
            colorPorDefecto = Color(0xFF635C57),
            mutador = { vm.ajustarEngranajesColor("sombra_medio", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "sombra_oscuro",
            nombre = "Cuerpo: Sombra profunda",
            descripcion = "Relieve y sombra inferior del cuerpo metálico",
            colorActual = parsearColorO(ajustes.engranajesColorSombraOscuro, Color(0xFF404038)),
            colorPorDefecto = Color(0xFF404038),
            mutador = { vm.ajustarEngranajesColor("sombra_oscuro", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "bisel",
            nombre = "Bordes y biseles",
            descripcion = "Líneas de contorno, bisel de dientes y remaches",
            colorActual = parsearColorO(ajustes.engranajesColorBisel, Color(0xFFA5A19D)),
            colorPorDefecto = Color(0xFFA5A19D),
            mutador = { vm.ajustarEngranajesColor("bisel", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "interior",
            nombre = "Fondo cavidad interior",
            descripcion = "Fondo de hendidura (transparente para ver a través de las ruedas)",
            colorActual = parsearColorO(ajustes.engranajesColorInterior, Color(0x00000000)),
            colorPorDefecto = Color(0x00000000),
            mutador = { vm.ajustarEngranajesColor("interior", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "cubo",
            nombre = "Cubo central (remache)",
            descripcion = "Centro de rotación del engranaje",
            colorActual = parsearColorO(ajustes.engranajesColorCubo, Color(0xFFD3D1C8)),
            colorPorDefecto = Color(0xFFD3D1C8),
            mutador = { vm.ajustarEngranajesColor("cubo", it.aHex()) }
        ),
        InfoParteEngranaje(
            id = "eje",
            nombre = "Eje central (núcleo)",
            descripcion = "Orificio interior y ranura del eje de giro",
            colorActual = parsearColorO(ajustes.engranajesColorEje, Color(0xFF141316)),
            colorPorDefecto = Color(0xFF141316),
            mutador = { vm.ajustarEngranajesColor("eje", it.aHex()) }
        )
    )
}
