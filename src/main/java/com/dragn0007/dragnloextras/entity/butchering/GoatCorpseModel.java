package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GoatCorpseModel extends DefaultedEntityGeoModel<GoatCorpse> {
    public GoatCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "goat_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/goat_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/goat_body.animation.json");
    @Override
    public ResourceLocation getModelResource(GoatCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(GoatCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(GoatCorpse animatable) {return ANIMATION;}
}

