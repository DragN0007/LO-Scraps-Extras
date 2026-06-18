package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.ai.HorseFollowHerdLeaderGoal;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.horse.HorseBreed;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseModel;
import com.dragn0007.dragnlivestock.entities.mule.OMuleModel;
import com.dragn0007.dragnlivestock.entities.unicorn.Unicorn;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineEyeColorOverlay;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineMarkingOverlay;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.EquineSleepGoal;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.HorseFollowOwnerGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.items.custom.HalterItem;
import com.dragn0007.dragnloextras.items.custom.TurnoutBlanketItem;
import com.dragn0007.dragnloextras.network.*;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mixin(OHorse.class)
public abstract class OHorseMixin extends AbstractOMount implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISleepAsLeaderHolder, ISickModHolder {

    @Shadow(remap = false) public abstract boolean isDraftBreed();
    @Shadow(remap = false) public abstract boolean isWarmbloodedBreed();
    @Shadow(remap = false) public abstract boolean isRacingBreed();
    @Shadow(remap = false) public abstract boolean isPonyBreed();
    @Shadow(remap = false) public abstract boolean isStockBreed();
    @Shadow(remap = false) public abstract int getEyeVariant();

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);
    @Shadow public abstract void registerGoals();

    @Unique
    public boolean sleepingAsLeader = false;
    @Unique
    public boolean isSleepingAsLeader() {
        return this.sleepingAsLeader;
    }
    @Unique
    public void setSleepingAsLeader(boolean sleepingAsLeader) {
        this.sleepingAsLeader = sleepingAsLeader;
    }

    @Unique
    public boolean hungry = false;
    @Unique
    public boolean isHungry() {
        return this.hungry;
    }
    @Unique
    public void setHungry(boolean hungry) {
        this.hungry = hungry;
    }

