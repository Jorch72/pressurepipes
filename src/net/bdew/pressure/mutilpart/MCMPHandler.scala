/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.mutilpart

import java.util

import mcmultipart.multipart.{IMultipart, IPartConverter, IPartFactory, MultipartRegistry}
import net.bdew.pressure.blocks.pipe.BlockPipe
import net.bdew.pressure.pressurenet.Helper
import net.minecraft.block.Block
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

object MCMPHandler extends IPartFactory with IPartConverter {

  override def createPart(kind: ResourceLocation, client: Boolean): IMultipart = {
    if (kind.getResourceDomain == "pressure")
      kind.getResourcePath match {
        case "pipe" => new PipePart
        case _ => null
      }
    else null
  }

  override def convertBlock(world: IBlockAccess, pos: BlockPos, simulated: Boolean): util.Collection[_ <: IMultipart] = {
    if (world.getBlockState(pos).getBlock == BlockPipe)
      util.Collections.singletonList(new PipePart)
    else
      util.Collections.emptyList()
  }

  override def getConvertableBlocks: util.Collection[Block] = util.Collections.singletonList(BlockPipe)

  def init(): Unit = {
    MultipartRegistry.registerPart(classOf[PipePart], "pressure:pipe")
    MultipartRegistry.registerPartFactory(this, "pressure:pipe")
    MultipartRegistry.registerPartConverter(this)
    Helper.registerExtension(MCMPPressureExtension)
  }
}
