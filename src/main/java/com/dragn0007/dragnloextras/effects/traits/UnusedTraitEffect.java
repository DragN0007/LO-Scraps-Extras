package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class UnusedTraitEffect extends MobEffect {
    public UnusedTraitEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    //an effect class that doesn't really do anything. generally used for traits that are only handled in the entities' classes.

   @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}