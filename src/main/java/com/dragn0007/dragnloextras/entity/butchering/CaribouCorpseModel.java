package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CaribouCorpseModel extends DefaultedEntityGeoModel<CaribouCorpse> {
    public CaribouCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "caribou_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/caribou_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/caribou_body.animation.json");
    @Override
    public ResourceLocation getModelResource(CaribouCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(CaribouCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(CaribouCorpse animatable) {return ANIMATION;}
}

