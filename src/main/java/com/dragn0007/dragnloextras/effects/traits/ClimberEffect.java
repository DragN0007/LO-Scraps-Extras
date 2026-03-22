package com.dragn0007.dragnloextras.effects.traits;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class ClimberEffect extends MobEffect {
    public ClimberEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    public static final UUID STEP_MOD_UUID = UUID.fromString("0cf380e4-1053-436c-b386-4fe0caf7acbc");
    public static final AttributeModifier STEP_MOD = new AttributeModifier(STEP_MOD_UUID, "Step mod", 2D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        AttributeInstance stepHeight = entity.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
        if (stepHeight != null) {
            stepHeight.removeModifier(STEP_MOD_UUID);
            stepHeight.addPermanentModifier(STEP_MOD);
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}