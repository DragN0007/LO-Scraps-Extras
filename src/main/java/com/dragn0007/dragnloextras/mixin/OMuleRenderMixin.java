package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.mule.OMuleRender;
import com.dragn0007.dragnloextras.entity.OMuleExtrasLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OMuleRender.class)
public abstract class OMuleRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        OMuleRender self = (OMuleRender) (Object) this;
        self.addRenderLayer(new OMuleExtrasLayer(self));
    }
}
