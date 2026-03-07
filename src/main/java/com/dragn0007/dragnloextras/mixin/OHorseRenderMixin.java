package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.horse.OHorseRender;
import com.dragn0007.dragnloextras.entity.OHorseExtrasLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OHorseRender.class)
public abstract class OHorseRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        OHorseRender self = (OHorseRender) (Object) this;
        self.addRenderLayer(new OHorseExtrasLayer(self));
    }
}
