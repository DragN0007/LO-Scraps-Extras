package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class VaulterEffect extends MobEffect {
    public VaulterEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final UUID VAULTER_MOD_UUID = UUID.fromString("baa3a32c-1091-4d44-8a10-a4794d7bc4c8");
    public static final AttributeModifier VAULTER_MOD = new AttributeModifier(VAULTER_MOD_UUID, "Vaulter speed mod", +0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            AttributeInstance movementSpeed = entity.getAttribute(Attributes.JUMP_STRENGTH);

            if (!movementSpeed.hasModifier(VAULTER_MOD))
            movementSpeed.addTransientModifier(VAULTER_MOD);
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}