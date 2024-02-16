package org.rsmod.game.pathfinder.collision;

import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_EAST
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_EAST_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_NORTH
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_NORTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_NORTH_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_NORTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_SOUTH
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_SOUTH_EAST_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_SOUTH_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_SOUTH_WEST_PROJECTILE_BLOCKER
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_WEST
import org.rsmod.game.pathfinder.flag.CollisionFlag.WALL_WEST_PROJECTILE_BLOCKER

public class CollisionFlagExt {
private val pawnFlags = arrayOf(
        WALL_NORTH_WEST,
        WALL_NORTH,
        WALL_NORTH_EAST,
        WALL_WEST,
        WALL_EAST,
        WALL_SOUTH_WEST,
        WALL_SOUTH,
        WALL_SOUTH_EAST)

private val projectileFlags = arrayOf(
        WALL_NORTH_WEST_PROJECTILE_BLOCKER,
        WALL_NORTH_PROJECTILE_BLOCKER,
        WALL_NORTH_EAST_PROJECTILE_BLOCKER,
        WALL_WEST_PROJECTILE_BLOCKER,
        WALL_EAST_PROJECTILE_BLOCKER,
        WALL_SOUTH_WEST_PROJECTILE_BLOCKER,
        WALL_SOUTH_PROJECTILE_BLOCKER,
        WALL_SOUTH_EAST_PROJECTILE_BLOCKER)

        fun getFlags(projectiles: Boolean): Array<Int> = if (projectiles) projectileFlags() else pawnFlags()

        fun pawnFlags() = pawnFlags

        fun projectileFlags() = projectileFlags

}