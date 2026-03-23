package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
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

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToTracking(MSG message, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    @SubscribeEvent
    public static void commonSetupEvent(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            INSTANCE.registerMessage(0, SyncDirtyLayerPacket.class, SyncDirtyLayerPacket::encode, SyncDirtyLayerPacket::decode, SyncDirtyLayerPacket::handle);
            INSTANCE.registerMessage(1, SyncHalterLayerPacket.class, SyncHalterLayerPacket::encode, SyncHalterLayerPacket::decode, SyncHalterLayerPacket::handle);
            INSTANCE.registerMessage(2, SyncTraitPacket.class, SyncTraitPacket::encode, SyncTraitPacket::decode, SyncTraitPacket::handle);
            INSTANCE.registerMessage(4, SyncHalterColorPacket.class, SyncHalterColorPacket::encode, SyncHalterColorPacket::decode, SyncHalterColorPacket::handle);
        });
    }
}
