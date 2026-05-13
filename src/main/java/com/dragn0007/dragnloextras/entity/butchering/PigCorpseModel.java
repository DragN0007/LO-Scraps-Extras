package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class PigCorpseModel extends DefaultedEntityGeoModel<PigCorpse> {
    public PigCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "pig_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/pig_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/pig_body.animation.json");
    @Override
    public ResourceLocation getModelResource(PigCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(PigCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(PigCorpse animatable) {return ANIMATION;}
}

