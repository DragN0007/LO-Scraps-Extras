package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.horse.HorseBreed;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseModel;
import com.dragn0007.dragnlivestock.entities.horse.headlesshorseman.HeadlessHorseman;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineEyeColorOverlay;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineMarkingOverlay;
import com.dragn0007.dragnlivestock.spawn.SpawnReplacer;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.holders.ITraitByBreedTypeHolder;
import com.dragn0007.dragnloextras.holders.Trait;
import com.dragn0007.dragnloextras.holders.TraitDuck;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;


@Mixin(SpawnReplacer.class)
public class SpawnReplacerMixin {

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

                    if (ScrapsExtrasCommonConfig.TRAITS_SYSTEM.get()) {
                        if (ScrapsExtrasCommonConfig.TRAITS_BY_BREED.get()) {
                            ((ITraitByBreedTypeHolder) oHorse).setTraitByBreedType();
                        } else {
                            ((TraitDuck) oHorse).livestockOverhaulScraps$setTrait(Trait.values().length);
                        }
                        switch (((TraitDuck) oHorse).livestockOverhaulScraps$getTrait()) {
                            case 0: oHorse.addEffect(new MobEffectInstance(SEEffects.BRAVE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 1: oHorse.addEffect(new MobEffectInstance(SEEffects.IMMUNOCOMPETENT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 2: oHorse.addEffect(new MobEffectInstance(SEEffects.SWIFT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 3: oHorse.addEffect(new MobEffectInstance(SEEffects.VAULTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 4: oHorse.addEffect(new MobEffectInstance(SEEffects.CLIMBER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 5: oHorse.addEffect(new MobEffectInstance(SEEffects.BUSTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 6: oHorse.addEffect(new MobEffectInstance(SEEffects.STURDY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 7: oHorse.addEffect(new MobEffectInstance(SEEffects.COWARDLY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 8: oHorse.addEffect(new MobEffectInstance(SEEffects.IMMUNOSUPPRESSED.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 9: oHorse.addEffect(new MobEffectInstance(SEEffects.STUBBORN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 10: oHorse.addEffect(new MobEffectInstance(SEEffects.LAGGARD.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 11: oHorse.addEffect(new MobEffectInstance(SEEffects.FRAIL.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                            case 12: oHorse.addEffect(new MobEffectInstance(SEEffects.MEAN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                break;
                        }
                    }

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
    }
}