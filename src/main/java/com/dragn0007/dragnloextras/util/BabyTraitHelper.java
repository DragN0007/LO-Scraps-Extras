package com.dragn0007.dragnloextras.util;

import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.capabilities.TraitCapabilityInterface;
import com.dragn0007.dragnloextras.effects.SEEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class BabyTraitHelper {
    public static void setTraitEffect(LivingEntity entity) {
        Random random = new Random();
        entity.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
            TraitCapabilityInterface traitCap = entity.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
                switch (traitCap.getTrait()) {
                    case 0:
                        entity.addEffect(new MobEffectInstance(SEEffects.BRAVE.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 1:
                        entity.addEffect(new MobEffectInstance(SEEffects.IMMUNOCOMPETENT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 2:
                        entity.addEffect(new MobEffectInstance(SEEffects.SWIFT.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 3:
                        entity.addEffect(new MobEffectInstance(SEEffects.VAULTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 4:
                        entity.addEffect(new MobEffectInstance(SEEffects.CLIMBER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 5:
                        entity.addEffect(new MobEffectInstance(SEEffects.BUSTER.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 6:
                        entity.addEffect(new MobEffectInstance(SEEffects.STURDY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 7:
                        entity.addEffect(new MobEffectInstance(SEEffects.COWARDLY.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 8:
                        entity.addEffect(new MobEffectInstance(SEEffects.IMMUNOSUPPRESSED.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 9:
                        entity.addEffect(new MobEffectInstance(SEEffects.STUBBORN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 10:
                        entity.addEffect(new MobEffectInstance(SEEffects.LAGGARD.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 11:
                        entity.addEffect(new MobEffectInstance(SEEffects.FRAIL.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
                    case 12:
                        entity.addEffect(new MobEffectInstance(SEEffects.MEAN.get(), MobEffectInstance.INFINITE_DURATION, 0, false, false));
                        break;
            }
        });
    }
}
