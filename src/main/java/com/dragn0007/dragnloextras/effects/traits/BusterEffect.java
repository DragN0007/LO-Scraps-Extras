package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class BusterEffect extends MobEffect {
    public BusterEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final UUID BUSTER_MOD_UUID = UUID.fromString("76651a68-70b2-4f5a-9b16-8ef2ec85b0f1");
    public static final AttributeModifier BUSTER_MOD = new AttributeModifier(BUSTER_MOD_UUID, "Buster speed mod", +1D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            AttributeInstance movementSpeed = entity.getAttribute(Attributes.ATTACK_DAMAGE);

            if (!movementSpeed.hasModifier(BUSTER_MOD))
            movementSpeed.addTransientModifier(BUSTER_MOD);
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}