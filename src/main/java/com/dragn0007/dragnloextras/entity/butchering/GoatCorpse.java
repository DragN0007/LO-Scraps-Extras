package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnlivestock.entities.goat.OGoatModel;
import com.dragn0007.dragnlivestock.items.LOItems;
import com.dragn0007.dragnloextras.items.SEItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;

public class GoatCorpse extends Corpse implements GeoEntity {

	public GoatCorpse(EntityType<? extends GoatCorpse> type, Level level) {
		super(type, level);
		this.xpReward = 0;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
	}

	Item baseMeat;
	public void dropButcheredItems() {
		if (ModList.get().isLoaded("tfc")) {
			baseMeat = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tfc", "food/chevon"));
		} else {
			baseMeat = LOItems.CHEVON.get();
		}

		if (this.getButcherStage() == 1) {
			this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 2)), 0F);
			if (this.isGreatQuality()) {
				this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 2)), 0F);
			} else if (this.isFantasticQuality()) {
				this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 3)), 0F);
			} else if (this.isExquisiteQuality()) {
				this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 4)), 0F);
			}
		} else if (this.getButcherStage() == 2 || this.getButcherStage() == 3 || this.getButcherStage() == 4 || this.getButcherStage() == 5) {
			this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.CHEVON_LEG.get(), 1), 0F);
			if (this.isGreatQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
			} else if (this.isFantasticQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_LEG.get(), random.nextInt(1)), 0F);
			} else if (this.isExquisiteQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 4)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_LEG.get(), random.nextInt(1, 2)), 0F);
			}
		} else if (this.getButcherStage() == 6) {
			this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
			this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 4)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.CHEVON_RIB.get(), random.nextInt(1, 4)), 0F);
			this.spawnAtLocation(new ItemStack(SEItems.CHEVON_FLANK.get(), random.nextInt(1, 2)), 0F);
			if (this.isGreatQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_RIB.get(), random.nextInt(1)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_FLANK.get(), random.nextInt(1)), 0F);
			} else if (this.isFantasticQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_RIB.get(), random.nextInt(1, 2)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_FLANK.get(), random.nextInt(1)), 0F);
			} else if (this.isExquisiteQuality()) {
				this.spawnAtLocation(new ItemStack(Items.BONE, random.nextInt(1, 4)), 0F);
				this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(1, 3)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_RIB.get(), random.nextInt(2)), 0F);
				this.spawnAtLocation(new ItemStack(SEItems.CHEVON_FLANK.get(), random.nextInt(2)), 0F);
			}
			if (ModList.get().isLoaded("tfc")) {
				Item hide = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tfc", "medium_raw_hide"));
				this.spawnAtLocation(new ItemStack(hide, 1), 0F);
			}
		}
	}

	public static final EntityDataAccessor<Integer> BUTCHER_STAGE = SynchedEntityData.defineId(GoatCorpse.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(GoatCorpse.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> QUALITY = SynchedEntityData.defineId(GoatCorpse.class, EntityDataSerializers.INT);
	public ResourceLocation getTextureLocation() {
		return OGoatModel.Variant.variantFromOrdinal(getVariant()).resourceLocation;
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
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource p_21385_, int p_21386_, boolean p_21387_) {
		super.dropCustomDeathLoot(p_21385_, p_21386_, p_21387_);
		if (ModList.get().isLoaded("tfc")) {
			baseMeat = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tfc", "food/chevon"));
		} else {
			baseMeat = LOItems.CHEVON.get();
		}

		//just drops basic things if killed without butchering
		//not recommended but works okay if you're in a pinch
		if (this.getButcherStage() < 6) {
			this.spawnAtLocation(new ItemStack(baseMeat, random.nextInt(3)), 0F);
			if (ModList.get().isLoaded("tfc")) {
				Item hide = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tfc", "medium_raw_hide"));
				this.spawnAtLocation(new ItemStack(hide, random.nextInt(1)), 0F);
			}
		}
	}
}