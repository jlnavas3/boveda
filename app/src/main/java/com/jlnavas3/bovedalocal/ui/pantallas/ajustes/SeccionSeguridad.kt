package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionSeguridad(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    contexto: Context,
    haptica: Haptica,
    flujo: FlujoBiometria,
    modoActivo: BiometricKeyStore.Modo?,
    nivel: Biometria.Nivel,
    capacidad: Biometria.Capacidad,
    activarFuerte: () -> Unit,
    ofrecerCompatible: (String) -> Unit,
    inicialmenteAbierta: Boolean = false,
    seccionDestino: String? = null
) {
    val esSenuelo = vm.repositorio.esModoSenuelo
    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()

    TarjetaAjuste(
        titulo = "Seguridad",
        icono = Icons.Filled.Security,
        descripcion = if (esSenuelo) "Bloqueo automático y borrado del portapapeles." else "Huella, bloqueo automático y borrado del portapapeles.",
        colorIcono = ColorSeguridad,
        inicialmenteAbierta = inicialmenteAbierta || (seccionDestino != null && seccionDestino.startsWith("01")),
        idEtiqueta = "01",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        Spacer(Modifier.height(8.dp))

        // Switch interactivo para FLAG_SECURE
        var confirmarDesactivar by remember { mutableStateOf(false) }
        FilaAjuste(
            titulo = "Protección de pantalla (FLAG_SECURE)",
            descripcion = if (ajustes.proteccionPantalla)
                "Activa: capturas de pantalla y vista en recientes están bloqueadas."
            else
                "Desactivada: las capturas de pantalla y la vista en recientes están permitidas. Tu bóveda puede quedar visible en grabaciones y capturas.",
            activo = ajustes.proteccionPantalla,
            alCambiar = { activar ->
                if (activar) {
                    vm.ajustarProteccionPantalla(true)
                    haptica.tic()
                } else {
                    confirmarDesactivar = true
                }
            }
        )
        if (confirmarDesactivar) {
            val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { confirmarDesactivar = false },
                containerColor = colorDialogo,
                tonalElevation = 0.dp,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                title = { Text("¿Desactivar protección de pantalla?", color = TextoPrincipal) },
                text = {
                    Text(
                        "Al desactivar FLAG_SECURE, las capturas de pantalla y la vista previa en " +
                            "aplicaciones recientes estarán permitidas. Tus contraseñas y datos " +
                            "sensibles podrían quedar visibles en grabaciones de pantalla.\n\n" +
                            "Puedes volver a activarla en cualquier momento.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        confirmarDesactivar = false
                        vm.ajustarProteccionPantalla(false)
                        haptica.tic()
                    }) { Text("Desactivar", color = Peligro) }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { confirmarDesactivar = false }) {
                        Text("Cancelar", color = TextoSecundario)
                    }
                }
            )
        }

        if (!esSenuelo) {
            Spacer(Modifier.height(12.dp))
            FilaAjuste(
                titulo = "Abrir con huella",
                descripcion = when {
                    modoActivo != null -> "Activa en modo ${modoActivo.etiqueta}."
                    nivel == Biometria.Nivel.FUERTE ->
                        "La clave maestra se guarda envuelta por el Keystore, atada a tu huella de Clase 3."
                    nivel == Biometria.Nivel.COMPATIBLE ->
                        "${Biometria.explicarFaltaDeFuerte(capacidad)} Hay un modo compatible: Android comprueba la huella o el PIN y la app abre la bóveda."
                    else ->
                        "Sin huella ni PIN utilizables ahora mismo: ${Biometria.explicar(capacidad.compatible)}."
                },
                activo = ajustes.biometriaActiva,
                habilitado = nivel != Biometria.Nivel.NINGUNO || ajustes.biometriaActiva,
                alCambiar = { activar ->
                    if (activar) {
                        when (nivel) {
                            Biometria.Nivel.FUERTE -> activarFuerte()
                            Biometria.Nivel.COMPATIBLE -> ofrecerCompatible(Biometria.explicarFaltaDeFuerte(capacidad))
                            Biometria.Nivel.NINGUNO -> vm.avisar("Este móvil no ofrece huella ni PIN utilizables ahora mismo")
                        }
                    } else {
                        flujo.desactivar()
                        haptica.tic()
                        vm.avisar("Huella desactivada")
                    }
                }
            )
            if (capacidad.fuerte == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED &&
                capacidad.debil != BiometricManager.BIOMETRIC_SUCCESS
            ) {
                Spacer(Modifier.height(10.dp))
                BotonColorido(
                    texto = "Registrar una huella en Android",
                    color = ColorSeguridad,
                    icono = Icons.Filled.Fingerprint
                ) {
                    if (!AjustesSistema.abrirRegistroHuella(contexto)) vm.avisar("No encuentro esa pantalla en este móvil")
                }
            }
            when {
                ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.FUERTE && Biometria.hayCompatible(capacidad) -> {
                    Spacer(Modifier.height(10.dp))
                    BotonColorido(
                        texto = "Cambiar a modo compatible",
                        color = ColorSeguridad,
                        icono = Icons.Filled.Fingerprint
                    ) {
                        ofrecerCompatible("Si la huella te falla en este móvil aunque Android la acepte, el modo compatible suele funcionar.")
                    }
                }
                ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.COMPATIBLE && Biometria.hayFuerte(capacidad) -> {
                    Spacer(Modifier.height(10.dp))
                    BotonColorido(
                        texto = "Volver al modo fuerte",
                        color = ColorSeguridad,
                        icono = Icons.Filled.Security
                    ) {
                        activarFuerte()
                    }
                }
                !ajustes.biometriaActiva && nivel == Biometria.Nivel.FUERTE && Biometria.hayCompatible(capacidad) -> {
                    Spacer(Modifier.height(10.dp))
                    BotonColorido(
                        texto = "Activar en modo compatible",
                        color = ColorSeguridad,
                        icono = Icons.Filled.Fingerprint
                    ) {
                        ofrecerCompatible("Para quien ya sabe que la huella de Clase 3 le falla en este móvil.")
                    }
                }
            }
        } else {
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(8.dp))
        SelectorAjuste(
            titulo = "Bloqueo automático",
            icono = Icons.Filled.Lock,
            seleccionado = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.firstOrNull { it.first == ajustes.autoBloqueoSegundos }?.second ?: "30 segundos",
            opciones = AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.map { (valor, etiqueta) ->
                OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Lock)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarAutoBloqueo(valor.toInt()) }
        )
        Spacer(Modifier.height(14.dp))
        SelectorAjuste(
            titulo = "Borrado del portapapeles",
            icono = Icons.Filled.Backup,
            seleccionado = AlmacenAjustes.OPCIONES_PORTAPAPELES.first { it.first == ajustes.portapapelesSegundos }.second,
            opciones = AlmacenAjustes.OPCIONES_PORTAPAPELES.map { (valor, etiqueta) ->
                OpcionAjuste(valor.toString(), etiqueta, Icons.Filled.Timer)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarPortapapeles(valor.toInt()) }
        )
        BotonRestablecerItem {
            vm.ajustarAutoBloqueo(60)
            vm.ajustarPortapapeles(30)
            vm.ajustarProteccionPantalla(true)
        }
        if (!esSenuelo) {
            Spacer(Modifier.height(14.dp))
            TarjetaAjuste(
                titulo = "Bóveda señuelo",
                icono = Icons.Filled.Shield,
                descripcion = "Apertura señuelo transparente con credenciales simuladas inofensivas.",
                inicialmenteAbierta = seccionDestino == "01.1",
                colorIcono = ColorSeguridad,
                idEtiqueta = "01.1",
                mostrarId = ajustes.mostrarIdsAjustes
            ) {
                Text(
                    text = "Si alguien te obliga a desbloquear la app bajo amenaza o coacción, puedes introducir un PIN señuelo especial. La app se abrirá normalmente pero mostrará solo datos simulados.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(10.dp))
                BotonColorido(
                    texto = "Configurar bóveda señuelo",
                    color = ColorSeguridad,
                    icono = Icons.Filled.Shield
                ) {
                    haptica.toque()
                    vm.irPorId("01.1")
                }
            }

            Spacer(Modifier.height(14.dp))
            TarjetaAjuste(
                titulo = "Autodestrucción",
                icono = Icons.Filled.DeleteForever,
                descripcion = "Borrado irreversible inmediato de la bóveda ante peligro extremo.",
                inicialmenteAbierta = seccionDestino == "01.2",
                colorIcono = Peligro,
                idEtiqueta = "01.2",
                mostrarId = ajustes.mostrarIdsAjustes
            ) {
                Text(
                    text = "Al introducir este PIN en la pantalla de desbloqueo, toda la bóveda y sus claves serán destruidas permanentemente sin dejar rastro en el dispositivo.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(10.dp))
                BotonColorido(
                    texto = "Configurar PIN de autodestrucción",
                    color = Peligro,
                    icono = Icons.Filled.DeleteForever
                ) {
                    haptica.toque()
                    vm.irPorId("01.2")
                }
            }
        }
    }
}

