package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaEnVivo

import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura

val ColorAjustesFondo: Color get() = paletaSobriaEnVivo?.fondo ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.fondo ?: PaletaSobriaDefaults.OSCURA.fondo) else (paletaSobriaGuardadaClara?.fondo ?: PaletaSobriaDefaults.CLARA.fondo)
val ColorTarjetaAjustes: Color get() = paletaSobriaEnVivo?.tarjeta ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.tarjeta ?: PaletaSobriaDefaults.OSCURA.tarjeta) else (paletaSobriaGuardadaClara?.tarjeta ?: PaletaSobriaDefaults.CLARA.tarjeta)
val ColorTextoAjustes: Color get() = paletaSobriaEnVivo?.textoPrincipal ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.textoPrincipal ?: PaletaSobriaDefaults.OSCURA.textoPrincipal) else (paletaSobriaGuardadaClara?.textoPrincipal ?: PaletaSobriaDefaults.CLARA.textoPrincipal)
val ColorAjusteGris: Color get() = paletaSobriaEnVivo?.textoSecundario ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.textoSecundario ?: PaletaSobriaDefaults.OSCURA.textoSecundario) else (paletaSobriaGuardadaClara?.textoSecundario ?: PaletaSobriaDefaults.CLARA.textoSecundario)
val ColorSeparadorAjustes: Color get() = paletaSobriaEnVivo?.borde ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.borde ?: PaletaSobriaDefaults.OSCURA.borde) else (paletaSobriaGuardadaClara?.borde ?: PaletaSobriaDefaults.CLARA.borde)
val ColorCampoAjustes: Color get() = paletaSobriaEnVivo?.campo ?: if (esOscuroActivo) (paletaSobriaGuardadaOscura?.campo ?: PaletaSobriaDefaults.OSCURA.campo) else (paletaSobriaGuardadaClara?.campo ?: PaletaSobriaDefaults.CLARA.campo)



