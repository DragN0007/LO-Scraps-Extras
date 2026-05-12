package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class UnicornCorpseModel extends DefaultedEntityGeoModel<UnicornCorpse> {
    public UnicornCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "unicorn_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/unicorn_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/unicorn_body.animation.json");
    @Override
    public ResourceLocation getModelResource(UnicornCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(UnicornCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(UnicornCorpse animatable) {return ANIMATION;}
}

