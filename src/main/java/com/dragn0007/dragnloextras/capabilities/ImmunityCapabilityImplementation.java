package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;

public class ImmunityCapabilityImplementation implements ImmunityCapabilityInterface {

    private static final String NBT_KEY_IMMUNITY = "immunity";

    private int immunity;

    @Override
    public int getImmunity() {
        return this.immunity;
    }

    @Override
    public void setImmunity(int immunity) {
        this.immunity = immunity;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_IMMUNITY, this.getImmunity());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setImmunity(nbt.getInt(NBT_KEY_IMMUNITY));
    }
}