package com.dragn0007.dragnloextras.entity.ai;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.SleepingCapabilityInterface;
import com.dragn0007.dragnloextras.network.SyncSleepingAsLeaderPacket;
import com.dragn0007.dragnloextras.network.SyncSleepingPacket;
import com.dragn0007.dragnloextras.util.ISleepAsLeaderHolder;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class EquineSleepGoal extends Goal implements ISleepAsLeaderHolder {
   private final OHorse mob;

   public boolean sleepingAsLeader = false;
   public boolean isSleepingAsLeader() {
      return this.sleepingAsLeader;
   }
   public void setSleepingAsLeader(boolean sleepingAsLeader) {
      this.sleepingAsLeader = sleepingAsLeader;
   }

   public EquineSleepGoal(OHorse p_25898_) {
      this.mob = p_25898_;
      this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
   }

   public boolean canContinueToUse() {
      return canUse();
   }

   public boolean canUse() {
      if (this.mob.isInWaterOrBubble()) {
         return false;
      } else if (!this.mob.onGround()) {
         return false;
      } else if (this.mob.isVehicle()) {
         return false;
      } else if (this.mob.getHealth() < this.mob.getMaxHealth()) {
         return false;
      } else if (mob.level().isDay()) {
         return false;
      } else if (!ScrapsExtrasCommonConfig.SLEEPING.get()) {
         return false;
      } else return mob.level().isNight();
   }

   public void start() {
      SleepingCapabilityInterface sleepingCap = this.mob.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
      this.mob.getNavigation().stop();
      sleepingCap.setSleeping(true);
      SyncSleepingPacket.syncToTracking(this.mob, true);
      if (this.mob.isFollower()) {
         ((ISleepAsLeaderHolder) this.mob).setSleepingAsLeader(false);
         SyncSleepingAsLeaderPacket.syncToTracking(this.mob, false);
      } else {
         ((ISleepAsLeaderHolder) this.mob).setSleepingAsLeader(true);
         SyncSleepingAsLeaderPacket.syncToTracking(this.mob, true);
      }
   }

   public void stop() {
      SleepingCapabilityInterface sleepingCap = this.mob.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
      sleepingCap.setSleeping(false);
      SyncSleepingPacket.syncToTracking(this.mob, false);
      ((ISleepAsLeaderHolder) this.mob).setSleepingAsLeader(false);
      SyncSleepingAsLeaderPacket.syncToTracking(this.mob, false);
   }
}