package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DonkeyCorpseModel extends DefaultedEntityGeoModel<DonkeyCorpse> {
    public DonkeyCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "donkey_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/donkey_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/donkey_body.animation.json");
    @Override
    public ResourceLocation getModelResource(DonkeyCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(DonkeyCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(DonkeyCorpse animatable) {return ANIMATION;}
}

