package com.dragn0007.dragnloextras.capabilities;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IDirtyCapability {
    boolean livestockOverhaulScraps$isDirty();
    void livestockOverhaulScraps$setDirty(boolean value);
}
