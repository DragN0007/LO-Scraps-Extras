package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnloextras.capabilities.DirtyCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.SleepingCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.TraitCapabilityInterface;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.SleepGoal;
import com.dragn0007.dragnloextras.entity.ai.VaulterLeapAtTargetGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.SyncDirtyLayerPacket;
import com.dragn0007.dragnloextras.network.SyncSpikeCollarLayerPacket;
import com.dragn0007.dragnloextras.network.SyncTraitPacket;
import com.dragn0007.dragnloextras.util.*;
import com.dragn0007.dragnpets.entities.POEntityTypes;
import com.dragn0007.dragnpets.entities.dog.DogBreed;
import com.dragn0007.dragnpets.entities.dog.DogMarkingOverlay;
import com.dragn0007.dragnpets.entities.dog.ODog;
import com.dragn0007.dragnpets.entities.dog.ODogModel;
import com.dragn0007.dragnpets.entities.wolf.OWolf;
import com.dragn0007.dragnpets.util.PetsOverhaulCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

@Mixin(ODog.class)
public abstract class ODogMixin extends TamableAnimal implements DirtyCapabilityInterface, ITraitByBreedTypeHolder, IHungerHolder, ISickModHolder {

    //stop remapping my shit bitch i aint ask you to do all that. piss me off
    @Shadow(remap = false) public abstract boolean isGuardDog();
    @Shadow(remap = false) public abstract boolean isLivestockGuardian();
    @Shadow(remap = false) public abstract boolean isHuntingDog();
    @Shadow(remap = false) public abstract boolean isHerdingDog();
    @Shadow(remap = false) public abstract boolean isBigGameHunter();

    @Shadow(remap = false) public abstract int getVariant();
    @Shadow(remap = false) public abstract int getOverlayVariant();
    @Shadow(remap = false) public abstract int getBreed();
    @Shadow(remap = false) public abstract int getFluff();
    @Shadow(remap = false) public abstract boolean isWagging();

    @Shadow public abstract boolean isCollared();

    @Shadow public abstract InteractionResult mobInteract(Player player, InteractionHand hand);

    @Shadow public SimpleContainer inventory;
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

