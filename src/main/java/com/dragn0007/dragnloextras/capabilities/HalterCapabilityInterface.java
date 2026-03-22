package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface HalterCapabilityInterface extends INBTSerializable<CompoundTag> {
    boolean isHalter();
    void setHalter(boolean halter);
}