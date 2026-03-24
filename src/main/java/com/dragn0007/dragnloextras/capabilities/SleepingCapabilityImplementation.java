package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;

public class SleepingCapabilityImplementation implements SleepingCapabilityInterface {

    private static final String NBT_KEY_SLEEPING = "sleeping";

    private Boolean sleeping = false;

    @Override
    public boolean isSleeping() {
        return this.sleeping;
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean(NBT_KEY_SLEEPING, this.sleeping);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.sleeping = nbt.getBoolean(NBT_KEY_SLEEPING);
    }
}