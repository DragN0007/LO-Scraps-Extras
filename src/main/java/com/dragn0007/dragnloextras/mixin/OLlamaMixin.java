package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.llama.OLlama;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.SleepingCapabilityInterface;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

@Mixin(OLlama.class)
public abstract class OLlamaMixin extends Animal {

    @Shadow(remap = false) public abstract void registerGoals();
    @Shadow(remap = false) public abstract boolean isSheared();
    @Shadow(remap = false) public abstract void dropWoolByColorAndMarking();

    @Shadow public abstract int getWooly();

    public OLlamaMixin(EntityType<? extends OLlamaMixin> entityType, Level level) {
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
            return OLlama.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return OLlama.VANILLA_LOOT_TABLE;
        } else {
            return OLlama.LOOT_TABLE;
        }
    }

    @Override
    public void setBaby(boolean p_146756_) {
        this.setAge(p_146756_ ? -ScrapsExtrasCommonConfig.MEDIUM_GROWTH_TIME.get() : 0);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OLlama self = (OLlama) (Object) this;
        this.goalSelector.addGoal(0, new SleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                (entity instanceof Player || entity instanceof Villager || entity instanceof Animal) && !this.isBaby() && this.hasEffect(SEEffects.RABIES.get())
        ));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
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

            if (random.nextDouble() <= abrasionChance) {
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
        double movementSpeed = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
        double animationSpeed = Math.max(0.1, movementSpeed);
        double x = this.getX() - this.xo;
        double z = this.getZ() - this.zo;
        double currentSpeed = this.getDeltaMovement().lengthSqr();
        double speedThreshold = 0.015;
        boolean isMoving = (x * x + z * z) > 0.0001;

        AnimationController<T> controller = tAnimationState.getController();

        SleepingCapabilityInterface sleepingCap = null;
        if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
            sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
        }

        if (isMoving) {
            if (currentSpeed > speedThreshold) {
                controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(Math.max(0.1, 0.8 * controller.getAnimationSpeed() + animationSpeed));
            } else {
                if (sleepingCap != null && sleepingCap.isSleeping()) {
                    controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
                } else {
                    controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                    controller.setAnimationSpeed(Math.max(0.1, 0.82 * controller.getAnimationSpeed() + animationSpeed));
                }
            }
        } else {
            if (sleepingCap != null && sleepingCap.isSleeping()) {
                controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
            } else {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(1.0);
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
        Item hide;
        if (!this.isSheared()) {
            this.dropWoolByColorAndMarking();

            if (!ScrapsExtrasCommonConfig.BUTCHERING.get()) {
                if (!this.isSheared() && this.getWooly() == 1) {
                    if (ModList.get().isLoaded("tfc")) {
                        hide = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tfc", "large_sheepskin_hide"));
                        this.spawnAtLocation(new ItemStack(hide, 1), 0F);
                    }
                } else {
                    hide = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tfc", "large_raw_hide"));
                    this.spawnAtLocation(new ItemStack(hide, 1), 0F);
                }
            }
        }
    }

}
