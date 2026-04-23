package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnlivestock.entities.cow.OCowModel;
import com.dragn0007.dragnloextras.items.SEItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CowCorpse extends Mob implements GeoEntity {

	public CowCorpse(EntityType<? extends CowCorpse> type, Level level) {
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
		if (this.getButcherStage() == 1) {
			this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.BEEF_BRISKET.get(), 1), 0F);
			if (this.isGreatQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 2)), 0F);
			} else if (this.isFantasticQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 3)), 0F);
			} else if (this.isExquisiteQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 4)), 0F);
				this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 4)), 0F);
			}
			if (this.isMeatBreed()) {
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 2)), 0F);
			}
		} else if (this.getButcherStage() == 2 || this.getButcherStage() == 3 || this.getButcherStage() == 4 || this.getButcherStage() == 5) {
			this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.BEEF_SHANK.get(), 1), 0F);
			if (this.isGreatQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
			} else if (this.isFantasticQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_SHANK.get(), random.nextInt(1)), 0F);
			} else if (this.isExquisiteQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 4)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_SHANK.get(), random.nextInt(1, 2)), 0F);
			}
			if (this.isMeatBreed()) {
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(2)), 0F);
			}
		} else if (this.getButcherStage() == 6) {
			this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 4)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.BEEF_RIB.get(), random.nextInt(1, 4)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.BEEF_CHUCK.get(), random.nextInt(1, 2)), 0F);
			if (this.isGreatQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_RIB.get(), random.nextInt(1)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_CHUCK.get(), random.nextInt(1)), 0F);
			} else if (this.isFantasticQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_RIB.get(), random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_CHUCK.get(), random.nextInt(1)), 0F);
			} else if (this.isExquisiteQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 4)), 0F);
				this.spawnAtLocation(new ItemStack(Items.LEATHER, random.nextInt(1, 4)), 0F);
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_RIB.get(), random.nextInt(2)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.BEEF_CHUCK.get(), random.nextInt(2)), 0F);
			}
			if (this.isMeatBreed()) {
				this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(1, 4)), 0F);
			}
		}
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

	public boolean meat = false;
	public boolean isMeatBreed() {
		return this.meat;
	}
	public void setMeatBreed(boolean breed) {
		this.meat = breed;
	}

	public boolean normal = false;
	public void setNormalBreed(boolean breed) {
		this.normal = breed;
	}

	public boolean mini = false;
	public boolean isMiniBreed() {
		return this.mini;
	}
	public void setMiniBreed(boolean breed) {
		this.mini = breed;
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

	public static final EntityDataAccessor<Integer> BUTCHER_STAGE = SynchedEntityData.defineId(CowCorpse.class, EntityDataSerializers.INT);
	public int getButcherStage() {
		return this.entityData.get(BUTCHER_STAGE);
	}
	public void setButcherStage(int pose) {
		this.entityData.set(BUTCHER_STAGE, pose);
	}

	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(CowCorpse.class, EntityDataSerializers.INT);
	public ResourceLocation getTextureLocation() {
		return OCowModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
	}
	public int getVariant() {
		return this.entityData.get(VARIANT);
	}
	public void setVariant(int variant) {
		this.entityData.set(VARIANT, variant);
	}

	public static final EntityDataAccessor<Integer> QUALITY = SynchedEntityData.defineId(CowCorpse.class, EntityDataSerializers.INT);
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
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("ButcherStage", this.getButcherStage());
		tag.putInt("Variant", this.getVariant());
		tag.putInt("Quality", this.getQuality());
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
		super.dropCustomDeathLoot(p_21385_, p_21386_, p_21387_);
		//just drops basic things if killed without butchering
		//not recommended but works okay if you're in a pinch
		if (this.getButcherStage() < 6) {
			this.spawnAtLocation(new ItemStack(Items.BEEF, random.nextInt(3)), 0F);
			this.spawnAtLocation(new ItemStack(Items.LEATHER, 1), 0F);
		}
	}
}