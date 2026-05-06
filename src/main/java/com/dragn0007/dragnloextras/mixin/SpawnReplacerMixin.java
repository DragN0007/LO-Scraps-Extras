package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.spawn.SpawnReplacer;
import com.dragn0007.dragnloextras.util.BaseImmunityHelper;
import com.dragn0007.dragnloextras.util.BaseTraitHelper;
import com.dragn0007.dragnloextras.util.ISickModHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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

//    @Inject(method = "onSpawn", at = @At("TAIL"))
//    private static void afterSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
//        if (event.getLevel().isClientSide) return;
//
//        if (event.getEntity() instanceof AdvancedZombie advancedZombie) {
//            if (event.getEntity().getClass() == AdvancedZombie.class) {
//                if (event.getLevel().isClientSide) {
//                    return;
//                }
//                advancedZombie.setHat(random.nextInt(Hats.values().length));
//            }
//        }
//    }

    @Inject(method = "onSpawn", at = @At("TAIL"))
    private static void afterSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        if (event.getLevel().isClientSide) return;

        //Horse
        if (event.getEntity() instanceof OHorse oHorse) {
            if (event.getEntity().getClass() == OHorse.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag data = oHorse.getPersistentData();
                if (!data.getBoolean("loextras_initialized")) {
                    BaseImmunityHelper.setBaseImmunity(oHorse);
                    BaseTraitHelper.setBaseTrait(oHorse, true);
                    data.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Mule
        if (event.getEntity() instanceof OMule oMule) {
            if (event.getEntity().getClass() == OMule.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }
                CompoundTag data = oMule.getPersistentData();
                if (!data.getBoolean("loextras_initialized")) {
                    BaseImmunityHelper.setBaseImmunity(oMule);
                    BaseTraitHelper.setBaseTrait(oMule, false);
                    data.putBoolean("loextras_initialized", true);
                }
            }
        }

        //Cow
        if (event.getEntity() instanceof OCow oCow) {
            if (event.getEntity().getClass() == OCow.class) {
                if (event.getLevel().isClientSide) {
                    return;
                }CompoundTag data = oCow.getPersistentData();
                if (!data.getBoolean("loextras_initialized")) {
                    BaseImmunityHelper.setBaseImmunity(oCow);
                    data.putBoolean("loextras_initialized", true);
                }
            }
        }
    }
}