package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class StubbornEffect extends MobEffect {
    public StubbornEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    Random random = new Random();
    public int buckCounter = 0;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (buckCounter >= 1200 && random.nextDouble() <= 0.20) {
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