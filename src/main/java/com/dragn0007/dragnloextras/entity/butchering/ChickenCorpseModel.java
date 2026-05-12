package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ChickenCorpseModel extends DefaultedEntityGeoModel<ChickenCorpse> {
    public ChickenCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "chicken_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/chicken_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/chicken_body.animation.json");
    @Override
    public ResourceLocation getModelResource(ChickenCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(ChickenCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(ChickenCorpse animatable) {return ANIMATION;}
}

