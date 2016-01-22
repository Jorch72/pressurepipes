/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.api;

import net.minecraft.world.World;

public interface IFilterableProvider {
    /**
     * Provide IFilterable for something in the world
     *
     * @return IFilterable instance or null if not applicable
     */
    IFilterable getFilterableForWorldCoordinates(World world, int x, int y, int z, int side);
}
