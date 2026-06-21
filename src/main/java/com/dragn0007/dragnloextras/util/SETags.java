package com.dragn0007.dragnloextras.util;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.entities.EntityTypes;
import com.dragn0007.dragnpets.entities.POEntityTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class SETags {

    public static class Items {
        public static final TagKey<Item> GRAINS = forgeTag("grains");
        public static TagKey<Item> forgeTag (String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Entity_Types {
        public static final TagKey<EntityType<?>> TRAITABLE = tag("traitable");
        public static TagKey<EntityType<?>> forgeTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", name));
        }
        public static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(LivestockOverhaul.MODID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> EDIBLE_GRASS = forgeTag("edible_grass");

        public static TagKey<Block> forgeTag (String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static final Set<EntityType<?>> CAPABLE = Set.of( //if you break this tag, this entire mod, basically, wont work. no pressure tho
            EntityTypes.O_HORSE_ENTITY.get(),
            EntityTypes.O_MULE_ENTITY.get(),
            EntityTypes.O_DONKEY_ENTITY.get(),
            EntityTypes.O_COW_ENTITY.get(),
            EntityTypes.O_CAMEL_ENTITY.get(),
            EntityTypes.O_RABBIT_ENTITY.get(),
            EntityTypes.O_CHICKEN_ENTITY.get(),
            EntityTypes.O_PIG_ENTITY.get(),
            EntityTypes.O_GOAT_ENTITY.get(),
            EntityTypes.O_SHEEP_ENTITY.get(),
            EntityTypes.O_MOOSHROOM_ENTITY.get(),
            EntityTypes.FARM_GOAT_ENTITY.get(),
            EntityTypes.CARIBOU_ENTITY.get(),
            EntityTypes.UNICORN_ENTITY.get(),
            EntityTypes.AZALEA_MOOBLOOM_ENTITY.get(),
            EntityTypes.BEETROOT_MOOBLOOM_ENTITY.get(),
            EntityTypes.CARROT_MOOBLOOM_ENTITY.get(),
            EntityTypes.FLOWERING_MOOBLOOM_ENTITY.get(),
            EntityTypes.GLOW_BERRY_MOOBLOOM_ENTITY.get(),
            EntityTypes.MELON_MOOBLOOM_ENTITY.get(),
            EntityTypes.PEACH_MOOBLOOM_ENTITY.get(),
            EntityTypes.POTATO_MOOBLOOM_ENTITY.get(),
            EntityTypes.PUMPKIN_MOOBLOOM_ENTITY.get(),
            EntityTypes.SWEET_BERRY_MOOBLOOM_ENTITY.get(),
            EntityTypes.WHEAT_MOOBLOOM_ENTITY.get(),

            POEntityTypes.O_CAT_ENTITY.get(),
            POEntityTypes.O_OCELOT_ENTITY.get(),
            POEntityTypes.O_DOG_ENTITY.get(),
            POEntityTypes.O_WOLF_ENTITY.get()
    );

    public static final Set<EntityType<?>> SLEEPABLE = Set.of(
            EntityTypes.O_HORSE_ENTITY.get(),
            EntityTypes.O_MULE_ENTITY.get(),
            EntityTypes.O_DONKEY_ENTITY.get(),
            EntityTypes.O_COW_ENTITY.get(),
            EntityTypes.O_CAMEL_ENTITY.get(),
            EntityTypes.O_RABBIT_ENTITY.get(),
            EntityTypes.O_CHICKEN_ENTITY.get(),
            EntityTypes.O_PIG_ENTITY.get(),
            EntityTypes.O_GOAT_ENTITY.get(),
            EntityTypes.O_SHEEP_ENTITY.get(),
            EntityTypes.O_LLAMA_ENTITY.get(),
            EntityTypes.O_MOOSHROOM_ENTITY.get(),
            EntityTypes.FARM_GOAT_ENTITY.get(),
            EntityTypes.CARIBOU_ENTITY.get(),
            EntityTypes.UNICORN_ENTITY.get(),
            EntityTypes.AZALEA_MOOBLOOM_ENTITY.get(),
            EntityTypes.BEETROOT_MOOBLOOM_ENTITY.get(),
            EntityTypes.CARROT_MOOBLOOM_ENTITY.get(),
            EntityTypes.FLOWERING_MOOBLOOM_ENTITY.get(),
            EntityTypes.GLOW_BERRY_MOOBLOOM_ENTITY.get(),
            EntityTypes.MELON_MOOBLOOM_ENTITY.get(),
            EntityTypes.PEACH_MOOBLOOM_ENTITY.get(),
            EntityTypes.POTATO_MOOBLOOM_ENTITY.get(),
            EntityTypes.PUMPKIN_MOOBLOOM_ENTITY.get(),
            EntityTypes.SWEET_BERRY_MOOBLOOM_ENTITY.get(),
            EntityTypes.WHEAT_MOOBLOOM_ENTITY.get(),

            POEntityTypes.O_CAT_ENTITY.get(),
            POEntityTypes.O_OCELOT_ENTITY.get(),
            POEntityTypes.O_DOG_ENTITY.get(),
            POEntityTypes.O_WOLF_ENTITY.get()
    );

    public static final Set<EntityType<?>> DIRTYABLE = Set.of(
            EntityTypes.O_HORSE_ENTITY.get(),
            EntityTypes.O_MULE_ENTITY.get(),
            EntityTypes.O_DONKEY_ENTITY.get(),
            EntityTypes.O_CAMEL_ENTITY.get(),
            EntityTypes.CARIBOU_ENTITY.get(),

            POEntityTypes.O_DOG_ENTITY.get(),
            POEntityTypes.O_WOLF_ENTITY.get()
    );

    public static final Set<EntityType<?>> TRAITABLE = Set.of(
            EntityTypes.O_HORSE_ENTITY.get(),
            EntityTypes.O_MULE_ENTITY.get(),
            EntityTypes.O_DONKEY_ENTITY.get(),
            EntityTypes.O_CAMEL_ENTITY.get(),
            EntityTypes.CARIBOU_ENTITY.get(),
            EntityTypes.UNICORN_ENTITY.get(),

            POEntityTypes.O_CAT_ENTITY.get(),
            POEntityTypes.O_OCELOT_ENTITY.get(),
            POEntityTypes.O_DOG_ENTITY.get(),
            POEntityTypes.O_WOLF_ENTITY.get()
    );

    public static final Set<EntityType<?>> IMMUNITYABLE = Set.of(
            EntityTypes.O_HORSE_ENTITY.get(),
            EntityTypes.O_MULE_ENTITY.get(),
            EntityTypes.O_DONKEY_ENTITY.get(),
            EntityTypes.O_CAMEL_ENTITY.get(),
            EntityTypes.O_COW_ENTITY.get(),
            EntityTypes.O_MOOSHROOM_ENTITY.get(),
            EntityTypes.CARIBOU_ENTITY.get(),
            EntityTypes.UNICORN_ENTITY.get(),
            EntityTypes.AZALEA_MOOBLOOM_ENTITY.get(),
            EntityTypes.BEETROOT_MOOBLOOM_ENTITY.get(),
            EntityTypes.CARROT_MOOBLOOM_ENTITY.get(),
            EntityTypes.FLOWERING_MOOBLOOM_ENTITY.get(),
            EntityTypes.GLOW_BERRY_MOOBLOOM_ENTITY.get(),
            EntityTypes.MELON_MOOBLOOM_ENTITY.get(),
            EntityTypes.PEACH_MOOBLOOM_ENTITY.get(),
            EntityTypes.POTATO_MOOBLOOM_ENTITY.get(),
            EntityTypes.PUMPKIN_MOOBLOOM_ENTITY.get(),
            EntityTypes.SWEET_BERRY_MOOBLOOM_ENTITY.get(),
            EntityTypes.WHEAT_MOOBLOOM_ENTITY.get(),

            POEntityTypes.O_CAT_ENTITY.get(),
            POEntityTypes.O_OCELOT_ENTITY.get(),
            POEntityTypes.O_DOG_ENTITY.get(),
            POEntityTypes.O_WOLF_ENTITY.get()
    );

    public static final Set<EntityType<?>> HALTERABLE = Set.of(
            EntityTypes.O_HORSE_ENTITY.get(),
            EntityTypes.O_MULE_ENTITY.get(),
            EntityTypes.O_DONKEY_ENTITY.get(),
            EntityTypes.CARIBOU_ENTITY.get(),
            EntityTypes.UNICORN_ENTITY.get()
    );

    public static final Set<EntityType<?>> SPIKE_COLLARABLE = Set.of(
            POEntityTypes.O_DOG_ENTITY.get(),
            POEntityTypes.O_WOLF_ENTITY.get()
    );
}
