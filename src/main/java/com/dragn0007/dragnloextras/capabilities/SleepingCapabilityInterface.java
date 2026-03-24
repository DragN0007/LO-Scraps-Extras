package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface SleepingCapabilityInterface extends INBTSerializable<CompoundTag> {
    boolean isSleeping();
    void setSleeping(boolean sleeping);
}