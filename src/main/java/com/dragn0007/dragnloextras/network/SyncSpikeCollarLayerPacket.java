package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSpikeCollarLayerPacket {
    private final int entityId;
    private final boolean isSpikeCollar;

    public SyncSpikeCollarLayerPacket(int entityId, boolean isSpikeCollar) {
        this.entityId = entityId;
        this.isSpikeCollar = isSpikeCollar;
    }

    public static void encode(SyncSpikeCollarLayerPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.isSpikeCollar);
    }

    public static SyncSpikeCollarLayerPacket decode(FriendlyByteBuf buffer) {
        return new SyncSpikeCollarLayerPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SyncSpikeCollarLayerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                    cap.setSpikeCollared(msg.isSpikeCollar);
                });
            }
        }
    }

    public static void syncToTracking(Entity entity, boolean value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncSpikeCollarLayerPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncSpikeCollarLayerPacket(target.getId(), cap.hasSpikeCollar()), (ServerPlayer) event.getEntity());
        });
    }
}