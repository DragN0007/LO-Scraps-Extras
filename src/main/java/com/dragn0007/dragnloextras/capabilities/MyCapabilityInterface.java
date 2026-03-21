package com.dragn0007.dragnloextras.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface MyCapabilityInterface extends INBTSerializable<CompoundTag> {

    String getValue();

    void setMyValue(String myValue);
}