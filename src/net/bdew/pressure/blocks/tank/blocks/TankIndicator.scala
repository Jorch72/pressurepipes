/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks.tank.blocks

import net.bdew.lib.PimpVanilla._
import net.bdew.lib.multiblock.interact.CITankInfo
import net.bdew.lib.multiblock.tile.{TileController, TileModule}
import net.bdew.lib.property.EnumerationProperty
import net.bdew.pressure.blocks.tank.{BaseModule, ModuleNeedsRenderUpdate}
import net.minecraft.block.state.IBlockState
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.IBlockAccess

import scala.collection.mutable

object BlockTankIndicator extends BaseModule("TankIndicator", "TankBlock", classOf[TileTankIndicator]) with ModuleNeedsRenderUpdate {

  object Position extends Enumeration {
    val TOP, BOTTOM, MIDDLE, ALONE = Value
    val faces = Set(EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH)
    val properties = faces.map(f => f -> EnumerationProperty.create(this, f.getName)).toMap
  }

  override def getProperties = super.getProperties ++ Position.properties.values

  override def getActualState(state: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = {
    super.getActualState(state, worldIn, pos).withProperties(
      Position.faces map { face =>
        val (below, above) = getPositionInColumn(worldIn, pos, face)
        Position.properties(face) -> ((below > 0, above > 0) match {
          case (true, false) => Position.TOP
          case (true, true) => Position.MIDDLE
          case (false, true) => Position.BOTTOM
          case (false, false) => Position.ALONE
        })
      })
  }

  override def getMetaFromState(state: IBlockState) = 0

  private def scanColumn(r: Seq[BlockPos], core: TileController, world: IBlockAccess, face: EnumFacing) = {
    r.view.map { p =>
      world.getTileSafe[TileTankIndicator](p)
    } prefixLength { tileOpt =>
      tileOpt exists { tile =>
        tile.getCore.contains(core) && shouldSideBeRendered(world, tile.getPos.offset(face), face)
      }
    }
  }

  def getPositionInColumn(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = {
    val te = getTE(world, pos)
    val result = te.getCore map { core =>
      val below = scanColumn(pos.down() to pos.copy(y = 1), core, world, face)
      val above = scanColumn(pos.up() to pos.copy(y = 255), core, world, face)
      (below, above)
    } getOrElse ((0, 0))
    te.cachedPosition(face) = result
    result
  }
}

class TileTankIndicator extends TileModule {
  val kind: String = "TankBlock"

  override def getCore = getCoreAs[CITankInfo]

  // Used to cache position info for rendering
  var cachedPosition = mutable.Map.empty[EnumFacing, (Int, Int)]
}