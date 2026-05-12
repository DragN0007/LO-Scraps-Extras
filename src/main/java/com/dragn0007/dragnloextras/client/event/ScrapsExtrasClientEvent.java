package com.dragn0007.dragnloextras.client.event;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.client.gui.MannequinScreen;
import com.dragn0007.dragnloextras.common.gui.SEMenuTypes;
import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.entity.butchering.*;
import com.dragn0007.dragnloextras.entity.mannequin.HorseMannequinRender;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = ScrapsExtras.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ScrapsExtrasClientEvent {

    @SubscribeEvent
    public static void clientSetupEvent(FMLClientSetupEvent event) {
        EntityRenderers.register(SEEntityTypes.HORSE_MANNEQUIN_ENTITY.get(), HorseMannequinRender::new);
        EntityRenderers.register(SEEntityTypes.COW_CORPSE.get(), CowCorpseRender::new);
        EntityRenderers.register(SEEntityTypes.HORSE_CORPSE.get(), HorseCorpseRender::new);
        EntityRenderers.register(SEEntityTypes.SHEEP_CORPSE.get(), SheepCorpseRender::new);
        EntityRenderers.register(SEEntityTypes.MULE_CORPSE.get(), MuleCorpseRender::new);
        EntityRenderers.register(SEEntityTypes.DONKEY_CORPSE.get(), DonkeyCorpseRender::new);
        EntityRenderers.register(SEEntityTypes.UNICORN_CORPSE.get(), UnicornCorpseRender::new);

        MenuScreens.register(SEMenuTypes.MANNEQUIN_MENU.get(), MannequinScreen::new);
    }

}