package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.DirtyCapabilityInterface;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(OCow.class)
public abstract class OCowMixin extends AbstractOMount implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISleepAsLeaderHolder, ISickModHolder {

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);

    @Shadow public abstract @NotNull ResourceLocation getDefaultLootTable();

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

    @Inject(method = "getDefaultLootTable", at = @At("HEAD"), cancellable = true)
    public void getDefaultLootTable(CallbackInfoReturnable<ResourceLocation> cir) {
        if (ScrapsExtrasCommonConfig.BUTCHERING.get() && !LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            cir.setReturnValue(BuiltInLootTables.EMPTY);
        } else {
            super.getDefaultLootTable();
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
    @Unique public int livestockOverhaulScraps$heartwormMedTick;
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

                if (ScrapsExtrasCommonConfig.MANGE.get()) {
                    if (livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                        if (random.nextDouble() <= 0.05) {
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
                        if (random.nextDouble() <= 0.05) {
                            this.addEffect(new MobEffectInstance(SEEffects.RINGWORM.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        }
                    }
                }

                if (ScrapsExtrasCommonConfig.HEARTWORMS.get()) {
                    if (livestockOverhaulScraps$heartwormMedTick >= 0)
                        livestockOverhaulScraps$heartwormMedTick--;

                    if (livestockOverhaulScraps$heartwormMedTick < ScrapsExtrasCommonConfig.HEARTWORM_MED_GRACE.get() &&
                            livestockOverhaulScraps$sickTick >= 72000 && livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {
                        if (random.nextDouble() <= 0.01) {
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
                    if (damageSource.getEntity() instanceof Animal && random.nextDouble() <= 0.02) {
                        this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    }
                    if (damageSource.getEntity() instanceof Animal animal && animal.hasEffect(SEEffects.RABIES.get())) {
                        this.addEffect(new MobEffectInstance(SEEffects.RABIES.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    }
                }
            }
        }
        return super.hurt(damageSource, dmg);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (itemstack.is(SEItems.HEARTWORM_MEDICINE.get())) {
            livestockOverhaulScraps$heartwormMedTick = ScrapsExtrasCommonConfig.HEARTWORM_MED_GRACE.get();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (itemstack.is(SEItems.HOOF_PICK.get())) {
            livestockOverhaulScraps$hoofPickTick = ScrapsExtrasCommonConfig.HOOF_PICK_GRACE.get();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addData(CompoundTag tag, CallbackInfo ci) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SickChance", this.livestockOverhaulScraps$becomeSickChance);
        tag.putInt("SickChanceMod", this.livestockOverhaulScraps$becomeSickChanceMod);
        tag.putInt("SickTick", this.livestockOverhaulScraps$sickTick);
        tag.putInt("HeartwormMedTick", this.livestockOverhaulScraps$heartwormMedTick);
        tag.putInt("HoofPickTick", this.livestockOverhaulScraps$hoofPickTick);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("SickChance")) {this.livestockOverhaulScraps$becomeSickChance = tag.getInt("SickChance");}
        if (tag.contains("SickChanceMod")) {this.livestockOverhaulScraps$becomeSickChanceMod = tag.getInt("SickChanceMod");}
        if (tag.contains("SickTick")) {this.livestockOverhaulScraps$sickTick = tag.getInt("SickTick");}
        if (tag.contains("HeartwormMedTick")) {this.livestockOverhaulScraps$heartwormMedTick = tag.getInt("HeartwormMedTick");}
        if (tag.contains("HoofPickTick")) {this.livestockOverhaulScraps$hoofPickTick = tag.getInt("HoofPickTick");}
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
//        BaseImmunityHelper.setBaseImmunity(this);
    }

//    @Inject(method = "predicate", at = @At("HEAD"), remap = false, cancellable = true)
//    private <T extends GeoAnimatable> void predicate(AnimationState<T> tAnimationState, CallbackInfoReturnable<PlayState> cir) {
//        cir.setReturnValue(PlayState.CONTINUE);
//    }

    /**
     * @author DragN0007
     * @reason why are you asking me this on my own fucking code. i hate robots
     */
//    @Overwrite
//    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
//    }

}
