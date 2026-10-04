package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.SelectAll
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

val CATEGORIAS_OPCIONES_REGISTRO: List<CategoriaOpcion> = listOf(
    CategoriaOpcion("Todos", "Todos los eventos del sistema", Icons.Filled.SelectAll, ColorAcento),
    CategoriaOpcion("Bóveda", "Apertura, cifrado, cambios de clave y entradas", Icons.Filled.Lock, Menta),
    CategoriaOpcion("Papelera", "Entradas eliminadas, restauradas y vaciado", Icons.Filled.Delete, ColorPapelera),
    CategoriaOpcion("Portapapeles", "Elementos copiados y vaciado automático", Icons.Filled.ContentCopy, Advertencia),
    CategoriaOpcion("2FA", "Códigos TOTP, sincronización y doble factor", Icons.Filled.Password, Color2FA),
    CategoriaOpcion("Huella", "Autenticación biométrica y Keystore de Android", Icons.Filled.Fingerprint, ColorSeguridad),
    CategoriaOpcion("Cámara", "Escaneo de QR y motores de cámara", Icons.Filled.CameraAlt, ColorAcento),
    CategoriaOpcion("Autofill", "Autocompletado, Passkeys y Credential Manager", Icons.Filled.Description, ColorPasskeys),
    CategoriaOpcion("Errores", "Fallos, excepciones y anomalías", Icons.Filled.ErrorOutline, Peligro)
)
