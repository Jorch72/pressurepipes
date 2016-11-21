/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.misc

import net.bdew.lib.capabilities.helpers.FluidHelper
import net.bdew.lib.items.ItemUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.{IFluidHandler, IFluidTankProperties}

class UnstackingFluidHandler(baseHandler: IFluidHandler, stack: ItemStack, player: EntityPlayer, slot: Int) extends IFluidHandler {
  override def getTankProperties: Array[IFluidTankProperties] = baseHandler.getTankProperties
  override def drain(resource: FluidStack, doDrain: Boolean): FluidStack = null
  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = null
  override def fill(resource: FluidStack, doFill: Boolean): Int = {
    val res = baseHandler.fill(resource, doFill)
    if (res > 0 && doFill) {
      player.inventory.decrStackSize(slot, 1)
      ItemUtils.dropItemToPlayer(player.getEntityWorld, player, stack)
    }
    res
  }
}

object UnstackingFluidHandler {
  def getIfNeeded(player: EntityPlayer, slot: Int): Option[IFluidHandler] = {
    val mainStack = player.inventory.getStackInSlot(slot)
    if (!mainStack.isEmpty) {
      if (mainStack.getCount > 1) {
        val singleStack = mainStack.copy()
        singleStack.setCount(1)
        FluidHelper.getFluidHandler(singleStack) map { handler => new UnstackingFluidHandler(handler, singleStack, player, slot) }
      } else if (mainStack.getCount == 1) {
        FluidHelper.getFluidHandler(mainStack)
      } else None
    } else None
  }
}