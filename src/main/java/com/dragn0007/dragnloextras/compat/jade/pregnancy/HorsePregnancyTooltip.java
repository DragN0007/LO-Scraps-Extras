package com.dragn0007.dragnloextras.compat.jade.pregnancy;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import com.dragn0007.dragnloextras.util.impls.IPregnancyHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class HorsePregnancyTooltip implements IEntityComponentProvider, IPregnancyHolder {
    public HorsePregnancyTooltip() {}

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        OHorse animal = (OHorse)entityAccessor.getEntity();
        if (entityAccessor.getPlayer().isShiftKeyDown()) {
            if (((IPregnancyHolder) animal).isPregnant() && ((IPregnancyHolder) animal).livestockOverhaulScraps$pregnantTick() > 0) {
                int gestationTime = ((IPregnancyHolder) animal).livestockOverhaulScraps$pregnantTick();
                int timeUntilBirth = ScrapsExtrasCommonConfig.LARGE_GESTATION_TIME.get() - gestationTime;
                tooltip.add(Component.literal("Gestation Time Left: " + timeUntilBirth));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {return new ResourceLocation(LivestockOverhaul.MODID, "o_tooltips");}

    public boolean pregnant = false;
    public boolean isPregnant() {return this.pregnant;}
    public void setPregnant(boolean pregnant) {this.pregnant = pregnant;}

    @Override
    public int livestockOverhaulScraps$pregnantTick() {return 0;}

    @Override
    public void setPregnantTick(int pregnantTick) {}
}
