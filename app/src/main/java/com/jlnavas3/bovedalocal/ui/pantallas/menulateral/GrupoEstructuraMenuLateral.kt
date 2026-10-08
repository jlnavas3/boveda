package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch

/**
 * Grupo de conmutadores para configurar la estructura y visibilidad de los bloques de la barra lateral.
 */
@Composable
fun GrupoEstructuraMenuLateral(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Estructura y diseño",
        idGrupo = "02-APA-MNL-EST",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Mostrar cabecera",
            subtitulo = "Título de la bóveda y candado superior",
            activo = ajustes.menuLateralMostrarCabecera,
            alCambiar = { vm.ajustarMenuLateralMostrarCabecera(it) },
            idFila = "02-APA-MNL-EST-CAB",
            mostrarId = ajustes.mostrarIdsAjustes
        )

        ComponenteSeparador()

        ComponenteSwitch(
            titulo = "Mostrar pie de seguridad",
            subtitulo = "Cápsulas de perfil Argon2id y versión",
            activo = ajustes.menuLateralMostrarPie,
            alCambiar = { vm.ajustarMenuLateralMostrarPie(it) },
            idFila = "02-APA-MNL-EST-PIE",
            mostrarId = ajustes.mostrarIdsAjustes
        )

        ComponenteSeparador()

        ComponenteSwitch(
            titulo = "Mostrar botón «Bloquear»",
            subtitulo = "Botón inferior de ancho completo para bloqueo inmediato",
            activo = ajustes.menuLateralMostrarBotonBloquear,
            alCambiar = { vm.ajustarMenuLateralMostrarBotonBloquear(it) },
            idFila = "02-APA-MNL-EST-BLO",
            mostrarId = ajustes.mostrarIdsAjustes
        )

        ComponenteSeparador()

        ComponenteSwitch(
            titulo = "Agrupar en categorías",
            subtitulo = "Dividir en tarjetas por sección o mostrar lista plana continua",
            activo = ajustes.menuLateralAgruparItems,
            alCambiar = { vm.ajustarMenuLateralAgruparItems(it) },
            idFila = "02-APA-MNL-EST-AGR",
            mostrarId = ajustes.mostrarIdsAjustes
        )

        ComponenteSeparador()

        ComponenteSwitch(
            titulo = "Sin bordes en barra lateral",
            subtitulo = "Ignorar los bordes de «Formas y bordes» en las tarjetas laterales",
            activo = ajustes.menuLateralSinBordes,
            alCambiar = { vm.ajustarMenuLateralSinBordes(it) },
            idFila = "02-APA-MNL-EST-SBD",
            mostrarId = ajustes.mostrarIdsAjustes
        )
    }
}
