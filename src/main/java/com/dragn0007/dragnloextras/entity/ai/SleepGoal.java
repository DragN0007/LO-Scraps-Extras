package com.dragn0007.dragnloextras.entity.ai;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.SleepingCapabilityInterface;
import com.dragn0007.dragnloextras.network.SyncSleepingPacket;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import com.dragn0007.dragnpets.entities.cat.OCat;
import com.dragn0007.dragnpets.entities.dog.ODog;
import com.dragn0007.dragnpets.entities.ocelot.OOcelot;
import com.dragn0007.dragnpets.entities.wolf.OWolf;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

import java.util.EnumSet;

public class SleepGoal extends Goal {
   private final Animal mob;

   public SleepGoal(Animal p_25898_) {
      this.mob = p_25898_;
      this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
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
//      } else if (this.mob.getClass() != OHorse.class && this.mob.getClass() != ODog.class) {
//         return false;
      } else if (this.mob instanceof ODog tamable && (!tamable.wasToldToWander() && tamable.isTame() && !tamable.isOrderedToSit())) {
         return false;
      } else if (this.mob instanceof OWolf tamable && (!tamable.wasToldToWander() && tamable.isTame() && !tamable.isOrderedToSit())) {
         return false;
      } else if (this.mob instanceof OCat tamable && (!tamable.wasToldToWander() && tamable.isTame() && !tamable.isOrderedToSit())) {
         return false;
      } else if (this.mob instanceof OOcelot tamable && (!tamable.wasToldToWander() && tamable.isTame() && !tamable.isOrderedToSit())) {
         return false;
      } else if (!ScrapsExtrasCommonConfig.SLEEPING.get()) {
         return false;
      } else return mob.level().isNight();
   }

   public void start() {
      if (this.mob.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
         SleepingCapabilityInterface sleepingCap = this.mob.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
         this.mob.getNavigation().stop();
         sleepingCap.setSleeping(true);
         SyncSleepingPacket.syncToTracking(this.mob, true);
      }
   }

   @Override
   public void tick() {
      super.tick();
      if (!canUse()) {
         stop();
      }
   }

   public void stop() {
      if (this.mob.getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
         SleepingCapabilityInterface sleepingCap = this.mob.getCapability(SECapabilities.SLEEPING_CAPABILITY).orElse(null);
         sleepingCap.setSleeping(false);
         SyncSleepingPacket.syncToTracking(this.mob, false);
      }
   }
}