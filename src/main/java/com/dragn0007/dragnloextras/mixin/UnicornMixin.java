package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.horse.OHorseModel;
import com.dragn0007.dragnlivestock.entities.unicorn.Unicorn;
import com.dragn0007.dragnlivestock.entities.unicorn.UnicornMarkingLayer;
import com.dragn0007.dragnlivestock.entities.unicorn.UnicornModel;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.entities.util.marking_layer.EquineEyeColorOverlay;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.ai.EquineSleepGoal;
import com.dragn0007.dragnloextras.entity.ai.FleeRainGoal;
import com.dragn0007.dragnloextras.entity.ai.HorseFollowOwnerGoal;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.items.custom.HalterItem;
import com.dragn0007.dragnloextras.network.SyncHalterColorPacket;
import com.dragn0007.dragnloextras.network.SyncHalterLayerPacket;
import com.dragn0007.dragnloextras.network.SyncImmunityPacket;
import com.dragn0007.dragnloextras.network.SyncTraitPacket;
import com.dragn0007.dragnloextras.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Unicorn.class)
public abstract class UnicornMixin extends AbstractOMount implements DirtyCapabilityInterface, IHungerHolder, ISickModHolder {

    @Shadow(remap = false) public abstract int getEyeVariant();
    @Shadow(remap = false) public abstract int getSpecies();
    @Shadow(remap = false) public abstract int getHornVariant();