@Composable
fun SeccionArgon2id(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    inicialmenteAbierta: Boolean = false,
    seccionDestino: String? = null
) {
    var perfilPendiente by remember { mutableStateOf<PerfilArgon2?>(null) }

    TarjetaAjuste(
        titulo = "Perfil de cifrado Argon2id",
        icono = Icons.Filled.Memory,
        descripcion = "Intensidad de memoria e iteraciones contra ataques de fuerza bruta.",
        inicialmenteAbierta = inicialmenteAbierta || (seccionDestino != null && seccionDestino.startsWith("03")),
        colorIcono = ColorArgon2,
        idEtiqueta = "03",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        val perfilActual = vm.repositorio.perfilArgon2Actual()
        SelectorPerfilArgon2(
            perfilActual = perfilActual,
            alSeleccionarPerfil = { nuevo ->
                if (nuevo != perfilActual) {
                    if (vm.repositorio.estaDesbloqueada) {
                        perfilPendiente = nuevo
                    } else {
                        vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = nuevo.clave) }
                        com.jlnavas3.bovedalocal.util.Diagnostico.apuntar("bóveda", "Perfil Argon2id predeterminado establecido en ${nuevo.titulo}")
                        vm.avisar("Perfil de cifrado predeterminado: ${nuevo.titulo}")
                    }
                }
            }
        )
        BotonRestablecerItem(texto = "Restablecer predefinido") {
            val perfilDef = PerfilArgon2.ESTANDAR
            if (perfilActual != perfilDef) {
                if (vm.repositorio.estaDesbloqueada) {
                    perfilPendiente = perfilDef
                } else {
                    vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = perfilDef.clave) }
                    com.jlnavas3.bovedalocal.util.Diagnostico.apuntar("bóveda", "Perfil Argon2id predeterminado establecido en ${perfilDef.titulo}")
                    vm.avisar("Perfil de cifrado predeterminado: ${perfilDef.titulo}")
                }
            }
        }
    }

    perfilPendiente?.let { objetivo ->
        DialogoContrasena(
            titulo = "Aplicar ${objetivo.titulo}",
            descripcion = "Para re-cifrar la bóveda con ${objetivo.resumen}, introduce tu contraseña maestra.\n\nNota criptográfica: La huella dactilar no conoce tu contraseña (solo custodia la llave derivada en Keystore). Para derivar la nueva clave con ${objetivo.memoriaKiB / 1024} MiB de memoria, se requiere tu contraseña maestra.",
            textoBoton = "Re-cifrar bóveda",
            alConfirmar = { pass ->
                vm.reForjarBoveda(pass, objetivo) { exito ->
                    if (exito) {
                        haptica.exito()
                        perfilPendiente = null
                    }
                }
            },
            alCancelar = { perfilPendiente = null }
        )
    }
}

