package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SwimmerEffect extends MobEffect {
    public SwimmerEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.isInWaterOrRain()) {
                entity.setSpeed(entity.getSpeed() * 2);
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}