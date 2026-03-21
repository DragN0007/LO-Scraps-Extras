package com.dragn0007.dragnloextras.entity.mannequin;

import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.common.gui.MannequinMenu;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.items.custom.HalterItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HorseMannequin extends AbstractOMount implements GeoEntity {

	public HorseMannequin(EntityType<? extends HorseMannequin> type, Level level) {
		super(type, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void registerGoals() {
	}

	protected final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
	}

	private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
		AnimationController<T> controller = tAnimationState.getController();

		if (this.getStandPose() == 0) {
			controller.setAnimation(RawAnimation.begin().then("default", Animation.LoopType.LOOP));
		} else if (this.getStandPose() == 1) {
			controller.setAnimation(RawAnimation.begin().then("noble", Animation.LoopType.LOOP));
		} else if (this.getStandPose() == 2) {
			controller.setAnimation(RawAnimation.begin().then("resting", Animation.LoopType.LOOP));
		} else if (this.getStandPose() == 3) {
			controller.setAnimation(RawAnimation.begin().then("showoff", Animation.LoopType.LOOP));
		} else if (this.getStandPose() == 4) {
			controller.setAnimation(RawAnimation.begin().then("eat", Animation.LoopType.LOOP));
		}

		return PlayState.CONTINUE;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public void openInventory(Player player) {
		if(player instanceof ServerPlayer serverPlayer) {
			NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((containerId, inventory, p) ->
					new MannequinMenu(containerId, inventory, this.inventory, this), this.getDisplayName()), (data) -> {
				data.writeInt(this.getInventorySize());
				data.writeInt(this.getId());
			});
		}
	}

	@Override
	protected int getInventorySize() {
		return 17;
	}

	public boolean isSaddleable() {
		return this.isAlive() && !this.isBaby();
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		Item item = itemStack.getItem();

		if (itemStack.is(Items.SHEARS) && player.isShiftKeyDown()) {
			if (this.hasChest()) {
				this.dropEquipment();
				this.inventory.removeAllItems();
				this.setChest(false);
				this.playChestEquipsSound();

				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}

			if (this.hasHalter()) {
				this.setHaltered(false);
				DyeColor dyeColor = DyeColor.byId(this.getHalterColor().getId());
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

		if (this.isSaddle(itemStack) && this.isSaddleable() && !this.isSaddled()) {
			// item is a saddle, entity can be saddled, and isn't already wearing a saddle
			if (!this.level().isClientSide) {
				this.level().gameEvent(this, GameEvent.EQUIP, this.position());
				ItemStack saddleItem = itemStack.copy();
				this.inventory.setItem(this.saddleSlot(), saddleItem);
				itemStack.shrink(1);

				this.setSaddleItem(saddleItem);
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}

		if (!itemStack.isEmpty()) {
			InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, hand);
			if (interactionResult.consumesAction()) {
				return interactionResult;
			}

			if (item instanceof HalterItem halterItem) {
				this.setHaltered(true);
				DyeColor dyecolor = halterItem.getDyeColor();
				if (dyecolor != this.getHalterColor()) {
					this.setHalterColor(dyecolor);
					if (!player.getAbilities().instabuild) {
						itemStack.shrink(1);
					}
					return InteractionResult.SUCCESS;
				}
			}

			if (player.isShiftKeyDown() && itemStack.getItem() == Items.STICK) {
				Poses current = Poses.values()[this.getStandPose()];
				Poses next = current.next();
				this.setStandPose(next.ordinal());
			}

			if (!this.hasChest() && itemStack.is(Blocks.CHEST.asItem())) {
				this.setChest(true);
				this.playChestEquipsSound();
				if (!player.getAbilities().instabuild) {
					itemStack.shrink(1);
				}

				this.createInventory();
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}

			boolean canSaddle = !this.isBaby() && !this.isSaddled() && this.isSaddle(itemStack);
			if (this.isArmor(itemStack) || canSaddle) {
				this.openInventory(player);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			}
		} else {
			if (player.isSecondaryUseActive()) {
				this.openInventory(player);
				return InteractionResult.sidedSuccess(this.level().isClientSide);
			} else {
				this.setYBodyRot(player.getYHeadRot());
			}
		}

		return InteractionResult.sidedSuccess(this.level().isClientSide);
	}

	public enum Poses {
		DEFAULT,
		NOBLE,
		RESTING,
		SHOWOFF,
		EAT;

		public Poses next() {
			return Poses.values()[(this.ordinal() + 1) % Poses.values().length];
		}

	}

	@Override
	public void playEmote(String s, String s1) {
	}

	@Override
	public boolean canParent() {
		return false;
	}

	@Override
	public boolean canMate(Animal p_30553_) {
		return false;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_149506_, AgeableMob p_149507_) {
		return null;
	}

	public static final EntityDataAccessor<Integer> GENDER = SynchedEntityData.defineId(HorseMannequin.class, EntityDataSerializers.INT); //unused
	public int getGender() {
		return this.entityData.get(GENDER);
	}
	public void setGender(int gender) {
		this.entityData.set(GENDER, gender);
	}

	public static final EntityDataAccessor<Integer> STAND_POSE = SynchedEntityData.defineId(HorseMannequin.class, EntityDataSerializers.INT);
	public int getStandPose() {
		return this.entityData.get(STAND_POSE);
	}
	public void setStandPose(int pose) {
		this.entityData.set(STAND_POSE, pose);
	}

	public static final EntityDataAccessor<Boolean> HALTER = SynchedEntityData.defineId(HorseMannequin.class, EntityDataSerializers.BOOLEAN);
	public boolean hasHalter() {
		return this.entityData.get(HALTER);
	}
	public void setHaltered(boolean collared) {
		this.entityData.set(HALTER, collared);
	}

	public static final EntityDataAccessor<Integer> DATA_HALTER_COLOR = SynchedEntityData.defineId(HorseMannequin.class, EntityDataSerializers.INT);
	public DyeColor getHalterColor() {
		return DyeColor.byId(this.entityData.get(DATA_HALTER_COLOR));
	}
	public void setHalterColor(DyeColor color) {
		this.entityData.set(DATA_HALTER_COLOR, color.getId());
	}

	@Override
	public void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STAND_POSE, 0);
		this.entityData.define(GENDER, 0);
		this.entityData.define(DATA_HALTER_COLOR, DyeColor.RED.getId());
		this.entityData.define(HALTER, false);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("StandPose")) {
			this.setStandPose(tag.getInt("StandPose"));
		}
		if (tag.contains("Gender")) {
			this.setGender(tag.getInt("Gender"));
		}
		if (tag.contains("Gender")) {
			this.setYBodyRot(tag.getFloat("BodyRot"));
		}
		if (tag.contains("HalterColor", 99)) {
			this.setHalterColor(DyeColor.byId(tag.getInt("HalterColor")));
		}

		if(tag.contains("Haltered")) {
			this.setHaltered(tag.getBoolean("Haltered"));
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("StandPose", this.getStandPose());
		tag.putInt("Gender", this.getGender());
		tag.putFloat("BodyRot", this.yBodyRotO);
		tag.putByte("HalterColor", (byte)this.getHalterColor().getId());
		tag.putBoolean("Haltered", this.hasHalter());
	}

	@Override
	@javax.annotation.Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance instance, MobSpawnType spawnType, @javax.annotation.Nullable SpawnGroupData data, @javax.annotation.Nullable CompoundTag tag) {
		this.setGender(0);
		return super.finalizeSpawn(serverLevelAccessor, instance, spawnType, data, tag);
	}
}