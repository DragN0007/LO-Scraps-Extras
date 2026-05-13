package com.dragn0007.dragnloextras.entity;

import com.dragn0007.dragnloextras.entity.butchering.*;
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


    public static final RegistryObject<EntityType<CowCorpse>> COW_CORPSE = ENTITY_TYPES.register("cow_corpse",
            () -> EntityType.Builder.of(CowCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"cow_corpse").toString()));

    public static final RegistryObject<EntityType<HorseCorpse>> HORSE_CORPSE = ENTITY_TYPES.register("horse_corpse",
            () -> EntityType.Builder.of(HorseCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"horse_corpse").toString()));

    public static final RegistryObject<EntityType<CamelCorpse>> CAMEL_CORPSE = ENTITY_TYPES.register("camel_corpse",
            () -> EntityType.Builder.of(CamelCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"camel_corpse").toString()));

    public static final RegistryObject<EntityType<SheepCorpse>> SHEEP_CORPSE = ENTITY_TYPES.register("sheep_corpse",
            () -> EntityType.Builder.of(SheepCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"sheep_corpse").toString()));

    public static final RegistryObject<EntityType<MuleCorpse>> MULE_CORPSE = ENTITY_TYPES.register("mule_corpse",
            () -> EntityType.Builder.of(MuleCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"mule_corpse").toString()));

    public static final RegistryObject<EntityType<DonkeyCorpse>> DONKEY_CORPSE = ENTITY_TYPES.register("donkey_corpse",
            () -> EntityType.Builder.of(DonkeyCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"donkey_corpse").toString()));

    public static final RegistryObject<EntityType<ChickenCorpse>> CHICKEN_CORPSE = ENTITY_TYPES.register("chicken_corpse",
            () -> EntityType.Builder.of(ChickenCorpse::new,
                            MobCategory.CREATURE)
                    .sized(0.5f,0.5f)
                    .build(new ResourceLocation(MODID,"chicken_corpse").toString()));

    public static final RegistryObject<EntityType<RabbitCorpse>> RABBIT_CORPSE = ENTITY_TYPES.register("rabbit_corpse",
            () -> EntityType.Builder.of(RabbitCorpse::new,
                            MobCategory.CREATURE)
                    .sized(0.5f,0.5f)
                    .build(new ResourceLocation(MODID,"rabbit_corpse").toString()));

    public static final RegistryObject<EntityType<PigCorpse>> PIG_CORPSE = ENTITY_TYPES.register("pig_corpse",
            () -> EntityType.Builder.of(PigCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.0f,1.0f)
                    .build(new ResourceLocation(MODID,"pig_corpse").toString()));

    public static final RegistryObject<EntityType<UnicornCorpse>> UNICORN_CORPSE = ENTITY_TYPES.register("unicorn_corpse",
            () -> EntityType.Builder.of(UnicornCorpse::new,
                            MobCategory.CREATURE)
                    .sized(1.5f,1.0f)
                    .build(new ResourceLocation(MODID,"unicorn_corpse").toString()));
}

