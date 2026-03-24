package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

public class RainRotEffect extends MobEffect {
    public RainRotEffect(MobEffectCategory pCategory, int pColor) {
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
            int amp = entity.getEffect(SEEffects.RAIN_ROT.get()).getAmplifier();
            int duration = entity.getEffect(SEEffects.RAIN_ROT.get()).getDuration();
            this.setActive(entity instanceof OHorse horse && horse.isSaddled() && horse.isVehicle());
            if (this.isActive()) {
                entity.hurt(entity.damageSources().generic(), 1F);
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        if (this.isActive()) {
            return duration % 100 == 0;
        } else {
            return duration % 1200 == 0;
        }
    }

}