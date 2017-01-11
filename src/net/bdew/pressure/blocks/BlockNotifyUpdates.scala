/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks

import net.bdew.pressure.pressurenet.Helper
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

trait BlockNotifyUpdates extends Block {
  def notifyPressureSystemUpdate(w: World, pos: BlockPos) =
    Helper.notifyBlockChanged(w, pos)

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState) = {
    notifyPressureSystemUpdate(world, pos)
    super.breakBlock(world, pos, state)
  }

  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack): Unit = {
    notifyPressureSystemUpdate(world, pos)
    super.onBlockPlacedBy(world, pos, state, placer, stack)
  }

  override def rotateBlock(world: World, pos: BlockPos, axis: EnumFacing) = {
    if (super.rotateBlock(world, pos, axis)) {
      notifyPressureSystemUpdate(world, pos)
      true
    } else false
  }
}
