package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.DirtyCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncDirtyLayerPacket {
    private final int entityId;
    private final boolean isDirty;

    public SyncDirtyLayerPacket(int entityId, boolean isDirty) {
        this.entityId = entityId;
        this.isDirty = isDirty;
    }

    public static void encode(SyncDirtyLayerPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.isDirty);
    }

    public static SyncDirtyLayerPacket decode(FriendlyByteBuf buffer) {
        return new SyncDirtyLayerPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(SyncDirtyLayerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
        if (level != null) {
            net.minecraft.world.entity.Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(DirtyCapability.DIRTY_CAPABILITY).ifPresent(cap -> {
                    cap.setDirty(msg.isDirty);
                });
            }
        }
    }
}