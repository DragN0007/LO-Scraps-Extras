package com.dragn0007.dragnloextras.entity.butchering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LlamaCorpseRender extends GeoEntityRenderer<LlamaCorpse> {

    public LlamaCorpseRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LlamaCorpseModel());
    }

    @Override
    public void preRender(PoseStack poseStack, LlamaCorpse entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (entity.getButcherStage() == 0) {
            model.getBone("head").ifPresent(b -> b.setHidden(false));
            model.getBone("neck").ifPresent(b -> b.setHidden(false));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 1) {
            model.getBone("head").ifPresent(b -> b.setHidden(true));
            model.getBone("neck").ifPresent(b -> b.setHidden(true));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 2) {
            model.getBone("head").ifPresent(b -> b.setHidden(true));
            model.getBone("neck").ifPresent(b -> b.setHidden(true));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 3) {
            model.getBone("head").ifPresent(b -> b.setHidden(true));
            model.getBone("neck").ifPresent(b -> b.setHidden(true));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 4) {
            model.getBone("head").ifPresent(b -> b.setHidden(true));
            model.getBone("neck").ifPresent(b -> b.setHidden(true));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 5) {
            model.getBone("head").ifPresent(b -> b.setHidden(true));
            model.getBone("neck").ifPresent(b -> b.setHidden(true));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(true));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(true));
        } else {
            model.getBone("head").ifPresent(b -> b.setHidden(false));
            model.getBone("neck").ifPresent(b -> b.setHidden(false));
            model.getBone("front_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("front_right_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_left_leg").ifPresent(b -> b.setHidden(false));
            model.getBone("back_right_leg").ifPresent(b -> b.setHidden(false));
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

}