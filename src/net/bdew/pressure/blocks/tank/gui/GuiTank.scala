/*
 * Copyright (c) bdew, 2013 - 2017
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.blocks.tank.gui

import net.bdew.lib.Misc
import net.bdew.lib.gui._
import net.bdew.lib.gui.widgets._
import net.bdew.pressure.blocks.tank.controller.TileTankController
import net.bdew.pressure.{Pressure, Textures}
import net.minecraft.entity.player.EntityPlayer

class GuiTank(val te: TileTankController, player: EntityPlayer) extends BaseScreen(new ContainerTank(te, player), 176, 166) {
  val background = Texture(Pressure.modId, "textures/gui/tank.png", rect)
  override def initGui() {
    super.initGui()
    widgets.add(new WidgetFluidGauge(new Rect(8, 19, 16, 58), Textures.tankOverlay, te.tank))
    widgets.add(new WidgetLabel(Misc.toLocal("pressure.gui.tank.title"), 8, 6, Color.darkGray))

    widgets.add(new WidgetFilterIcon(Point(149, 19), te))

    for (output <- 0 until 6)
      widgets.add(new TankOutputWidget(te, Point(44 + 21 * output, 43), output))
  }

  protected override def drawScreen(x: Int, y: Int, f: Float) = {
    super.drawScreen(x, y, f)
  }

}