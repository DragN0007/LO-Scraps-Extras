package com.dragn0007.dragnloextras.mixin;

import com.dragn0007.dragnloextras.entity.ODogExtrasLayer;
import com.dragn0007.dragnloextras.entity.OWolfExtrasLayer;
import com.dragn0007.dragnpets.entities.wolf.OWolfRender;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OWolfRender.class)
public abstract class OWolfRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        OWolfRender self = (OWolfRender) (Object) this;
        self.addRenderLayer(new OWolfExtrasLayer(self));
    }
}
