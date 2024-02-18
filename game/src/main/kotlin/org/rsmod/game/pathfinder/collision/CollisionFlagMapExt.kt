package org.rsmod.game.pathfinder.collision

import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.CollisionUpdate
import org.rsmod.game.pathfinder.LineValidator
import org.rsmod.game.pathfinder.StepValidator
import org.rsmod.game.pathfinder.collision.CollisionFlagMap
import org.rsmod.game.pathfinder.collision.CollisionStrategies
import org.rsmod.game.pathfinder.flag.CollisionFlag

fun CollisionFlagMap.isClipped(tile: Tile): Boolean {
    val zoneIndex = zoneIndex(tile.x, tile.z, tile.height)
    val tileIndex = tileIndex(tile.x, tile.z)
    return flags[zoneIndex]?.get(tileIndex) != 0
}

/**
 * Gets the shortest path using Bresenham's Line Algorithm from [start] to [target],
 * in tiles.
 */
fun CollisionFlagMap.raycastTiles(start: Tile, target: Tile): Int {
    check(start.height == target.height) { "Tiles must be on the same height level." }

    var x0 = start.x
    var y0 = start.z
    val x1 = target.x
    val y1 = target.z

    val dx = Math.abs(x1 - x0)
    val dy = Math.abs(y1 - y0)

    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1

    var err = dx - dy
    var err2: Int

    var tiles = 0

    while (x0 != x1 || y0 != y1) {
        err2 = err shl 1

        if (err2 > -dy) {
            err -= dy
            x0 += sx
        }

        if (err2 < dx) {
            err += dx
            y0 += sy
        }
        tiles++
    }

    return tiles
}

/**
 * Casts a line using Bresenham's Line Algorithm with point A [start] and
 * point B [target] being its two points and makes sure that there's no
 * collision flag that can block movement from and to both points.
 *
 * @param projectile
 * Projectiles have a higher tolerance for certain objects when the object's
 * metadata explicitly allows them to.
 */
fun CollisionFlagMap.raycast(start: Tile, target: Tile, projectile: Boolean): Boolean {
    check(start.height == target.height) { "Tiles must be on the same height level." }

    var x0 = start.x
    var y0 = start.z
    val x1 = target.x
    val y1 = target.z
    val height = start.height
    val validator = LineValidator(this);
    return if(projectile)
        validator.hasLineOfSight(height, x0, y0, x1, y1);
    else
        validator.hasLineOfWalk(height, x0, y0, x1, y1);
}

fun CollisionFlagMap.canTraverse(
    source: Tile,
    direction: Direction,
    srcSize: Int = 1,
): Boolean {
    val stepValidator = StepValidator(this)
    return stepValidator.canTravel(
        level = source.height,
        x = source.x,
        z = source.z,
        offsetX = direction.getDeltaX(),
        offsetZ = direction.getDeltaZ(),
        size = srcSize,
        collision = CollisionStrategies.Normal
    )
}

private val pawnFlags = arrayOf(
    CollisionFlag.WALL_NORTH_WEST,
    CollisionFlag.WALL_NORTH,
    CollisionFlag.WALL_NORTH_EAST,
    CollisionFlag.WALL_WEST,
    CollisionFlag.WALL_EAST,
    CollisionFlag.WALL_SOUTH_WEST,
    CollisionFlag.WALL_SOUTH,
    CollisionFlag.WALL_SOUTH_EAST
)

private val projectileFlags = arrayOf(
    CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER,
    CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER
)

public fun pawnFlags() = pawnFlags

public fun projectileFlags() = projectileFlags

private fun tileIndex(x: Int, z: Int): Int = (x and 0x7) or ((z and 0x7) shl 3)

private fun zoneIndex(x: Int, z: Int, level: Int): Int = ((x shr 3) and 0x7FF) or
        (((z shr 3) and 0x7FF) shl 11) or ((level and 0x3) shl 22)