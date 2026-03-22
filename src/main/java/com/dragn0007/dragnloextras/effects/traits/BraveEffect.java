package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BraveEffect extends MobEffect {
    public BraveEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int regenHealthCounter = 0;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        regenHealthCounter++;
        if (entity.getHealth() < entity.getMaxHealth() && regenHealthCounter >= 100 && entity.isAlive()) {
            entity.setHealth(entity.getHealth() + 4);
            regenHealthCounter = 0;
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}