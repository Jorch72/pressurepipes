/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks.valves.check

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.bdew.lib.Misc
import net.bdew.lib.block.{HasItemBlock, HasTE}
import net.bdew.pressure.Pressure
import net.bdew.pressure.blocks.CustomItemBlock
import net.bdew.pressure.blocks.valves.BlockValve
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.world.World

object BlockCheckValve extends BlockValve("CheckValve") with HasTE[TileCheckValve] with HasItemBlock {
  override val TEClass = classOf[TileCheckValve]
  override val ItemBlockClass = classOf[CustomItemBlock]

  setHardness(2)

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block) {
    val meta = world.getBlockMetadata(x, y, z)
    val powered = world.isBlockIndirectlyGettingPowered(x, y, z)
    if (powered && ((meta & 8) == 0))
      world.setBlockMetadataWithNotify(x, y, z, (meta & 7) | 8, 2)
    else if (!powered && ((meta & 8) == 8))
      world.setBlockMetadataWithNotify(x, y, z, meta & 7, 2)
  }

  override def onBlockPlacedBy(world: World, x: Int, y: Int, z: Int, ent: EntityLivingBase, stack: ItemStack): Unit = {
    super.onBlockPlacedBy(world, x, y, z, ent, stack)
    onNeighborBlockChange(world, x, y, z, this)
  }

  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(ir: IIconRegister) = {
    frontIcon = ir.registerIcon(Misc.iconName(Pressure.modId, name, "front"))
    sideIconOn = ir.registerIcon(Misc.iconName(Pressure.modId, name, "side_on"))
    sideIconOff = ir.registerIcon(Misc.iconName(Pressure.modId, name, "side_off"))
  }
}
