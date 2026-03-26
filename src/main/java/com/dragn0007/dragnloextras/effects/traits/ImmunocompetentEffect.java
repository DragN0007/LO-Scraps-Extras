package com.dragn0007.dragnloextras.effects.traits;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.util.ISickModHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class ImmunocompetentEffect extends MobEffect implements ISickModHolder {
    public ImmunocompetentEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    int livestockOverhaulScraps$becomeSickChanceMod;
    @Override
    public void setSickChanceMod(int sickChanceMod) {
        livestockOverhaulScraps$becomeSickChanceMod = sickChanceMod;
    }

    @Override
    public void setSickChance(int sickChance) {
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            if (entity instanceof OHorse horse) {
                ((ISickModHolder) horse).setSickChanceMod(-25);
            }
        }
    }

    //how fast the entity takes damage (how fast applyEffectTick() is run)
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

}