package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaEnVivo

val ColorAjustesFondo: Color get() = paletaSobriaEnVivo?.fondo ?: if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.fondo else PaletaSobriaDefaults.CLARA.fondo
val ColorTarjetaAjustes: Color get() = paletaSobriaEnVivo?.tarjeta ?: if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.tarjeta else PaletaSobriaDefaults.CLARA.tarjeta
val ColorTextoAjustes: Color get() = paletaSobriaEnVivo?.textoPrincipal ?: if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.textoPrincipal else PaletaSobriaDefaults.CLARA.textoPrincipal
val ColorAjusteGris: Color get() = paletaSobriaEnVivo?.textoSecundario ?: if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.textoSecundario else PaletaSobriaDefaults.CLARA.textoSecundario
val ColorSeparadorAjustes: Color get() = paletaSobriaEnVivo?.borde ?: if (esOscuroActivo) PaletaSobriaDefaults.OSCURA.borde else PaletaSobriaDefaults.CLARA.borde


