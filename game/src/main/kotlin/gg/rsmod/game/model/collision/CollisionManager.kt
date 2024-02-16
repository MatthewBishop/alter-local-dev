package gg.rsmod.game.model.collision

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.region.ChunkSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CollisionManager(val chunks: ChunkSet, val createChunksIfNeeded: Boolean = true) {

    companion object {
        const val BLOCKED_TILE = 0x1
        const val BRIDGE_TILE = 0x2
        const val ROOF_TILE = 0x4
    }
}
