package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.entity.ai.VaulterLeapAtTargetGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.SyncDirtyLayerPacket;
import com.dragn0007.dragnloextras.network.SyncImmunityPacket;
import com.dragn0007.dragnloextras.network.SyncSpikeCollarLayerPacket;
import com.dragn0007.dragnloextras.network.SyncTraitPacket;
import com.dragn0007.dragnloextras.util.*;
import com.dragn0007.dragnpets.entities.POEntityTypes;
import com.dragn0007.dragnpets.entities.ai.CanineFollowPackLeaderGoal;
import com.dragn0007.dragnpets.entities.dog.DogBase;
import com.dragn0007.dragnpets.entities.dog.ODog;
import com.dragn0007.dragnpets.entities.wolf.OWolf;
import com.dragn0007.dragnpets.entities.wolf.OWolfMarkingLayer;
import com.dragn0007.dragnpets.entities.wolf.OWolfModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mixin(OWolf.class)
public abstract class OWolfMixin extends TamableAnimal implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISickModHolder {

    //stop remapping my shit bitch i aint ask you to do all that. piss me off
    @Shadow(remap = false) public abstract int getVariant();
    @Shadow(remap = false) public abstract int getOverlayVariant();
    @Shadow(remap = false) public abstract boolean isWagging();

    @Shadow public abstract boolean isCollared();
    @Shadow public abstract InteractionResult mobInteract(Player player, InteractionHand hand);

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

