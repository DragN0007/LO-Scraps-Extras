package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class RabiesEffect extends MobEffect {
    public RabiesEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public int deathTick;

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.hasEffect(SEEffects.RABIES.get())) {
                int amp = entity.getEffect(SEEffects.RABIES.get()).getAmplifier();
                int duration = entity.getEffect(SEEffects.RABIES.get()).getDuration();

                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20, amp + 1, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, amp, false, false));

                if (ScrapsExtrasCommonConfig.RABIES.get() && ScrapsExtrasCommonConfig.LETHAL_RABIES.get())
                    deathTick++;

                if (deathTick >= ScrapsExtrasCommonConfig.RABIES_DEATH_TICK.get()) {
                    float damage;
                    if (amplifier > 0) {
                        damage = 1F * amplifier;
                    } else {
                        damage = 1F;
                    }
                    entity.hurt(entity.damageSources().generic(), damage);
                }
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (deathTick >= ScrapsExtrasCommonConfig.RABIES_DEATH_TICK.get()) {
            if (amplifier > 0) {
                return duration % (60 / amplifier) == 0;
            } else {
                return duration % 40 == 0;
            }
        } else {
            return true;
        }
    }

}