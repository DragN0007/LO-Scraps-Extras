package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MuleCorpseModel extends DefaultedEntityGeoModel<MuleCorpse> {
    public MuleCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "mule_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/mule_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/mule_body.animation.json");
    @Override
    public ResourceLocation getModelResource(MuleCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(MuleCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(MuleCorpse animatable) {return ANIMATION;}
}

