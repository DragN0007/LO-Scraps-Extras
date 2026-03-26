package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class AbrasionEffect extends MobEffect {
    public AbrasionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int turnIntoInfectionTick = 0;
    Random random = new Random();

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.hasEffect(SEEffects.ABRASION.get())) {
                turnIntoInfectionTick++;
                if (turnIntoInfectionTick >= ScrapsExtrasCommonConfig.INFECTION_TICK.get() && ScrapsExtrasCommonConfig.INFECTIONS.get()) {
                    turnIntoInfectionTick = 0;
                    if (random.nextDouble() <= 0.01) {
                        entity.addEffect(new MobEffectInstance(SEEffects.INFECTION.get(), MobEffectInstance.INFINITE_DURATION, 1, true, false));
                    }
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}