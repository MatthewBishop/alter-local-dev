package gg.rsmod.game.model.collision

import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.GameObject
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList

class CollisionUpdate private constructor(val type: Type, val flags: Object2ObjectOpenHashMap<Tile, ObjectList<DirectionFlag>>) {

    enum class Type {
        ADD,
        REMOVE
    }

    class Builder {

        private val flags = Object2ObjectOpenHashMap<Tile, ObjectList<DirectionFlag>>()

        private var type: Type? = null

        fun build(): CollisionUpdate {
            check(type != null) { "Type has not been set." }
            return CollisionUpdate(type!!, flags)
        }

        fun setType(type: Type) {
            check(this.type == null) { "Type has already been set." }
            this.type = type
        }

        fun putTile(tile: Tile, impenetrable: Boolean, vararg directions: Direction) {
            check(directions.isNotEmpty()) { "Directions must not be empty." }
            val flags = flags[tile] ?: ObjectArrayList<DirectionFlag>()
            directions.forEach { dir -> flags.add(DirectionFlag(dir, impenetrable)) }
            this.flags[tile] = flags
        }

        fun putTile(tile: Tile, vararg directions: Direction) {
            check(directions.isNotEmpty()) { "Directions must not be empty." }
            val flags = flags[tile] ?: ObjectArrayList<DirectionFlag>()
            directions.forEach { dir -> flags.add(DirectionFlag(dir, false)) }
            this.flags[tile] = flags
        }

        private fun putWall(tile: Tile, impenetrable: Boolean, orientation: Direction) {
            putTile(tile, impenetrable, orientation)
            putTile(tile.step(orientation), impenetrable, orientation.getOpposite())
        }

        private fun putLargeCornerWall(tile: Tile, impenetrable: Boolean, orientation: Direction) {
            val directions = orientation.getDiagonalComponents()
            putTile(tile, impenetrable, *directions)

            directions.forEach { dir ->
                putTile(tile.step(dir), impenetrable, dir.getOpposite())
            }
        }

        fun putObject(definitions: DefinitionSet, obj: GameObject) {
            val def = definitions.get(ObjectDef::class.java, obj.id)
            val type = obj.type
            val tile = obj.tile

            if (!unwalkable(def, type)) {
                return
            }

            val x = tile.x
            val z = tile.z
            val height = tile.height
            var width = def.width
            var length = def.length
            val impenetrable = def.impenetrable
            val orientation = obj.rot

            if (orientation == 1 || orientation == 3) {
                width = def.length
                length = def.width
            }

            if (type == 22) {
                if (def.interactive && def.solid) {
                    putTile(Tile(x, z, height), impenetrable, *Direction.NESW)
                }
            } else if (type >= 9 && type < 22) {
                for (dx in 0 until width) {
                    for (dz in 0 until length) {
                        putTile(Tile(x + dx, z + dz, height), impenetrable, *Direction.NESW)
                    }
                }
            } else if (type == 0) {
                putWall(tile, impenetrable, Direction.WNES[orientation])
            } else if (type == 1 || type == 3) {
                putWall(tile, impenetrable, Direction.WNES_DIAGONAL[orientation])
            } else if (type == 2) {
                putLargeCornerWall(tile, impenetrable, Direction.WNES_DIAGONAL[orientation])
            }
        }

        private fun unwalkable(def: ObjectDef, type: Int): Boolean {
            val isSolidFloorDecoration = type == 22 && def.interactive
            val isRoof = type > 11 && type < 22 && def.solid
            val isWall = (type >= 0 && type <= 3 || type == 9) && def.solid
            val isSolidInteractable = (type == 11 || type == 10) && def.solid

            return isWall || isRoof || isSolidInteractable || isSolidFloorDecoration
        }
    }
}