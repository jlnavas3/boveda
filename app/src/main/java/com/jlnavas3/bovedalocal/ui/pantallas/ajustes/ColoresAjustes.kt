package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaEnVivo

val ColorAjustesFondo: Color get() = paletaSobriaEnVivo?.fondo ?: if (esOscuroActivo) Color(0xFF101012) else Color(0xFFF5F6F9)
val ColorTarjetaAjustes: Color get() = paletaSobriaEnVivo?.tarjeta ?: if (esOscuroActivo) Color(0xFF1A1A1E) else Color(0xFFFFFFFF)
val ColorTextoAjustes: Color get() = paletaSobriaEnVivo?.textoPrincipal ?: if (esOscuroActivo) Color(0xFFF3F3F6) else Color(0xFF141519)
val ColorAjusteGris: Color get() = paletaSobriaEnVivo?.textoSecundario ?: if (esOscuroActivo) Color(0xFF9A9AA4) else Color(0xFF5E636E)
val ColorSeparadorAjustes: Color get() = paletaSobriaEnVivo?.borde ?: if (esOscuroActivo) Color(0xFF33333D) else Color(0xFFD9DCE3)

