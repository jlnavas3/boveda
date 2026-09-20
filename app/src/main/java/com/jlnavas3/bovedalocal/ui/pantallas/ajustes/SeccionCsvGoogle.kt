package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionCsvGoogle(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    contexto: Context,
    haptica: Haptica,
    alAbrirDialogoImportar: () -> Unit,
    alMostrarDialogoBorradoManual: () -> Unit,
    inicialmenteAbierta: Boolean = false,
    seccionDestino: String? = null
) {
    TarjetaAjuste(
        titulo = "Passwords de Google",
        icono = Icons.Filled.Key,
        descripcion = "Importa un CSV exportado desde Google Password Manager.",
        inicialmenteAbierta = inicialmenteAbierta || (seccionDestino != null && seccionDestino.startsWith("08")) || ajustes.csvGoogleRuta.isNotBlank(),
        colorIcono = ColorExportacion,
        idEtiqueta = "08",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "Importar archivo CSV 'Google Passwords.csv' exportado y descargado desde 'https://passwords.google.com/'.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        BotonColorido(
            texto = "Importar contraseñas de Google",
            color = ColorExportacion,
            icono = Icons.Filled.FileUpload
        ) { alAbrirDialogoImportar() }

        if (ajustes.csvGoogleRuta.isNotBlank()) {
            Spacer(Modifier.height(14.dp))
            if (ajustes.csvGoogleEliminado) {
                Surface(
                    color = ColorSalud.copy(alpha = 0.08f),
                    shape = FormaTarjeta,
                    border = BorderStroke(1.dp, ColorSalud.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = ColorSalud,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Archivo CSV eliminado de forma segura",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    "El archivo original '${ajustes.csvGoogleRuta}' ha sido eliminado. Se encuentran seguras y cifradas ${ajustes.csvGoogleCuentas} contraseñas en tu bóveda.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundario
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        BotonColorido(
                            texto = "Entendido / Cerrar aviso",
                            color = ColorSalud,
                            icono = Icons.Filled.Check
                        ) {
                            haptica.tic()
                            vm.descartarAvisoCsvGoogle()
                        }
                    }
                }
            } else {
                Surface(
                    color = Peligro.copy(alpha = 0.08f),
                    shape = FormaTarjeta,
                    border = BorderStroke(1.dp, Peligro.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Filled.Security,
                                contentDescription = null,
                                tint = Peligro,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Archivo CSV sin cifrar en el almacenamiento",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoPrincipal
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Se importaron con éxito ${ajustes.csvGoogleCuentas} contraseñas a la bóveda. El archivo original sin cifrar sigue guardado en tu teléfono:\n\n📁 ${ajustes.csvGoogleRuta}\n\nCualquier persona o app con acceso a tus archivos puede leer tus contraseñas en texto claro. Se recomienda encarecidamente eliminarlo ahora.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextoSecundario
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        BotonColorido(
                            texto = "Eliminar archivo CSV",
                            color = Peligro,
                            icono = Icons.Filled.Delete
                        ) {
                            haptica.toque()
                            var borrado = false
                            try {
                                if (ajustes.csvGoogleUri.isNotBlank()) {
                                    val docUri = Uri.parse(ajustes.csvGoogleUri)
                                    borrado = DocumentsContract.deleteDocument(contexto.contentResolver, docUri)
                                }
                            } catch (_: Exception) {
                                borrado = false
                            }
                            if (borrado) {
                                haptica.exito()
                                vm.marcarCsvGoogleEliminado(true)
                                Toast.makeText(contexto, "Archivo CSV eliminado del almacenamiento", Toast.LENGTH_SHORT).show()
                            } else {
                                haptica.error()
                                alMostrarDialogoBorradoManual()
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { vm.descartarAvisoCsvGoogle() }) {
                                Text("Descartar aviso", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
