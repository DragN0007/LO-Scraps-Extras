package com.dragn0007.dragnloextras.compat.wildhorse.mixin;

import com.dragn0007.dragnlivestock.spawn.SpawnReplacer;
import com.dragn0007.dragnloextras.util.BaseImmunityHelper;
import com.dragn0007.dragnloextras.util.BaseTraitHelper;
import com.dragn0007.wildhorse.entity.equus_ferus.EquusFerus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = SpawnReplacer.class, remap = false)
public class SpawnReplacerMixinWH {

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private static void afterSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof EquusFerus equusFerus) {
            if (event.getEntity().getClass() == EquusFerus.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = equusFerus.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") && !equusFerus.isBaby() && equusFerus.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(equusFerus);
                    BaseTraitHelper.setBaseTrait(equusFerus, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }
    }
}