package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.util.ISleepAsLeaderHolder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSleepingAsLeaderPacket {
    private final int entityId;
    private final boolean sleepingAsLeader;

    public SyncSleepingAsLeaderPacket(int entityId, boolean isSleepingAsLeader) {
        this.entityId = entityId;
        this.sleepingAsLeader = isSleepingAsLeader;
    }

    public static void encode(SyncSleepingAsLeaderPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.sleepingAsLeader);
    }

    public static SyncSleepingAsLeaderPacket decode(FriendlyByteBuf buffer) {
        return new SyncSleepingAsLeaderPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SyncSleepingAsLeaderPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            OHorse entity = (OHorse) level.getEntity(msg.entityId);
            if (entity != null) {
                ((ISleepAsLeaderHolder) entity).setSleepingAsLeader(msg.sleepingAsLeader);
            }
        }
    }

    public static void syncToTracking(Entity entity, boolean value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncSleepingAsLeaderPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        OHorse target = (OHorse) event.getTarget();
        SENetwork.sendToPlayer(new SyncSleepingAsLeaderPacket(target.getId(), ((ISleepAsLeaderHolder) target).isSleepingAsLeader()), (ServerPlayer) event.getEntity());
    }
}