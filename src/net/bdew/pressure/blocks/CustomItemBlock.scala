/*
 * Copyright (c) bdew, 2013 - 2014
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks

import net.bdew.pressure.misc.PressureCreativeTabs
import net.bdew.pressure.pressurenet.Helper
import net.minecraft.block.Block
import net.minecraft.entity.player.{EntityPlayer, EntityPlayerMP}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraft.world.World

class CustomItemBlock(bl: Block) extends ItemBlock(bl) {
  setCreativeTab(PressureCreativeTabs.main)

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, pos: BlockPos, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (!world.isRemote && player.isInstanceOf[EntityPlayerMP]) {
      val p = player.asInstanceOf[EntityPlayerMP]
      if (Helper.tryPlaceBlock(world, pos, bl, p) || Helper.tryPlaceBlock(world, pos.offset(side), bl, p)) {
        if (!p.capabilities.isCreativeMode)
          player.inventory.decrStackSize(player.inventory.currentItem, 1)
        true
      } else false
    } else true
  }
}
