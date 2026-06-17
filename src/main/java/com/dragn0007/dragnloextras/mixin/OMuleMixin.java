package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.HorseFollowOwnerGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.items.custom.HalterItem;
import com.dragn0007.dragnloextras.items.custom.TurnoutBlanketItem;
import com.dragn0007.dragnloextras.network.SyncDirtyLayerPacket;
import com.dragn0007.dragnloextras.network.SyncHalterColorPacket;
import com.dragn0007.dragnloextras.network.SyncHalterLayerPacket;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mixin(OMule.class)
public abstract class OMuleMixin extends AbstractOMount implements DirtyCapabilityInterface, IHungerHolder, ISickModHolder {

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
            return OMule.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return OMule.VANILLA_LOOT_TABLE;
        } else {
            return OMule.LOOT_TABLE;
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

    public OMuleMixin(EntityType<? extends OMuleMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OMule self = (OMule) (Object) this;
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
                        if (ScrapsExtrasCommonConfig.GRAZING.get() && (!this.level().getBlockState(blockpos1).is(Blocks.GRASS_BLOCK) && !this.isGroundTied()) && this.isTamed()) {
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
        if (tag.contains("RainRotTick")) {this.livestockOverhaulScraps$rainRotTick = tag.getInt("RainRotTick");}
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
        OMule self = (OMule) (Object) this;
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

}
