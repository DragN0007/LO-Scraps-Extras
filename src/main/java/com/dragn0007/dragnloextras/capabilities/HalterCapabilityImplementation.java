package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;

public class HalterCapabilityImplementation implements HalterCapabilityInterface {

    private static final String NBT_KEY_HALTER = "halter";

    private Boolean halter = false;

    @Override
    public boolean hasHalter() {
        return this.halter;
    }

    @Override
    public void setHaltered(boolean halter) {
        this.halter = halter;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean(NBT_KEY_HALTER, this.halter);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.halter = nbt.getBoolean(NBT_KEY_HALTER);
    }
}