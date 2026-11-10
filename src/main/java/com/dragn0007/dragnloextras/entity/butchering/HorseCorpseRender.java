package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnlivestock.util.LOUtils;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Optional;

public class HorseCorpseRender extends GeoEntityRenderer<HorseCorpse> {

    public HorseCorpseRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HorseCorpseModel());
    }

    @Override
    public void preRender(PoseStack poseStack, HorseCorpse entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Optional<GeoBone> body = this.getGeoModel().getBone("body");
        Optional<GeoBone> head = this.getGeoModel().getBone("head");
        Optional<GeoBone> neck = this.getGeoModel().getBone("neck");
        Optional<GeoBone> body_armor = this.getGeoModel().getBone("body_armor");
        Optional<GeoBone> neck_armor = this.getGeoModel().getBone("neck_armor");
        Optional<GeoBone> wagon_harness = this.getGeoModel().getBone("wagon_harness");
        Optional<GeoBone> saddlebags = this.getGeoModel().getBone("saddlebags");
        Optional<GeoBone> saddle = this.getGeoModel().getBone("saddle");
        Optional<GeoBone> reins = this.getGeoModel().getBone("saddle2");
        Optional<GeoBone> mane = this.getGeoModel().getBone("mane");
        Optional<GeoBone> mane_roached = this.getGeoModel().getBone("roached");
        Optional<GeoBone> mane_buttons = this.getGeoModel().getBone("buttons");
        Optional<GeoBone> mane_long = this.getGeoModel().getBone("long");
        Optional<GeoBone> tail = this.getGeoModel().getBone("tail");
        Optional<GeoBone> feathering_fr = this.getGeoModel().getBone("front_right_feathering");
        Optional<GeoBone> feathering_fl = this.getGeoModel().getBone("front_left_feathering");
        Optional<GeoBone> feathering_br = this.getGeoModel().getBone("back_right_feathering");
        Optional<GeoBone> feathering_bl = this.getGeoModel().getBone("back_left_feathering");
        Optional<GeoBone> front_left_leg = this.getGeoModel().getBone("front_left_leg");
        Optional<GeoBone> front_right_leg = this.getGeoModel().getBone("front_right_leg");
        Optional<GeoBone> back_left_leg = this.getGeoModel().getBone("back_left_leg");
        Optional<GeoBone> back_right_leg = this.getGeoModel().getBone("back_right_leg");

        //culls unnecessary bones at a certain distance to improve performance
        Player player = Minecraft.getInstance().player;
        double distanceSq = animatable.distanceToSqr(player);
        boolean atCullDistance = distanceSq > LivestockOverhaulClientConfig.CULL_CUBES_DISTANCE.get();
        if (player != null) {
            if (LivestockOverhaulClientConfig.CULL_HIDDEN.get())
                if (body.isPresent()) {body.ifPresent(b -> b.setHidden(LOUtils.entityIsHidden(animatable)));}
            if (saddle.isPresent()) {saddle.ifPresent(b -> b.setHidden(true));}
            if (reins.isPresent()) {reins.ifPresent(b -> b.setHidden(true));}
            if (saddlebags.isPresent()) {saddlebags.ifPresent(b -> b.setHidden(true));}
            if (wagon_harness.isPresent()) {wagon_harness.ifPresent(b -> b.setHidden(true));}
            if (body_armor.isPresent()) {body_armor.ifPresent(b -> b.setHidden(true));}
            if (neck_armor.isPresent()) {neck_armor.ifPresent(b -> b.setHidden(true));}
            if (mane.isPresent()) {mane.ifPresent(b -> b.setHidden(atCullDistance));}
            if (mane_roached.isPresent()) {mane_roached.ifPresent(b -> b.setHidden(true));}
            if (mane_buttons.isPresent()) {mane_buttons.ifPresent(b -> b.setHidden(true));}
            if (mane_long.isPresent()) {mane_long.ifPresent(b -> b.setHidden(true));}
            if (tail.isPresent()) {tail.ifPresent(b -> b.setHidden(atCullDistance));}
            if (feathering_fr.isPresent()) {feathering_fr.ifPresent(b -> b.setHidden(true));}
            if (feathering_fl.isPresent()) {feathering_fl.ifPresent(b -> b.setHidden(true));}
            if (feathering_br.isPresent()) {feathering_br.ifPresent(b -> b.setHidden(true));}
            if (feathering_bl.isPresent()) {feathering_bl.ifPresent(b -> b.setHidden(true));}
        }
        if (atCullDistance) return;
        
        if (entity.getButcherStage() == 0) {
            head.ifPresent(b -> b.setHidden(false));
            neck.ifPresent(b -> b.setHidden(false));
            front_left_leg.ifPresent(b -> b.setHidden(false));
            front_right_leg.ifPresent(b -> b.setHidden(false));
            back_left_leg.ifPresent(b -> b.setHidden(false));
            back_right_leg.ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 1) {
            head.ifPresent(b -> b.setHidden(true));
            neck.ifPresent(b -> b.setHidden(true));
            front_left_leg.ifPresent(b -> b.setHidden(false));
            front_right_leg.ifPresent(b -> b.setHidden(false));
            back_left_leg.ifPresent(b -> b.setHidden(false));
            back_right_leg.ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 2) {
            head.ifPresent(b -> b.setHidden(true));
            neck.ifPresent(b -> b.setHidden(true));
            front_left_leg.ifPresent(b -> b.setHidden(true));
            front_right_leg.ifPresent(b -> b.setHidden(false));
            back_left_leg.ifPresent(b -> b.setHidden(false));
            back_right_leg.ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 3) {
            head.ifPresent(b -> b.setHidden(true));
            neck.ifPresent(b -> b.setHidden(true));
            front_left_leg.ifPresent(b -> b.setHidden(true));
            front_right_leg.ifPresent(b -> b.setHidden(true));
            back_left_leg.ifPresent(b -> b.setHidden(false));
            back_right_leg.ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 4) {
            head.ifPresent(b -> b.setHidden(true));
            neck.ifPresent(b -> b.setHidden(true));
            front_left_leg.ifPresent(b -> b.setHidden(true));
            front_right_leg.ifPresent(b -> b.setHidden(true));
            back_left_leg.ifPresent(b -> b.setHidden(true));
            back_right_leg.ifPresent(b -> b.setHidden(false));
        } else if (entity.getButcherStage() == 5) {
            head.ifPresent(b -> b.setHidden(true));
            neck.ifPresent(b -> b.setHidden(true));
            front_left_leg.ifPresent(b -> b.setHidden(true));
            front_right_leg.ifPresent(b -> b.setHidden(true));
            back_left_leg.ifPresent(b -> b.setHidden(true));
            back_right_leg.ifPresent(b -> b.setHidden(true));
        } else {
            head.ifPresent(b -> b.setHidden(false));
            neck.ifPresent(b -> b.setHidden(false));
            front_left_leg.ifPresent(b -> b.setHidden(false));
            front_right_leg.ifPresent(b -> b.setHidden(false));
            back_left_leg.ifPresent(b -> b.setHidden(false));
            back_right_leg.ifPresent(b -> b.setHidden(false));
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

}