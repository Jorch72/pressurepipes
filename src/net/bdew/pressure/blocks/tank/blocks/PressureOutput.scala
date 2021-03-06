/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks.tank.blocks

import net.bdew.lib.multiblock.block.BlockOutput
import net.bdew.lib.multiblock.data.OutputConfigFluid
import net.bdew.lib.multiblock.interact.CIFluidOutput
import net.bdew.lib.multiblock.tile.{RSControllableOutput, TileOutput}
import net.bdew.pressure.api.{IPressureConnectableBlock, IPressureConnection, IPressureInject}
import net.bdew.pressure.blocks.BlockNotifyUpdates
import net.bdew.pressure.blocks.tank.{BaseModule, PressureModule}
import net.bdew.pressure.pressurenet.Helper
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.IBlockAccess

object BlockPressureOutput extends BaseModule("TankPressureOutput", "FluidOutput", classOf[TilePressureOutput])
with BlockOutput[TilePressureOutput] with BlockNotifyUpdates with IPressureConnectableBlock {
  override def canConnectTo(world: IBlockAccess, pos: BlockPos, side: EnumFacing) =
    getTE(world, pos).exists(_.getCore.isDefined)
  override def isTraversable(world: IBlockAccess, pos: BlockPos) = false
  override def canConnectRedstone(world: IBlockAccess, pos: BlockPos, side: EnumFacing) = true
}

class TilePressureOutput extends TileOutput[OutputConfigFluid] with PressureModule with RSControllableOutput with IPressureInject {
  val kind: String = "FluidOutput"

  override val outputConfigType = classOf[OutputConfigFluid]
  override def getCore = getCoreAs[CIFluidOutput]

  override def canConnectToFace(d: EnumFacing) =
    Helper.canPipeConnectFrom(worldObj, pos.offset(d), d.getOpposite)

  override def makeCfgObject(face: EnumFacing) = new OutputConfigFluid

  override def invalidateConnection(direction: EnumFacing) = connections -= direction

  var connections = Map.empty[EnumFacing, IPressureConnection]

  override def doOutput(face: EnumFacing, cfg: OutputConfigFluid) =
    if (checkCanOutput(cfg)) {
      getCore foreach { core =>
        if (!connections.isDefinedAt(face))
          connections ++= Option(Helper.recalculateConnectionInfo(this, face)) map { cObj => face -> cObj }

        connections.get(face) foreach { conn =>
          val fs = core.outputFluid(Int.MaxValue, false)
          val out = conn.pushFluid(fs, true)
          if (out > 0) {
            core.outputFluid(out, true)
            cfg.updateAvg(out)
            core.outputConfig.updated()
          }
        }
      }
    }

  override def pressureNodePos = getPos
  override def pressureNodeWorld = getWorld
}

