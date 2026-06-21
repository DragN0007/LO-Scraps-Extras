package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.camel.CamelBreed;
import com.dragn0007.dragnlivestock.entities.camel.OCamel;
import com.dragn0007.dragnlivestock.entities.camel.OCamelMarkingLayer;
import com.dragn0007.dragnlivestock.entities.camel.OCamelModel;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.HorseFollowOwnerGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.SyncDirtyLayerPacket;
import com.dragn0007.dragnloextras.network.SyncImmunityPacket;
import com.dragn0007.dragnloextras.network.SyncTraitPacket;
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
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mixin(OCamel.class)
public abstract class OCamelMixin extends AbstractOMount implements DirtyCapabilityInterface, IHungerHolder, ISickModHolder {

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);
    @Shadow public abstract void registerGoals();

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

    /**
     * @author who do you think
     * @reason cuz i can
     */
    @Overwrite
    public ResourceLocation getDefaultLootTable() {
        if (ScrapsExtrasCommonConfig.BUTCHERING.get()) {
            return BuiltInLootTables.EMPTY;
        } else if (ModList.get().isLoaded("tfc")) {
            return OCamel.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return OCamel.VANILLA_LOOT_TABLE;
        } else {
            return OCamel.LOOT_TABLE;
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

    public OCamelMixin(EntityType<? extends OCamelMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OCamel self = (OCamel) (Object) this;
        this.goalSelector.addGoal(0, new SleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(2, new HorseFollowOwnerGoal(self, 1.0D, 2.0F, 2.0F, false));
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
    @Unique public int livestockOverhaulScraps$saddleSoreTick;
    @Unique public int livestockOverhaulScraps$heartwormMedTick;
    @Unique public int livestockOverhaulScraps$hoofPickTick;

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


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
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
                this.playSound(SoundEvents.CAMEL_EAT, 0.5f, 1f);
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
                this.playSound(SoundEvents.CAMEL_EAT, 0.5f, 1f);
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
                if (this.hasChest() && this.isOwnedBy(player)) {
                    this.dropEquipment();
                    this.inventory.removeAllItems();
                    this.setChest(false);
                    this.playChestEquipsSound();

                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
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
        tag.putInt("HeartwormMedTick", this.livestockOverhaulScraps$heartwormMedTick);
        tag.putInt("HoofPickTick", this.livestockOverhaulScraps$hoofPickTick);
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
        if (tag.contains("SaddleSoreTick")) {this.livestockOverhaulScraps$saddleSoreTick = tag.getInt("SaddleSoreTick");}
        if (tag.contains("HeartwormMedTick")) {this.livestockOverhaulScraps$heartwormMedTick = tag.getInt("HeartwormMedTick");}
        if (tag.contains("HoofPickTick")) {this.livestockOverhaulScraps$hoofPickTick = tag.getInt("HoofPickTick");}
        if (tag.contains("Hungry")) this.setHungry(tag.getBoolean("Hungry"));
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        CompoundTag nbt = this.getPersistentData();
        if (!nbt.getBoolean("loextras_initialized")) {
            BaseImmunityHelper.setBaseImmunity(this);
            BaseTraitHelper.setBaseTrait(this, false);
            nbt.putBoolean("loextras_initialized", true);
        }
    }

    @Inject(method = "registerControllers", at = @At("TAIL"), remap = false, cancellable = true)
    public void registerControllersTail(AnimatableManager.ControllerRegistrar controllers, CallbackInfo ci) {
        OCamel self = (OCamel) (Object) this;
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
            controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
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
        OCamel calf = (OCamel) ageableMob;

        if (ageableMob instanceof OCamel) {
            OCamel partnerCamel = (OCamel) ageableMob;
            calf = EntityTypes.O_CAMEL_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface partnerimmunityCap = partnerCamel.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface calfimmunityCap = calf.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partnerCamel.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            TraitCapabilityInterface calftraitCap = calf.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int i = this.random.nextInt(100);
            int variant;
            if (i < ((100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get()) / 2)) {
                variant = this.getVariant();
            } else if (i < (100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get())) {
                variant = partnerCamel.getVariant();
            } else {
                variant = this.random.nextInt(OCamelModel.Variant.values().length);
            }

            int j = this.random.nextInt(100);
            int overlay;
            if (j < ((100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get()) / 2)) {
                overlay = this.getOverlayVariant();
            } else if (j < (100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get())) {
                overlay = partnerCamel.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(OCamelMarkingLayer.Overlay.values().length);
            }

            int breedChance = this.random.nextInt(100);
            int breed;
            if (breedChance < ((100 - LivestockOverhaulCommonConfig.BREED_CHANCE.get()) / 2)) {
                breed = this.getBreed();
            } else if (breedChance < (100 - LivestockOverhaulCommonConfig.BREED_CHANCE.get())) {
                breed = partnerCamel.getBreed();
            } else {
                breed = this.random.nextInt(CamelBreed.Breed.values().length);
            }
            partnerCamel.setBreed(breed);

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                calftraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(calf, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                calftraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(calf, trait);
            } else {
                ((ITraitByBreedTypeHolder) calf).setTraitByBreedType();
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

            BabyTraitHelper.setTraitEffect(calf);
            ((ISickModHolder) calf).setSickChance(100 - calfimmunityCap.getImmunity());
            if (immunityCap.getImmunity() > 100) {
                immunityCap.setImmunity(100);
                SyncImmunityPacket.syncToTracking(this, 100);
            } else if (immunityCap.getImmunity() < 1) {
                immunityCap.setImmunity(1);
                SyncImmunityPacket.syncToTracking(this, 1);
            }

            calf.setVariant(variant);
            calf.setOverlayVariant(overlay);
            calf.setGender(random.nextInt(Gender.values().length));
            calf.setBreed(breed);
        }

        CompoundTag nbt = this.getPersistentData();
        nbt.putBoolean("loextras_initialized", true);
        return calf;
    }

}
