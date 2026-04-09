package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

public class EarInfectionEffect extends MobEffect {
    public EarInfectionEffect(MobEffectCategory pCategory, int pColor) {
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
    public int turnIntoInfectionTick = 0;
    Random random = new Random();

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity.hasEffect(SEEffects.EAR_INFECTION.get())) {
                turnIntoInfectionTick++;
                if (turnIntoInfectionTick >= ScrapsExtrasCommonConfig.INFECTION_TICK.get() && ScrapsExtrasCommonConfig.INFECTIONS.get()) {
                    turnIntoInfectionTick = 0;
                    if (random.nextDouble() <= 0.0008) {
                        entity.addEffect(new MobEffectInstance(SEEffects.INFECTION.get(), MobEffectInstance.INFINITE_DURATION, 1, true, false));
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