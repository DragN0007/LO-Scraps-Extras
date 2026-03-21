package com.dragn0007.dragnloextras.entity.mannequin;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HorseMannequinModel extends DefaultedEntityGeoModel<HorseMannequin> {

    public HorseMannequinModel() {
        super(new ResourceLocation(ScrapsExtras.MODID, "mannequin"), true);
    }

    @Override
    public void setCustomAnimations(HorseMannequin animatable, long instanceId, AnimationState<HorseMannequin> animationState) {

        CoreGeoBone neck = getAnimationProcessor().getBone("neck");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (neck != null) {
            neck.setRotY(neck.getRotY());
        }
    }

    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/horse_mannequin.geo.json");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ScrapsExtras.MODID, "textures/entity/mannequin.png");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/horse_mannequin.animation.json");

    @Override
    public ResourceLocation getModelResource(HorseMannequin object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(HorseMannequin object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(HorseMannequin animatable) {
        return ANIMATION;
    }
}

