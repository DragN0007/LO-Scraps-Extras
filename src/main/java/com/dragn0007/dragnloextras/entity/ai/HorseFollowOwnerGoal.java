package com.dragn0007.dragnloextras.entity.ai;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.capabilities.HalterCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public class HorseFollowOwnerGoal extends Goal {
   public final OHorse tamable;
   public LivingEntity owner;
   public final LevelReader level;
   public final double speedModifier;
   public final PathNavigation navigation;
   public int timeToRecalcPath;
   public final float stopDistance;
   public final float startDistance;
   public float oldWaterCost;
   public final boolean canFly;

   public HorseFollowOwnerGoal(OHorse p_25294_, double v, float dist, float stopDist, boolean p_25298_) {
      this.tamable = p_25294_;
      this.level = p_25294_.level();
      this.speedModifier = 2.0D;
      this.navigation = p_25294_.getNavigation();
      this.startDistance = dist;
      this.stopDistance = stopDist;
      this.canFly = p_25298_;
      this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
      if (!(p_25294_.getNavigation() instanceof GroundPathNavigation) && !(p_25294_.getNavigation() instanceof FlyingPathNavigation)) {
         throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
      }
   }

   public boolean canUse() {
      LivingEntity livingentity = this.tamable.getOwner();
      if (livingentity == null) {
         return false;
      } else if (this.unableToMove() || !this.tamable.isTamed() || livingentity.isSpectator()) {
         return false;
      } else if (this.tamable.distanceToSqr(livingentity) < (double)(this.startDistance * this.startDistance)) {
         return false;
      } else if (!ScrapsExtrasCommonConfig.HALTER.get()) {
         return false;
      } else if (this.tamable.getCapability(SECapabilities.HALTER_CAPABILITY).isPresent()) {
         HalterCapabilityInterface cap = this.tamable.getCapability(SECapabilities.HALTER_CAPABILITY).orElse(null);
         if (cap.hasHalter() && ScrapsExtrasCommonConfig.HALTER.get()) {
            this.owner = livingentity;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      if (this.navigation.isDone()) {
         return false;
      } else if (this.unableToMove()) {
         return false;
      } else if (this.tamable.distanceToSqr(this.owner) < (double)(this.startDistance * this.startDistance)) {
         return false;
      } else if (this.tamable.getCapability(SECapabilities.HALTER_CAPABILITY).isPresent()) {
         HalterCapabilityInterface cap = this.tamable.getCapability(SECapabilities.HALTER_CAPABILITY).orElse(null);
         if (cap.hasHalter()) {
            return true;
         }
      } else {
         return false;
      }
      return false;
   }

   public boolean unableToMove() {
      return this.tamable.isPassenger() || this.tamable.isLeashed();
   }

   public void start() {
      this.timeToRecalcPath = 0;
      this.oldWaterCost = this.tamable.getPathfindingMalus(BlockPathTypes.WATER);
      this.tamable.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
   }

   public void stop() {
      this.owner = null;
      this.navigation.stop();
      this.tamable.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
   }

   public void tick() {
      this.tamable.getLookControl().setLookAt(this.owner, 10.0F, (float)this.tamable.getMaxHeadXRot());
      if (--this.timeToRecalcPath <= 0) {
         this.timeToRecalcPath = this.adjustedTickDelay(10);
         this.navigation.moveTo(this.owner, this.speedModifier);
      }
   }
}