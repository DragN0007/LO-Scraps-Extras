package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class LaggardEffect extends MobEffect {
    public LaggardEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final UUID LAGGARD_MOD_UUID = UUID.fromString("55e4df2b-c969-4c00-84c0-4fa2b9ba012f");
    public static final AttributeModifier LAGGARD_MOD = new AttributeModifier(LAGGARD_MOD_UUID, "Laggard speed mod", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);

            if (!movementSpeed.hasModifier(LAGGARD_MOD))
            movementSpeed.addTransientModifier(LAGGARD_MOD);
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}