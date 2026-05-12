package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnlivestock.entities.unicorn.UnicornRender;
import com.dragn0007.dragnloextras.entity.UnicornExtrasLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UnicornRender.class)
public abstract class UnicornRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        UnicornRender self = (UnicornRender) (Object) this;
        self.addRenderLayer(new UnicornExtrasLayer(self));
    }
}
