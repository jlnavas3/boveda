package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal de advertencia de seguridad previo a compartir la contraseña maestra vía Intent del sistema.
 */
@Composable
fun DialogoAdvertenciaCompartir(
    contrasena: String,
    contexto: Context,
    alCerrar: () -> Unit
) {
    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        fijarAbajo = false,
        titulo = "Advertencia de Seguridad",
        descripcion = "Resguardo de contraseña maestra",
        icono = Icons.Filled.Warning,
        colorIcono = Color.White,
        fondoIcono = Peligro,
        mostrarBotonCerrar = false
    ) {
        Text(
            text = "Vas a compartir tu contraseña maestra mediante las opciones del sistema.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoPrincipal
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Si decides enviártela a ti mismo (por ejemplo en WhatsApp a tu propio chat, «Mensajes guardados» en Telegram o en tus notas), te recomendamos fuertemente memorizarla y resguardarla en un lugar seguro fuera del alcance de terceros.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = TextoSecundario
        )
        Spacer(Modifier.height(12.dp))

        ComponenteAlerta(
            tipo = TipoAlerta.DANGER,
            titulo = "Sin recuperación posible",
            mensaje = "La app no almacena tu contraseña maestra. Si la olvidas o pierdes, será matemáticamente imposible descifrar tu bóveda."
        )

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonBoveda(
                texto = "Cancelar",
                variante = VarianteBoton.SECUNDARIO,
                modifier = Modifier.weight(1f),
                alPulsar = alCerrar
            )

            BotonBoveda(
                texto = "Compartir",
                variante = VarianteBoton.PELIGRO,
                icono = Icons.Filled.Share,
                modifier = Modifier.weight(1f),
                alPulsar = {
                    alCerrar()
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Contraseña maestra - Bóveda Local")
                        putExtra(Intent.EXTRA_TEXT, contrasena)
                    }
                    contexto.startActivity(Intent.createChooser(intent, "Guardar o compartir contraseña"))
                }
            )
        }
    }
}
