package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnloextras.util.BaseImmunityHelper;
import com.dragn0007.dragnloextras.util.BaseTraitHelper;
import com.dragn0007.dragnloextras.util.ISickModHolder;
import com.dragn0007.dragnpets.entities.cat.OCat;
import com.dragn0007.dragnpets.entities.dog.ODog;
import com.dragn0007.dragnpets.entities.ocelot.OOcelot;
import com.dragn0007.dragnpets.entities.wolf.OWolf;
import com.dragn0007.dragnpets.spawn.SpawnReplacer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = SpawnReplacer.class, remap = false)
public class PSpawnReplacerMixin {

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private static void afterSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getLevel().isClientSide) return;

        //Wolf
        if (event.getEntity() instanceof OWolf oWolf) {
            if (event.getEntity().getClass() == OWolf.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oWolf.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") && !oWolf.isBaby() && oWolf.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oWolf);
                    BaseTraitHelper.setBaseTrait(oWolf, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Dog
        if (event.getEntity() instanceof ODog oDog) {
            if (event.getEntity().getClass() == ODog.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oDog.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") && !oDog.isBaby() && oDog.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oDog);
                    BaseTraitHelper.setBaseTrait(oDog, true);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Cat
        if (event.getEntity() instanceof OCat oCat) {
            if (event.getEntity().getClass() == OCat.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oCat.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") && !oCat.isBaby() && oCat.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oCat);
                    BaseTraitHelper.setBaseTrait(oCat, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Ocelot
        if (event.getEntity() instanceof OOcelot oOcelot) {
            if (event.getEntity().getClass() == OOcelot.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oOcelot.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") && !oOcelot.isBaby() && oOcelot.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oOcelot);
                    BaseTraitHelper.setBaseTrait(oOcelot, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }
    }
}