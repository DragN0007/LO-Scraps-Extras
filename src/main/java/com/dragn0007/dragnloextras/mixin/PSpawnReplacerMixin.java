package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.util.BaseImmunityHelper;
import com.dragn0007.dragnloextras.util.BaseTraitHelper;
import com.dragn0007.dragnloextras.util.ISickModHolder;
import com.dragn0007.dragnpets.entities.POEntityTypes;
import com.dragn0007.dragnpets.entities.cat.*;
import com.dragn0007.dragnpets.entities.dog.ODog;
import com.dragn0007.dragnpets.entities.wolf.OWolf;
import com.dragn0007.dragnpets.entities.wolf.OWolfMarkingLayer;
import com.dragn0007.dragnpets.entities.wolf.OWolfModel;
import com.dragn0007.dragnpets.spawn.SpawnReplacer;
import com.dragn0007.dragnpets.util.PetsOverhaulCommonConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(SpawnReplacer.class)
public class PSpawnReplacerMixin implements ISickModHolder {

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

    @Inject(method = "onSpawn", at = @At("HEAD"), remap = false)
    private static void onSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        Random random = new Random();

        //Wolf
        OWolf oWolf = POEntityTypes.O_WOLF_ENTITY.get().create(event.getLevel());
        if (!LivestockOverhaulCommonConfig.FAILSAFE_REPLACER.get() && PetsOverhaulCommonConfig.REPLACE_WOLVES.get() && event.getEntity() instanceof Wolf vanillaWolf) {

            if (event.getEntity().getClass() == Wolf.class && (((!(vanillaWolf.getSpawnType() == MobSpawnType.SPAWN_EGG)) && !LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get()) || LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get())) {

                if (event.getLevel().isClientSide) {
                    return;
                }

                if (oWolf != null) {
                    oWolf.copyPosition(vanillaWolf);
                    oWolf.setOwnerUUID(vanillaWolf.getOwnerUUID());
                    oWolf.setCustomName(vanillaWolf.getCustomName());
                    oWolf.setAge(vanillaWolf.getAge());
                    oWolf.setGender(random.nextInt(OWolf.Gender.values().length));
                    oWolf.setOverlayVariant(random.nextInt(OWolfMarkingLayer.Overlay.values().length));

                    BaseImmunityHelper.setBaseImmunity(oWolf);
                    BaseTraitHelper.setBaseTrait(oWolf, false);

                    if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                        if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Tags.Biomes.IS_HOT_OVERWORLD)) {
                            int[] variants = {3, 5, 6};
                            int randomIndex = new Random().nextInt(variants.length);
                            oWolf.setVariant(variants[randomIndex]);
                        } else if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Tags.Biomes.IS_COLD_OVERWORLD)) {
                            int[] variants = {4, 7, 8};
                            int randomIndex = new Random().nextInt(variants.length);
                            oWolf.setVariant(variants[randomIndex]);
                        } else if (random.nextDouble() > 0.30) {
                            int[] variants = {0, 1, 2, 7};
                            int randomIndex = new Random().nextInt(variants.length);
                            oWolf.setVariant(variants[randomIndex]);
                        }
                    } else {
                        oWolf.setVariant(random.nextInt(OWolfModel.Variant.values().length));
                    }

                    if (event.getLevel().isClientSide) {
                        vanillaWolf.remove(Entity.RemovalReason.DISCARDED);
                    }

                    event.getLevel().addFreshEntity(oWolf);
                    vanillaWolf.remove(Entity.RemovalReason.DISCARDED);

                    event.setCanceled(true);
                }
            }
        }

        //Cat (includes Dogs)
        OCat oCat = POEntityTypes.O_CAT_ENTITY.get().create(event.getLevel());
        ODog oDog = POEntityTypes.O_DOG_ENTITY.get().create(event.getLevel());

        if (!LivestockOverhaulCommonConfig.FAILSAFE_REPLACER.get() && PetsOverhaulCommonConfig.REPLACE_CATS.get() && event.getEntity() instanceof Cat cat) {

            if (event.getEntity().getClass() == Cat.class && (((!(cat.getSpawnType() == MobSpawnType.SPAWN_EGG)) && !LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get()) || LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get())) {

                if (event.getLevel().isClientSide) {
                    return;
                }

                int i = event.getLevel().getRandom().nextInt(100);

                if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.SWAMP)) {
                    if (oCat != null) {
                        oCat.copyPosition(cat);

                        oCat.setCustomName(cat.getCustomName());
                        oCat.setAge(cat.getAge());
                        oCat.setOwnerUUID(cat.getOwnerUUID());

                        if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                            oCat.setEyeColor();
                        } else {
                            oCat.setEyes(random.nextInt(OCatEyeLayer.Eyes.values().length));
                        }

                        oCat.setVariant(0);
                        oCat.setOverlayVariant(0);
                        oCat.setGender(random.nextInt(OCat.Gender.values().length));

                        if (event.getLevel().isClientSide) {
                            cat.remove(Entity.RemovalReason.DISCARDED);
                        }

                        event.getLevel().addFreshEntity(oCat);
                        cat.remove(Entity.RemovalReason.DISCARDED);

                        event.setCanceled(true);
                    }

                } else {
                    if (i <= 70) {
                        oCat.copyPosition(cat);

                        oCat.setCustomName(cat.getCustomName());
                        oCat.setAge(cat.getAge());
                        oCat.setOwnerUUID(cat.getOwnerUUID());

                        oCat.setGender(random.nextInt(OCat.Gender.values().length));

                        if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                            oCat.setBreedByBiome();
                            oCat.setColor();
                            oCat.setMarking();
                            oCat.setEyeColor();
                        } else {
                            oCat.setBreed(random.nextInt(CatBreed.values().length));
                            oCat.setVariant(random.nextInt(OCatModel.Variant.values().length));
                            oCat.setOverlayVariant(random.nextInt(CatMarkingOverlay.values().length));
                            oCat.setEyes(random.nextInt(OCatEyeLayer.Eyes.values().length));
                        }

                        if (event.getLevel().isClientSide) {
                            cat.remove(Entity.RemovalReason.DISCARDED);
                        }

                        event.getLevel().addFreshEntity(oCat);
                        cat.remove(Entity.RemovalReason.DISCARDED);

                        event.setCanceled(true);
                    } else if (i <= 100) {
                        oDog.copyPosition(cat);

                        oDog.setCustomName(cat.getCustomName());
                        oDog.setAge(cat.getAge());
                        oDog.setOwnerUUID(cat.getOwnerUUID());
                        oDog.setGender(random.nextInt(ODog.Gender.values().length));

                        BaseImmunityHelper.setBaseImmunity(oDog);
                        BaseTraitHelper.setBaseTrait(oDog, true);

                        if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                            oDog.setBreedByBiome();
                            oDog.setColor();
                            oDog.setMarking();
                            oDog.setFluffChance();
                        } else {
                            oDog.setBreed(random.nextInt(CatBreed.values().length));
                            oDog.setVariant(random.nextInt(OCatModel.Variant.values().length));
                            oDog.setOverlayVariant(random.nextInt(CatMarkingOverlay.values().length));
                            oDog.setFluffChance();
                        }

                        if (PetsOverhaulCommonConfig.ALLOW_CROPPED_DOG_SPAWNS.get()) {
                            oDog.setCropChance();
                        } else {
                            oDog.setCropped(0);
                        }

                        oDog.setODogAttributes();

                        if (event.getLevel().isClientSide) {
                            cat.remove(Entity.RemovalReason.DISCARDED);
                        }

                        event.getLevel().addFreshEntity(oDog);
                        cat.remove(Entity.RemovalReason.DISCARDED);

                        event.setCanceled(true);
                    }
                }

            }
        }

    }
}