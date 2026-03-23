package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncHalterColorPacket {
    private final int entityId;
    private final DyeColor halterColor;

    public SyncHalterColorPacket(int entityId, DyeColor color) {
        this.entityId = entityId;
        this.halterColor = color;
    }

    public static void encode(SyncHalterColorPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeInt(msg.halterColor.getId());
    }

    public static SyncHalterColorPacket decode(FriendlyByteBuf buffer) {
        return new SyncHalterColorPacket(buffer.readInt(), DyeColor.byId(buffer.readInt()));
    }

    public static void handle(SyncHalterColorPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).ifPresent(cap -> {
                    cap.setHalterColor(DyeColor.byId(msg.halterColor.getId()));
                });
            }
        }
    }

    public static void syncToTracking(Entity entity, DyeColor value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncHalterColorPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncHalterColorPacket(target.getId(), cap.getHalterColor()), (ServerPlayer) event.getEntity());
        });
    }
}