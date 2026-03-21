package com.dragn0007.dragnloextras.entity.mannequin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HorseMannequinRender extends GeoEntityRenderer<HorseMannequin> {

    public HorseMannequinRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HorseMannequinModel());
        this.addRenderLayer(new HorseMannequinCaparisonLayer(this));
        this.addRenderLayer(new HorseMannequinSaddleLayer(this));
        this.addRenderLayer(new HorseMannequinArmorLayer(this));
        this.addRenderLayer(new HorseMannequinCarpetLayer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, HorseMannequin animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        if (animatable.hasChest()) {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(false));
        } else {
            model.getBone("saddlebags").ifPresent(b -> b.setHidden(true));
        }

        super.preRender(poseStack, this.animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}