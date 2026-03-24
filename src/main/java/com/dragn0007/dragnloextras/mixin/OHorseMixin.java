package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.client.event.LivestockOverhaulClientEvent;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import org.spongepowered.asm.mixin.Mixin;
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
import java.util.Objects;
import java.util.Random;

@Mixin(OHorse.class)
public abstract class OHorseMixin extends AbstractOMount implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISleepAsLeaderHolder {

    //TODO: hunger tick, trait mechanics, illness mechanics, born-with traits

    @Shadow public abstract boolean isDraftBreed();
    @Shadow public abstract boolean isWarmbloodedBreed();
    @Shadow public abstract boolean isRacingBreed();
    @Shadow public abstract boolean isPonyBreed();
    @Shadow public abstract boolean isStockBreed();

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);

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

    @Unique int livestockOverhaulScraps$beMeanTargetTick = random.nextInt(24000) + 1200;
    @Unique int livestockOverhaulScraps$becomeSickChance;
    @Unique int livestockOverhaulScraps$becomeSickChanceMod;
    @Unique int livestockOverhaulScraps$becomeSick = random.nextInt(100);

    public OHorseMixin(EntityType<? extends OHorseMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
        this.setSleepingAsLeader(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OHorse self = (OHorse) (Object) this;
        this.goalSelector.addGoal(0, new EquineSleepGoal(self));
        this.goalSelector.addGoal(1, new FleeRainGoal(self, 1.2F));
        this.goalSelector.addGoal(6, new HorseFollowOwnerGoal(self, 1.0D, 2.0F, 2.0F, false));
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity ->
                entity instanceof Player && !this.isBaby() && this.hasEffect(SEEffects.RABIES.get())
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
    @Unique public int livestockOverhaulScraps$saddleSoreTick;
    @Unique public int livestockOverhaulScraps$rainRotTick;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {

            if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
                livestockOverhaulScraps$sickTick++;
                if (livestockOverhaulScraps$sickTick >= 72000) {
                    if (livestockOverhaulScraps$becomeSick <= livestockOverhaulScraps$becomeSickChance) {
                        livestockOverhaulScraps$sickTick = 0;
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
                    }
                }
            }

            if (ScrapsExtrasCommonConfig.FEEDING_SYSTEM.get()) {
                if (!this.isHungry()) {
                    livestockOverhaulScraps$hungryTick++;
                    if (livestockOverhaulScraps$hungryTick >= ScrapsExtrasCommonConfig.HORSE_FEED_TICK.get()) {
                        this.setHungry(true);
                        this.addEffect(new MobEffectInstance(SEEffects.HUNGER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        livestockOverhaulScraps$hungryTick = 0;
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
                this.removeEffect(SEEffects.DIRTY.get());
                this.playSound(SoundEvents.BRUSH_GENERIC, 0.5f, 1f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.isHungry()) {
                if (itemstack.is(SEItems.GRAIN_FEED.get())) {
                    this.setHungry(false);
                    if (this.hasEffect(SEEffects.HUNGER.get())) {
                        this.removeEffect(SEEffects.HUNGER.get());
                    }
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }

                if (itemstack.is(SEItems.HEARTY_GRAIN_FEED.get())) {
                    this.setHungry(false);
                    if (this.hasEffect(SEEffects.HUNGER.get())) {
                        this.removeEffect(SEEffects.HUNGER.get());
                    }
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 12000, 0, false, false));
                    this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 12000, 0, false, false));
                    ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
                    if (immunityCap.getImmunity() < 75) {
                        SyncImmunityPacket.syncToTracking(this, immunityCap.getImmunity() + 2);
                        immunityCap.setImmunity(immunityCap.getImmunity() + 2);
                    }
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }

            if (itemstack.is(Items.SHEARS) && player.isShiftKeyDown()) {
                if (this.getCapability(SECapabilities.HALTER_CAPABILITY).isPresent()) {
                    HalterCapabilityInterface halterCapabilityInterface = this.getCapability(SECapabilities.HALTER_CAPABILITY).orElse(null);
                    HalterColorCapabilityInterface halterColorCapabilityInterface = this.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).orElse(null);
                    if (halterCapabilityInterface.hasHalter()) {
                        halterCapabilityInterface.setHaltered(false);
                        SyncHalterLayerPacket.syncToTracking(this, false);
                        DyeColor dyeColor = DyeColor.byId(halterColorCapabilityInterface.getHalterColor().getId());
                        Item halter = switch (dyeColor) {
                            case WHITE -> SEItems.WHITE_HALTER.get();
                            case ORANGE -> SEItems.ORANGE_HALTER.get();
                            case MAGENTA -> SEItems.MAGENTA_HALTER.get();
                            case LIGHT_BLUE -> SEItems.LIGHT_BLUE_HALTER.get();
                            case YELLOW -> SEItems.YELLOW_HALTER.get();
                            case LIME -> SEItems.LIME_HALTER.get();
                            case PINK -> SEItems.PINK_HALTER.get();
                            case GRAY -> SEItems.GREY_HALTER.get();
                            case LIGHT_GRAY -> SEItems.LIGHT_GREY_HALTER.get();
                            case CYAN -> SEItems.CYAN_HALTER.get();
                            case PURPLE -> SEItems.PURPLE_HALTER.get();
                            case BLUE -> SEItems.BLUE_HALTER.get();
                            case BROWN -> SEItems.BROWN_HALTER.get();
                            case GREEN -> SEItems.GREEN_HALTER.get();
                            case RED -> SEItems.RED_HALTER.get();
                            case BLACK -> SEItems.BLACK_HALTER.get();
                        };
                        spawnAtLocation(halter);
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
        tag.putInt("SickTick", this.livestockOverhaulScraps$sickTick);
        tag.putInt("SaddleSoreTick", this.livestockOverhaulScraps$saddleSoreTick);
        tag.putInt("RainRotTick", this.livestockOverhaulScraps$rainRotTick);
        tag.putBoolean("SleepingAsLeader", this.isSleepingAsLeader());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DirtyTick")) {this.livestockOverhaulScraps$dirtyTick = tag.getInt("DirtyTick");}
        if (tag.contains("MeanTick")) {this.livestockOverhaulScraps$beMeanTick = tag.getInt("MeanTick");}
        if (tag.contains("HungerTick")) {this.livestockOverhaulScraps$hungryTick = tag.getInt("HungerTick");}
        if (tag.contains("SickChance")) {this.livestockOverhaulScraps$becomeSickChance = tag.getInt("SickChance");}
        if (tag.contains("SickTick")) {this.livestockOverhaulScraps$sickTick = tag.getInt("SickTick");}
        if (tag.contains("SaddleSoreTick")) {this.livestockOverhaulScraps$saddleSoreTick = tag.getInt("SaddleSoreTick");}
        if (tag.contains("RainRotTick")) {this.livestockOverhaulScraps$rainRotTick = tag.getInt("RainRotTick");}
        if (tag.contains("SleepingAsLeader")) this.setSleepingAsLeader(tag.getBoolean("SleepingAsLeader"));
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        Random random = new Random();
        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

        if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
            int baseImmunity = random.nextInt(50);
            immunityCap.setImmunity(random.nextInt(baseImmunity));
            SyncImmunityPacket.syncToTracking(this, random.nextInt(baseImmunity));

            int traitImmunityAdditionMajor = random.nextInt(50) + 25;
            int traitImmunityAdditionMinor = random.nextInt(25);
            if (traitCap.getTrait() == 1) { //immunocompetent (major)
                immunityCap.setImmunity(immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMajor));
                SyncImmunityPacket.syncToTracking(this, immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMajor));
            } else if (traitCap.getTrait() == 8) { //immunosuppressed (major)
                immunityCap.setImmunity(immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMajor));
                SyncImmunityPacket.syncToTracking(this, immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMajor));
            } else if (traitCap.getTrait() == 6) { //sturdy (minor)
                immunityCap.setImmunity(immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMinor));
                SyncImmunityPacket.syncToTracking(this, immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMinor));
            } else if (traitCap.getTrait() == 11) { //frail (minor)
                immunityCap.setImmunity(immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMinor));
                SyncImmunityPacket.syncToTracking(this, immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMinor));
            }

            livestockOverhaulScraps$becomeSickChance = 100 - immunityCap.getImmunity();
        }

        if (ScrapsExtrasCommonConfig.TRAITS_SYSTEM.get()) {
            if (ScrapsExtrasCommonConfig.TRAITS_BY_BREED.get()) {
                if (ScrapsExtrasCommonConfig.GOOD_TRAITS_ONLY.get()) {
                    do {
                        ((ITraitByBreedTypeHolder) this).setTraitByBreedType();
                    } while (traitCap.getTrait() == 7 || traitCap.getTrait() == 8 || traitCap.getTrait() == 9 ||
                            traitCap.getTrait() == 10 || traitCap.getTrait() == 11 || traitCap.getTrait() == 12);
                } else {
                    ((ITraitByBreedTypeHolder) this).setTraitByBreedType();
                }
            } else {
                int trait = random.nextInt(Trait.values().length);
                if (ScrapsExtrasCommonConfig.GOOD_TRAITS_ONLY.get()) {
                    do {
                        traitCap.setTrait(trait);
                        SyncTraitPacket.syncToTracking(this, trait);
                    } while (traitCap.getTrait() == 7 || traitCap.getTrait() == 8 || traitCap.getTrait() == 9 ||
                            traitCap.getTrait() == 10 || traitCap.getTrait() == 11 || traitCap.getTrait() == 12);
                } else {
                    traitCap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                }
            }

            switch (traitCap.getTrait()) {
                case 0:
                    this.addEffect(new MobEffectInstance(SEEffects.BRAVE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 1:
                    this.addEffect(new MobEffectInstance(SEEffects.IMMUNOCOMPETENT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 2:
                    this.addEffect(new MobEffectInstance(SEEffects.SWIFT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 3:
                    this.addEffect(new MobEffectInstance(SEEffects.VAULTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 4:
                    this.addEffect(new MobEffectInstance(SEEffects.CLIMBER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 5:
                    this.addEffect(new MobEffectInstance(SEEffects.BUSTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 6:
                    this.addEffect(new MobEffectInstance(SEEffects.STURDY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 7:
                    this.addEffect(new MobEffectInstance(SEEffects.COWARDLY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 8:
                    this.addEffect(new MobEffectInstance(SEEffects.IMMUNOSUPPRESSED.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 9:
                    this.addEffect(new MobEffectInstance(SEEffects.STUBBORN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 10:
                    this.addEffect(new MobEffectInstance(SEEffects.LAGGARD.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 11:
                    this.addEffect(new MobEffectInstance(SEEffects.FRAIL.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
                case 12:
                    this.addEffect(new MobEffectInstance(SEEffects.MEAN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    break;
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

    @Inject(method = "predicate", at = @At("HEAD"), remap = false, cancellable = true)
    private <T extends GeoAnimatable> void predicate(AnimationState<T> tAnimationState, CallbackInfoReturnable<PlayState> cir) {
        double x = this.getX() - this.xo;
        double z = this.getZ() - this.zo;
        double currentSpeed = this.getDeltaMovement().lengthSqr();
        double speedThreshold = 0.025;
        double speedRunThreshold = 0.02;
        double speedTrotThreshold = 0.015;
        double wagonSpeedRunThreshold = 0.09;
        double wagonSpeedTrotThreshold = 0.06;

        boolean isMoving = (x * x + z * z) > 0.0001;

        double movementSpeed = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
        double animationSpeed = Math.max(0.1, movementSpeed);

        AnimationController<T> controller = tAnimationState.getController();
        SleepingCapabilityInterface sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);

        if ((!this.isTamed() || this.isWearingRodeoHarness()) && this.isVehicle() && !this.isJumping()) {
            controller.setAnimation(RawAnimation.begin().then("buck", Animation.LoopType.LOOP));
            controller.setAnimationSpeed(1.3);
        } else if (this.isJumping()) {
            controller.setAnimation(RawAnimation.begin().then("jump", Animation.LoopType.PLAY_ONCE));
            controller.setAnimationSpeed(1.0);
        } else {
            if (isMoving) {
                if (!LivestockOverhaulClientEvent.HORSE_WALK_BACKWARDS.isDown()) {
                    if (this.isNoAi() && !this.isVehicle()) { //for wagons
                        if (currentSpeed < wagonSpeedTrotThreshold) {
                            controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.80 * controller.getAnimationSpeed() + animationSpeed));
                        } else if (currentSpeed > wagonSpeedTrotThreshold && currentSpeed < wagonSpeedRunThreshold) {
                            controller.setAnimation(RawAnimation.begin().then("trot", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.78 * controller.getAnimationSpeed() + animationSpeed));
                        } else if (currentSpeed > wagonSpeedRunThreshold) {
                            controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.78 * controller.getAnimationSpeed() + animationSpeed));
                        } else {
                            controller.setAnimation(RawAnimation.begin().then("trot", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.82 * controller.getAnimationSpeed() + animationSpeed));
                        }

                    } else if (this.isAggressive() || (this.isVehicle() && this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPRINT_SPEED_MOD)) || (!this.isVehicle() && currentSpeed > speedThreshold)) {
                        controller.setAnimation(RawAnimation.begin().then("sprint", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(Math.max(0.1, 0.82 * controller.getAnimationSpeed() + animationSpeed));

                    } else if ((this.isVehicle() && this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(TROT_SPEED_MOD)) || (!this.isVehicle() && currentSpeed > speedTrotThreshold)) {
                        controller.setAnimation(RawAnimation.begin().then("trot", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(Math.max(0.1, 0.78 * controller.getAnimationSpeed() + animationSpeed));

                    } else if ((this.isVehicle() && !this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(WALK_SPEED_MOD) && !this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(SPRINT_SPEED_MOD) && !this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(TROT_SPEED_MOD))
                            || (!this.isVehicle() && currentSpeed > speedRunThreshold && currentSpeed < speedThreshold)) {
                        if (this.isOnSand()) {
                            controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.75 * controller.getAnimationSpeed() + animationSpeed));
                        } else {
                            controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.78 * controller.getAnimationSpeed() + animationSpeed));
                        }

                    } else if (this.isVehicle() && this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(WALK_SPEED_MOD)) {
                        if (LivestockOverhaulClientEvent.HORSE_SPANISH_WALK_TOGGLE.isDown()) {
                            controller.setAnimation(RawAnimation.begin().then("spanish_walk", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.78 * controller.getAnimationSpeed() + animationSpeed));
                        } else {
                            controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                            controller.setAnimationSpeed(Math.max(0.1, 0.83 * controller.getAnimationSpeed() + animationSpeed));
                        }

                    } else {
                        controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(Math.max(0.1, 0.80 * controller.getAnimationSpeed() + animationSpeed));
                    }

                } else if (this.isVehicle() && LivestockOverhaulClientEvent.HORSE_WALK_BACKWARDS.isDown()) {
                    if (this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(WALK_SPEED_MOD)) {
                        controller.setAnimation(RawAnimation.begin().then("walk_back", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(Math.max(0.1, 0.76 * controller.getAnimationSpeed() + animationSpeed));
                    } else {
                        controller.setAnimation(RawAnimation.begin().then("walk_back", Animation.LoopType.LOOP));
                        controller.setAnimationSpeed(Math.max(0.1, 0.83 * controller.getAnimationSpeed() + animationSpeed));
                    }
                }

            } else {
                if (this.isGroundTied() && LivestockOverhaulCommonConfig.GROUND_TIE.get()) {
                    controller.setAnimation(RawAnimation.begin().then("ground_tie", Animation.LoopType.LOOP));
                } else if (sleepingCap.isSleeping()) {
                    if (this.isSleepingAsLeader()) {
                        controller.setAnimation(RawAnimation.begin().then("relax", Animation.LoopType.LOOP));
                    } else {
                        controller.setAnimation(RawAnimation.begin().then("sleep", Animation.LoopType.LOOP));
                    }
                } else {
                    controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                }
                controller.setAnimationSpeed(1.0);
            }
        }
        cir.setReturnValue(PlayState.CONTINUE);
    }
}
