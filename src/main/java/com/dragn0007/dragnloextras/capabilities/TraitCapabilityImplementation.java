package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;

public class TraitCapabilityImplementation implements TraitCapabilityInterface {

    private static final String NBT_KEY_TRAIT = "trait";

    private int trait;

    @Override
    public int getTrait() {
        return this.trait;
    }

    @Override
    public void setTrait(int trait) {
        this.trait = trait;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_TRAIT, this.getTrait());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.setTrait(nbt.getInt(NBT_KEY_TRAIT));
    }
}