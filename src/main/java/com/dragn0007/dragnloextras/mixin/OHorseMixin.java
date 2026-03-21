package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.capabilities.IDirtyCapability;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

@Mixin(OHorse.class)
public abstract class OHorseMixin extends AbstractOMount implements IsDirtyDuck, TraitDuck, ITraitByBreedTypeHolder {

    //TODO: IsDirty fix, halters, hunger tick

    @Shadow public abstract boolean isDraftBreed();

    @Shadow public abstract boolean isWarmbloodedBreed();

    @Shadow public abstract boolean isRacingBreed();

    @Shadow public abstract boolean isPonyBreed();

    @Shadow public abstract boolean isStockBreed();

    public OHorseMixin(EntityType<? extends OHorseMixin> entityType, Level level) {
        super(entityType, level);
    }

    //something in here causes a config crash on servers
    //put me out of my misery

    @Unique
    public int livestockOverhaulScraps$dirtyTick;
    @Unique
    public int livestockOverhaulScraps$hungryTick;

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            if (ScrapsExtrasCommonConfig.HYGIENE_SYSTEM.get()) {
                livestockOverhaulScraps$dirtyTick++;

                if (livestockOverhaulScraps$isDirty() && livestockOverhaulScraps$dirtyTick >= ScrapsExtrasCommonConfig.DIRTY_TICK.get() &&
                        this.hasEffect(SEEffects.DIRTY.get())) {
                    int amp = Objects.requireNonNull(this.getEffect(SEEffects.DIRTY.get())).getAmplifier();
                    if (amp <= 4) {
                        this.addEffect(new MobEffectInstance(SEEffects.DIRTY.get(), MobEffectInstance.INFINITE_DURATION, amp + 1, false, false));
                    }
                }

                if (livestockOverhaulScraps$dirtyTick >= ScrapsExtrasCommonConfig.DIRTY_TICK.get()) {
                    this.livestockOverhaulScraps$setDirty(true);
                    this.addEffect(new MobEffectInstance(SEEffects.DIRTY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                    livestockOverhaulScraps$dirtyTick = 0;
                }
            }
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    protected void onAIStep(CallbackInfo ci) {
        if (this.hasEffect(SEEffects.DIRTY.get())) {
            this.level().addParticle(ParticleTypes.MYCELIUM, this.getRandomX(0.6D), this.getRandomY(), this.getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
        }
    }

    @Unique
    public boolean livestockOverhaulScraps$dirty = false;

    @Unique
    public boolean livestockOverhaulScraps$isDirty() {
        return this.livestockOverhaulScraps$dirty;
    }
    @Unique
    public void livestockOverhaulScraps$setDirty(boolean dirty) {
        this.livestockOverhaulScraps$dirty = dirty;
    }

    @Unique
    private int livestockOverhaulScraps$trait;
    @Override
    public int livestockOverhaulScraps$getTrait() {
        return this.livestockOverhaulScraps$trait;
    }
    @Override
    public void livestockOverhaulScraps$setTrait(int value) {
        this.livestockOverhaulScraps$trait = value;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (itemstack.is(SEItems.BRUSH.get())) {
            livestockOverhaulScraps$dirtyTick = 0;
            this.livestockOverhaulScraps$setDirty(false);
            this.playSound(SoundEvents.BRUSH_GENERIC, 0.5f, 1f);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addData(CompoundTag tag, CallbackInfo ci) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Dirty", this.livestockOverhaulScraps$isDirty());
        tag.putInt("DirtyTick", this.livestockOverhaulScraps$dirtyTick);
        tag.putInt("Trait", this.livestockOverhaulScraps$getTrait());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("Dirty")) {
            this.livestockOverhaulScraps$setDirty(tag.getBoolean("Dirty"));
        }
        if (tag.contains("DirtyTick")) {
            this.livestockOverhaulScraps$dirtyTick = tag.getInt("DirtyTick");
        }
        if (tag.contains("Trait")) {
            this.livestockOverhaulScraps$setTrait(tag.getInt("Trait"));
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineData(CallbackInfo ci) {
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void spawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, SpawnGroupData data, CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir) {
        Random random = new Random();
        this.livestockOverhaulScraps$setTrait(random.nextInt(Trait.values().length));
        switch (this.livestockOverhaulScraps$getTrait()) {
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
        if (this.isDraftBreed()) { //more likely to have strong and docile traits
            if (random.nextDouble() <= 0.15) {
                this.livestockOverhaulScraps$setTrait(Trait.values().length);
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {0, 5, 6, 10};
                int randomIndex = new Random().nextInt(traits.length);
                this.livestockOverhaulScraps$setTrait(traits[randomIndex]);
            }
        } else if (this.isWarmbloodedBreed()) { //more likely to have athletic traits
            if (random.nextDouble() <= 0.15) {
                this.livestockOverhaulScraps$setTrait(Trait.values().length);
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {3, 4, 6, 8};
                int randomIndex = new Random().nextInt(traits.length);
                this.livestockOverhaulScraps$setTrait(traits[randomIndex]);
            }
        } else if (this.isRacingBreed()) { //more likely to have high-energy traits
            if (random.nextDouble() <= 0.15) {
                this.livestockOverhaulScraps$setTrait(Trait.values().length);
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {2, 5, 4, 12};
                int randomIndex = new Random().nextInt(traits.length);
                this.livestockOverhaulScraps$setTrait(traits[randomIndex]);
            }
        } else if (this.isPonyBreed()) { //more likely to have docile traits
            if (random.nextDouble() <= 0.15) {
                this.livestockOverhaulScraps$setTrait(Trait.values().length);
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {1, 4, 6, 9};
                int randomIndex = new Random().nextInt(traits.length);
                this.livestockOverhaulScraps$setTrait(traits[randomIndex]);
            }
        } else if (this.isStockBreed()) { //more likely to have all-rounder traits
            if (random.nextDouble() <= 0.15) {
                this.livestockOverhaulScraps$setTrait(Trait.values().length);
            } else if (random.nextDouble() > 0.15) {
                int[] traits = {1, 2, 3, 7};
                int randomIndex = new Random().nextInt(traits.length);
                this.livestockOverhaulScraps$setTrait(traits[randomIndex]);
            }
        }
    }
}
