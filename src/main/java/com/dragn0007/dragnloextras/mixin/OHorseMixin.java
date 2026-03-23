package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.HorseFollowOwnerGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.items.custom.HalterItem;
import com.dragn0007.dragnloextras.network.SyncDirtyLayerPacket;
import com.dragn0007.dragnloextras.network.SyncHalterColorPacket;
import com.dragn0007.dragnloextras.network.SyncHalterLayerPacket;
import com.dragn0007.dragnloextras.network.SyncTraitPacket;
import com.dragn0007.dragnloextras.util.ITraitByBreedTypeHolder;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import com.dragn0007.dragnloextras.util.Trait;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
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

import java.util.Objects;
import java.util.Random;

@Mixin(OHorse.class)
public abstract class OHorseMixin extends AbstractOMount implements DirtyCapabilityInterface, ITraitByBreedTypeHolder {

    //TODO: hunger tick, trait mechanics, illness mechanics

    @Shadow public abstract boolean isDraftBreed();
    @Shadow public abstract boolean isWarmbloodedBreed();
    @Shadow public abstract boolean isRacingBreed();
    @Shadow public abstract boolean isPonyBreed();
    @Shadow public abstract boolean isStockBreed();

    public OHorseMixin(EntityType<? extends OHorseMixin> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        OHorse self = (OHorse) (Object) this;
        this.goalSelector.addGoal(6, new HorseFollowOwnerGoal(self, 1.0D, 2.0F, 2.0F, false));
    }


    @Unique
    public int livestockOverhaulScraps$dirtyTick;
    @Unique
    public int livestockOverhaulScraps$hungryTick;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {

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

    @Inject(method = "aiStep", at = @At("HEAD"))
    protected void onAIStep(CallbackInfo ci) {
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt(DamageSource damageSource, float v, CallbackInfoReturnable<Boolean> cir) {
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
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DirtyTick")) {
            this.livestockOverhaulScraps$dirtyTick = tag.getInt("DirtyTick");
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineData(CallbackInfo ci) {
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        Random random = new Random();

        if (ScrapsExtrasCommonConfig.TRAITS_SYSTEM.get()) {
            if (ScrapsExtrasCommonConfig.TRAITS_BY_BREED.get()) {
                ((ITraitByBreedTypeHolder)this).setTraitByBreedType();
            } else {
                int trait = random.nextInt(Trait.values().length);
                this.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                    cap.setTrait(trait);
                    SyncTraitPacket.syncToTracking(this, trait);
                });
            }
        }

        TraitCapabilityInterface cap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
        switch (cap.getTrait()) {
            case 0: this.addEffect(new MobEffectInstance(SEEffects.BRAVE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 1: this.addEffect(new MobEffectInstance(SEEffects.IMMUNOCOMPETENT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 2: this.addEffect(new MobEffectInstance(SEEffects.SWIFT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 3: this.addEffect(new MobEffectInstance(SEEffects.VAULTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 4: this.addEffect(new MobEffectInstance(SEEffects.CLIMBER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 5: this.addEffect(new MobEffectInstance(SEEffects.BUSTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 6: this.addEffect(new MobEffectInstance(SEEffects.STURDY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 7: this.addEffect(new MobEffectInstance(SEEffects.COWARDLY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 8: this.addEffect(new MobEffectInstance(SEEffects.IMMUNOSUPPRESSED.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 9: this.addEffect(new MobEffectInstance(SEEffects.STUBBORN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 10: this.addEffect(new MobEffectInstance(SEEffects.LAGGARD.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 11: this.addEffect(new MobEffectInstance(SEEffects.FRAIL.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
            case 12: this.addEffect(new MobEffectInstance(SEEffects.MEAN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                break;
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
}
