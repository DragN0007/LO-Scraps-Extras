package com.dragn0007.dragnloextras.util;

import com.dragn0007.dragnloextras.capabilities.ImmunityCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.TraitCapabilityInterface;
import com.dragn0007.dragnloextras.network.SyncImmunityPacket;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class BaseImmunityHelper {
    public static void setBaseImmunity(LivingEntity entity) {
        Random random = new Random();
        TraitCapabilityInterface traitCap = entity.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
        ImmunityCapabilityInterface immunityCap = entity.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);

        if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
            int baseImmunity = random.nextInt(1, 50);
            immunityCap.setImmunity(random.nextInt(baseImmunity));
            SyncImmunityPacket.syncToTracking(entity, random.nextInt(baseImmunity));

            int traitImmunityAdditionMajor = random.nextInt(1, 50) + 25;
            int traitImmunityAdditionMinor = random.nextInt(1, 25);
            if (traitCap.getTrait() == 1) { //immunocompetent (major)
                if (immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMajor) < 100) {
                    immunityCap.setImmunity(immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMajor));
                    SyncImmunityPacket.syncToTracking(entity, immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMajor));
                } else {
                    immunityCap.setImmunity(100);
                    SyncImmunityPacket.syncToTracking(entity, 100);
                }
            } else if (traitCap.getTrait() == 8) { //immunosuppressed (major)
                int result = Math.max(1, immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMajor));
                immunityCap.setImmunity(result);
                SyncImmunityPacket.syncToTracking(entity, result);
                immunityCap.setImmunity(immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMajor));
                SyncImmunityPacket.syncToTracking(entity, immunityCap.getImmunity() - (random.nextInt(traitImmunityAdditionMajor)));
            } else if (traitCap.getTrait() == 6) { //sturdy (minor)
                if (immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMajor) < 100) {
                    immunityCap.setImmunity(immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMinor));
                    SyncImmunityPacket.syncToTracking(entity, immunityCap.getImmunity() + random.nextInt(traitImmunityAdditionMinor));
                } else {
                    immunityCap.setImmunity(100);
                    SyncImmunityPacket.syncToTracking(entity, 100);
                }
            } else if (traitCap.getTrait() == 11) { //frail (minor)
                int result = Math.max(1, immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMinor));
                immunityCap.setImmunity(result);
                SyncImmunityPacket.syncToTracking(entity, result);
                immunityCap.setImmunity(immunityCap.getImmunity() - random.nextInt(traitImmunityAdditionMinor));
                SyncImmunityPacket.syncToTracking(entity, immunityCap.getImmunity() - (random.nextInt(traitImmunityAdditionMinor)));
            }

            if (immunityCap.getImmunity() > 100) {
                immunityCap.setImmunity(100);
                SyncImmunityPacket.syncToTracking(entity, 100);
            } else if (immunityCap.getImmunity() < 1) {
                immunityCap.setImmunity(1);
                SyncImmunityPacket.syncToTracking(entity, 1);
            }

            ((ISickModHolder) entity).setSickChance(100 - immunityCap.getImmunity());
        }
    }
}
