package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncImmunityPacket {
    private final int entityId;
    private final int immunity;

    public SyncImmunityPacket(int entityId, int immunity) {
        this.entityId = entityId;
        this.immunity = immunity;
    }

    public static void encode(SyncImmunityPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeInt(msg.immunity);
    }

    public static SyncImmunityPacket decode(FriendlyByteBuf buffer) {
        return new SyncImmunityPacket(buffer.readInt(), buffer.readInt());
    }

    public static void handle(SyncImmunityPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(SECapabilities.IMMUNITY_CAPABILITY).ifPresent(cap -> {
                    cap.setImmunity(msg.immunity);
                });
            }
        }
    }

    public static void syncToTracking(Entity entity, int value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncImmunityPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(SECapabilities.IMMUNITY_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncImmunityPacket(target.getId(), cap.getImmunity()), (ServerPlayer) event.getEntity());
        });
    }
}