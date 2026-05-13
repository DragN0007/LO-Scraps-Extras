package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CamelCorpseModel extends DefaultedEntityGeoModel<CamelCorpse> {
    public CamelCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "camel_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/camel_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/camel_body.animation.json");
    @Override
    public ResourceLocation getModelResource(CamelCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(CamelCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(CamelCorpse animatable) {return ANIMATION;}
}

