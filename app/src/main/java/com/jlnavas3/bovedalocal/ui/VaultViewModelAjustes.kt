package com.jlnavas3.bovedalocal.ui

import android.app.Application
import com.jlnavas3.bovedalocal.data.VaultRepository

/**
 * Contrato compuesto que orquesta todos los sub-delegados temáticos de ajustes de la bóveda:
 * - Seguridad y bloqueo ([VaultAjustesSeguridadDelegate])
 * - Colores y temas ([VaultAjustesColoresTemaDelegate])
 * - Formas y tipografía ([VaultAjustesFormasTipografiaDelegate])
 * - Animación, engranajes y puerta ([VaultAjustesAnimacionEngranajesDelegate])
 * - Organización, listas e índices ([VaultAjustesOrganizacionDelegate])
 * - Copias de seguridad automáticas ([VaultAjustesCopiasDelegate])
 * - Interacción, háptica y launcher ([VaultAjustesInteraccionDelegate])
 * - Tarjeta rápida Tile y TOTP manual ([VaultAjustesTileTotpManualDelegate])
 * - Widget TOTP ([VaultAjustesWidgetTotpDelegate])
 * - Widget 1x1 ([VaultAjustesWidget1x1Delegate])
 */
interface VaultAjustesDelegate :
    VaultAjustesSeguridadDelegate,
    VaultAjustesColoresTemaDelegate,
    VaultAjustesFormasTipografiaDelegate,
    VaultAjustesAnimacionEngranajesDelegate,
    VaultAjustesOrganizacionDelegate,
    VaultAjustesCopiasDelegate,
    VaultAjustesInteraccionDelegate,
    VaultAjustesTileTotpManualDelegate,
    VaultAjustesWidgetTotpDelegate,
    VaultAjustesWidget1x1Delegate {

    override val repositorio: VaultRepository
    override fun obtenerApp(): Application
}
