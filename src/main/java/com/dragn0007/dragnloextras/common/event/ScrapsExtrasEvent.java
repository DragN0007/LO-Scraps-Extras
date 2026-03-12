package com.dragn0007.dragnloextras.common.event;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.common.gui.MannequinMenu;
import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.entity.mannequin.HorseMannequin;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = ScrapsExtras.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScrapsExtrasEvent {

    @SubscribeEvent
    public static void entityAttrbiuteCreationEvent(EntityAttributeCreationEvent event) {
        event.put(SEEntityTypes.HORSE_MANNEQUIN_ENTITY.get(), HorseMannequin.createAttributes().build());
    }

}