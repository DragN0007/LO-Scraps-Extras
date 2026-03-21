package com.dragn0007.dragnloextras.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ImmunocomprimisedEffect extends MobEffect {
    public ImmunocomprimisedEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int immunityDamperTick;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
//            int amp = entity.getEffect(SEEffects.IMMUNOCOMPROMISED.get()).getAmplifier();
//            int duration = entity.getEffect(SEEffects.IMMUNOCOMPROMISED.get()).getDuration();
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}