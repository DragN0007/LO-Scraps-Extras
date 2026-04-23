package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CowCorpseModel extends DefaultedEntityGeoModel<CowCorpse> {
    public CowCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "cow_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/cow_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/cow_body.animation.json");
    @Override
    public ResourceLocation getModelResource(CowCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(CowCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(CowCorpse animatable) {return ANIMATION;}
}

