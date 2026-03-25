package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.util.AbstractOMount;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.util.IHungerHolder;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractOMount.class)
public abstract class AbstractOMountMixin extends AbstractChestedHorse implements IHungerHolder {

    @Shadow public abstract boolean isFemale();
    @Shadow public abstract boolean isMale();

    public AbstractOMountMixin(EntityType<? extends AbstractOMountMixin> entityType, Level level) {
        super(entityType, level);
    }

    @Unique int livestockOverhaulScraps$pregnantTick;

    @Inject(method = "finalizeSpawnChildFromBreeding", at = @At("HEAD"), cancellable = true)
    public void gestate(ServerLevel pLevel, Animal pAnimal, AgeableMob pBaby, CallbackInfo ci) {
        if (this.isFemale() && ScrapsExtrasCommonConfig.GESTATION.get()) {
            livestockOverhaulScraps$pregnantTick = ScrapsExtrasCommonConfig.GESTATION_TICK.get();
        }

        if (pAnimal instanceof AbstractOMount partner) {
            if (LivestockOverhaulCommonConfig.GENDERS_AFFECT_BREEDING.get()) {
                if (this.isMale()) {
                    this.setAge(LivestockOverhaulCommonConfig.MALE_COOLDOWN.get());
                } else {
                    this.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
                }
                if (partner.isMale()) {
                    partner.setAge(LivestockOverhaulCommonConfig.MALE_COOLDOWN.get());
                } else {
                    partner.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
                }
            } else {
                this.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
                partner.setAge(LivestockOverhaulCommonConfig.FEMALE_COOLDOWN.get());
            }
        }
        ci.cancel();
    }
}
