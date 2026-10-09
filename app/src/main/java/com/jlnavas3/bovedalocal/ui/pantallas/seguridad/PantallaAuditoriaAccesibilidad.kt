package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.DetectorServiciosAccesibilidad
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada a la auditoría de accesibilidad:
 * Detecta aplicaciones espías o con permisos de accesibilidad activos en Android,
 * permite gestionarlas en lista blanca o abrir los ajustes del sistema.
 */
@Composable
fun PantallaAuditoriaAccesibilidad(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val duenoCicloVida = LocalLifecycleOwner.current
    var triggerCicloVida by remember { mutableStateOf(0) }
    DisposableEffect(duenoCicloVida) {
        val observador = LifecycleEventObserver { _, evento ->
            if (evento == Lifecycle.Event.ON_RESUME) {
                triggerCicloVida++
            }
        }
        duenoCicloVida.lifecycle.addObserver(observador)
        onDispose { duenoCicloVida.lifecycle.removeObserver(observador) }
    }

    val diagnostico = remember(contexto, ajustes.listaBlancaAccesibilidad, triggerCicloVida) {
        DetectorServiciosAccesibilidad.evaluar(contexto, ajustes.listaBlancaAccesibilidad)
    }

    val sospechosos = diagnostico.serviciosSospechosos
    val enListaBlanca = diagnostico.servicios.filter { it.esEnListaBlanca }
    val delSistema = diagnostico.servicios.filter { it.esAppSistema }.sortedByDescending { it.estaActivo }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Auditoría de accesibilidad",
                idEtiqueta = "01-SEG-ACC",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DescripcionPantalla(
                    subtitulo = "Los servicios de accesibilidad en Android tienen permisos para leer el contenido en pantalla, capturar eventos táctiles e inspeccionar otras aplicaciones."
                )

                BotonAjustesAccesibilidadSistema()

                // Banner positivo si no hay aplicaciones no autorizadas activas
                if (sospechosos.isEmpty() && enListaBlanca.isEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Menta.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = Menta,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "Sin aplicaciones de terceros con permisos de accesibilidad activos.",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = Menta
                            )
                        }
                    }
                }

                // Sección 1: Sospechosos / No autorizados
                if (sospechosos.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "APLICACIONES NO AUTORIZADAS (${sospechosos.size})",
                            color = Peligro,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        sospechosos.forEach { item ->
                            TarjetaServicioAccesibilidad(
                                servicio = item,
                                alAlternarListaBlanca = { paquete, agregar ->
                                    haptica.tic()
                                    if (agregar) {
                                        vm.agregarAppListaBlancaAccesibilidad(paquete)
                                        vm.avisar("App agregada a la lista blanca de confianza")
                                    } else {
                                        vm.quitarAppListaBlancaAccesibilidad(paquete)
                                    }
                                }
                            )
                        }
                    }
                }

                // Sección 2: En lista blanca
                if (enListaBlanca.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "APLICACIONES DE CONFIANZA / LISTA BLANCA (${enListaBlanca.size})",
                            color = Menta,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        enListaBlanca.forEach { item ->
                            TarjetaServicioAccesibilidad(
                                servicio = item,
                                alAlternarListaBlanca = { paquete, agregar ->
                                    haptica.tic()
                                    if (agregar) {
                                        vm.agregarAppListaBlancaAccesibilidad(paquete)
                                    } else {
                                        vm.quitarAppListaBlancaAccesibilidad(paquete)
                                        vm.avisar("App removida de la lista blanca")
                                    }
                                }
                            )
                        }
                    }
                }

                // Sección 3: Servicios del sistema
                if (delSistema.isNotEmpty()) {
                    val activosSistema = delSistema.count { it.estaActivo }
                    val subtituloSeccion = if (activosSistema > 0) {
                        "SERVICIOS DEL SISTEMA ($activosSistema activos / ${delSistema.size} totales)"
                    } else {
                        "SERVICIOS DEL SISTEMA (${delSistema.size} disponibles, inactivos)"
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = subtituloSeccion,
                            color = ColorAjusteGris,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        )
                        delSistema.forEach { item ->
                            TarjetaServicioAccesibilidad(
                                servicio = item,
                                alAlternarListaBlanca = { _, _ -> }
                            )
                        }
                    }
                }

                if (diagnostico.servicios.isEmpty()) {
                    Text(
                        text = "No se detectaron servicios de accesibilidad registrados en el sistema operativo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ColorAjusteGris,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
