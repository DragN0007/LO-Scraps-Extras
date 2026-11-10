package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.camel.OCamel;
import com.dragn0007.dragnlivestock.entities.caribou.Caribou;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.entities.unicorn.Unicorn;
import com.dragn0007.dragnlivestock.spawn.SpawnReplacer;
import com.dragn0007.dragnloextras.capabilities.ImmunityCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.util.BaseImmunityHelper;
import com.dragn0007.dragnloextras.util.BaseTraitHelper;
import com.dragn0007.dragnloextras.util.SETags;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = SpawnReplacer.class, remap = false)
public class SpawnReplacerMixin {

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private static void afterSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getLevel().isClientSide) return;
        CompoundTag nbt = event.getEntity().getPersistentData();
        boolean uninitialized = !nbt.getBoolean("loextras_initialized") || !nbt.contains("loextras_initialized");

//        if (event.getEntity() instanceof Animal baby && baby.isBaby() && SETags.CAPABLE.contains(event.getEntity().getType())) {
//            if ((!nbt.getBoolean("spawned_from_pregnancy") || !nbt.contains("spawned_from_pregnancy")) && ScrapsExtrasCommonConfig.GESTATION.get()) {
//                if (event.getLevel().isClientSide) baby.remove(Entity.RemovalReason.DISCARDED);
//                baby.remove(Entity.RemovalReason.DISCARDED);
//                event.setCanceled(true);
//            }
//        }

        //Horse
        if (event.getEntity() instanceof OHorse oHorse) {
            if (event.getEntity().getClass() == OHorse.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !oHorse.isBaby() && oHorse.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oHorse);
                    BaseTraitHelper.setBaseTrait(oHorse, true);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Mule
        if (event.getEntity() instanceof OMule oMule) {
            if (event.getEntity().getClass() == OMule.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !oMule.isBaby() && oMule.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oMule);
                    BaseTraitHelper.setBaseTrait(oMule, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Donkey
        if (event.getEntity() instanceof ODonkey oDonkey) {
            if (event.getEntity().getClass() == ODonkey.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !oDonkey.isBaby() && oDonkey.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oDonkey);
                    BaseTraitHelper.setBaseTrait(oDonkey, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Camel
        if (event.getEntity() instanceof OCamel oCamel) {
            if (event.getEntity().getClass() == OCamel.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !oCamel.isBaby() && oCamel.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oCamel);
                    BaseTraitHelper.setBaseTrait(oCamel, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Cow
        if (event.getEntity() instanceof OCow oCow) {
            if (event.getEntity().getClass() == OCow.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !oCow.isBaby() && oCow.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oCow);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Caribou
        if (event.getEntity() instanceof Caribou caribou) {
            if (event.getEntity().getClass() == Caribou.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !caribou.isBaby() && caribou.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(caribou);
                    BaseTraitHelper.setBaseTrait(caribou, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Unicorn
        if (event.getEntity() instanceof Unicorn unicorn) {
            if (event.getEntity().getClass() == Unicorn.class) {
                if (event.getLevel().isClientSide) return;
                if (uninitialized && !unicorn.isBaby() && unicorn.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    ImmunityCapabilityInterface immunityCap = null;
                    if (unicorn.getCapability(SECapabilities.IMMUNITY_CAPABILITY).isPresent()) {
                        immunityCap = unicorn.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
                    }
                    immunityCap.setImmunity(100);
                    BaseTraitHelper.setBaseTrait(unicorn, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }
    }
}