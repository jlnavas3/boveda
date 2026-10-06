package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoTitulosSitio
import com.jlnavas3.bovedalocal.data.ModoFormatoTitulos
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.IconosMarcas
import com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios

@Composable
fun TarjetaGrupoTitulos(
    grupo: GrupoTitulosSitio,
    modo: ModoFormatoTitulos,
    alCambiarNombre: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    val infoMarca = remember(grupo.dominioClave) {
        IconosMarcas.buscarInfo(grupo.dominioClave, grupo.nombreEfectivo)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Superficie)
            .border(1.dp, ColorAcento.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        // Cabecera del grupo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icono de marca o fallback
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SuperficieAlta)
            ) {
                if (infoMarca != null) {
                    Icon(
                        painter = painterResource(infoMarca.drawableRes),
                        contentDescription = grupo.nombreEfectivo,
                        tint = if (infoMarca.esMulticolor) androidx.compose.ui.graphics.Color.Unspecified else (infoMarca.colorOficial ?: ColorAcento),
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "Sitio",
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = grupo.dominioClave,
                    color = TextoPrincipal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${grupo.entradas.size} ${if (grupo.entradas.size == 1) "cuenta" else "cuentas"}",
                    color = TextoSecundario,
                    fontSize = 12.sp
                )
            }

            // Botón desplegar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { expandido = !expandido }
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Plegar" else "Desplegar",
                    tint = TextoSecundario,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo para editar el nombre del servicio para todo el grupo
        CampoBoveda(
            valor = grupo.nombrePersonalizado,
            alCambiar = alCambiarNombre,
            etiqueta = "Nombre del servicio",
            modifier = Modifier.fillMaxWidth()
        )

        // Detalle de cuentas expandible
        AnimatedVisibility(visible = expandido) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Previsualización de cuentas:",
                    color = TextoSecundario,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                grupo.entradas.forEach { entrada ->
                    val tituloNuevo = NormalizadorTitulosSitios.generarTituloFinal(
                        nombreBase = grupo.nombreEfectivo,
                        usuario = entrada.usuario,
                        modo = modo,
                        tieneColision = grupo.tieneColision
                    )
                    FilaPreviaCuentaNormalizada(
                        usuario = entrada.usuario,
                        tituloActual = entrada.titulo,
                        tituloNuevo = tituloNuevo,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaTarjetaGrupoTitulos() {
    BovedaTheme {
        TarjetaGrupoTitulos(
            grupo = GrupoTitulosSitio(
                dominioClave = "account.lenovo.com",
                nombreSugerido = "Lenovo",
                entradas = listOf(
                    Entrada(id = "1", titulo = "account.lenovo.com", usuario = "jolunavi"),
                    Entrada(id = "2", titulo = "account.lenovo.com", usuario = "jlnavas3")
                )
            ),
            modo = ModoFormatoTitulos.EXPLICITO_PARENTESIS,
            alCambiarNombre = {}
        )
    }
}
