package com.pepotech.pepoboveda.ui.pantallas

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.pepotech.pepoboveda.ui.VaultViewModel
import com.pepotech.pepoboveda.ui.theme.Ambar
import com.pepotech.pepoboveda.ui.theme.Obsidiana
import com.pepotech.pepoboveda.ui.theme.Peligro
import com.pepotech.pepoboveda.ui.theme.TextoPrincipal
import com.pepotech.pepoboveda.ui.theme.TextoSecundario
import com.pepotech.pepoboveda.util.AjustesSistema
import com.pepotech.pepoboveda.util.Diagnostico
import com.pepotech.pepoboveda.util.Portapapeles

@Composable
fun PantallaRegistro(vm: VaultViewModel) {
    val contexto = LocalContext.current
    var registro by remember { mutableStateOf<List<String>>(emptyList()) }
    var refresco by remember { mutableIntStateOf(0) }
    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }
    LaunchedEffect(refresco) { registro = Diagnostico.ultimas(Diagnostico.MAX_LINEAS_MEMORIA) }

    fun textoRegistro(): String = registro.joinToString("\n")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { vm.ir(com.pepotech.pepoboveda.ui.Pantalla.Ajustes) }, modifier = Modifier.size(48.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver a ajustes", tint = TextoPrincipal, modifier = Modifier.size(28.dp))
            }
            Text("Registro", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "Historial técnico local de Bóveda local. Nunca incluye contraseñas, PINes, títulos ni dominios.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Obsidiana)
                .padding(12.dp)
                .horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState())
        ) {
            if (registro.isEmpty()) {
                Text("(nada registrado todavía)", color = TextoSecundario, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
            } else {
                registro.forEach { linea ->
                    Text(linea, color = TextoPrincipal, fontFamily = FontFamily.Monospace, fontSize = 12.sp, maxLines = 1)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                Portapapeles.copiar(contexto, "Registro Bóveda local", textoRegistro())
                vm.avisar("Registro copiado")
            }, modifier = Modifier.size(64.dp)) {
                Icon(Icons.Filled.ContentCopy, contentDescription = "Copiar registro", tint = Ambar, modifier = Modifier.size(32.dp))
            }
            IconButton(onClick = {
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, "Registro Bóveda local")
                    .putExtra(Intent.EXTRA_TEXT, textoRegistro())
                if (!AjustesSistema.abrir(contexto, Intent.createChooser(intent, "Compartir registro"))) {
                    vm.avisar("No hay ninguna app con la que compartirlo")
                }
            }, modifier = Modifier.size(64.dp)) {
                Icon(Icons.Filled.Share, contentDescription = "Compartir registro", tint = Ambar, modifier = Modifier.size(32.dp))
            }
            IconButton(onClick = {
                Diagnostico.borrar()
                registro = emptyList()
                vm.avisar("Registro borrado")
            }, modifier = Modifier.size(64.dp)) {
                Icon(Icons.Filled.Delete, contentDescription = "Borrar registro", tint = Peligro, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}