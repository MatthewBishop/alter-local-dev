package org.rsmod.game.pathfinder.collision

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.collision.CollisionFlag.Companion.pawnFlags
import gg.rsmod.game.model.collision.CollisionFlag.Companion.projectileFlags
import gg.rsmod.game.model.collision.CollisionUpdate
import kotlin.experimental.or

/**
 * Interop between existing RSMod1 collision logic and RSMod2 [CollisionFlagMap] logic.
 */
fun CollisionFlagMap.applyUpdate(update: CollisionUpdate) {
    val map = update.flags

    for (entry in map.entries) {
        val tile = entry.key

        val pawns = pawnFlags()
        val projectiles = projectileFlags()

        for (flag in entry.value) {
            val direction = flag.direction
            if (direction == Direction.NONE) {
                continue
            }

            val orientation = direction.orientationValue
            add(
                absoluteX = tile.x,
                absoluteZ = tile.z,
                level = tile.height,
                mask = if (flag.impenetrable) {
                    projectiles[orientation].getBitAsShort() or pawns[orientation].getBitAsShort()
                } else {
                    pawns[orientation].getBitAsShort()
                }.toInt(),
            )
        }
    }
}