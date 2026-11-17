package com.dragn0007.dragnloextras.compat.jade.pregnancy;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnloextras.util.SETags;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import com.dragn0007.dragnloextras.util.impls.IPregnancyHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class HorsePregnancyTooltip implements IEntityComponentProvider {
    public HorsePregnancyTooltip() {}

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
         if (entityAccessor.getEntity() instanceof IPregnancyHolder animal && entityAccessor.getPlayer().isShiftKeyDown()) {
            int gestationTime = animal.livestockOverhaulScraps$pregnantTick();
            if (gestationTime > 0) {
                int timeUntilBirth = 0;
                if (entityAccessor.getEntity().getType().is(SETags.Entity_Types.LARGE_ANIMAL))
                    timeUntilBirth = ScrapsExtrasCommonConfig.LARGE_GESTATION_TIME.get() - gestationTime;
                if (entityAccessor.getEntity().getType().is(SETags.Entity_Types.MEDIUM_ANIMAL))
                    timeUntilBirth = ScrapsExtrasCommonConfig.MEDIUM_GESTATION_TIME.get() - gestationTime;
                if (entityAccessor.getEntity().getType().is(SETags.Entity_Types.SMALL_ANIMAL))
                    timeUntilBirth = ScrapsExtrasCommonConfig.SMALL_GESTATION_TIME.get() - gestationTime;
                int timeUntilBirthSeconds = timeUntilBirth / 20;
                tooltip.add(Component.literal("Gestation Time Left: " + timeUntilBirthSeconds + "s"));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {return new ResourceLocation(LivestockOverhaul.MODID, "o_tooltips");}
}
