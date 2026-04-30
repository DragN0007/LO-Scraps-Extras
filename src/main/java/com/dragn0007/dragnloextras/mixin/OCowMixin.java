package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.cow.CowBreed;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.cow.OCowModel;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.BovineMarkingOverlay;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.DirtyCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.ImmunityCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.SleepingCapabilityInterface;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.SyncImmunityPacket;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModList;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@Mixin(OCow.class)
public abstract class OCowMixin extends AbstractOMount implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISleepAsLeaderHolder, ISickModHolder {

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);
    @Shadow public abstract int getHornVariant();
    @Shadow public abstract int getQuality();
    @Shadow public abstract boolean isHarnessed();
    @Shadow public abstract boolean isMeatBreed();
    @Shadow public abstract boolean isNormalBreed();
    @Shadow public abstract boolean isExquisiteQuality();
    @Shadow public abstract boolean isFantasticQuality();
    @Shadow public abstract boolean isGreatQuality();

    @Shadow public abstract void registerGoals();

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
    @Unique int livestockOverhaulScraps$becomeSickRand = random.nextInt(100);

    public OCowMixin(EntityType<? extends OCowMixin> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @author who do you think
     * @reason cuz i can
     */
    @Overwrite
    public ResourceLocation getDefaultLootTable() {
        if (ScrapsExtrasCommonConfig.BUTCHERING.get()) {
            return BuiltInLootTables.EMPTY;
        } else if (ModList.get().isLoaded("tfc")) {
            return OCow.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return OCow.VANILLA_LOOT_TABLE;
        } else {
            return OCow.LOOT_TABLE;
        }
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OCow self = (OCow) (Object) this;
        this.goalSelector.addGoal(0, new SleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) && !this.isBaby() && this.hasEffect(SEEffects.RABIES.get())
        ));
    }

    @Unique public int livestockOverhaulScraps$sickTick;
    @Unique public int livestockOverhaulScraps$sickModDissipateTick = 72000;
    @Unique public int livestockOverhaulScraps$hoofPickTick;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {

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
                        if (target.hasEffect(SEEffects.RABIES.get()) && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                            this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        }
                    }
                }

                if (ScrapsExtrasCommonConfig.HOOF_ABSCESS.get()) {
                    if (livestockOverhaulScraps$hoofPickTick >= 0)
                        livestockOverhaulScraps$hoofPickTick--;

                    if (livestockOverhaulScraps$hoofPickTick < ScrapsExtrasCommonConfig.HOOF_PICK_GRACE.get() &&
                            livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                        if (random.nextDouble() <= 0.10) {
                            this.addEffect(new MobEffectInstance(SEEffects.HOOF_ABSCESS.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float dmg) {
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
                    if (damageSource.getEntity() instanceof Animal animal && animal.hasEffect(SEEffects.RABIES.get())) {
                        this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    }
                }
            }
        }
        return super.hurt(damageSource, dmg);
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if (itemstack.is(SEItems.HOOF_PICK.get())) {
            livestockOverhaulScraps$hoofPickTick = ScrapsExtrasCommonConfig.HOOF_PICK_GRACE.get();
            cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addData(CompoundTag tag, CallbackInfo ci) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SickChance", this.livestockOverhaulScraps$becomeSickChance);
        tag.putInt("SickChanceMod", this.livestockOverhaulScraps$becomeSickChanceMod);
        tag.putInt("SickTick", this.livestockOverhaulScraps$sickTick);
        tag.putInt("HoofPickTick", this.livestockOverhaulScraps$hoofPickTick);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SickChance")) {this.livestockOverhaulScraps$becomeSickChance = tag.getInt("SickChance");}
        if (tag.contains("SickChanceMod")) {this.livestockOverhaulScraps$becomeSickChanceMod = tag.getInt("SickChanceMod");}
        if (tag.contains("SickTick")) {this.livestockOverhaulScraps$sickTick = tag.getInt("SickTick");}
        if (tag.contains("HoofPickTick")) {this.livestockOverhaulScraps$hoofPickTick = tag.getInt("HoofPickTick");}
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        OCow self = (OCow) (Object) this;
        if (self.getClass() == OCow.class) {
            BaseImmunityHelper.setBaseImmunity(this);
        }
    }

    @Inject(method = "predicate", at = @At("HEAD"), remap = false, cancellable = true)
    private <T extends GeoAnimatable> void predicate(AnimationState<T> tAnimationState, CallbackInfoReturnable<PlayState> cir) {
        double x = this.getX() - this.xo;
        double z = this.getZ() - this.zo;
        boolean isMoving = (x * x + z * z) > 0.0001;
        double currentSpeed = this.getDeltaMovement().lengthSqr();
        double speedThreshold = 0.02;

        AnimationController<T> controller = tAnimationState.getController();

        SleepingCapabilityInterface sleepingCap = null;
        if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
            sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
        }

        if (this.isHarnessed() && this.isVehicle()) {
            controller.setAnimation(RawAnimation.begin().then("buck", Animation.LoopType.LOOP));
            controller.setAnimationSpeed(1.3);
        } else {
            if (isMoving) {
                if (currentSpeed > speedThreshold) {
                    if (this.isAggressive()) {
                        controller.setAnimation(RawAnimation.begin().then("charge", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(1.1);
                    } else {
                        controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(1.1);
                    }
                } else {
                    controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                }
            } else {
                if (this.isAggressive()) {
                    controller.setAnimation(RawAnimation.begin().then("posture", Animation.LoopType.LOOP));
                } else if (sleepingCap != null && sleepingCap.isSleeping()) {
                    controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
                } else {
                    controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                }
            }
        }
        cir.setReturnValue(PlayState.CONTINUE);
    }

    /**
     * @author DragN0007
     * @reason why are you asking me this on my own fucking code. i hate robots
     */
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        OCow calf;
        OCow partner = (OCow) ageableMob;
        calf = EntityTypes.O_COW_ENTITY.get().create(serverLevel);
        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        ImmunityCapabilityInterface partnerimmunityCap = partner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        ImmunityCapabilityInterface calfimmunityCap = calf.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);

        int breedChance = this.random.nextInt(100);
        int breed;
        if (breedChance < ((100 - LivestockOverhaulCommonConfig.BREED_CHANCE.get()) / 2)) {
            breed = this.getBreed();
        } else if (breedChance < (100 - LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
            breed = partner.getBreed();
        } else {
            breed = this.random.nextInt(CowBreed.Breed.values().length);
        }
        calf.setBreed(breed);

        if (!(breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
            int variantChance = this.random.nextInt(100);
            int variant;
            if (variantChance < ((100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get()) / 2)) {
                variant = this.getVariant();
            } else if (variantChance < (100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get())) {
                variant = partner.getVariant();
            } else {
                variant = this.random.nextInt(OCowModel.Variant.values().length);
            }
            calf.setVariant(variant);
        } else if (random.nextDouble() < 0.5) {
            calf.setColorByBreed();
        }

        if (!(breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
            int overlayChance = this.random.nextInt(100);
            int overlay;
            if (overlayChance < ((100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get()) / 2)) {
                overlay = this.getOverlayVariant();
            } else if (overlayChance < (100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get())) {
                overlay = partner.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(BovineMarkingOverlay.values().length);
            }
            calf.setOverlayVariant(overlay);
        } else if (random.nextDouble() < 0.5) {
            calf.setMarkingByBreed();
        }

        if (!(breedChance <= LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
            int hornsChance = this.random.nextInt(100);
            int hornType;
            if (hornsChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                hornType = this.getHornVariant();
            } else if (hornsChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                hornType = partner.getHornVariant();
            } else {
                hornType = this.random.nextInt(OCow.BreedHorns.values().length);
            }
            calf.setHornVariant(hornType);
        } else if (random.nextDouble() < 0.5) {
            calf.setHornsByBreed();
        }

        if (calf.getBreed() == 10) {
            calf.setGender(1);
        } else {
            calf.setGender(random.nextInt(OCow.Gender.values().length));
        }

        int immunityChance = this.random.nextInt(100);
        int immunity;
        if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
            immunity = immunityCap.getImmunity();
            if (random.nextDouble() < 0.25) {
                calfimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                SyncImmunityPacket.syncToTracking(calf, immunity);
            } else {
                calfimmunityCap.setImmunity(immunity);
                SyncImmunityPacket.syncToTracking(calf, immunity);
            }
        } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
            immunity = partnerimmunityCap.getImmunity();
            if (random.nextDouble() < 0.25) {
                calfimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                SyncImmunityPacket.syncToTracking(calf, immunity);
            } else {
                calfimmunityCap.setImmunity(immunity);
                SyncImmunityPacket.syncToTracking(calf, immunity);
            }
        } else {
            int baseImmunity = random.nextInt(1, 100);
            calfimmunityCap.setImmunity(random.nextInt(baseImmunity));
            SyncImmunityPacket.syncToTracking(calf, random.nextInt(baseImmunity));
        }

        if (LivestockOverhaulCommonConfig.QUALITY.get()) {
            int qual_avg = (this.getQuality() + partner.getQuality()) / 2;
            if (random.nextDouble() <= 0.05) {
                calf.setQuality(qual_avg + random.nextInt(50));
            } else if (random.nextDouble() >= 0.05 && random.nextDouble() <= 0.25) {
                calf.setQuality(qual_avg + random.nextInt(25));
            } else if (random.nextDouble() >= 0.25 && random.nextDouble() <= 0.60) {
                calf.setQuality(qual_avg + random.nextInt(10));
            } else {
                calf.setQuality(qual_avg + random.nextInt(5));
            }
        }

        if (calf.getQuality() > 100) {
            calf.setQuality(100);
        }

        calf.setAttackDamage();
        return calf;
    }

    @Inject(method = "dropCustomDeathLoot", at = @At("HEAD"), remap = false)
    public void dropCustomDeathLoot(DamageSource p_33574_, int p_33575_, boolean p_33576_, CallbackInfo ci) {
        super.dropCustomDeathLoot(p_33574_, p_33575_, p_33576_);
        if (ScrapsExtrasCommonConfig.BUTCHERING.get()) {
            return;
        }
    }

}
