package com.dragn0007.dragnloextras.common.event;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.entity.butchering.*;
import com.dragn0007.dragnloextras.entity.mannequin.HorseMannequin;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = ScrapsExtras.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScrapsExtrasEvent {

    @SubscribeEvent
    public static void entityAttrbiuteCreationEvent(EntityAttributeCreationEvent event) {
        event.put(SEEntityTypes.HORSE_MANNEQUIN_ENTITY.get(), HorseMannequin.createAttributes().build());
        event.put(SEEntityTypes.COW_CORPSE.get(), CowCorpse.createAttributes().build());
        event.put(SEEntityTypes.HORSE_CORPSE.get(), DonkeyCorpse.createAttributes().build());
        event.put(SEEntityTypes.SHEEP_CORPSE.get(), SheepCorpse.createAttributes().build());
        event.put(SEEntityTypes.MULE_CORPSE.get(), MuleCorpse.createAttributes().build());
        event.put(SEEntityTypes.DONKEY_CORPSE.get(), DonkeyCorpse.createAttributes().build());
        event.put(SEEntityTypes.CHICKEN_CORPSE.get(), ChickenCorpse.createAttributes().build());
        event.put(SEEntityTypes.RABBIT_CORPSE.get(), RabbitCorpse.createAttributes().build());
        event.put(SEEntityTypes.UNICORN_CORPSE.get(), UnicornCorpse.createAttributes().build());
    }

}