@Composable
fun SelectorPerfilArgon2(
    perfilActual: PerfilArgon2,
    alSeleccionarPerfil: (PerfilArgon2) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val forma = FormaCampo

    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val fondoCaja = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)
    val bordeCaja = if (desplegado) ColorAcento else (if (esOscuro) Color(0xFF333238) else Color(0xFFDFDFE3))

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(forma)
                    .background(fondoCaja)
                    .clickable { desplegado = true }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Memory,
                    contentDescription = null,
                    tint = if (desplegado) ColorAcento else ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = perfilActual.titulo,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    )
                    Text(
                        text = perfilActual.resumen,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        maxLines = 1
                    )
                }
                Icon(
                    imageVector = if (desplegado) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Desplegar perfiles",
                    tint = TextoSecundario,
                    modifier = Modifier.size(18.dp)
                )
            }

            MenuDesplegableBoveda(
                expanded = desplegado,
                onDismissRequest = { desplegado = false }
            ) {
                PerfilArgon2.entries.forEachIndexed { index, perfil ->
                    if (index > 0) {
                        SeparadorOpcionMenu()
                    }
                    val seleccionado = perfil == perfilActual
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = if (perfil == PerfilArgon2.ULTRASEGURO) Icons.Filled.Shield else Icons.Filled.Memory,
                                contentDescription = null,
                                tint = if (seleccionado) ColorAcento else TextoSecundario,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        text = {
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(
                                    text = perfil.titulo,
                                    color = if (seleccionado) ColorTitulos else TextoPrincipal,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = perfil.detalle,
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall,
                                    lineHeight = 16.sp
                                )
                            }
                        },
                        trailingIcon = {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = ColorAcento,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        onClick = {
                            desplegado = false
                            if (perfil != perfilActual) {
                                alSeleccionarPerfil(perfil)
                            }
                        }
                    )
                }
            }
        }
    }
}
