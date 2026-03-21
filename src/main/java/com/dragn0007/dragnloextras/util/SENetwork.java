package com.dragn0007.dragnloextras.util;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = ScrapsExtras.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SENetwork {

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ScrapsExtras.MODID, "se_network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SubscribeEvent
    public static void commonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}
