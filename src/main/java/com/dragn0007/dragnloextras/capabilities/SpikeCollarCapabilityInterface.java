package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface SpikeCollarCapabilityInterface extends INBTSerializable<CompoundTag> {
    boolean hasSpikeCollar();
    void setSpikeCollared(boolean spikeCollar);
}