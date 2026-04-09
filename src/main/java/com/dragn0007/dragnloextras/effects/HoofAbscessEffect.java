package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

public class HoofAbscessEffect extends MobEffect {
    public HoofAbscessEffect(MobEffectCategory pCategory, int pColor) {
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
            if (entity.hasEffect(SEEffects.HOOF_ABSCESS.get())) {
                this.setActive(entity instanceof OHorse horse && horse.isVehicle());
                if (this.isActive()) {
                    entity.hurt(entity.damageSources().generic(), 1F);
                }

                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1, false, false));

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
        if (this.isActive()) {
            return duration % 20 == 0;
        } else {
            return duration % 1200 == 0;
        }
    }

}