package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.client.event.LivestockOverhaulClientEvent;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.horse.HorseBreed;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseModel;
import com.dragn0007.dragnlivestock.entities.mule.OMuleModel;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineEyeColorOverlay;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineMarkingOverlay;
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
import net.minecraftforge.fml.ModList;
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
public abstract class OHorseMixin extends AbstractOMount implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISleepAsLeaderHolder, ISickModHolder {

    //TODO: hunger tick, trait mechanics, illness mechanics, baby traits, baby immunity, gestation

    @Shadow public abstract boolean isDraftBreed();
    @Shadow public abstract boolean isWarmbloodedBreed();
    @Shadow public abstract boolean isRacingBreed();
    @Shadow public abstract boolean isPonyBreed();
    @Shadow public abstract boolean isStockBreed();

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);

    @Shadow public abstract int getEyeVariant();

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

    @Unique
    public boolean pregnant = false;
    @Unique
    public boolean isPregnant() {
        return this.pregnant;
    }
    @Unique
    public void setPregnant(boolean pregnant) {
        this.pregnant = pregnant;
    }

    @Unique
    int livestockOverhaulScraps$becomeSickChanceMod = 0;
    @Unique
    public void setSickChanceMod(int sickChanceMod) {
        this.livestockOverhaulScraps$becomeSickChanceMod = sickChanceMod;
    }

    @Unique int livestockOverhaulScraps$beMeanTargetTick = random.nextInt(24000) + 1200;
    @Unique int livestockOverhaulScraps$becomeSickChance;
    @Unique int livestockOverhaulScraps$becomeSickRand = random.nextInt(100);
    @Unique int livestockOverhaulScraps$pregnantTick;

    public OHorseMixin(EntityType<? extends OHorseMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
        this.setPregnant(false);
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
    @Unique public int livestockOverhaulScraps$sickModDissipateTick = 72000;
    @Unique public int livestockOverhaulScraps$saddleSoreTick;
    @Unique public int livestockOverhaulScraps$rainRotTick;

    @Inject(method = "aiStep", at = @At("HEAD"))
    protected void aiStep(CallbackInfo ci) {
        if (!this.level().isClientSide) {

            if (ScrapsExtrasCommonConfig.GESTATION.get()) {
                if (this.livestockOverhaulScraps$pregnantTick > 0) {
                    livestockOverhaulScraps$pregnantTick--;
                    if (this.livestockOverhaulScraps$pregnantTick <= 0) {
                        this.spawnGestationChildFromBreeding();
                    }
                }
            }
        }
    }

    @Unique
    private void spawnGestationChildFromBreeding() {
        Animal parent = (Animal) this;
        AgeableMob baby = parent.getBreedOffspring((ServerLevel) this.level(), parent);
        baby.setBaby(true);
        baby.moveTo(this.getX(), this.getY(), this.getZ());
        this.level().addFreshEntity(baby);
        this.livestockOverhaulScraps$pregnantTick = 0;
    }

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

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt(DamageSource damageSource, float v, CallbackInfoReturnable<Boolean> cir) {
        super.hurt(damageSource, v);

        if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
            if (livestockOverhaulScraps$becomeSickRand <= livestockOverhaulScraps$becomeSickChance) {

                //abrasions happen sometimes, but they're no big deal
                if (random.nextDouble() <= 0.10) {
                    this.addEffect(new MobEffectInstance(SEEffects.ABRASION.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                }

                if (ScrapsExtrasCommonConfig.RABIES.get()) {
                    //animals with higher immunity are less likely to get rabies from a bite. this is not realistic.
                    //do not go around getting bit by animals even if you've never had the flu. not a good idea
                    if (damageSource.getEntity() instanceof Animal && random.nextDouble() <= 0.02) {
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
                this.removeEffect(SEEffects.DIRTY.get());
                this.playSound(SoundEvents.BRUSH_GENERIC, 0.5f, 1f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.isHungry() || this.hasEffect(SEEffects.HUNGER.get())) {
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
                    this.setSickChanceMod(livestockOverhaulScraps$becomeSickChanceMod - 25);
                    livestockOverhaulScraps$becomeSickChance = livestockOverhaulScraps$becomeSickChanceMod;
                    if (livestockOverhaulScraps$becomeSickChance < 0) {
                        livestockOverhaulScraps$becomeSickChance = 0;
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
        tag.putInt("SickChanceMod", this.livestockOverhaulScraps$becomeSickChanceMod);
        tag.putInt("SickTick", this.livestockOverhaulScraps$sickTick);
        tag.putInt("SaddleSoreTick", this.livestockOverhaulScraps$saddleSoreTick);
        tag.putInt("RainRotTick", this.livestockOverhaulScraps$rainRotTick);
        tag.putInt("GestationTick", this.livestockOverhaulScraps$pregnantTick);
        tag.putBoolean("SleepingAsLeader", this.isSleepingAsLeader());
        tag.putBoolean("Hungry", this.isHungry());
        tag.putBoolean("Pregnant", this.isPregnant());
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
        if (tag.contains("GestationTick")) {this.livestockOverhaulScraps$pregnantTick = tag.getInt("GestationTick");}
        if (tag.contains("SleepingAsLeader")) this.setSleepingAsLeader(tag.getBoolean("SleepingAsLeader"));
        if (tag.contains("Hungry")) this.setHungry(tag.getBoolean("Hungry"));
        if (tag.contains("Pregnant")) this.setPregnant(tag.getBoolean("Pregnant"));
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

    @Inject(method = "canMate", at = @At("HEAD"))
    public void canMate(Animal animal, CallbackInfoReturnable<Boolean> cir) {
        if (this.isHungry() || livestockOverhaulScraps$pregnantTick > 0) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBreedOffspring", at = @At("HEAD"))
    public void getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob, CallbackInfoReturnable<AgeableMob> cir) {
        AbstractOMount foal;
        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

        if (ageableMob instanceof ODonkey partnerDonkey) {
            ImmunityCapabilityInterface partnerimmunityCap = partnerDonkey.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partnerDonkey.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            foal = EntityTypes.O_MULE_ENTITY.get().create(serverLevel);

            int overlayChance = this.random.nextInt(10);
            int overlay;
            if (overlayChance < 4) {
                overlay = this.getOverlayVariant();
            } else if (overlayChance < 8) {
                overlay = partnerDonkey.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(EquineMarkingOverlay.values().length);
            }
            foal.setVariant(overlay);

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

            foal.setGender(random.nextInt(Gender.values().length));

        } else {
            OHorse partner = (OHorse) ageableMob;
            foal = EntityTypes.O_HORSE_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface partnerimmunityCap = partner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partner.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int breedChance = this.random.nextInt(5);
            int breed;
            if (breedChance == 0) {
                if (!ModList.get().isLoaded("deadlydinos")) {
                    int[] breeds = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18, 19, 20, 21};
                    int randomIndex = new Random().nextInt(breeds.length);
                    breed = (breeds[randomIndex]);
                } else {
                    breed = this.random.nextInt(HorseBreed.values().length);
                }
            } else {
                breed = (this.random.nextInt(2) == 0) ? this.getBreed() : partner.getBreed();
            }
            foal.setBreed(breed);

            if (!(breedChance == 0)) {
                int variantChance = this.random.nextInt(14);
                int variant;
                if (variantChance < 6) {
                    variant = this.getVariant();
                } else if (variantChance < 12) {
                    variant = partner.getVariant();
                } else {
                    variant = this.random.nextInt(OHorseModel.Variant.values().length);
                }
                foal.setVariant(variant);
            } else if (breedChance == 0 && random.nextDouble() < 0.5) {
                ((OHorse) foal).setColorByBreed();
            }

            if (!(breedChance == 0)) {
                int overlayChance = this.random.nextInt(10);
                int overlay;
                if (overlayChance < 4) {
                    overlay = this.getOverlayVariant();
                } else if (overlayChance < 8) {
                    overlay = partner.getOverlayVariant();
                } else {
                    overlay = this.random.nextInt(EquineMarkingOverlay.values().length);
                }
                foal.setOverlayVariant(overlay);
            } else if (breedChance == 0 && random.nextDouble() < 0.5) {
                ((OHorse) foal).setMarkingByBreed();
            }

            int eyeColorChance = this.random.nextInt(11);
            int eyes;
            if (eyeColorChance < 5) {
                eyes = this.getEyeVariant();
            } else if (eyeColorChance < 10) {
                eyes = partner.getEyeVariant();
            } else {
                eyes = this.random.nextInt(EquineEyeColorOverlay.values().length);
            }
            ((OHorse) foal).setEyeVariant(eyes);

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

        if (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
            if (this.isFemale()) {
                livestockOverhaulScraps$pregnantTick = ScrapsExtrasCommonConfig.GESTATION_TICK.get();
            }
        }

        this.setOffspringAttributes(ageableMob, foal);
        cir.getReturnValue();
    }
}
