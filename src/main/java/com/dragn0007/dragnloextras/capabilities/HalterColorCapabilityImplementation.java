package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;

public class HalterColorCapabilityImplementation implements HalterColorCapabilityInterface {

    private static final String NBT_KEY_HALTERCOLOR = "halterColor";

    private DyeColor halterColor = DyeColor.WHITE;

    @Override
    public DyeColor getHalterColor() {
        return this.halterColor;
    }

    @Override
    public void setHalterColor(DyeColor color) {
        this.halterColor = color;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_HALTERCOLOR, this.halterColor.getId());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains(NBT_KEY_HALTERCOLOR)) {
            this.halterColor = DyeColor.byId(nbt.getInt(NBT_KEY_HALTERCOLOR));
        }
    }
}