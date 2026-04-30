package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class HorseCorpseModel extends DefaultedEntityGeoModel<HorseCorpse> {
    public HorseCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "horse_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/horse_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/horse_body.animation.json");
    @Override
    public ResourceLocation getModelResource(HorseCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(HorseCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(HorseCorpse animatable) {return ANIMATION;}
}

