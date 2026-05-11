package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.util.IHungerHolder;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractOMount.class)
public abstract class AbstractOMountMixin extends AbstractChestedHorse implements IHungerHolder {

//    @Shadow public abstract boolean isFemale();
//    @Shadow public abstract boolean isMale();
//
    public AbstractOMountMixin(EntityType<? extends AbstractOMountMixin> entityType, Level level) {
        super(entityType, level);
    }

    //TODO
    @Override
    public void travel(Vec3 vec3) {
        if (this.isVehicle() && this.isInWater() && !this.onGround() && ScrapsExtrasCommonConfig.SWIMMING.get()) {
            double waterLevel = this.getFluidHeight(FluidTags.WATER);
            Vec3 movement = this.getDeltaMovement();
            double swim_speed;
            if (this.hasEffect(SEEffects.SWIMMER.get())) {
                swim_speed = 2.0D;
            } else if (this.hasEffect(SEEffects.SINKER.get())) {
                swim_speed = -0.5D;
            } else {
                swim_speed = 1.0D;
            }

            if (waterLevel > 1.3) {
                this.setDeltaMovement(movement.x * swim_speed, 0.02D, movement.z * swim_speed);
            } else {
                if (waterLevel < 1.0) {
                    this.setDeltaMovement(movement.x * swim_speed, -0.04D, movement.z * swim_speed);
                }
                if (this.horizontalCollision) {
                    this.setDeltaMovement(movement.x * swim_speed, 0.3D, movement.z * swim_speed);
                }
            }
            super.travel(vec3);
        } else {
            super.travel(vec3);
        }
    }

    //
//    @Unique int livestockOverhaulScraps$pregnantTick;
//
//
//    @Inject(method = "finalizeSpawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
//    public void gestate(ServerLevel pLevel, Animal pAnimal, AgeableMob pBaby, CallbackInfo ci) {
//        if (this.isFemale() && ScrapsExtrasCommonConfig.GESTATION.get()) {
//            livestockOverhaulScraps$pregnantTick = ScrapsExtrasCommonConfig.GESTATION_TICK.get();
//        }
//
//        if (pAnimal instanceof AbstractOMount partner) {
//            if (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
//                if (this.isMale()) {
//                    this.setAge(LivestockOverhaulCommonConfig.MALE_COOLDOWN.get());
//                } else {
//                    this.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
//                }
//                if (partner.isMale()) {
//                    partner.setAge(LivestockOverhaulCommonConfig.MALE_COOLDOWN.get());
//                } else {
//                    partner.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
//                }
//            } else {
//                this.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
//                partner.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
//            }
//        }
//        ci.cancel();
//    }
}
