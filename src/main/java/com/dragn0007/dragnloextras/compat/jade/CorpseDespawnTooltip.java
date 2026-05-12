package com.dragn0007.dragnloextras.compat.jade;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.entity.butchering.Corpse;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class CorpseDespawnTooltip implements IEntityComponentProvider {

    public CorpseDespawnTooltip() {
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getEntity() instanceof Corpse corpse) {
            int seconds = corpse.decomposeTimer / 20;
            int finalSeconds = (ScrapsExtrasCommonConfig.CORPSE_DECOMP_TIMER.get() / 20) - seconds;
            tooltip.add(Component.translatable("Decomposes in: " + finalSeconds + "s"));
            tooltip.add(Component.translatable("Harvestable with an axe. (Shift + Right-Click)"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(LivestockOverhaul.MODID, "o_tooltips");
    }
}
