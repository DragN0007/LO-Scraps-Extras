package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.IsDirtyDuck;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OHorse.class)
public abstract class OHorseMixin extends AbstractOMount implements IsDirtyDuck {

    //TODO: IsDirty fix, halters, hunger tick

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

                if (livestockOverhaulScraps$dirtyTick >= ScrapsExtrasCommonConfig.DIRTY_TICK.get()) {
                    this.livestockOverhaulScraps$setDirty(true);
                }
            }
        }
    }

    //the "dirty" function seems to only be running on client-side and sometimes not at all (?)

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

        /*
        if (itemstack.is(Items.SHEARS)) {
            if (this.livestockOverhaulScraps$isHaltered()) {
                this.livestockOverhaulScraps$setHaltered(false);
                this.playSound(SoundEvents.SHEEP_SHEAR, 0.5f, 1f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        if (item instanceof HalterItem) {
            HalterItem halterItem = (HalterItem) item;
            if (this.isOwnedBy(player)) {
                this.livestockOverhaulScraps$setHaltered(true);
                DyeColor dyecolor = halterItem.getDyeColor();
                if (dyecolor != this.livestockOverhaulScraps$getHalterColor()) {
                    this.livestockOverhaulScraps$setHalterColor(dyecolor);
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
         */
        return super.mobInteract(player, hand);
    }

    //literally none of this works. dunno what a Forge Capability is

    /*
    @Unique
    private static EntityDataAccessor<Integer> livestockOverhaulScraps$DATA_HALTER_COLOR;
    @Unique
    private static EntityDataAccessor<Boolean> livestockOverhaulScraps$HALTERED;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(EntityType<?> type, Level level, CallbackInfo ci) {
        livestockOverhaulScraps$DATA_HALTER_COLOR = SynchedEntityData.defineId(OHorseMixin.class, EntityDataSerializers.INT);
        livestockOverhaulScraps$HALTERED = SynchedEntityData.defineId(OHorseMixin.class, EntityDataSerializers.BOOLEAN);
    }

    @Unique
    public boolean livestockOverhaulScraps$isHaltered() {
        return this.entityData.get(livestockOverhaulScraps$HALTERED);
    }
    @Unique
    public void livestockOverhaulScraps$setHaltered(boolean haltered) {
        this.entityData.set(livestockOverhaulScraps$HALTERED, haltered);
    }

    @Unique
    public DyeColor livestockOverhaulScraps$getHalterColor() {
        return DyeColor.byId(this.entityData.get(livestockOverhaulScraps$DATA_HALTER_COLOR));
    }
    @Unique
    public void livestockOverhaulScraps$setHalterColor(DyeColor p_30398_) {
        this.entityData.set(livestockOverhaulScraps$DATA_HALTER_COLOR, p_30398_.getId());
    }
     */

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addData(CompoundTag tag, CallbackInfo ci) {
        super.addAdditionalSaveData(tag);
//        tag.putByte("HalterColor", (byte)this.livestockOverhaulScraps$getHalterColor().getId());
//        tag.putBoolean("Haltered", this.livestockOverhaulScraps$isHaltered());
        tag.putBoolean("Dirty", this.livestockOverhaulScraps$isDirty());
        tag.putInt("DirtyTick", this.livestockOverhaulScraps$dirtyTick);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readData(CompoundTag tag, CallbackInfo ci) {
        super.readAdditionalSaveData(tag);
//        if (tag.contains("HalterColor", 99)) {
//            this.livestockOverhaulScraps$setHalterColor(DyeColor.byId(tag.getInt("HalterColor")));
//        }
//        if(tag.contains("Haltered")) {
//            this.livestockOverhaulScraps$setHaltered(tag.getBoolean("Haltered"));
//        }
        if(tag.contains("Dirty")) {
            this.livestockOverhaulScraps$setDirty(tag.getBoolean("Dirty"));
        }
        if (tag.contains("DirtyTick")) {
            this.livestockOverhaulScraps$dirtyTick = tag.getInt("DirtyTick");
        }
    }

//    @Inject(method = "defineSynchedData", at = @At("TAIL"))
//    public void defineData(CallbackInfo ci) {
//        super.defineSynchedData();
//        this.getEntityData().define(livestockOverhaulScraps$DATA_HALTER_COLOR, DyeColor.RED.getId());
//        this.getEntityData().define(livestockOverhaulScraps$HALTERED, false);
//    }

}
