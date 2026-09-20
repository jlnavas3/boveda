package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesAutenticador(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Autenticador 2FA",
                idEtiqueta = "04.1",
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
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Parámetros predeterminados para códigos TOTP generados manualmente")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Valores manuales
                ComponenteGrupo(
                    etiqueta = "Valores manuales predeterminados",
                    idGrupo = "04.1.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Se aplican al introducir claves secretas Base32 sin código QR"
                ) {
                    val opcionesDigitos = remember {
                        listOf(
                            OpcionSelectorModal(6, "6 dígitos", "6 dígitos", "Estándar común para Google Authenticator, Microsoft y Authy", Icons.Filled.Timer),
                            OpcionSelectorModal(7, "7 dígitos", "7 dígitos", "Formato empleado en algunos servicios corporativos", Icons.Filled.Timer),
                            OpcionSelectorModal(8, "8 dígitos", "8 dígitos", "Alta entropía para banca y servicios gubernamentales", Icons.Filled.Timer)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Dígitos predeterminados",
                        descripcionModal = "Número de cifras numéricas para códigos generados manualmente",
                        icono = Icons.Filled.Timer,
                        colorIcono = Color2FA,
                        idFila = "04.1.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.totpManualDigitos,
                        opciones = opcionesDigitos,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarTotpManualDigitos(valor)
                        }
                    )

                    ComponenteSeparador()

                    val opcionesPeriodo = remember {
                        listOf(
                            OpcionSelectorModal(30, "30 segundos", "30 segundos", "Estándar universal de renovación TOTP", Icons.Filled.Timer),
                            OpcionSelectorModal(60, "60 segundos", "60 segundos", "Mayor ventana de tiempo para introducir el código", Icons.Filled.Timer),
                            OpcionSelectorModal(90, "90 segundos", "90 segundos", "Ventana extendida para entornos con latencia", Icons.Filled.Timer)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Período de renovación",
                        descripcionModal = "Frecuencia con la que expira y cambia el código generado",
                        icono = Icons.Filled.Timer,
                        colorIcono = Color2FA,
                        idFila = "04.1.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.totpManualPeriodo,
                        opciones = opcionesPeriodo,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarTotpManualPeriodo(valor)
                        }
                    )

                    ComponenteSeparador()

                    val opcionesHash = remember {
                        listOf(
                            OpcionSelectorModal("HmacSHA1", "SHA-1", "SHA-1 (Predeterminado)", "Compatible con el 99% de servicios que usan TOTP", Icons.Filled.Security),
                            OpcionSelectorModal("HmacSHA256", "SHA-256", "SHA-256", "Recomendado por RFC 6238 con mayor seguridad de digest", Icons.Filled.Security),
                            OpcionSelectorModal("HmacSHA512", "SHA-512", "SHA-512", "Máxima robustez criptográfica contra colisiones", Icons.Filled.Security)
                        )
                    }
                    ComponenteSelectorModal(
                        titulo = "Algoritmo de hash",
                        descripcionModal = "Función criptográfica para calcular el código de verificación",
                        icono = Icons.Filled.Security,
                        colorIcono = Color2FA,
                        idFila = "04.1.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.totpManualAlgoritmo,
                        opciones = opcionesHash,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarTotpManualAlgoritmo(valor)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteSwitch(
                        titulo = "Separar dígitos (123 456)",
                        icono = Icons.Filled.Numbers,
                        colorIcono = Color2FA,
                        idFila = "04.1.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        activo = ajustes.totpSepararDigitos,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarTotpSepararDigitos(it)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer grupo",
                        alPulsar = {
                            vm.ajustarTotpManualDigitos(6)
                            vm.ajustarTotpManualPeriodo(30)
                            vm.ajustarTotpManualAlgoritmo("HmacSHA1")
                            vm.ajustarTotpSepararDigitos(true)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Accesos rápidos
                ComponenteGrupo(
                    etiqueta = "Accesos rápidos",
                    idGrupo = "04.1.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Atajos de integración directa en pantalla"
                ) {
                    ComponenteNavegacion(
                        titulo = "Personalizar widget de inicio",
                        icono = Icons.Filled.Widgets,
                        colorIcono = Color2FA,
                        idFila = "04.1.5",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.AjustesWidget("03.3"))
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
