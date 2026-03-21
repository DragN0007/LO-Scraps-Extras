package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class DirtyEffect extends MobEffect {
    public DirtyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int immunityDamperTick;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            int amp = entity.getEffect(SEEffects.DIRTY.get()).getAmplifier();
            int duration = entity.getEffect(SEEffects.DIRTY.get()).getDuration();

            if (ScrapsExtrasCommonConfig.HYGIENE_SYSTEM.get())
                immunityDamperTick++;

            if (immunityDamperTick >= ScrapsExtrasCommonConfig.DIRTY_TICK.get()) {
                entity.addEffect(new MobEffectInstance(SEEffects.IMMUNOCOMPROMISED.get(), 20, amp, false, false));
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}