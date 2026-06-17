package com.dragn0007.dragnloextras.compat.wildhorse.mixin;

import com.dragn0007.dragnloextras.compat.wildhorse.EquusFerusExtrasLayer;
import com.dragn0007.wildhorse.entity.equus_ferus.EquusFerusRender;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquusFerusRender.class)
public abstract class EquusFerusRenderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addCustomLayer(EntityRendererProvider.Context ctx, CallbackInfo ci) {
        EquusFerusRender self = (EquusFerusRender) (Object) this;
        self.addRenderLayer(new EquusFerusExtrasLayer(self));
    }
}
