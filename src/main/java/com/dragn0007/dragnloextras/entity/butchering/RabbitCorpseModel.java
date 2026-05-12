package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RabbitCorpseModel extends DefaultedEntityGeoModel<RabbitCorpse> {
    public RabbitCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "rabbit_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/rabbit_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/rabbit_body.animation.json");
    @Override
    public ResourceLocation getModelResource(RabbitCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(RabbitCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(RabbitCorpse animatable) {return ANIMATION;}
}