//    @Unique
//    public boolean pregnant = false;
//    @Unique
//    public boolean isPregnant() {
//        return this.pregnant;
//    }
//    @Unique
//    public void setPregnant(boolean pregnant) {
//        this.pregnant = pregnant;
//    }

    /**
     * @author who do you think
     * @reason cuz i can
     */
    @Overwrite
    public ResourceLocation getDefaultLootTable() {
        if (ScrapsExtrasCommonConfig.BUTCHERING.get()) {
            return BuiltInLootTables.EMPTY;
        } else if (ModList.get().isLoaded("tfc")) {
            return OHorse.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return OHorse.VANILLA_LOOT_TABLE;
        } else {
            return OHorse.LOOT_TABLE;
        }
    }

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

    @Unique int livestockOverhaulScraps$beMeanTargetTick = random.nextInt(24000) + 1200;
    @Unique int livestockOverhaulScraps$becomeSickRand = random.nextInt(100);
    @Unique int livestockOverhaulScraps$pregnantTick;

    public OHorseMixin(EntityType<? extends OHorseMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
//        this.setPregnant(false);
        this.setSleepingAsLeader(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OHorse self = (OHorse) (Object) this;
        this.goalSelector.addGoal(0, new EquineSleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(2, new HorseFollowOwnerGoal(self, 1.0D, 2.0F, 2.0F, false));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) && !this.isBaby() && this.hasEffect(SEEffects.RABIES.get())
        ));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) &&
                        !this.isBaby() && this.hasEffect(SEEffects.MEAN.get()) && livestockOverhaulScraps$beMeanTick >= livestockOverhaulScraps$beMeanTargetTick
        ));

        this.goalSelector.addGoal(3, new HorseFollowHerdLeaderGoal(self));
    }

    @Unique public int livestockOverhaulScraps$dirtyTick;
    @Unique public int livestockOverhaulScraps$hungryTick;
    @Unique public int livestockOverhaulScraps$beMeanTick;
    @Unique public int livestockOverhaulScraps$sickTick;
    @Unique public int livestockOverhaulScraps$sickModDissipateTick = 72000;
    @Unique public int livestockOverhaulScraps$saddleSoreTick;
    @Unique public int livestockOverhaulScraps$rainRotTick;
    @Unique public int livestockOverhaulScraps$heartwormMedTick;
    @Unique public int livestockOverhaulScraps$hoofPickTick;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {

            if (ScrapsExtrasCommonConfig.SLEEPING.get()) {
                SleepingCapabilityInterface sleepingCap;
                if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
                    sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
                    if (sleepingCap != null && (sleepingCap.isSleeping() || this.isSleepingAsLeader())) {
                        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof LookAtPlayerGoal);
//                        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof HorseFollowHerdLeaderGoal);
                    }
                }
            }

            if (this.isTamed()) {
                if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
                    if (livestockOverhaulScraps$becomeSickChanceMod != 0) {
                        livestockOverhaulScraps$sickModDissipateTick--;
                        if (livestockOverhaulScraps$sickModDissipateTick <= 0) {
                            livestockOverhaulScraps$becomeSickChance = livestockOverhaulScraps$becomeSickChance - livestockOverhaulScraps$becomeSickChanceMod;
                            livestockOverhaulScraps$becomeSickChanceMod = 0;
                            livestockOverhaulScraps$sickModDissipateTick = 72000;
                        }
                    }

                    if (livestockOverhaulScraps$becomeSickChance < 0) {
                        livestockOverhaulScraps$becomeSickChance = 0;
                    } else if (livestockOverhaulScraps$becomeSickChance > 100) {
                        livestockOverhaulScraps$becomeSickChance = 100;
                    }

                    livestockOverhaulScraps$sickTick++;
                    if (livestockOverhaulScraps$sickTick >= 72000) {
                        if (livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            livestockOverhaulScraps$sickTick = 0;
                        }
                    }

                    double range = 5.0;
                    AABB searchBox = this.getBoundingBox().inflate(range);
                    List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(LivingEntity.class, searchBox);

                    for (LivingEntity target : nearbyEntities) {
                        if (target == this) continue;
                        if (random.nextDouble() <= 0.001) {
                            if (target.hasEffect(SEEffects.MANGE.get()) && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                                this.addEffect(new MobEffectInstance(SEEffects.MANGE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                            if (target.hasEffect(SEEffects.BOTFLY_INFESTATION.get()) && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                                this.addEffect(new MobEffectInstance(SEEffects.BOTFLY_INFESTATION.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                            if (target.hasEffect(SEEffects.FLEA_INFESTATION.get()) && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                                this.addEffect(new MobEffectInstance(SEEffects.FLEA_INFESTATION.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                            if (target.hasEffect(SEEffects.RINGWORM.get()) && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                                this.addEffect(new MobEffectInstance(SEEffects.RINGWORM.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                            if (target.hasEffect(SEEffects.RABIES.get()) && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                                this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.EAR_INFECTION.get()) {
                        if (livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.05) {
                                this.addEffect(new MobEffectInstance(SEEffects.EAR_INFECTION.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.MANGE.get()) {
                        if (livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.03) {
                                this.addEffect(new MobEffectInstance(SEEffects.MANGE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.BOTFLY_INFESTATION.get()) {
                        if (livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.05) {
                                this.addEffect(new MobEffectInstance(SEEffects.BOTFLY_INFESTATION.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.FLEA_INFESTATION.get()) {
                        if (livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.05) {
                                this.addEffect(new MobEffectInstance(SEEffects.FLEA_INFESTATION.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.RINGWORM.get()) {
                        if (livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.03) {
                                this.addEffect(new MobEffectInstance(SEEffects.RINGWORM.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.HEARTWORMS.get()) {
                        if (livestockOverhaulScraps$heartwormMedTick >= 0)
                            livestockOverhaulScraps$heartwormMedTick--;

                        if (livestockOverhaulScraps$heartwormMedTick < ScrapsExtrasCommonConfig.HEARTWORM_MED_GRACE.get() &&
                                livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.02) {
                                this.addEffect(new MobEffectInstance(SEEffects.HEARTWORMS.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.HOOF_ABSCESS.get()) {
                        if (livestockOverhaulScraps$hoofPickTick >= 0)
                            livestockOverhaulScraps$hoofPickTick--;

                        if (livestockOverhaulScraps$hoofPickTick < ScrapsExtrasCommonConfig.HOOF_PICK_GRACE.get() &&
                                livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            if (random.nextDouble() <= 0.50) {
                                this.addEffect(new MobEffectInstance(SEEffects.HOOF_ABSCESS.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }

                    if (ScrapsExtrasCommonConfig.SADDLE_SORE.get()) {
                        if (this.isSaddled()) {
                            livestockOverhaulScraps$saddleSoreTick++;
                            if (livestockOverhaulScraps$saddleSoreTick >= 24000) {
                                if (random.nextDouble() <= 0.75) {
                                    this.addEffect(new MobEffectInstance(SEEffects.SADDLE_SORE.get(), 48000, 0, false, false));
                                }
                                livestockOverhaulScraps$saddleSoreTick = 0;
                            }
                        } else {
                            livestockOverhaulScraps$saddleSoreTick = 0;
                        }
                    }

                    if (ScrapsExtrasCommonConfig.RAIN_ROT.get()) {
                        if (this.level().isRaining() && this.level().canSeeSky(this.blockPosition())) {
                            livestockOverhaulScraps$rainRotTick++;
                            if (livestockOverhaulScraps$rainRotTick >= (random.nextInt(3000) + 12000)) {
                                if (random.nextDouble() <= 0.75 && !(this.getArmor().getItem() instanceof TurnoutBlanketItem)) {
                                    this.addEffect(new MobEffectInstance(SEEffects.RAIN_ROT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                }
                                livestockOverhaulScraps$rainRotTick = 0;
                            }
                        } else {
                            livestockOverhaulScraps$rainRotTick = 0;
                        }
                    }
                }

                if (ScrapsExtrasCommonConfig.FEEDING_SYSTEM.get()) {
                    if (!this.isHungry()) {
                        BlockPos blockpos = this.blockPosition();
                        BlockPos blockpos1 = blockpos.below();
                        if (ScrapsExtrasCommonConfig.GRAZING.get() && (!this.level().getBlockState(blockpos1).is(SETags.Blocks.EDIBLE_GRASS) && !this.isGroundTied()) && this.isTamed()) {
                            livestockOverhaulScraps$hungryTick++;
                            if (livestockOverhaulScraps$hungryTick >= ScrapsExtrasCommonConfig.HORSE_FEED_TICK.get()) {
                                this.setHungry(true);
                                this.addEffect(new MobEffectInstance(SEEffects.HUNGER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                                livestockOverhaulScraps$hungryTick = 0;
                            }
                        }
                    }
                }

                if (ScrapsExtrasCommonConfig.HYGIENE_SYSTEM.get()) {
                    livestockOverhaulScraps$dirtyTick++;

                    if (livestockOverhaulScraps$dirtyTick >= ScrapsExtrasCommonConfig.DIRTY_TICK.get() && this.hasEffect(SEEffects.DIRTY.get())) {
                        this.getCapability(SECapabilities.DIRTY_CAPABILITY).ifPresent(cap -> {
                            if (cap.isDirty()) {
                                int amp = Objects.requireNonNull(this.getEffect(SEEffects.DIRTY.get())).getAmplifier();
                                if (amp <= 4) {
                                    this.addEffect(new MobEffectInstance(SEEffects.DIRTY.get(), MobEffectInstance.INFINITE_DURATION, amp + 1, false, false));
                                }
                            }
                        });
                    }

                    if (livestockOverhaulScraps$dirtyTick >= ScrapsExtrasCommonConfig.DIRTY_TICK.get()) {
                        this.getCapability(SECapabilities.DIRTY_CAPABILITY).ifPresent(cap -> {
                            cap.setDirty(true);
                            SyncDirtyLayerPacket.syncToTracking(this, true);
                        });
                        this.addEffect(new MobEffectInstance(SEEffects.DIRTY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        livestockOverhaulScraps$dirtyTick = 0;
                    }
                }
            }
        }

        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
        if (ScrapsExtrasCommonConfig.TRAITS_SYSTEM.get() && this.hasEffect(SEEffects.MEAN.get()) && traitCap.getTrait() == 12) {
            livestockOverhaulScraps$beMeanTick++;
            if (livestockOverhaulScraps$beMeanTick >= livestockOverhaulScraps$beMeanTargetTick) {
                livestockOverhaulScraps$beMeanTick = 0;
            }
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt(DamageSource damageSource, float dmg, CallbackInfoReturnable<Boolean> cir) {
        super.hurt(damageSource, dmg);

        if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
            if (livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {

                double abrasionChance = dmg / 10;

                //abrasions happen sometimes, but they're no big deal
                if (random.nextDouble() <= abrasionChance) {
                    this.addEffect(new MobEffectInstance(SEEffects.ABRASION.get(), ScrapsExtrasCommonConfig.INFECTION_TICK.get() + 20, 0, false, false));
                }

                if (ScrapsExtrasCommonConfig.RABIES.get()) {
                    //animals with higher immunity are less likely to get rabies from a bite. this is not realistic.
                    //do not go around getting bit by animals even if you've never had the flu. not a good idea
                    if (damageSource.getEntity() instanceof Animal && random.nextDouble() <= 0.02) {
                        this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    }
                    if (damageSource.getEntity() instanceof Animal animal && animal.hasEffect(SEEffects.RABIES.get())) {
                        this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    }
                }
            }
        }
    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (!this.level().isClientSide) {
            if (itemstack.is(SEItems.BRUSH.get())) {
                livestockOverhaulScraps$dirtyTick = 0;
                this.getCapability(SECapabilities.DIRTY_CAPABILITY).ifPresent(cap -> {
                    cap.setDirty(false); //                                                                            this value
                    SyncDirtyLayerPacket.syncToTracking(this, false); //always make sure this value matches
                });
                if (this.hasEffect(SEEffects.DIRTY.get())) {
                    this.removeEffect(SEEffects.DIRTY.get());
                }
                this.playSound(SoundEvents.BRUSH_GENERIC, 0.5f, 1f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (itemstack.is(SEItems.GRAIN_FEED.get())) {
                this.livestockOverhaulScraps$hungryTick = 0;
                if (this.isHungry()) {
                    this.setHungry(false);
                }
                if (this.hasEffect(SEEffects.HUNGER.get())) {
                    this.removeEffect(SEEffects.HUNGER.get());
                }
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.HORSE_EAT, 0.5f, 1f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else if (itemstack.is(SEItems.HEARTY_GRAIN_FEED.get())) {
                this.livestockOverhaulScraps$hungryTick = 0;
                if (this.isHungry()) {
                    this.setHungry(false);
                }
                if (this.hasEffect(SEEffects.HUNGER.get())) {
                    this.removeEffect(SEEffects.HUNGER.get());
                }
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 12000, 0, false, false));
                this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 12000, 0, false, false));
                this.setSickChanceMod(livestockOverhaulScraps$becomeSickChanceMod - 25);
                livestockOverhaulScraps$becomeSickChance = livestockOverhaulScraps$becomeSickChanceMod;
                if (livestockOverhaulScraps$becomeSickChance < 0) {
                    livestockOverhaulScraps$becomeSickChance = 0;
                }
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.playSound(SoundEvents.HORSE_EAT, 0.5f, 1f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (itemstack.is(SEItems.HEARTWORM_MEDICINE.get())) {
                livestockOverhaulScraps$heartwormMedTick = ScrapsExtrasCommonConfig.HEARTWORM_MED_GRACE.get();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (itemstack.is(SEItems.HOOF_PICK.get())) {
                livestockOverhaulScraps$hoofPickTick = ScrapsExtrasCommonConfig.HOOF_PICK_GRACE.get();
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (itemstack.is(Items.SHEARS) && player.isShiftKeyDown()) {
                if (this.getCapability(SECapabilities.HALTER_CAPABILITY).isPresent()) {
                    HalterCapabilityInterface halterCapabilityInterface = this.getCapability(SECapabilities.HALTER_CAPABILITY).orElse(null);
                    HalterColorCapabilityInterface halterColorCapabilityInterface = this.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).orElse(null);
                    if (halterCapabilityInterface.hasHalter()) {
                        halterCapabilityInterface.setHaltered(false);
                        SyncHalterLayerPacket.syncToTracking(this, false);
                        DyeColor dyeColor = DyeColor.byId(halterColorCapabilityInterface.getHalterColor().getId());
                        spawnAtLocation(SEItems.HALTERS.get(dyeColor).get());
                        this.playSound(SoundEvents.SHEEP_SHEAR, 0.5f, 1f);
                        return InteractionResult.sidedSuccess(this.level().isClientSide);
                    }
                }

                if (this.isEquine(this)) {
                    AbstractOMount equine = this;
                    equine.setFlowerType(0);
                    equine.setFlowerItem(Items.AIR.getDefaultInstance());
                }

                if (this.hasChest() && this.isOwnedBy(player)) {
                    this.dropEquipment();
                    this.inventory.removeAllItems();
                    this.setChest(false);
                    this.playChestEquipsSound();

                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }

            if (item instanceof HalterItem) {
                HalterItem halterItem = (HalterItem)item;
                this.getCapability(SECapabilities.HALTER_CAPABILITY).ifPresent(cap -> {
                    cap.setHaltered(true);
                    SyncHalterLayerPacket.syncToTracking(this, true);
                });
                DyeColor dyecolor = halterItem.getDyeColor();
                this.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).ifPresent(cap -> {
                    cap.setHalterColor(dyecolor);
                    SyncHalterColorPacket.syncToTracking(this, dyecolor);
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addData(CompoundTag tag, CallbackInfo ci) {
        super.addAdditionalSaveData(tag);
        tag.putInt("DirtyTick", this.livestockOverhaulScraps$dirtyTick);
        tag.putInt("MeanTick", this.livestockOverhaulScraps$beMeanTick);
        tag.putInt("HungerTick", this.livestockOverhaulScraps$hungryTick);
        tag.putInt("SickChance", this.livestockOverhaulScraps$becomeSickChance);
        tag.putInt("SickChanceMod", this.livestockOverhaulScraps$becomeSickChanceMod);
        tag.putInt("SickTick", this.livestockOverhaulScraps$sickTick);
        tag.putInt("SaddleSoreTick", this.livestockOverhaulScraps$saddleSoreTick);
        tag.putInt("RainRotTick", this.livestockOverhaulScraps$rainRotTick);
        tag.putInt("HeartwormMedTick", this.livestockOverhaulScraps$heartwormMedTick);
        tag.putInt("HoofPickTick", this.livestockOverhaulScraps$hoofPickTick);
//        tag.putInt("GestationTick", this.livestockOverhaulScraps$pregnantTick);
        tag.putBoolean("SleepingAsLeader", this.isSleepingAsLeader());
        tag.putBoolean("Hungry", this.isHungry());
//        tag.putBoolean("Pregnant", this.isPregnant());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DirtyTick")) {this.livestockOverhaulScraps$dirtyTick = tag.getInt("DirtyTick");}
        if (tag.contains("MeanTick")) {this.livestockOverhaulScraps$beMeanTick = tag.getInt("MeanTick");}
        if (tag.contains("HungerTick")) {this.livestockOverhaulScraps$hungryTick = tag.getInt("HungerTick");}
        if (tag.contains("SickChance")) {this.livestockOverhaulScraps$becomeSickChance = tag.getInt("SickChance");}
        if (tag.contains("SickChanceMod")) {this.livestockOverhaulScraps$becomeSickChanceMod = tag.getInt("SickChanceMod");}
        if (tag.contains("SickTick")) {this.livestockOverhaulScraps$sickTick = tag.getInt("SickTick");}
        if (tag.contains("SaddleSoreTick")) {this.livestockOverhaulScraps$saddleSoreTick = tag.getInt("SaddleSoreTick");}
        if (tag.contains("RainRotTick")) {this.livestockOverhaulScraps$rainRotTick = tag.getInt("RainRotTick");}
        if (tag.contains("HeartwormMedTick")) {this.livestockOverhaulScraps$heartwormMedTick = tag.getInt("HeartwormMedTick");}
        if (tag.contains("HoofPickTick")) {this.livestockOverhaulScraps$hoofPickTick = tag.getInt("HoofPickTick");}
//        if (tag.contains("GestationTick")) {this.livestockOverhaulScraps$pregnantTick = tag.getInt("GestationTick");}
        if (tag.contains("SleepingAsLeader")) this.setSleepingAsLeader(tag.getBoolean("SleepingAsLeader"));
        if (tag.contains("Hungry")) this.setHungry(tag.getBoolean("Hungry"));
//        if (tag.contains("Pregnant")) this.setPregnant(tag.getBoolean("Pregnant"));
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        OHorse self = (OHorse) (Object) this;
        if (self.getClass() == OHorse.class || self.getClass() == Unicorn.class) {
            CompoundTag nbt = this.getPersistentData();
            if (!nbt.getBoolean("loextras_initialized")) {
                BaseImmunityHelper.setBaseImmunity(this);
                BaseTraitHelper.setBaseTrait(this, true);
                nbt.putBoolean("loextras_initialized", true);
            }
        }
    }

    @Unique
    public void setTraitByBreedType() {
        int trait = random.nextInt(Trait.values().length);

        if (this.isDraftBreed()) { //more likely to have strong and docile traits
            if (random.nextDouble() <= 0.15) {
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                });
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {0, 5, 6, 10};
                int randomIndex = new Random().nextInt(traits.length);
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(traits[randomIndex]);
                    SyncTraitPacket.syncToTracking(this, traits[randomIndex]);
                });
            }
        } else if (this.isWarmbloodedBreed()) { //more likely to have athletic traits
            if (random.nextDouble() <= 0.15) {
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                });
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {3, 4, 6, 8};
                int randomIndex = new Random().nextInt(traits.length);
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(traits[randomIndex]);
                    SyncTraitPacket.syncToTracking(this, traits[randomIndex]);
                });
            }
        } else if (this.isRacingBreed()) { //more likely to have high-energy traits
            if (random.nextDouble() <= 0.15) {
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                });
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {2, 5, 4, 12};
                int randomIndex = new Random().nextInt(traits.length);
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(traits[randomIndex]);
                    SyncTraitPacket.syncToTracking(this, traits[randomIndex]);
                });
            }
        } else if (this.isPonyBreed()) { //more likely to have docile traits
            if (random.nextDouble() <= 0.15) {
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                });
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {1, 4, 6, 9};
                int randomIndex = new Random().nextInt(traits.length);
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(traits[randomIndex]);
                    SyncTraitPacket.syncToTracking(this, traits[randomIndex]);
                });
            }
        } else if (this.isStockBreed()) { //more likely to have all-rounder traits
            if (random.nextDouble() <= 0.15) {
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                });
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {1, 2, 3, 7};
                int randomIndex = new Random().nextInt(traits.length);
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(traits[randomIndex]);
                    SyncTraitPacket.syncToTracking(this, traits[randomIndex]);
                });
            }
        }
    }

    @Inject(method = "registerControllers", at = @At("TAIL"), remap = false, cancellable = true)
    public void registerControllersTail(AnimatableManager.ControllerRegistrar controllers, CallbackInfo ci) {
        OHorse self = (OHorse) (Object) this;
        ci.cancel();
        controllers.add(new AnimationController<>(self, "swimController", 2, this::swimmingPredicate));
        controllers.add(new AnimationController<>(self, "sleepController", 2, this::sleepingPredicate));
        ci.cancel();
    }

    private <T extends GeoAnimatable> PlayState sleepingPredicate(AnimationState<T> tAnimationState) {
        double x = this.getX() - this.xo;
        double z = this.getZ() - this.zo;
        boolean isMoving = (x * x + z * z) > 0.0001;

        AnimationController<T> controller = tAnimationState.getController();

        SleepingCapabilityInterface sleepingCap = null;
        if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
            sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
        }

        if (!isVehicle() && !isMoving && !this.isGroundTied() && sleepingCap != null && sleepingCap.isSleeping()) {
            if (this.isSleepingAsLeader()) {
                controller.setAnimation(RawAnimation.begin().then("relax", Animation.LoopType.LOOP));
            } else {
                controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
            }
            controller.setAnimationSpeed(1.0);
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState swimmingPredicate(AnimationState<T> tAnimationState) {
        double x = this.getX() - this.xo;
        double z = this.getZ() - this.zo;
        boolean isMoving = (x * x + z * z) > 0.0001;
        double movementSpeed = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
        double animationSpeed = Math.max(0.1, movementSpeed);

        AnimationController<T> controller = tAnimationState.getController();

        if (this.isInWater() && !this.onGround()) {
            controller.setAnimation(RawAnimation.begin().then("swim", Animation.LoopType.LOOP));
            if (isMoving) {
                controller.setAnimationSpeed(Math.max(0.1, 0.65 * controller.getAnimationSpeed() + animationSpeed));
            } else {
                controller.setAnimationSpeed(Math.max(0.1, 0.60 * controller.getAnimationSpeed() + animationSpeed));
            }
        } else {
            return PlayState.STOP;
        }

        return PlayState.CONTINUE;
    }

    @Inject(method = "canMate", at = @At("HEAD"), cancellable = true)
    public void canMate(Animal animal, CallbackInfoReturnable<Boolean> cir) {
        if (this.isHungry() || livestockOverhaulScraps$pregnantTick > 0) {
            cir.setReturnValue(false);
        }
    }

    /**
     * @author DragN0007
     * @reason why are you asking me this on my own fucking code. i hate robots
     */
    @Overwrite
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        AbstractOMount foal;
        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

        if (ageableMob instanceof ODonkey partnerDonkey) {
            foal = EntityTypes.O_MULE_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface partnerimmunityCap = partnerDonkey.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface foalimmunityCap = foal.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partnerDonkey.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            TraitCapabilityInterface foaltraitCap = foal.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int overlayChance = this.random.nextInt(100);
            int overlay;
            if (overlayChance < ((100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get()) / 2)) {
                overlay = this.getOverlayVariant();
            } else if (overlayChance < (100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get())) {
                overlay = partnerDonkey.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(EquineMarkingOverlay.values().length);
            }
            foal.setOverlayVariant(overlay);
            foal.setVariant(random.nextInt(OMuleModel.Variant.values().length));

            if (this.isStockBreed() || this.isWarmbloodedBreed() || this.isRacingBreed()) {
                foal.setBreed(0);
            }
            if (this.isPonyBreed()) {
                foal.setBreed(1);
            }
            if (this.isDraftBreed()) {
                foal.setBreed(2);
            }

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else {
                ((ITraitByBreedTypeHolder) foal).setTraitByBreedType();
            }

            int immunityChance = this.random.nextInt(100);
            int immunity;
            if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                immunity = immunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                immunity = partnerimmunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else {
                int baseImmunity = random.nextInt(1, 100);
                foalimmunityCap.setImmunity(random.nextInt(baseImmunity));
                SyncImmunityPacket.syncToTracking(foal, random.nextInt(baseImmunity));
            }

            BabyTraitHelper.setTraitEffect(foal);
            ((ISickModHolder) foal).setSickChance(100 - foalimmunityCap.getImmunity());
            if (immunityCap.getImmunity() > 100) {
                immunityCap.setImmunity(100);
                SyncImmunityPacket.syncToTracking(this, 100);
            } else if (immunityCap.getImmunity() < 1) {
                immunityCap.setImmunity(1);
                SyncImmunityPacket.syncToTracking(this, 1);
            }

            foal.setGender(random.nextInt(Gender.values().length));

        } else {
            OHorse partner = (OHorse) ageableMob;
            foal = EntityTypes.O_HORSE_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface partnerimmunityCap = partner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface foalimmunityCap = foal.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partner.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            TraitCapabilityInterface foaltraitCap = foal.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int breedChance = this.random.nextInt(100);
            int breed;
            if (breedChance < ((100 - LivestockOverhaulCommonConfig.BREED_CHANCE.get()) / 2)) {
                breed = this.getBreed();
            } else if (breedChance < (100 - LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
                breed = partner.getBreed();
            } else {
                if (!ModList.get().isLoaded("deadlydinos")) {
                    int[] breeds = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21};
                    int randomIndex = new Random().nextInt(breeds.length);
                    breed = (breeds[randomIndex]);
                } else {
                    breed = this.random.nextInt(HorseBreed.values().length);
                }
            }
            foal.setBreed(breed);

            if (!(breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
                int variantChance = this.random.nextInt(100);
                int variant;
                if (variantChance < ((100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get()) / 2)) {
                    variant = this.getVariant();
                } else if (variantChance < (100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get())) {
                    variant = partner.getVariant();
                } else {
                    variant = this.random.nextInt(OHorseModel.Variant.values().length);
                }
                foal.setVariant(variant);
            } else if (breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get() && random.nextDouble() < 0.5) {
                ((OHorse) foal).setColorByBreed();
            }

            if (!(breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
                int overlayChance = this.random.nextInt(100);
                int overlay;
                if (overlayChance < ((100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get()) / 2)) {
                    overlay = this.getOverlayVariant();
                } else if (overlayChance < (100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get())) {
                    overlay = partner.getOverlayVariant();
                } else {
                    overlay = this.random.nextInt(EquineMarkingOverlay.values().length);
                }
                foal.setOverlayVariant(overlay);
            } else if (breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get() && random.nextDouble() < 0.5) {
                ((OHorse) foal).setMarkingByBreed();
            }

            int eyeColorChance = this.random.nextInt(100);
            int eyes;
            if (eyeColorChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                eyes = this.getEyeVariant();
            } else if (eyeColorChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                eyes = partner.getEyeVariant();
            } else {
                eyes = this.random.nextInt(EquineEyeColorOverlay.values().length);
            }
            ((OHorse) foal).setEyeVariant(eyes);

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else {
                ((ITraitByBreedTypeHolder) foal).setTraitByBreedType();
            }

            int immunityChance = this.random.nextInt(100);
            int immunity;
            if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                immunity = immunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                immunity = partnerimmunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else {
                int baseImmunity = random.nextInt(1, 100);
                foalimmunityCap.setImmunity(random.nextInt(baseImmunity));
                SyncImmunityPacket.syncToTracking(foal, random.nextInt(baseImmunity));
            }

            BabyTraitHelper.setTraitEffect(foal);
            ((ISickModHolder) foal).setSickChance(100 - foalimmunityCap.getImmunity());
            if (immunityCap.getImmunity() > 100) {
                immunityCap.setImmunity(100);
                SyncImmunityPacket.syncToTracking(this, 100);
            } else if (immunityCap.getImmunity() < 1) {
                immunityCap.setImmunity(1);
                SyncImmunityPacket.syncToTracking(this, 1);
            }

            foal.setGender(random.nextInt(Gender.values().length));
            ((OHorse) foal).setFeatheringByBreed();
            ((OHorse) foal).setManeType(3);
            ((OHorse) foal).setTailType(2);

            if (this.random.nextInt(3) >= 1) {
                ((OHorse) foal).generateRandomOHorseJumpStrength();

                int betterSpeed = (int) Math.max(partner.getSpeed(), this.random.nextInt(10) + 20);
                foal.setSpeed(betterSpeed);

                int betterHealth = (int) Math.max(partner.getHealth(), this.random.nextInt(20) + 40);
                foal.setHealth(betterHealth);

                //generate random stats of the breed standard of the baby if the breed is random
            } else if (breedChance == 0) {
                ((OHorse) foal).randomizeOHorseAttributes();
            }
        }

//        if (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
//            if (this.isFemale()) {
//                livestockOverhaulScraps$pregnantTick = ScrapsExtrasCommonConfig.GESTATION_TICK.get();
//            }
//        }

        CompoundTag nbt = this.getPersistentData();
        if (!nbt.getBoolean("loextras_initialized")) {
            nbt.putBoolean("loextras_initialized", true);
        }

        this.setOffspringAttributes(ageableMob, foal);
        return foal;
    }

//    @Inject(method = "aiStep", at = @At("HEAD"))
//    protected void aiStep(CallbackInfo ci) {
//        if (!this.level().isClientSide) {
//
//            if (ScrapsExtrasCommonConfig.GESTATION.get()) {
//                if (this.livestockOverhaulScraps$pregnantTick > 0) {
//                    livestockOverhaulScraps$pregnantTick--;
//                    if (this.livestockOverhaulScraps$pregnantTick <= 0) {
//                        this.spawnGestationChildFromBreeding();
//                    }
//                }
//            }
//        }
//    }
//
//    @Unique
//    private void spawnGestationChildFromBreeding() {
//        Animal parent = this;
//        AgeableMob baby = parent.getBreedOffspring((ServerLevel) this.level(), parent);
//        baby.setBaby(true);
//        baby.moveTo(this.getX(), this.getY(), this.getZ());
//        this.level().addFreshEntity(baby);
//        this.livestockOverhaulScraps$pregnantTick = 0;
//    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void dropCustomDeathLoot(DamageSource p_33574_, int p_33575_, boolean p_33576_) {
        super.dropCustomDeathLoot(p_33574_, p_33575_, p_33576_);
        Random random = new Random();

        if (!LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get() && !ModList.get().isLoaded("tfc") && !ScrapsExtrasCommonConfig.BUTCHERING.get()) {
            if (this.isDraftBreed()) {
                if (random.nextDouble() < 0.40) {
                    this.spawnAtLocation(new ItemStack(LOItems.HORSE.get(), 2), 0F);
                    this.spawnAtLocation(new ItemStack(Items.LEATHER, 2), 0F);
                } else if (random.nextDouble() > 0.40) {
                    this.spawnAtLocation(new ItemStack(LOItems.HORSE.get(), 1), 0F);
                    this.spawnAtLocation(new ItemStack(Items.LEATHER, 1), 0F);
                }
            }

            if (this.isWarmbloodedBreed()) {
                if (random.nextDouble() < 0.20) {
                    this.spawnAtLocation(new ItemStack(LOItems.HORSE.get(), 1), 0F);
                    this.spawnAtLocation(new ItemStack(Items.LEATHER, 1), 0F);
                }
            }

            if (this.isStockBreed()) {
                if (random.nextDouble() < 0.10) {
                    this.spawnAtLocation(new ItemStack(LOItems.HORSE.get(), 1), 0F);
                    this.spawnAtLocation(new ItemStack(Items.LEATHER, 1), 0F);
                }
            }

            if (ModList.get().isLoaded("create")) {
                ResourceLocation resourceLocation = new ResourceLocation("create", "superglue");
                Item createSuperglue = ForgeRegistries.ITEMS.getValue(resourceLocation);
                if (random.nextDouble() < 0.25) {
                    this.spawnAtLocation(createSuperglue.getDefaultInstance());
                }
            }
        }
    }

}
