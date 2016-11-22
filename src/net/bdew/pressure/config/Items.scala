/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.config

import net.bdew.lib.config.ItemManager
import net.bdew.pressure.items.configurator.ItemConfigurator
import net.bdew.pressure.items.{Canister, HandPump, ItemDebugger}
import net.bdew.pressure.misc.PressureCreativeTabs

object Items extends ItemManager(PressureCreativeTabs.main) {
  regItem(ItemDebugger)
  val interface = regSimpleItem("interface")

  val tankWall = regSimpleItem("tank_wall")
  val fluidInterface = regSimpleItem("fluid_interface")

  regItem(HandPump)
  regItem(Canister).setCreativeTab(PressureCreativeTabs.canisters)
  regItem(ItemConfigurator)
}