    public ODogMixin(EntityType<? extends ODogMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        ODog self = (ODog) (Object) this;
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

            TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            if (ScrapsExtrasCommonConfig.TRAITS_SYSTEM.get() && this.hasEffect(SEEffects.MEAN.get()) && traitCap.getTrait() == 12) {
                livestockOverhaulScraps$beMeanTick++;
                if (livestockOverhaulScraps$beMeanTick >= livestockOverhaulScraps$beMeanTargetTick) {
                    livestockOverhaulScraps$beMeanTick = 0;
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

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
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
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
            } else if (itemstack.is(SEItems.COLLAR_SPIKES.get()) && this.isCollared()) {
                System.out.println("isCollared, itemstack is Collar Spikes");
                this.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                    System.out.println("Applied Spikes");
                    cap.setSpikeCollared(true); //                                                                          this value
                    SyncSpikeCollarLayerPacket.syncToTracking(this, true); //always make sure this value matches
                });
                this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 0.5f, 1f);
                cir.setReturnValue(InteractionResult.sidedSuccess(this.level().isClientSide()));
            } else if (itemstack.is(Tags.Items.SHEARS)) {
                this.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                    if (cap.hasSpikeCollar()) {
                        cap.setSpikeCollared(false); //                                                                          this value
                        SyncSpikeCollarLayerPacket.syncToTracking(this, false); //always make sure this value matches
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
        BaseTraitHelper.setBaseTrait(this);
    }

    @Unique
    public void setTraitByBreedType() {
        int trait = random.nextInt(Trait.values().length);

        if (this.isLivestockGuardian()) { //more likely to have strong and docile traits
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
        } else if (this.isGuardDog() || this.isHerdingDog()) { //more likely to have athletic traits
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
        } else if (this.isHerdingDog()) { //more likely to have high-energy traits
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
        } else if (this.isHuntingDog() || this.isBigGameHunter()) { //more likely to have all-rounder traits
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
        } else { //more likely to have docile traits
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
        }
    }

    @Inject(method = "predicate", at = @At("HEAD"), remap = false, cancellable = true)
    private <T extends GeoAnimatable> void predicate(AnimationState<T> tAnimationState, CallbackInfoReturnable<PlayState> cir) {
        double currentSpeed = this.getDeltaMovement().lengthSqr();
        double speedThreshold = 0.02;
        double movementSpeed = this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
        double animationSpeed = Math.max(0.1, movementSpeed);

        AnimationController<T> controller = tAnimationState.getController();

        SleepingCapabilityInterface sleepingCap = null;
        if (this.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
            sleepingCap = this.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
        }

        if (tAnimationState.isMoving()) {
            if (currentSpeed > speedThreshold) {
                controller.setAnimation(RawAnimation.begin().then("run", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(Math.max(0.1, 0.8 * controller.getAnimationSpeed() + animationSpeed));
            } else {
                controller.setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                controller.setAnimationSpeed(Math.max(0.1, 0.8 * controller.getAnimationSpeed() + animationSpeed));
            }
        } else {
            if (sleepingCap.isSleeping()) {
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
        ODog pup;
        pup = POEntityTypes.O_DOG_ENTITY.get().create(serverLevel);
        if (ageableMob instanceof OWolf) {
            pup.setBreed(25);

            int variantChance = this.random.nextInt(100);
            if (variantChance < 50) {
                pup.setVariant(this.getVariant());
            } else {
                pup.setColor();
            }

            int overlayChance = this.random.nextInt(100);
            if (overlayChance < 40) {
                pup.setOverlayVariant(this.getOverlayVariant());
            } else if (overlayChance < 80) {
                pup.setOverlayVariant(20);
            } else {
                pup.setMarking();
            }

            pup.setCropped(1);
            pup.setFluffChance();
            pup.setGender(random.nextInt(ODog.Gender.values().length));
            pup.setODogAttributes();

        } else {
            ODog partner = (ODog) ageableMob;

            int breedChance = this.random.nextInt(100);
            int breed;
            if (this.getBreed() == partner.getBreed()) {
                if (breedChance < 40) {
                    breed = this.getBreed();
                } else if (breedChance < 80) {
                    breed = partner.getBreed();
                } else if (breedChance < 95) {
                    breed = 0;
                } else {
                    breed = this.random.nextInt(DogBreed.values().length);
                }
            } else {
                if (breedChance < 20) {
                    breed = this.getBreed();
                } else if (breedChance < 40) {
                    breed = partner.getBreed();
                } else if (breedChance < 95) {
                    breed = 0;
                } else {
                    breed = this.random.nextInt(DogBreed.values().length);
                }
            }
            pup.setBreed(breed);

            if (!(breedChance == 0)) {
                int variantChance = this.random.nextInt(100);
                int variant;
                if (variantChance < 40) {
                    variant = this.getVariant();
                } else if (variantChance < 80) {
                    variant = partner.getVariant();
                } else {
                    variant = this.random.nextInt(ODogModel.Variant.values().length);
                }
                pup.setVariant(variant);
            } else if (breedChance == 0 && random.nextDouble() < 0.25) {
                pup.setColor();
            }

            if (!(breedChance == 0)) {
                int overlayChance = this.random.nextInt(100);
                int overlay;
                if (overlayChance < 40) {
                    overlay = this.getOverlayVariant();
                } else if (overlayChance < 80) {
                    overlay = partner.getOverlayVariant();
                } else {
                    overlay = this.random.nextInt(DogMarkingOverlay.values().length);
                }
                pup.setOverlayVariant(overlay);
            } else if (breedChance == 0 && random.nextDouble() < 0.25) {
                pup.setMarking();
            }

            int fluffyChance = this.random.nextInt(10);
            int fluff;
            if (fluffyChance < 5) {
                fluff = this.getFluff();
            } else {
                fluff = partner.getFluff();
            }
            pup.setFluff(fluff);
        }

        if (PetsOverhaulCommonConfig.ALLOW_CROPPED_DOG_SPAWNS.get()) {
            pup.setCropChance();
        } else {
            pup.setCropped(0);
        }

        pup.setGender(random.nextInt(ODog.Gender.values().length));
        pup.setODogAttributes();
        return pup;
    }

}
