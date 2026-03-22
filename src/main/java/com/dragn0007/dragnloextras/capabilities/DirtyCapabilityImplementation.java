package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;

public class DirtyCapabilityImplementation implements DirtyCapabilityInterface {

    private static final String NBT_KEY_DIRTY = "dirty";

    private Boolean dirty = false;

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putBoolean(NBT_KEY_DIRTY, this.dirty);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.dirty = nbt.getBoolean(NBT_KEY_DIRTY);
    }
}