package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Corpse extends Mob implements GeoEntity {

	public Corpse(EntityType<? extends Corpse> type, Level level) {
		super(type, level);
		this.xpReward = 0;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
	}

	@Override
	protected void registerGoals() {
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	public boolean canBeLeashed(Player p_21418_) {
		return true;
	}

	@Override
	public boolean removeWhenFarAway(double p_27598_) {
		return this.decomposeTimer >= ScrapsExtrasCommonConfig.CORPSE_DECOMP_TIMER.get();
	}

	public int decomposeTimer;

	@Override
	public void tick() {
		super.tick();
		decomposeTimer++;
		if (this.decomposeTimer >= ScrapsExtrasCommonConfig.CORPSE_DECOMP_TIMER.get()) {
			this.kill();
			this.remove(RemovalReason.DISCARDED);
			if (this.level().isClientSide) {
				this.remove(RemovalReason.DISCARDED);
			}
		}

		if (this.isLeashed() && this.getLeashHolder() != null) {
			Entity holder = this.getLeashHolder();
			double distance = this.distanceTo(holder);
			double holderX = holder.getX() - this.getX();
			double holderZ = holder.getZ() - this.getZ();
			float y = (float)(Math.atan2(holderZ, holderX) * (180D / Math.PI)) - 90.0F;
			if (distance > 3.0D) {
				this.setYBodyRot(y);
				this.getNavigation().moveTo(holder, 0.3D);
				if (distance > 15.0D) {
					this.getNavigation().stop();
					this.dropLeash(true, true);
				} else if (distance > 10.0D) {
					this.setYBodyRot(y);
					this.getNavigation().moveTo(holder, 1.0D);
				}
			} else {
				this.getNavigation().stop();
			}
		}
	}

	protected final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
	}
	private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
		AnimationController<T> controller = tAnimationState.getController();
		controller.setAnimation(RawAnimation.begin().then("dead", Animation.LoopType.LOOP));
		return PlayState.CONTINUE;
	}
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		Item item = itemStack.getItem();

		if (itemStack.is(ItemTags.AXES) && player.isShiftKeyDown()) {
			ButcherStage stage = ButcherStage.values()[this.getButcherStage()];
			ButcherStage nextStage = stage.next();
			this.setButcherStage(nextStage.ordinal());
			if (this.getButcherStage() == 6) {
				this.kill();
				this.remove(RemovalReason.DISCARDED);
				if (this.level().isClientSide) {
					this.remove(RemovalReason.DISCARDED);
				}
			}
			dropButcheredItems();
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}

        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

	public void dropButcheredItems() {
	}

	public enum ButcherStage {
		NONE, //default
		NECK, //removed neck & head
		FRONT_LEFT, //removed front leg
		FRONT_RIGHT, //removed front leg
		BACK_LEFT, //removed back leg
		BACK_RIGHT, //removed back leg
		BODY; //removed all (processed body)

		public ButcherStage next() {
			return ButcherStage.values()[(this.ordinal() + 1) % ButcherStage.values().length];
		}
	}

	public boolean isGreatQuality() {
		return this.getQuality() > 25 && this.getQuality() <= 50;
	}
	public boolean isFantasticQuality() {
		return this.getQuality() > 50 && this.getQuality() <= 75;
	}
	public boolean isExquisiteQuality() {
		return this.getQuality() > 75 && this.getQuality() <= 100;
	}

	public static final EntityDataAccessor<Integer> BUTCHER_STAGE = SynchedEntityData.defineId(Corpse.class, EntityDataSerializers.INT);
	public int getButcherStage() {
		return this.entityData.get(BUTCHER_STAGE);
	}
	public void setButcherStage(int pose) {
		this.entityData.set(BUTCHER_STAGE, pose);
	}

	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Corpse.class, EntityDataSerializers.INT);
	public int getVariant() {
		return this.entityData.get(VARIANT);
	}
	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}

	public static final EntityDataAccessor<Integer> QUALITY = SynchedEntityData.defineId(Corpse.class, EntityDataSerializers.INT);
	public int getQuality() {
		return this.entityData.get(QUALITY);
	}
	public void setQuality(int i) {
		this.entityData.set(QUALITY, i);
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(BUTCHER_STAGE, 0);
		this.entityData.define(VARIANT, 0);
		this.entityData.define(QUALITY, 0);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("ButcherStage")) {
			this.setButcherStage(tag.getInt("ButcherStage"));
		}
		if (tag.contains("Variant")) {
			this.setVariant(tag.getInt("Variant"));
		}
		if(tag.contains("Quality")) {
			this.setQuality(tag.getInt("Quality"));
		}
		if (tag.contains("DecomposeTime")) {
			this.decomposeTimer = tag.getInt("DecomposeTime");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ButcherStage", this.getButcherStage());
		tag.putInt("Variant", this.getVariant());
		tag.putInt("Quality", this.getQuality());
		tag.putInt("DecomposeTime", this.decomposeTimer);
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
		super.dropCustomDeathLoot(p_21385_, p_21386_, p_21387_);
	}
}