package com.dragn0007.dragnloextras.entity;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.capabilities.DirtyCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.HalterCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.HalterColorCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.util.ScrapsExtrasClientConfig;
import com.dragn0007.dragnpets.PetsOverhaul;
import com.dragn0007.dragnpets.entities.dog.ODog;
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
public class ODogExtrasLayer extends GeoRenderLayer<ODog> {

    public ODogExtrasLayer(GeoRenderer<ODog> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, ODog animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ResourceLocation resourceLocation;

        if (ScrapsExtrasClientConfig.RENDER_DIRT.get()) {
            if (animatable.getCapability(SECapabilities.DIRTY_CAPABILITY).isPresent()) {
                DirtyCapabilityInterface cap = animatable.getCapability(SECapabilities.DIRTY_CAPABILITY).orElse(null);
                if (cap.isDirty()) {
                    resourceLocation = new ResourceLocation(PetsOverhaul.MODID, "textures/entity/dog/dirt.png");

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
    }
}
