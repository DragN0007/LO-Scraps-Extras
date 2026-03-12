package com.dragn0007.dragnloextras.entity;

import com.dragn0007.dragnloextras.entity.mannequin.HorseMannequin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.dragn0007.dragnloextras.ScrapsExtras.MODID;

public class SEEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<HorseMannequin>> HORSE_MANNEQUIN_ENTITY = ENTITY_TYPES.register("horse_mannequin",
            () -> EntityType.Builder.of(HorseMannequin::new,
                    MobCategory.CREATURE)
                    .sized(1.5f,2f)
                    .build(new ResourceLocation(MODID,"horse_mannequin").toString()));
}

