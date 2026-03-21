package com.dragn0007.dragnloextras.entity;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.util.IsDirtyDuck;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class OHorseExtrasLayer extends GeoRenderLayer<OHorse> {

    /*
    private static final ResourceLocation[] HALTER_LOCATION = new ResourceLocation[]{
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/white.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/orange.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/magenta.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/light_blue.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/yellow.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/lime.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/pink.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/grey.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/light_grey.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/cyan.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/purple.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/blue.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/brown.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/green.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/red.png"),
            new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/black.png")
    };
     */

    public OHorseExtrasLayer(GeoRenderer<OHorse> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, OHorse animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ResourceLocation resourceLocation;

        if (((IsDirtyDuck)animatable).livestockOverhaulScraps$isDirty()) {
            resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/dirt.png");

            RenderType renderType1 = RenderType.entityCutout(resourceLocation);
            poseStack.pushPose();
            poseStack.scale(1.0f, 1.0f, 1.0f);
            poseStack.translate(0.0d, 0.0d, 0.0d);
            poseStack.popPose();
            getRenderer().reRender(getDefaultBakedModel(animatable),
                    poseStack,
                    bufferSource,
                    animatable,
                    renderType1,
                    bufferSource.getBuffer(renderType1), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
    }
}
