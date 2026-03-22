package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class SturdyEffect extends MobEffect {
    public SturdyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {

        }
    }

    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int p_19419_) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(4 * (p_19419_ + 1)));
        super.removeAttributeModifiers(entity, map, p_19419_);
    }

    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int p_19423_) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(4 * (p_19423_ + 1)));
        super.addAttributeModifiers(entity, map, p_19423_);
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}