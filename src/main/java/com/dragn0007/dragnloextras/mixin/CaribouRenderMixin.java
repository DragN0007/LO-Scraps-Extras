package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.camel.OCamelRender;
import com.dragn0007.dragnlivestock.entities.caribou.Caribou;
import com.dragn0007.dragnlivestock.entities.caribou.CaribouRender;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkeyRender;
import com.dragn0007.dragnloextras.entity.CaribouExtrasLayer;
import com.dragn0007.dragnloextras.entity.OCamelExtrasLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CaribouRender.class)
public abstract class CaribouRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        CaribouRender self = (CaribouRender) (Object) this;
        self.addRenderLayer(new CaribouExtrasLayer(self));
    }
}
