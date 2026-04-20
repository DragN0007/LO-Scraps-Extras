package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;

public class SpikeCollarCapabilityImplementation implements SpikeCollarCapabilityInterface {

    private static final String NBT_KEY_SPIKE_COLLAR = "spike_collar";

    private Boolean spikeCollar = false;

    @Override
    public boolean hasSpikeCollar() {
        return this.spikeCollar;
    }

    @Override
    public void setSpikeCollared(boolean spikeCollar) {
        this.spikeCollar = spikeCollar;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean(NBT_KEY_SPIKE_COLLAR, this.spikeCollar);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.spikeCollar = nbt.getBoolean(NBT_KEY_SPIKE_COLLAR);
    }
}