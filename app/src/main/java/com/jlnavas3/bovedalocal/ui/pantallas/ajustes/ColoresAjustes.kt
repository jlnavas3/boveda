package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

val ColorAjustesFondo: Color get() = if (esOscuroActivo) Color(0xFF000000) else Color(0xFFF2F2F7)
val ColorTarjetaAjustes: Color get() = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF)
val ColorTextoAjustes: Color get() = if (esOscuroActivo) Color(0xFFFFFFFF) else Color(0xFF15171F)
val ColorAjusteGris: Color get() = if (esOscuroActivo) Color(0xFF99999B) else Color(0xFF8E8E93)
val ColorSeparadorAjustes: Color get() = if (esOscuroActivo) Color(0xFF333238) else Color(0xFFE5E5EA)