    @Shadow @Nullable public abstract SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @org.jetbrains.annotations.Nullable SpawnGroupData data, @org.jetbrains.annotations.Nullable CompoundTag tag);

    @Shadow(remap = false) public abstract void registerGoals();

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
            return Unicorn.TFC_LOOT_TABLE;
        } else if (LivestockOverhaulCommonConfig.USE_VANILLA_LOOT.get()) {
            return Unicorn.VANILLA_LOOT_TABLE;
        } else {
            return Unicorn.LOOT_TABLE;
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

    public UnicornMixin(EntityType<? extends UnicornMixin> entityType, Level level) {
        super(entityType, level);
        this.setHungry(false);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    public void registerGoals(CallbackInfo ci) {
        super.registerGoals();
        Unicorn self = (Unicorn) (Object) this;
        this.goalSelector.addGoal(0, new EquineSleepGoal(self));
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
    @Unique public int livestockOverhaulScraps$saddleSoreTick;
    @Unique public int livestockOverhaulScraps$rainRotTick;
    @Unique public int livestockOverhaulScraps$heartwormMedTick;
    @Unique public int livestockOverhaulScraps$hoofPickTick;


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();

        if (!this.level().isClientSide) {
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
        Unicorn self = (Unicorn) (Object) this;
        CompoundTag nbt = this.getPersistentData();
        if (!nbt.getBoolean("loextras_initialized")) {
            ImmunityCapabilityInterface immunityCap = null;
            if (this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).isPresent()) {
                immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            }
            immunityCap.setImmunity(100);
            BaseTraitHelper.setBaseTrait(this, false);
            nbt.putBoolean("loextras_initialized", true);
        }
    }

    /**
     * @author DragN0007
     * @reason why are you asking me this on my own fucking code. i hate robots
     */
    @Overwrite
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        AbstractOMount foal;
        ImmunityCapabilityInterface immunityCap = this.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        TraitCapabilityInterface traitCap = this.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

        if (ageableMob instanceof OHorse partnerHorse && ageableMob.getClass() == OHorse.class) {
            if (random.nextDouble() > 0.05) {
                foal = EntityTypes.O_HORSE_ENTITY.get().create(serverLevel);
            } else {
                foal = EntityTypes.UNICORN_ENTITY.get().create(serverLevel);
            }

            ImmunityCapabilityInterface partnerimmunityCap = partnerHorse.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface foalimmunityCap = foal.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partnerHorse.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            TraitCapabilityInterface foaltraitCap = foal.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int overlayChance = this.random.nextInt(100);
            int overlay;
            if (overlayChance < ((100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get()) / 2)) {
                overlay = this.getOverlayVariant();
            } else if (overlayChance < (100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get())) {
                overlay = partnerHorse.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(UnicornMarkingLayer.Overlay.values().length);
            }
            (foal).setVariant(overlay);

            ((OHorse) foal).setManeType(3);
            ((OHorse) foal).setTailType(3);
            (foal).setOverlayVariant(overlay);
            (foal).setVariant(random.nextInt(OHorseModel.Variant.values().length));

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else {
                ((ITraitByBreedTypeHolder) foal).setTraitByBreedType();
            }

            int immunityChance = this.random.nextInt(100);
            int immunity;
            if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                immunity = immunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                immunity = partnerimmunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else {
                int baseImmunity = random.nextInt(1, 100);
                foalimmunityCap.setImmunity(random.nextInt(baseImmunity));
                SyncImmunityPacket.syncToTracking(foal, random.nextInt(baseImmunity));
            }

            BabyTraitHelper.setTraitEffect(foal);
            ((ISickModHolder) foal).setSickChance(100 - foalimmunityCap.getImmunity());
            if (immunityCap.getImmunity() > 100) {
                immunityCap.setImmunity(100);
                SyncImmunityPacket.syncToTracking(this, 100);
            } else if (immunityCap.getImmunity() < 1) {
                immunityCap.setImmunity(1);
                SyncImmunityPacket.syncToTracking(this, 1);
            }

            CompoundTag nbt = this.getPersistentData();
            if (!nbt.getBoolean("loextras_initialized")) {
                nbt.putBoolean("loextras_initialized", true);
            }

        } else {
            Unicorn partner = (Unicorn) ageableMob;
            foal = EntityTypes.UNICORN_ENTITY.get().create(serverLevel);
            ImmunityCapabilityInterface partnerimmunityCap = partner.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            ImmunityCapabilityInterface foalimmunityCap = foal.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
            TraitCapabilityInterface partnertraitCap = partner.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
            TraitCapabilityInterface foaltraitCap = foal.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);

            int breed;
            breed = (this.random.nextInt(2) == 0) ? this.getSpecies() : partner.getSpecies();
            ((Unicorn) foal).setSpecies(breed);

            int variantChance = this.random.nextInt(100);
            int variant;
            if (variantChance < ((100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get()) / 2)) {
                variant = this.getVariant();
            } else if (variantChance < (100 - LivestockOverhaulCommonConfig.COAT_CHANCE.get())) {
                variant = partner.getVariant();
            } else {
                variant = this.random.nextInt(UnicornModel.Variant.values().length);
            }
            (foal).setVariant(variant);

            int overlayChance = this.random.nextInt(100);
            int overlay;
            if (overlayChance < ((100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get()) / 2)) {
                overlay = this.getOverlayVariant();
            } else if (overlayChance < (100 - LivestockOverhaulCommonConfig.MARKING_CHANCE.get())) {
                overlay = partner.getOverlayVariant();
            } else {
                overlay = this.random.nextInt(UnicornMarkingLayer.Overlay.values().length);
            }
            (foal).setOverlayVariant(overlay);

            int eyeColorChance = this.random.nextInt(100);
            int eyes;
            if (eyeColorChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                eyes = this.getEyeVariant();
            } else if (eyeColorChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                eyes = partner.getEyeVariant();
            } else {
                eyes = this.random.nextInt(EquineEyeColorOverlay.values().length);
            }
            ((Unicorn) foal).setEyeVariant(eyes);

            int hornColorChance = this.random.nextInt(100);
            int horn;
            if (hornColorChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                horn = this.getHornVariant();
            } else if (hornColorChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                horn = partner.getHornVariant();
            } else {
                horn = this.random.nextInt(EquineEyeColorOverlay.values().length);
            }
            ((Unicorn) foal).setHornVariant(horn);

            foal.setGender(random.nextInt(Unicorn.Gender.values().length));

            ((Unicorn) foal).setFeatheringByBreed();
            ((Unicorn) foal).setManeType(3);

            if (this.random.nextInt(3) >= 1) {
                ((Unicorn) foal).generateRandomOHorseJumpStrength();

                int betterSpeed = (int) Math.max(partner.getSpeed(), this.random.nextInt(10) + 20);
                foal.setSpeed(betterSpeed);

                int betterHealth = (int) Math.max(partner.getHealth(), this.random.nextInt(20) + 40);
                foal.setHealth(betterHealth);
            }

            int traitChance = this.random.nextInt(100);
            int trait;
            if (traitChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                trait = traitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else if (traitChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                trait = partnertraitCap.getTrait();
                foaltraitCap.setTrait(trait);
                SyncTraitPacket.syncToTracking(foal, trait);
            } else {
                ((ITraitByBreedTypeHolder) foal).setTraitByBreedType();
            }

            int immunityChance = this.random.nextInt(100);
            int immunity;
            if (immunityChance < ((100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get()) / 2)) {
                immunity = immunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else if (immunityChance < (100 - LivestockOverhaulCommonConfig.OTHER_CHANCE.get())) {
                immunity = partnerimmunityCap.getImmunity();
                if (random.nextDouble() < 0.25) {
                    foalimmunityCap.setImmunity(immunity + random.nextInt(1,25));
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                } else {
                    foalimmunityCap.setImmunity(immunity);
                    SyncImmunityPacket.syncToTracking(foal, immunity);
                }
            } else {
                int baseImmunity = random.nextInt(1, 100);
                foalimmunityCap.setImmunity(random.nextInt(baseImmunity));
                SyncImmunityPacket.syncToTracking(foal, random.nextInt(baseImmunity));
            }

            BabyTraitHelper.setTraitEffect(foal);
            ((ISickModHolder) foal).setSickChance(100 - foalimmunityCap.getImmunity());
            if (immunityCap.getImmunity() > 100) {
                immunityCap.setImmunity(100);
                SyncImmunityPacket.syncToTracking(this, 100);
            } else if (immunityCap.getImmunity() < 1) {
                immunityCap.setImmunity(1);
                SyncImmunityPacket.syncToTracking(this, 1);
            }
        }

        CompoundTag nbt = this.getPersistentData();
        if (!nbt.getBoolean("loextras_initialized")) {
            nbt.putBoolean("loextras_initialized", true);
        }

        this.setOffspringAttributes(ageableMob, foal);
        return foal;
    }

}
