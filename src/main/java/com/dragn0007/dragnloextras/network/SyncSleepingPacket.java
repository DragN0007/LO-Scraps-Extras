package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSleepingPacket {
    private final int entityId;
    private final boolean isSleeping;

    public SyncSleepingPacket(int entityId, boolean isSleeping) {
        this.entityId = entityId;
        this.isSleeping = isSleeping;
    }

    public static void encode(SyncSleepingPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.isSleeping);
    }

    public static SyncSleepingPacket decode(FriendlyByteBuf buffer) {
        return new SyncSleepingPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SyncSleepingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(SECapabilities.SLEEPING_CAPABILITY).ifPresent(cap -> {
                    cap.setSleeping(msg.isSleeping);
                });
            }
        }
    }

    public static void syncToTracking(Entity entity, boolean value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncSleepingPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(SECapabilities.SLEEPING_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncSleepingPacket(target.getId(), cap.isSleeping()), (ServerPlayer) event.getEntity());
        });
    }
}