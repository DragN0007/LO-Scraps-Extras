package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class CowardlyEffect extends MobEffect {
    public CowardlyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    Random random = new Random();
    public int buckCounter = 0;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.getHealth() < entity.getMaxHealth() && buckCounter >= 300 && random.nextDouble() <= 0.50) {
                if (entity.isVehicle()) {
                    entity.ejectPassengers();
                }
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}