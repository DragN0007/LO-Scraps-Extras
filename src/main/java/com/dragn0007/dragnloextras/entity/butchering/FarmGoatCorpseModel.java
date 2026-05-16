package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class FarmGoatCorpseModel extends DefaultedEntityGeoModel<FarmGoatCorpse> {
    public FarmGoatCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "farm_goat_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/farm_goat_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/farm_goat_body.animation.json");
    @Override
    public ResourceLocation getModelResource(FarmGoatCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(FarmGoatCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(FarmGoatCorpse animatable) {return ANIMATION;}
}

