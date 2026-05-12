package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.donkey.ODonkeyRender;
import com.dragn0007.dragnloextras.entity.ODonkeyExtrasLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ODonkeyRender.class)
public abstract class ODonkeyRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        ODonkeyRender self = (ODonkeyRender) (Object) this;
        self.addRenderLayer(new ODonkeyExtrasLayer(self));
    }
}
