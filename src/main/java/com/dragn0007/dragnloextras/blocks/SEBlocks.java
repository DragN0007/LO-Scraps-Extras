package com.dragn0007.dragnloextras.blocks;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.blocks.custom.FeedBowlBlock;
import com.dragn0007.dragnloextras.blocks.custom.FeedTroughBlock;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.WoodType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.Blocks.OAK_PLANKS;

public class SEBlocks {
    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, ScrapsExtras.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES
            = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ScrapsExtras.MODID);

    public static final Map<WoodType, RegistryObject<FeedTroughBlock>> TROUGHS = new EnumMap<>(WoodType.class);
    public static final Map<WoodType, RegistryObject<Item>> TROUGH_ITEMS = new EnumMap<>(WoodType.class);
    static {
        for (WoodType color : WoodType.values()) {
            String blockName = color.getName() + "_feed_trough";
            RegistryObject<FeedTroughBlock> block = BLOCKS.register(blockName, FeedTroughBlock::new);
            RegistryObject<Item> blockItem = SEItems.ITEMS.register(blockName,
                    () -> new BlockItem(block.get(), new Item.Properties()));
            TROUGHS.put(color, block);
            TROUGH_ITEMS.put(color, blockItem);
        }
    }

    public static final Map<DyeColor, RegistryObject<FeedBowlBlock>> BOWLS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistryObject<Item>> BOWL_ITEMS = new EnumMap<>(DyeColor.class);
    static {
        for (DyeColor color : DyeColor.values()) {
            String blockName = color.getName() + "_feed_bowl";
            RegistryObject<FeedBowlBlock> block = BLOCKS.register(blockName,
                    () -> new FeedBowlBlock(BlockBehaviour.Properties.copy(OAK_PLANKS)));
            RegistryObject<Item> blockItem = SEItems.ITEMS.register(blockName,
                    () -> new BlockItem(block.get(), new Item.Properties()));
            BOWLS.put(color, block);
            BOWL_ITEMS.put(color, blockItem);
        }
    }

    public static <T extends Block>RegistryObject<T> registerBlockWithoutItem(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }
    public static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    public static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        SEItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
