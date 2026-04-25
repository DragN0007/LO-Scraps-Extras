package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.cow.CowBreed;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.cow.OCowModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.azalea.AzaleaMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.azalea.AzaleaMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.beetroot.BeetrootMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.beetroot.BeetrootMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.carrot.CarrotMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.carrot.CarrotMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.flowering.FloweringMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.flowering.FloweringMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.glow_berry.GlowBerryMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.glow_berry.GlowBerryMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.melon.MelonMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.melon.MelonMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.peach.PeachMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.peach.PeachMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.potato.PotatoMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.potato.PotatoMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.pumpkin.PumpkinMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.pumpkin.PumpkinMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.sweet_berry.SweetBerryMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.sweet_berry.SweetBerryMoobloomModel;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.wheat.WheatMoobloom;
import com.dragn0007.dragnlivestock.entities.cow.moobloom.wheat.WheatMoobloomModel;
import com.dragn0007.dragnlivestock.entities.horse.HorseBreed;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseModel;
import com.dragn0007.dragnlivestock.entities.horse.headlesshorseman.HeadlessHorseman;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.BovineMarkingOverlay;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineEyeColorOverlay;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineMarkingOverlay;
import com.dragn0007.dragnlivestock.spawn.SpawnReplacer;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;


@Mixin(SpawnReplacer.class)
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

    @Inject(method = "onSpawn", at = @At("HEAD"), remap = false)
    private static void onSpawn(EntityJoinLevelEvent event, CallbackInfo ci) {
        Random random = new Random();

        //Horse
        if (!LivestockOverhaulCommonConfig.FAILSAFE_REPLACER.get() && LivestockOverhaulCommonConfig.REPLACE_HORSES.get() && event.getEntity() instanceof Horse vanillaHorse) {

            if (event.getEntity().getClass() == Horse.class && (((!(vanillaHorse.getSpawnType() == MobSpawnType.SPAWN_EGG)) && !LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get()) || LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get())) {

                if (event.getLevel().isClientSide) {
                    return;
                }

                LocalDate date = LocalDate.now();
                Month month = date.getMonth();
                int day = date.getDayOfMonth();

                if ((month == Month.OCTOBER && (day == 31)) || (month == Month.NOVEMBER && (day == 1 || day == 2)) && LivestockOverhaulCommonConfig.ALLOW_HOLIDAY_EVENTS.get()) {
                    if (event.getLevel().isNight() && event.getLevel().getRandom().nextDouble() <= 0.12) {
                        HeadlessHorseman headlessHorseman = EntityTypes.HEADLESS_HORSEMAN_ENTITY.get().create(event.getLevel());

                        if (headlessHorseman != null) {
                            headlessHorseman.copyPosition(vanillaHorse);
                            event.getLevel().addFreshEntity(headlessHorseman);

                            headlessHorseman.setVariant(0);

                            if (event.getLevel().isClientSide) {
                                vanillaHorse.remove(Entity.RemovalReason.DISCARDED);
                            }

                            event.getLevel().addFreshEntity(headlessHorseman);
                            vanillaHorse.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }
                } else {
                    if (event.getLevel().isNight() && event.getLevel().getRandom().nextDouble() <= 0.02) {
                        HeadlessHorseman headlessHorseman = EntityTypes.HEADLESS_HORSEMAN_ENTITY.get().create(event.getLevel());

                        if (headlessHorseman != null) {
                            headlessHorseman.copyPosition(vanillaHorse);
                            event.getLevel().addFreshEntity(headlessHorseman);

                            headlessHorseman.setVariant(0);

                            if (event.getLevel().isClientSide) {
                                vanillaHorse.remove(Entity.RemovalReason.DISCARDED);
                            }

                            event.getLevel().addFreshEntity(headlessHorseman);
                            vanillaHorse.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }
                }

                OHorse oHorse = EntityTypes.O_HORSE_ENTITY.get().create(event.getLevel());
                if (oHorse != null) {
                    oHorse.copyPosition(vanillaHorse);

                    //try to take on as many identifiers from the vanilla horse possible
                    oHorse.setCustomName(vanillaHorse.getCustomName());
                    oHorse.setOwnerUUID(vanillaHorse.getOwnerUUID());
                    oHorse.setAge(vanillaHorse.getAge());
                    oHorse.randomizeOHorseAttributes();
                    oHorse.setReindeerVariant(random.nextInt(OHorseModel.ReindeerVariant.values().length));
                    oHorse.setGender(random.nextInt(AbstractOMount.Gender.values().length));

                    BaseImmunityHelper.setBaseImmunity(oHorse);
                    BaseTraitHelper.setBaseTrait(oHorse, true);

                    //spawn breeds except for compat-only ones if the config allows it
                    if (LivestockOverhaulCommonConfig.NATURAL_HORSE_BREEDS.get()) {
                        if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Tags.Biomes.IS_HOT_OVERWORLD)) {
                            int[] breeds = {10, 13};
                            int randomIndex = new Random().nextInt(breeds.length);
                            oHorse.setBreed(breeds[randomIndex]);
                        } else if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Tags.Biomes.IS_COLD_OVERWORLD)) {
                            int[] breeds = {1, 5, 6, 8, 12, 15, 16, 17, 19, 21, 22};
                            int randomIndex = new Random().nextInt(breeds.length);
                            oHorse.setBreed(breeds[randomIndex]);
                        } else if (!ModList.get().isLoaded("deadlydinos")) {
                            int[] breeds = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21, 22};
                            int randomIndex = new Random().nextInt(breeds.length);
                            oHorse.setBreed(breeds[randomIndex]);
                        } else {
                            oHorse.setBreed(random.nextInt(HorseBreed.values().length));
                        }

                        oHorse.setManeType(1 + random.nextInt(4));
                        oHorse.setTailType(1 + random.nextInt(4));
                    } else {
                        oHorse.setBreed(0);
                        oHorse.setManeType(2);
                        oHorse.setTailType(1 + random.nextInt(4));
                    }

                    //spawn markings and colors by breed if the config allows it
                    if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                        oHorse.setColorByBreed();
                        oHorse.setMarkingByBreed();
                        oHorse.setFeatheringByBreed();
                    } else {
                        oHorse.setVariant(random.nextInt(OHorseModel.Variant.values().length));
                        oHorse.setOverlayVariant(random.nextInt(EquineMarkingOverlay.values().length));
                        oHorse.setFeathering(random.nextInt(OHorse.Feathering.values().length));
                    }

                    if (LivestockOverhaulCommonConfig.EYES_BY_COLOR.get()) {
                        oHorse.setEyeColorByChance();
                    } else {
                        oHorse.setEyeVariant(random.nextInt(EquineEyeColorOverlay.values().length));
                    }

                    //discard vanilla horse once it's been successfully replaced on client and server
                    if (event.getLevel().isClientSide) {
                        vanillaHorse.remove(Entity.RemovalReason.DISCARDED);
                    }

                    event.getLevel().addFreshEntity(oHorse);
                    vanillaHorse.remove(Entity.RemovalReason.DISCARDED);

                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("[DragN's Livestock Overhaul!]: Replaced a vanilla horse with an O-Horse at: " + oHorse.getOnPos());
                    }

                    if (random.nextDouble() <= LivestockOverhaulCommonConfig.SPAWN_PREVENTION_PERCENT.get() && (!(oHorse.getSpawnType() == MobSpawnType.SPAWN_EGG)) && (!(vanillaHorse.getSpawnType() == MobSpawnType.SPAWN_EGG))) {
                        if (event.getLevel().isClientSide) {
                            oHorse.remove(Entity.RemovalReason.DISCARDED);
                        }
                        oHorse.remove(Entity.RemovalReason.DISCARDED);
                    }

                    event.setCanceled(true);
                }
            }
        }

        //Cow
        if (!LivestockOverhaulCommonConfig.FAILSAFE_REPLACER.get() && LivestockOverhaulCommonConfig.REPLACE_COWS.get() && event.getEntity() instanceof Cow vanillacow) {

            if (event.getEntity().getClass() == Cow.class && (((!(vanillacow.getSpawnType() == MobSpawnType.SPAWN_EGG)) && !LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get()) || LivestockOverhaulCommonConfig.REPLACE_SPAWN_EGG_ANIMALS.get())) {

                if (event.getLevel().isClientSide) {
                    return;
                }

                OCow oCow = EntityTypes.O_COW_ENTITY.get().create(event.getLevel());
                if (oCow != null) {
                    if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                        if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Tags.Biomes.IS_HOT_OVERWORLD)) {
                            if (random.nextDouble() < 0.20) {
                                oCow.setBreed(random.nextInt(CowBreed.Breed.values().length));
                            } else {
                                int[] variants = {1, 2, 4, 5};
                                int randomIndex = new Random().nextInt(variants.length);
                                oCow.setBreed(variants[randomIndex]);
                            }
                        } else if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Tags.Biomes.IS_COLD_OVERWORLD)) {
                            if (random.nextDouble() < 0.20) {
                                oCow.setBreed(random.nextInt(CowBreed.Breed.values().length));
                            } else {
                                oCow.setBreed(9);
                            }
                        } else {
                            int[] variants = {0, 6, 7, 8};
                            int randomIndex = new Random().nextInt(variants.length);
                            oCow.setBreed(variants[randomIndex]);
                        }
                    } else {
                        oCow.setBreed(random.nextInt(CowBreed.Breed.values().length));
                    }

                    BaseImmunityHelper.setBaseImmunity(oCow);

                    oCow.copyPosition(vanillacow);
                    oCow.setGender(random.nextInt(OCow.Gender.values().length));
                    if (LivestockOverhaulCommonConfig.QUALITY.get()) {
                        oCow.setQuality(random.nextInt(30));
                    }

                    if (LivestockOverhaulCommonConfig.SPAWN_BY_BREED.get()) {
                        oCow.setColorByBreed();
                        oCow.setMarkingByBreed();
                        oCow.setHornsByBreed();
                    } else {
                        oCow.setVariant(random.nextInt(OCowModel.Variant.values().length));
                        oCow.setOverlayVariant(random.nextInt(BovineMarkingOverlay.values().length));
                        oCow.setHornVariant(random.nextInt(OCow.BreedHorns.values().length));
                    }

                    if (event.getLevel().isClientSide) {
                        vanillacow.remove(Entity.RemovalReason.DISCARDED);
                    }

                    event.getLevel().addFreshEntity(oCow);
                    vanillacow.remove(Entity.RemovalReason.DISCARDED);

                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("[DragN's Livestock Overhaul!]: Replaced a vanilla cow with an O-Cow at: " + oCow.getOnPos());
                    }

                    if (random.nextDouble() <= LivestockOverhaulCommonConfig.SPAWN_PREVENTION_PERCENT.get() && (!(oCow.getSpawnType() == MobSpawnType.SPAWN_EGG)) && (!(vanillacow.getSpawnType() == MobSpawnType.SPAWN_EGG))) {
                        if (event.getLevel().isClientSide) {
                            oCow.remove(Entity.RemovalReason.DISCARDED);
                        }
                        oCow.remove(Entity.RemovalReason.DISCARDED);
                    }


                    event.setCanceled(true);
                }
            }
        }
    }
}