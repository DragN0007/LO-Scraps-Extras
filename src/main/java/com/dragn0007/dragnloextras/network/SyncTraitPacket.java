package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncTraitPacket {
    private final int entityId;
    private final int trait;

    public SyncTraitPacket(int entityId, int trait) {
        this.entityId = entityId;
        this.trait = trait;
    }

    public static void encode(SyncTraitPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeInt(msg.trait);
    }

    public static SyncTraitPacket decode(FriendlyByteBuf buffer) {
        return new SyncTraitPacket(buffer.readInt(), buffer.readInt());
    }

    public static void handle(SyncTraitPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(msg.trait);
                });
            }
        }
    }

    public static void syncToTracking(Entity entity, int value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncTraitPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncTraitPacket(target.getId(), cap.getTrait()), (ServerPlayer) event.getEntity());
        });
    }
}