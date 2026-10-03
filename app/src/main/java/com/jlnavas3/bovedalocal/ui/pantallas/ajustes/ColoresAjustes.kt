package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura

val ColorAjustesFondo: Color get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura?.fondo ?: PaletaSobriaDefaults.OSCURA.fondo) else (paletaSobriaGuardadaClara?.fondo ?: PaletaSobriaDefaults.CLARA.fondo)
val ColorTarjetaAjustes: Color get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura?.tarjeta ?: PaletaSobriaDefaults.OSCURA.tarjeta) else (paletaSobriaGuardadaClara?.tarjeta ?: PaletaSobriaDefaults.CLARA.tarjeta)
val ColorTextoAjustes: Color get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura?.textoPrincipal ?: PaletaSobriaDefaults.OSCURA.textoPrincipal) else (paletaSobriaGuardadaClara?.textoPrincipal ?: PaletaSobriaDefaults.CLARA.textoPrincipal)
val ColorAjusteGris: Color get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura?.textoSecundario ?: PaletaSobriaDefaults.OSCURA.textoSecundario) else (paletaSobriaGuardadaClara?.textoSecundario ?: PaletaSobriaDefaults.CLARA.textoSecundario)
val ColorSeparadorAjustes: Color get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura?.borde ?: PaletaSobriaDefaults.OSCURA.borde) else (paletaSobriaGuardadaClara?.borde ?: PaletaSobriaDefaults.CLARA.borde)
val ColorCampoAjustes: Color get() = if (esOscuroActivo) (paletaSobriaGuardadaOscura?.campo ?: PaletaSobriaDefaults.OSCURA.campo) else (paletaSobriaGuardadaClara?.campo ?: PaletaSobriaDefaults.CLARA.campo)



