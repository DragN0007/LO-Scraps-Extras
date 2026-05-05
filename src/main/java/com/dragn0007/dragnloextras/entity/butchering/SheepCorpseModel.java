package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class SheepCorpseModel extends DefaultedEntityGeoModel<SheepCorpse> {
    public SheepCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "sheep_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/sheep_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/sheep_body.animation.json");
    @Override
    public ResourceLocation getModelResource(SheepCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(SheepCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(SheepCorpse animatable) {return ANIMATION;}
}

