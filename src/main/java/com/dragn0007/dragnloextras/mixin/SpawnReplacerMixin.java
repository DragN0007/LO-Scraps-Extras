package com.dragn0007.dragnloextras.mixin;

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
import com.dragn0007.dragnloextras.util.ISickModHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = SpawnReplacer.class, remap = false)
public class SpawnReplacerMixin implements ISickModHolder {

    @Unique
    int livestockOverhaulScraps$becomeSickChanceMod = 0;
    @Unique
    public void setSickChanceMod(int sickChanceMod) {
        this.livestockOverhaulScraps$becomeSickChanceMod = sickChanceMod;
    }

    @Unique
    int livestockOverhaulScraps$becomeSickChance = 0;
    @Unique
    public void setSickChance(int sickChance) {
        this.livestockOverhaulScraps$becomeSickChance = sickChance;
    }

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private static void afterSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getLevel().isClientSide) return;

        //for some reason when a baby is spawned with a spawn egg off of an adult,
        //it spawns with two traits. i guess they arent considered BREEDING or SPAWN_EGG spawn types... ?

        //Horse
        if (event.getEntity() instanceof OHorse oHorse) {
            if (event.getEntity().getClass() == OHorse.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oHorse.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") &&
                        oHorse.getSpawnType() != MobSpawnType.BREEDING && oHorse.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oHorse);
                    BaseTraitHelper.setBaseTrait(oHorse, true);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Mule
        if (event.getEntity() instanceof OMule oMule) {
            if (event.getEntity().getClass() == OMule.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oMule.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") &&
                        oMule.getSpawnType() != MobSpawnType.BREEDING && oMule.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oMule);
                    BaseTraitHelper.setBaseTrait(oMule, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Donkey
        if (event.getEntity() instanceof ODonkey oDonkey) {
            if (event.getEntity().getClass() == ODonkey.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oDonkey.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") &&
                        oDonkey.getSpawnType() != MobSpawnType.BREEDING && oDonkey.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oDonkey);
                    BaseTraitHelper.setBaseTrait(oDonkey, false);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Cow
        if (event.getEntity() instanceof OCow oCow) {
            if (event.getEntity().getClass() == OCow.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = oCow.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") &&
                        oCow.getSpawnType() != MobSpawnType.BREEDING && oCow.getSpawnType() != MobSpawnType.SPAWN_EGG) {
                    BaseImmunityHelper.setBaseImmunity(oCow);
                    nbt.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Unicorn
        if (event.getEntity() instanceof Unicorn unicorn) {
            if (event.getEntity().getClass() == Unicorn.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag nbt = unicorn.getPersistentData();
                if (!nbt.getBoolean("loextras_initialized") &&
                        unicorn.getSpawnType() != MobSpawnType.BREEDING && unicorn.getSpawnType() != MobSpawnType.SPAWN_EGG) {
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