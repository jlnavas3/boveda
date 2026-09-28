package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.Color2FA

@Composable
fun GrupoValoresManualesTotp(
    mostrarId: Boolean,
    totpManualDigitos: Int,
    totpManualPeriodo: Int,
    totpManualAlgoritmo: String,
    totpSepararDigitos: Boolean,
    alCambiarDigitos: (Int) -> Unit,
    alCambiarPeriodo: (Int) -> Unit,
    alCambiarAlgoritmo: (String) -> Unit,
    alCambiarSepararDigitos: (Boolean) -> Unit,
    alRestablecerGrupo: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Valores manuales predeterminados",
        icono = Icons.Filled.Pin,
        colorIcono = Color2FA,
        alRestablecer = alRestablecerGrupo,
        idGrupo = "04-HER-AUT-G01",
        mostrarId = mostrarId,
        descripcion = "Se aplican al introducir claves secretas Base32 sin código QR",
        modifier = modifier
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
            icono = null,
            idFila = "04-HER-AUT-DIG",
            mostrarId = mostrarId,
            valorSeleccionado = totpManualDigitos,
            opciones = opcionesDigitos,
            alSeleccionar = alCambiarDigitos
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

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
            icono = null,
            idFila = "04-HER-AUT-PRD",
            mostrarId = mostrarId,
            valorSeleccionado = totpManualPeriodo,
            opciones = opcionesPeriodo,
            alSeleccionar = alCambiarPeriodo
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

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
            icono = null,
            idFila = "04-HER-AUT-ALG",
            mostrarId = mostrarId,
            valorSeleccionado = totpManualAlgoritmo,
            opciones = opcionesHash,
            alSeleccionar = alCambiarAlgoritmo
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Separar dígitos (123 456)",
            icono = null,
            idFila = "04-HER-AUT-SEP",
            mostrarId = mostrarId,
            activo = totpSepararDigitos,
            alCambiar = alCambiarSepararDigitos
        )
    }
}
