package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.common.util.INBTSerializable;

public interface HalterColorCapabilityInterface extends INBTSerializable<CompoundTag> {
    DyeColor getHalterColor();
    void setHalterColor(DyeColor color);
}