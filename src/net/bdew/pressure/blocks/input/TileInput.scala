/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks.input

import net.bdew.lib.block.BlockRef
import net.bdew.lib.data.base.TileDataSlots
import net.bdew.pressure.api._
import net.bdew.pressure.blocks.TileFilterable
import net.bdew.pressure.misc.FakeTank
import net.bdew.pressure.pressurenet.Helper
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, IFluidHandler}

class TileInput extends TileDataSlots with FakeTank with IPressureInject with TileFilterable {
  def getFacing = BlockInput.getFacing(worldObj, xCoord, yCoord, zCoord)
  lazy val me = BlockRef.fromTile(this)
  var connection: IPressureConnection = null

  override def canFill(from: ForgeDirection, fluid: Fluid) = from == getFacing.getOpposite && isFluidAllowed(fluid)

  override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean): Int = {
    if (worldObj.isRemote) {
      if (resource != null && resource.getFluid != null && resource.amount > 0 && canFill(from, resource.getFluid))
        return resource.amount
    } else if (resource != null && resource.getFluid != null && resource.amount > 0 && canFill(from, resource.getFluid)) {
      if (connection == null && Helper.canPipeConnectTo(worldObj, me.neighbour(getFacing), getFacing.getOpposite))
        connection = Helper.recalculateConnectionInfo(this, getFacing)
      if (connection != null)
        return connection.pushFluid(resource, doFill)
    }
    return 0
  }

  serverTick.listen(doPushFluid)

  def doPushFluid() {
    if ((me.meta(worldObj) & 8) == 0) return
    val face = getFacing
    me.neighbour(face.getOpposite).getTile[IFluidHandler](worldObj).foreach { from =>
      val res = from.drain(face, Int.MaxValue, false)
      if (res != null && res.getFluid != null && res.amount > 0 && isFluidAllowed(res)) {
        val filled = fill(face.getOpposite, res, true)
        if (filled > 0)
          from.drain(face, filled, true)
      }
    }
  }

  override def invalidateConnection(direction: ForgeDirection) = connection = null

  override def getZCoord = zCoord
  override def getYCoord = yCoord
  override def getXCoord = xCoord
  override def getWorld = worldObj

  override def isValidDirectionForFakeTank(dir: ForgeDirection) = dir == getFacing.getOpposite
}
