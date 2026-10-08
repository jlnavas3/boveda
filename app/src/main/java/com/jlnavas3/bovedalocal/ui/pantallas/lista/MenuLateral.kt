package com.jlnavas3.bovedalocal.ui.pantallas.lista

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Menú lateral personalizable y rediseñado estilo MagicOS / Samsung One UI:
 * - Opciones dinámicas de visibilidad (cabecera, pie, botón bloquear).
 * - Modo agrupado por tarjetas o lista plana continua.
 * - Soporte para sin bordes en tarjetas.
 * - Ítems y orden dinámicos configurables por el usuario.
 */
@Composable
fun MenuLateral(
    nombreApp: String,
    totalEntradas: Int,
    totalPapelera: Int,
    totalDuplicadas: Int = 0,
    perfilArgon2: PerfilArgon2 = PerfilArgon2.ESTANDAR,
    mostrarIds: Boolean = false,
    ajustes: AjustesApp? = null,
    alIr: (Pantalla) -> Unit,
    alBloquear: () -> Unit = {}
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val topAjusted = (topInset - 24.dp).coerceAtLeast(8.dp)

    val mostrarCabecera = ajustes?.menuLateralMostrarCabecera != false
    val mostrarPie = ajustes?.menuLateralMostrarPie != false
    val mostrarBotonBloquear = ajustes?.menuLateralMostrarBotonBloquear != false
    val agruparItems = ajustes?.menuLateralAgruparItems != false
    val sinBordes = ajustes?.menuLateralSinBordes == true

    val itemsVisibles = remember(ajustes?.menuLateralItemsVisibles) {
        val listaCruda = ajustes?.menuLateralItemsVisibles ?: AjustesDefaults.MenuLateral.ITEMS_PREDETERMINADOS
        listaCruda.filter { id ->
            !(id == "04-HER-PSK" && Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp, end = 14.dp, top = topAjusted, bottom = 8.dp)
        ) {
            CabeceraMenuLateral(
                nombreApp = nombreApp,
                mostrarTitulo = mostrarCabecera,
                alPersonalizar = { alIr(Pantalla.PersonalizarMenuLateral()) }
            )
            Spacer(Modifier.height(if (mostrarCabecera) 10.dp else 4.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (agruparItems) {
                    val grupos = remember(itemsVisibles) {
                        val mapa = linkedMapOf<String, MutableList<String>>()
                        for (id in itemsVisibles) {
                            val grupo = resolverGrupoItemMenuLateral(id)
                            mapa.getOrPut(grupo) { mutableListOf() }.add(id)
                        }
                        mapa
                    }

                    grupos.forEach { (nombreGrupo, itemsDelGrupo) ->
                        GrupoMenuLateral(
                            titulo = nombreGrupo,
                            idEtiqueta = resolverIdEtiquetaGrupo(nombreGrupo),
                            mostrarId = mostrarIds,
                            ajustes = ajustes
                        ) {
                            itemsDelGrupo.forEachIndexed { indice, id ->
                                if (indice > 0) {
                                    SeparadorItemMenu()
                                }
                                FilaItemMenuLateral(
                                    id = id,
                                    totalDuplicadas = totalDuplicadas,
                                    totalPapelera = totalPapelera,
                                    mostrarIds = mostrarIds,
                                    ajustes = ajustes,
                                    alIr = alIr
                                )
                            }
                        }
                    }
                } else {
                    // Modo lista plana continua
                    val formaTarjeta = RoundedCornerShape(CurvaturaEsquinas)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && !sinBordes) {
                                    Modifier.border(
                                        width = GrosorBorde,
                                        color = ColorBordeActual,
                                        shape = formaTarjeta
                                    )
                                } else Modifier
                            )
                            .clip(formaTarjeta)
                            .background(ColorTarjetaAjustes)
                    ) {
                        itemsVisibles.forEachIndexed { indice, id ->
                            if (indice > 0) {
                                SeparadorItemMenu()
                            }
                            FilaItemMenuLateral(
                                id = id,
                                totalDuplicadas = totalDuplicadas,
                                totalPapelera = totalPapelera,
                                mostrarIds = mostrarIds,
                                ajustes = ajustes,
                                alIr = alIr
                            )
                        }
                    }
                }
            }

            if (mostrarPie) {
                Spacer(Modifier.height(8.dp))
                PieMenuLateral(perfilArgon2 = perfilArgon2)
            }
        }

        if (mostrarBotonBloquear) {
            BotonFilaBloquear(alBloquear = alBloquear)
        }
    }
}
