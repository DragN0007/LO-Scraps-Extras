package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SwiftEffect extends MobEffect {
    public SwiftEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final UUID SWIFT_MOD_UUID = UUID.fromString("5d4b254d-f5ea-4e22-bd63-ead4462fbeed");
    public static final AttributeModifier SWIFT_MOD = new AttributeModifier(SWIFT_MOD_UUID, "Swift speed mod", +0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);

            if (!movementSpeed.hasModifier(SWIFT_MOD))
            movementSpeed.addTransientModifier(SWIFT_MOD);
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}