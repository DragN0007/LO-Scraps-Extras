package com.dragn0007.dragnloextras.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

public class BotflyEffect extends MobEffect {
    public BotflyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Unique
    public boolean active = false;
    @Unique
    public boolean isActive() {
        return this.active;
    }
    @Unique
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.hasEffect(SEEffects.BOTFLY_INFESTATION.get())) {
                entity.hurt(entity.damageSources().generic(), 1F);
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 4800 == 0;
    }

}