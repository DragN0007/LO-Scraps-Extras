package com.dragn0007.dragnloextras.util.impls;

import org.spongepowered.asm.mixin.Unique;

public interface IPregnancyHolder {
    boolean isPregnant();
    void setPregnant(boolean pregnant);
    int livestockOverhaulScraps$pregnantTick();
    void setPregnantTick(int pregnantTick);
}