package com.dragn0007.dragnloextras.entity.butchering;

import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class LlamaCorpseModel extends DefaultedEntityGeoModel<LlamaCorpse> {
    public LlamaCorpseModel() {super(new ResourceLocation(ScrapsExtras.MODID, "llama_corpse"), false);}
    public static final ResourceLocation MODEL = new ResourceLocation(ScrapsExtras.MODID, "geo/llama_body.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(ScrapsExtras.MODID, "animations/llama_body.animation.json");
    @Override
    public ResourceLocation getModelResource(LlamaCorpse object) {return MODEL;}
    @Override
    public ResourceLocation getTextureResource(LlamaCorpse object) {return object.getTextureLocation();}
    @Override
    public ResourceLocation getAnimationResource(LlamaCorpse animatable) {return ANIMATION;}
}

