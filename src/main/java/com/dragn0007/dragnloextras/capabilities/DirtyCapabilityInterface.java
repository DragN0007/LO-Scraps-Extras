package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface DirtyCapabilityInterface extends INBTSerializable<CompoundTag> {

    boolean isDirty();

    void setDirty(boolean dirty);
}