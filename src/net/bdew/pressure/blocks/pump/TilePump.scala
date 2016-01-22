/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks.pump

import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.TileDataSlots
import net.bdew.pressure.blocks.TileFilterable
import net.bdew.pressure.misc.FakeTank
import net.minecraft.block.Block
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, IFluidHandler}

class TilePump extends TileDataSlots with FakeTank with TileFilterable {

  override def shouldRefresh(oldBlock: Block, newBlock: Block, oldMeta: Int, newMeta: Int, world: World, x: Int, y: Int, z: Int) =
    oldBlock != newBlock

  def getFacing = BlockPump.getFacing(worldObj, xCoord, yCoord, zCoord)

  lazy val me = BlockRef.fromTile(this)

  serverTick.listen(doPushFluid)

  def doPushFluid() {
    if ((me.meta(worldObj) & 8) == 0) return
    val face = getFacing
    for (from <- me.neighbour(face.getOpposite).getTile[IFluidHandler](worldObj);
         to <- me.neighbour(face).getTile[IFluidHandler](worldObj)) {
      val res = from.drain(face, Int.MaxValue, false)
      if (res != null && res.getFluid != null && res.amount > 0 && isFluidAllowed(res)) {
        val filled = to.fill(face.getOpposite, res, true)
        if (filled > 0)
          from.drain(face, filled, true)
      }
    }
  }

  override def canFill(from: ForgeDirection, fluid: Fluid) = from == getFacing.getOpposite && isFluidAllowed(fluid)
  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean) =
    if (resource != null && canFill(from, resource.getFluid))
      me.neighbour(getFacing).getTile[IFluidHandler](worldObj) map { target =>
        target.fill(from, resource, doFill)
      } getOrElse 0
    else 0

  override def isValidDirectionForFakeTank(dir: ForgeDirection) = dir == getFacing || dir.getOpposite == getFacing
}
