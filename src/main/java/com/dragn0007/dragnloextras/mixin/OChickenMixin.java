package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.ai.SheepFollowHerdLeaderGoal;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.sheep.OSheep;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.SleepingCapabilityInterface;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.util.ISickModHolder;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
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

import java.util.Random;

@Mixin(OChicken.class)
public abstract class OChickenMixin extends Animal implements ISickModHolder {

    @Shadow(remap = false) public abstract void registerGoals();
    @Shadow(remap = false) public abstract boolean isExquisiteQuality();
    @Shadow(remap = false) public abstract boolean isFantasticQuality();
    @Shadow(remap = false) public abstract boolean isGreatQuality();
    @Shadow(remap = false) public abstract boolean isMeatBreed();
    @Shadow(remap = false) public abstract boolean isNormalBreed();

    public OChickenMixin(EntityType<? extends OChickenMixin> entityType, Level level) {
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
            return OChicken.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return OChicken.VANILLA_LOOT_TABLE;
        } else {
            return OChicken.LOOT_TABLE;
        }
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OChicken self = (OChicken) (Object) this;
        this.goalSelector.addGoal(0, new SleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) && !this.isBaby() && this.hasEffect(SEEffects.RABIES.get())
        ));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (ScrapsExtrasCommonConfig.SLEEPING.get()) {
                SleepingCapabilityInterface sleepingCap;
                if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
                    sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
                    if (sleepingCap != null && sleepingCap.isSleeping()) {
                        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof LookAtPlayerGoal);
                    }
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float dmg) {
        if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
            double abrasionChance = dmg / 10;

            if (random.nextDouble() <= 0.20) {
                this.addEffect(new MobEffectInstance(SEEffects.ABRASION.get(), ScrapsExtrasCommonConfig.INFECTION_TICK.get() + 20, 0, false, false));
            }

            if (ScrapsExtrasCommonConfig.RABIES.get() && random.nextDouble() <= 0.50) {
                if (damageSource.getEntity() instanceof Animal animal && animal.hasEffect(SEEffects.RABIES.get())) {
                    this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                }
            }
        }
        return super.hurt(damageSource, dmg);
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

        if (!this.onGround()) {
            controller.setAnimation(RawAnimation.begin().then("flap", Animation.LoopType.LOOP));
            controller.setAnimationSpeed(1.5);
        } else if (isMoving) {
            if (currentSpeed > speedThreshold) {
                controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.1);
            } else {
                controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.1);
            }
        } else {
            if (sleepingCap != null && sleepingCap.isSleeping()) {
                controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
            } else {
            controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            controller.setAnimationSpeed(0.8);
            }
        }
        cir.setReturnValue(PlayState.CONTINUE);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void dropCustomDeathLoot(DamageSource p_33574_, int p_33575_, boolean p_33576_) {
        super.dropCustomDeathLoot(p_33574_, p_33575_, p_33576_);
        Random random = new Random();

        if (!LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get() && !ModList.get().isLoaded("tfc") && ScrapsExtrasCommonConfig.BUTCHERING.get()) {
            if (this.isMeatBreed()) {
                if (random.nextDouble() < 0.40) {
                    this.spawnAtLocation(Items.CHICKEN, 2);
                    this.spawnAtLocation(Items.FEATHER, 2);
                } else if (random.nextDouble() > 0.40) {
                    this.spawnAtLocation(Items.CHICKEN);
                    this.spawnAtLocation(Items.FEATHER);
                }
            }

            if (this.isNormalBreed()) {
                if (random.nextDouble() < 0.15) {
                    this.spawnAtLocation(Items.CHICKEN);
                    this.spawnAtLocation(Items.FEATHER);
                }
            }

            if (LivestockOverhaulCommonConfig.QUALITY.get()) {
                if (this.isExquisiteQuality()) {
                    this.spawnAtLocation(Items.CHICKEN, 3);
                    this.spawnAtLocation(Items.FEATHER, 3);
                } else if (this.isFantasticQuality()) {
                    this.spawnAtLocation(Items.CHICKEN, 2);
                    this.spawnAtLocation(Items.FEATHER, 2);
                } else if (this.isGreatQuality()) {
                    this.spawnAtLocation(Items.CHICKEN);
                    this.spawnAtLocation(Items.FEATHER);
                }
            }

        }
    }

}
