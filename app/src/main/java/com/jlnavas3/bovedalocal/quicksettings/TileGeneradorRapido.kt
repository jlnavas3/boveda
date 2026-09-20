package com.jlnavas3.bovedalocal.quicksettings

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class TileGeneradorRapido : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        val tile = qsTile ?: return
        tile.state = Tile.STATE_ACTIVE
        tile.label = "Generador Rápido"
        tile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        GeneradorRapidoHelper.generar(applicationContext, "Quick Settings Tile")
    }
}


