package com.dragn0007.dragnloextras.entity;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulClientConfig;
import com.dragn0007.dragnloextras.capabilities.DirtyCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.HalterCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.HalterColorCapabilityInterface;
import com.dragn0007.dragnloextras.capabilities.SECapabilities;
import com.dragn0007.dragnloextras.util.ScrapsExtrasClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class OMuleExtrasLayer extends GeoRenderLayer<OMule> {

    public OMuleExtrasLayer(GeoRenderer<OMule> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, OMule animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        Player player = Minecraft.getInstance().player;
        double distanceSq = animatable.distanceToSqr(player);
        boolean atCullDistance = distanceSq > LivestockOverhaulClientConfig.CULL_LAYERS_DISTANCE.get();
        if (atCullDistance) return;

        ResourceLocation resourceLocation;

        if (!ScrapsExtrasClientConfig.RENDER_DIRT.get() && !ScrapsExtrasClientConfig.RENDER_HALTER.get()) {
            return;
        }

        if (ScrapsExtrasClientConfig.RENDER_HALTER.get()) {
            if (animatable.getCapability(SECapabilities.HALTER_CAPABILITY).isPresent()) {
                HalterCapabilityInterface cap = animatable.getCapability(SECapabilities.HALTER_CAPABILITY).orElse(null);
                HalterColorCapabilityInterface colorcap = animatable.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).orElse(null);
                if (cap.hasHalter()) {
                    resourceLocation = new ResourceLocation(LivestockOverhaul.MODID, "textures/entity/horse/halter/" + colorcap.getHalterColor().getName() + ".png");

                    RenderType renderType1 = RenderType.entityCutout(resourceLocation);
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

        if (ScrapsExtrasClientConfig.RENDER_DIRT.get()) {
            if (animatable.getCapability(SECapabilities.DIRTY_CAPABILITY).isPresent()) {
                DirtyCapabilityInterface cap = animatable.getCapability(SECapabilities.DIRTY_CAPABILITY).orElse(null);
                if (cap.isDirty()) {
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
    }
}
