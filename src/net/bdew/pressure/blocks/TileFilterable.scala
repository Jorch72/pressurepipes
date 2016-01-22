/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks

import net.bdew.lib.data.DataSlotString
import net.bdew.lib.data.base.{TileDataSlots, UpdateKind}
import net.bdew.pressure.api.IFilterable
import net.minecraftforge.fluids.{Fluid, FluidRegistry, FluidStack}

trait TileFilterable extends TileDataSlots with IFilterable {
  val fluidFilter = new DataSlotString("fluidFilter", this).setUpdate(UpdateKind.SAVE, UpdateKind.WORLD, UpdateKind.RENDER)

  def isFluidAllowed(fluid: Fluid): Boolean =
    fluid != null && ((fluidFilter :== null) || fluid.getName.equals(fluidFilter.value))

  def isFluidAllowed(fs: FluidStack): Boolean =
    fs != null && isFluidAllowed(fs.getFluid)

  override def setFluidFilter(fluid: Fluid) = fluidFilter := (if (fluid == null) null else fluid.getName)
  override def clearFluidFilter() = fluidFilter := null

  def getFluidFilter =
    if ((fluidFilter :!= null) && FluidRegistry.isFluidRegistered(fluidFilter))
      Option(FluidRegistry.getFluid(fluidFilter))
    else
      None
}
