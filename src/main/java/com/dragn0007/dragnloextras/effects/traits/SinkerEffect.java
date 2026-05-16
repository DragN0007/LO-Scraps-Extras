package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class SinkerEffect extends MobEffect {
    public SinkerEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.isInWaterOrRain()) {
                if (entity.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    entity.removeEffect(MobEffects.MOVEMENT_SPEED);
                }
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2, false, false));
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}