    public OWolfMixin(EntityType<? extends OWolfMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OWolf self = (OWolf) (Object) this;
        this.goalSelector.addGoal(0, new SleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(3, new VaulterLeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) && !this.isBaby() && this.hasEffect(SEEffects.RABIES.get())
        ));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) &&
                        !this.isBaby() && this.hasEffect(SEEffects.MEAN.get()) && livestockOverhaulScraps$beMeanTick >= livestockOverhaulScraps$beMeanTargetTick
        ));
    }

    @Unique public int livestockOverhaulScraps$dirtyTick;
    @Unique public int livestockOverhaulScraps$hungryTick;
    @Unique public int livestockOverhaulScraps$beMeanTick;
    @Unique public int livestockOverhaulScraps$sickTick;
    @Unique public int livestockOverhaulScraps$sickModDissipateTick = 72000;
    @Unique public int livestockOverhaulScraps$heartwormMedTick;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {

            if (ScrapsExtrasCommonConfig.SLEEPING.get()) {
                SleepingCapabilityInterface sleepingCap;
                if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
                    sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
                    if (sleepingCap != null && sleepingCap.isSleeping()) {
                        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof LookAtPlayerGoal);
                        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof CanineFollowPackLeaderGoal);
                    }
                }
            }

            if (this.isTame()) {
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
                            if (random.nextDouble() <= 0.05) {
                                this.addEffect(new MobEffectInstance(SEEffects.MANGE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
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
                            if (random.nextDouble() <= 0.05) {
                                this.addEffect(new MobEffectInstance(SEEffects.HEARTWORMS.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            }
                        }
                    }
                }

                if (ScrapsExtrasCommonConfig.FEEDING_SYSTEM.get()) {
                    if (!this.isHungry()) {
                        livestockOverhaulScraps$hungryTick++;
                        if (livestockOverhaulScraps$hungryTick >= ScrapsExtrasCommonConfig.DOG_FEED_TICK.get()) {
                            this.setHungry(true);
                            this.addEffect(new MobEffectInstance(SEEffects.HUNGER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                            livestockOverhaulScraps$hungryTick = 0;
                        }
                    }
                }

                if (ScrapsExtrasCommonConfig.HYGIENE_SYSTEM.get()) {
                    if (this.isTame())
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

        if (ScrapsExtrasCommonConfig.SPIKE_COLLAR.get()) {
            if (damageSource.getEntity() instanceof Mob mob) {
                this.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                    if (cap.hasSpikeCollar()) {
                        if (mob instanceof OWolf) {
                            mob.hurt(this.damageSources().thorns(this), (float) 8 + this.random.nextInt(12));
                        } else {
                            mob.hurt(this.damageSources().thorns(this), (float) 2 + this.random.nextInt(8));
                        }
                        this.playSound(SoundEvents.THORNS_HIT, 0.5F, 1.0F);
                    }
                });
            }
        }

        if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
            if (livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {

                double abrasionChance = dmg / 10;

                if (random.nextDouble() <= abrasionChance) {
                    this.addEffect(new MobEffectInstance(SEEffects.ABRASION.get(), ScrapsExtrasCommonConfig.INFECTION_TICK.get() + 20, 0, false, false));
                }

                if (ScrapsExtrasCommonConfig.RABIES.get()) {
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

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (!this.level().isClientSide) {
            if (itemstack.is(SEItems.BRUSH.get())) {
                livestockOverhaulScraps$dirtyTick = 0;
                this.getCapability(SECapabilities.DIRTY_CAPABILITY).ifPresent(cap -> {
                    cap.setDirty(false);
                    SyncDirtyLayerPacket.syncToTracking(this, false);
                });
                if (this.hasEffect(SEEffects.DIRTY.get())) {
                    this.removeEffect(SEEffects.DIRTY.get());
                }
                this.playSound(SoundEvents.BRUSH_GENERIC, 0.5f, 1f);
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
            } else if (itemstack.is(SEItems.COLLAR_SPIKES.get()) && this.isCollared()) {
                this.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                    cap.setSpikeCollared(true);
                    SyncSpikeCollarLayerPacket.syncToTracking(this, true);
                });
                this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 0.5f, 1f);
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
            } else if (itemstack.is(Tags.Items.SHEARS)) {
                this.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                    if (cap.hasSpikeCollar()) {
                        cap.setSpikeCollared(false);
                        SyncSpikeCollarLayerPacket.syncToTracking(this, false);
                    }
                });
                this.spawnAtLocation(SEItems.COLLAR_SPIKES.get());
                this.playSound(SoundEvents.SHEEP_SHEAR, 0.5f, 1f);
                cir.setReturnValue(super.mobInteract(player, hand));
//                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
            }

            if (this.isHungry() || this.hasEffect(SEEffects.HUNGER.get())) {
                if (itemstack.is(SEItems.KIBBLE.get())) {
                    this.setHungry(false);
                    if (this.hasEffect(SEEffects.HUNGER.get())) {
                        this.removeEffect(SEEffects.HUNGER.get());
                    }
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
                } else if (itemstack.is(SEItems.HEARTY_KIBBLE.get())) {
                    this.setHungry(false);
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
                    cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
                }
            }

            if (itemstack.is(SEItems.HEARTWORM_MEDICINE.get())) {
                livestockOverhaulScraps$heartwormMedTick = ScrapsExtrasCommonConfig.HEARTWORM_MED_GRACE.get();
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
            }
        }

//        cir.setReturnValue(super.mobInteract(player, hand));
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
        tag.putInt("HeartwormMedTick", this.livestockOverhaulScraps$heartwormMedTick);
        tag.putBoolean("Hungry", this.isHungry());
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
        if (tag.contains("HeartwormMedTick")) {this.livestockOverhaulScraps$heartwormMedTick = tag.getInt("HeartwormMedTick");}
        if (tag.contains("Hungry")) this.setHungry(tag.getBoolean("Hungry"));
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        Random random = new Random();
        BaseImmunityHelper.setBaseImmunity(this);
        BaseTraitHelper.setBaseTrait(this, false);
    }

    @Inject(method = "predicate", at = @At("HEAD"), remap = false, cancellable = true)
    private <T extends GeoAnimatable> void predicate(AnimationState<T> tAnimationState, CallbackInfoReturnable<PlayState> cir) {
        double currentSpeed = this.getDeltaMovement().lengthSqr();
        double speedThreshold = 0.015;

        AnimationController<T> controller = tAnimationState.getController();

        SleepingCapabilityInterface sleepingCap = null;
        if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
            sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
        }

        if (tAnimationState.isMoving()) {
            if (currentSpeed > speedThreshold) {
                controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.3);
            } else {
                controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.3);
            }
        } else {
            if (sleepingCap != null && sleepingCap.isSleeping()) {
                controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
            } else if (isInSittingPose()) {
                controller.setAnimation(RawAnimation.begin().then("sit", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
            } else if (this.isWagging()) {
                controller.setAnimation(RawAnimation.begin().then("wag", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
            } else {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }
        }
        cir.setReturnValue(PlayState.CONTINUE);
    }

    /**
     * @author DragN0007
     * @reason why are you asking me this on my own fucking code. i hate robots
     */
    @Overwrite
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        DogBase pup;
        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
        if (ageableMob instanceof ODog dogPartner) {
            pup = POEntityTypes.O_DOG_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface partnerimmunityCap = dogPartner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = dogPartner.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface pupimmunityCap = pup.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface puptraitCap = pup.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            ((ODog) pup).setBreed(25);

            int variantChance = this.random.nextInt(100);
            if (variantChance < 50) {
                ((ODog) pup).setVariant(dogPartner.getVariant());
            } else {
                ((ODog) pup).setColor();
            }

            int overlayChance = this.random.nextInt(100);
            if (overlayChance < 40) {
                ((ODog) pup).setOverlayVariant(dogPartner.getOverlayVariant());
            } else if (overlayChance < 80) {
                ((ODog) pup).setOverlayVariant(20);
            } else {
                ((ODog) pup).setMarking();
            }

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                puptraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(pup, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                puptraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(pup, trait);
            } else {
                ((ITraitByBreedTypeHolder) pup).setTraitByBreedType();
            }

            int immunityChance = this.random.nextInt(100);
            int immunity;
            if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                immunity = immunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    pupimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                } else {
                    pupimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                }
            } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                immunity = partnerimmunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    pupimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                } else {
                    pupimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                }
            } else {
                int baseImmunity = random.nextInt(1, 100);
                pupimmunityCap.setImmunity(random.nextInt(baseImmunity));
                SyncImmunityPacket.syncToTracking(pup, random.nextInt(baseImmunity));
            }

            BabyTraitHelper.setTraitEffect(pup);
            ((ODog) pup).setCropped(1);
            ((ODog) pup).setFluffChance();
            ((ODog) pup).setODogAttributes();

        } else {
            OWolf partner = (OWolf) ageableMob;
            pup = POEntityTypes.O_WOLF_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface partnerimmunityCap = partner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partner.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface pupimmunityCap = pup.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface puptraitCap = pup.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int variantChance = this.random.nextInt(100);
            int variant;
            if (variantChance < 40) {
                variant = this.getVariant();
            } else if (variantChance < 80) {
                variant = partner.getVariant();
            } else {
                variant = this.random.nextInt(OWolfModel.Variant.values().length);
            }
            ((OWolf) pup).setVariant(variant);

            int overlayChance = this.random.nextInt(100);
            int overlay;
            if (overlayChance < 40) {
                overlay = this.getOverlayVariant();
            } else if (overlayChance < 80) {
                overlay = partner.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(OWolfMarkingLayer.Overlay.values().length);
            }
            ((OWolf) pup).setOverlayVariant(overlay);

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                puptraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(pup, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                puptraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(pup, trait);
            } else {
                ((ITraitByBreedTypeHolder) pup).setTraitByBreedType();
            }

            int immunityChance = this.random.nextInt(100);
            int immunity;
            if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                immunity = immunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    pupimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                } else {
                    pupimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                }
            } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                immunity = partnerimmunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    pupimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                } else {
                    pupimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(pup, immunity);
                }
            } else {
                int baseImmunity = random.nextInt(1, 100);
                pupimmunityCap.setImmunity(random.nextInt(baseImmunity));
                SyncImmunityPacket.syncToTracking(pup, random.nextInt(baseImmunity));
            }
        }

        BabyTraitHelper.setTraitEffect(pup);
        pup.setGender(random.nextInt(DogBase.Gender.values().length));
        return pup;
    }

}
