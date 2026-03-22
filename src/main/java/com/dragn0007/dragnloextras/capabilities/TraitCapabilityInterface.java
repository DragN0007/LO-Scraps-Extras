package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface TraitCapabilityInterface extends INBTSerializable<CompoundTag> {
    int getTrait();
    void setTrait(int value);
}