package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ImmunityCapabilityInterface extends INBTSerializable<CompoundTag> {
    int getImmunity();
    void setImmunity(int value);
}