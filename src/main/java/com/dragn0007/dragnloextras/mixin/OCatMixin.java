package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.SyncImmunityPacket;
import com.dragn0007.dragnloextras.network.SyncTraitPacket;
import com.dragn0007.dragnloextras.util.*;
import com.dragn0007.dragnpets.entities.POEntityTypes;
import com.dragn0007.dragnpets.entities.ai.DogFollowPackLeaderGoal;
import com.dragn0007.dragnpets.entities.cat.CatMarkingOverlay;
import com.dragn0007.dragnpets.entities.cat.OCat;
import com.dragn0007.dragnpets.entities.cat.OCatEyeLayer;
import com.dragn0007.dragnpets.entities.cat.OCatModel;
import com.dragn0007.dragnpets.entities.dog.DogBreed;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import java.util.Random;

@Mixin(OCat.class)
public abstract class OCatMixin extends TamableAnimal implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISickModHolder {

    @Shadow(remap = false) public abstract int getVariant();
    @Shadow(remap = false) public abstract int getOverlayVariant();
    @Shadow(remap = false) public abstract int getBreed();
    @Shadow(remap = false) public abstract boolean isWagging();
    @Shadow(remap = false) public abstract int getEyes();

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

    public OCatMixin(EntityType<? extends OCatMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OCat self = (OCat) (Object) this;
        this.goalSelector.addGoal(0, new SleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
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
                        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof DogFollowPackLeaderGoal);
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

    @Inject(method = "mobInteract", at = @At("TAIL"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (!this.level().isClientSide) {
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
        CompoundTag nbt = this.getPersistentData();
        if (!nbt.getBoolean("loextras_initialized")) {
            BaseImmunityHelper.setBaseImmunity(this);
            BaseTraitHelper.setBaseTrait(this, false);
            nbt.putBoolean("loextras_initialized", true);
        }
    }

    @Inject(method = "predicate", at = @At("HEAD"), remap = false, cancellable = true)
    private <T extends GeoAnimatable> void predicate(AnimationState<T> tAnimationState, CallbackInfoReturnable<PlayState> cir) {
        double currentSpeed = this.getDeltaMovement().lengthSqr();
        double speedThreshold = 0.02;

        AnimationController<T> controller = tAnimationState.getController();

        SleepingCapabilityInterface sleepingCap = null;
        if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
            sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
        }

        if (tAnimationState.isMoving()) {
            if (currentSpeed > speedThreshold) {
                controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.5);
            } else {
                controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.5);
            }
        } else {
            if (sleepingCap != null && sleepingCap.isSleeping()) {
                controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
            } else if (isInSittingPose()) {
                controller.setAnimation(RawAnimation.begin().then("sit", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
            } else if (this.isWagging()) {
                controller.setAnimation(RawAnimation.begin().then("flick_tail", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
            } else {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }
        }
        cir.setReturnValue(PlayState.CONTINUE);
    }

    @Inject(method = "getBreedOffspring", at = @At("TAIL"))
    public void getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob, CallbackInfoReturnable<AgeableMob> cir) {
        OCat kitten;
        OCat partner = (OCat) ageableMob;
        kitten = POEntityTypes.O_CAT_ENTITY.get().create(serverLevel);

        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
        ImmunityCapabilityInterface partnerimmunityCap = partner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface partnertraitCap = partner.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
        ImmunityCapabilityInterface kittenimmunityCap = kitten.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface kittentraitCap = kitten.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

        int traitChance = this.random.nextInt(100);
        int trait;
        if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
            trait = traitCap.getTrait();
            kittentraitCap.setTrait(trait);
            SyncTraitPacket.syncToTracking(kitten, trait);
        } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
            trait = partnertraitCap.getTrait();
            kittentraitCap.setTrait(trait);
            SyncTraitPacket.syncToTracking(kitten, trait);
        } else {
            trait = random.nextInt(Trait.values().length);
            kittentraitCap.setTrait(trait);
            SyncTraitPacket.syncToTracking(kitten, trait);
        }

        int immunityChance = this.random.nextInt(100);
        int immunity;
        if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
            immunity = immunityCap.getImmunity();
            if (random.nextDouble() < 0.25) {
                kittenimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                SyncImmunityPacket.syncToTracking(kitten, immunity);
            } else {
                kittenimmunityCap.setImmunity(immunity);
                SyncImmunityPacket.syncToTracking(kitten, immunity);
            }
        } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
            immunity = partnerimmunityCap.getImmunity();
            if (random.nextDouble() < 0.25) {
                kittenimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                SyncImmunityPacket.syncToTracking(kitten, immunity);
            } else {
                kittenimmunityCap.setImmunity(immunity);
                SyncImmunityPacket.syncToTracking(kitten, immunity);
            }
        } else {
            int baseImmunity = random.nextInt(1, 100);
            kittenimmunityCap.setImmunity(random.nextInt(baseImmunity));
            SyncImmunityPacket.syncToTracking(kitten, random.nextInt(baseImmunity));
        }

        BabyTraitHelper.setTraitEffect(kitten);
        ((ISickModHolder) kitten).setSickChance(100 - kittenimmunityCap.getImmunity());
        if (immunityCap.getImmunity() > 100) {
            immunityCap.setImmunity(100);
            SyncImmunityPacket.syncToTracking(this, 100);
        } else if (immunityCap.getImmunity() < 1) {
            immunityCap.setImmunity(1);
            SyncImmunityPacket.syncToTracking(this, 1);
        }

        CompoundTag nbt = this.getPersistentData();
        nbt.putBoolean("loextras_initialized", true);
    }

}
