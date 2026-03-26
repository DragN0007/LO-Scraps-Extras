package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class HungerEffect extends MobEffect {
    public HungerEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int immunityDamperTick;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.hasEffect(SEEffects.HUNGER.get())) {
                int amp = entity.getEffect(SEEffects.HUNGER.get()).getAmplifier();
                int duration = entity.getEffect(SEEffects.HUNGER.get()).getDuration();

                if (ScrapsExtrasCommonConfig.FEEDING_SYSTEM.get())
                    immunityDamperTick++;

                if (ScrapsExtrasCommonConfig.HUNGRY_IMMUNITY_DAMPER.get()) {
                    if (immunityDamperTick >= ScrapsExtrasCommonConfig.HUNGRY_IMMUNITY_DAMPER_TICK.get()) {
                        entity.addEffect(new MobEffectInstance(SEEffects.IMMUNOCOMPROMISED.get(), 100, amp, false, false));
                    }
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