package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.VaultCrypto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.util.DatosAuditoria

@Composable
fun GrupoCriptografiaBlindaje(
    datos: DatosAuditoria,
    modifier: Modifier = Modifier,
    mostrarIdsAjustes: Boolean = false
) {
    ComponenteGrupo(
        etiqueta = "Criptografía y blindaje",
        icono = Icons.Filled.Memory,
        colorIcono = ColorArgon2,
        idGrupo = "06.3.G2",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ItemMetrica("KDF Derivación de clave", "Argon2id · ${datos.perfilArgon2.titulo}")
            ItemMetrica("Parámetros KDF", "${datos.kdfParams.memoryKiB / 1024} MiB RAM, ${datos.kdfParams.iterations} pasadas, paralelismo ${datos.kdfParams.parallelism}")
            ItemMetrica("Cifrado autenticado", "AES-256-GCM (nonce 12B, tag 128b, AAD autenticado)")
            ItemMetrica("Generador aleatorio", "SecureRandom CSPRNG del kernel de Linux")
            ItemMetrica("Passkeys WebAuthn", "Claves asimétricas ECDSA P-256 (ES256) locales")
            ItemMetrica("Formato de cabecera", "Magic BVDA · v${VaultCrypto.VERSION} · ${VaultCrypto.TAM_CABECERA} bytes AAD")
        }
    }
}
