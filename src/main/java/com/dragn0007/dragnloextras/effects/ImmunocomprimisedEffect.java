package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnloextras.capabilities.ImmunityCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ImmunocomprimisedEffect extends MobEffect {
    public ImmunocomprimisedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            int amp = entity.getEffect(SEEffects.IMMUNOCOMPROMISED.get()).getAmplifier();
            int duration = entity.getEffect(SEEffects.IMMUNOCOMPROMISED.get()).getDuration();
            ImmunityCapabilityInterface immunityCap = entity.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}