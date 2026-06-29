package com.dragn0007.dragnloextras.compat.wildhorse;

import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.entity.butchering.HorseCorpse;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import com.dragn0007.wildhorse.entity.equus_ferus.EquusFerus;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CorpseEventWH {

    @SubscribeEvent
    public static void corpseOnEntityDeath(LivingDeathEvent event) {
        if (!ModList.get().isLoaded("wildhorse")) return;

        LivingEntity deceased = event.getEntity();
        Level level = deceased.level();

        if (!deceased.isBaby() && ScrapsExtrasCommonConfig.BUTCHERING.get()) {

            //WildHorse
            if (deceased instanceof EquusFerus animal) {
                if (!level.isClientSide()) {
                    HorseCorpse corpse = new HorseCorpse(SEEntityTypes.HORSE_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
        }
    }
}