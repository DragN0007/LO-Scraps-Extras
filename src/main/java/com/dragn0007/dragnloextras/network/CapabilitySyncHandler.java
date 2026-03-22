package com.dragn0007.dragnloextras.network;

import com.dragn0007.dragnloextras.capabilities.DirtyCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilitySyncHandler {

    public static void syncToTracking(Entity entity, boolean value) {
        if (!entity.level().isClientSide) {
            SENetwork.sendToTracking(new SyncDirtyLayerPacket(entity.getId(), value), entity);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        target.getCapability(DirtyCapability.DIRTY_CAPABILITY).ifPresent(cap -> {
            SENetwork.sendToPlayer(new SyncDirtyLayerPacket(target.getId(), cap.isDirty()), (ServerPlayer) event.getEntity());
        });
    }
}