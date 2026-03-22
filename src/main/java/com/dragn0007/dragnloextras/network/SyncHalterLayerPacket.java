package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncHalterLayerPacket {
    private final int entityId;
    private final boolean isHalter;

    public SyncHalterLayerPacket(int entityId, boolean isHalter) {
        this.entityId = entityId;
        this.isHalter = isHalter;
    }

    public static void encode(SyncHalterLayerPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.isHalter);
    }

    public static SyncHalterLayerPacket decode(FriendlyByteBuf buffer) {
        return new SyncHalterLayerPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SyncHalterLayerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(SECapabilities.HALTER_CAPABILITY).ifPresent(cap -> {
                    cap.setHalter(msg.isHalter);
                });
            }
        }
    }

    public static void syncToTracking(Entity entity, boolean value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncHalterLayerPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(SECapabilities.HALTER_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncHalterLayerPacket(target.getId(), cap.isHalter()), (ServerPlayer) event.getEntity());
        });
    }
}