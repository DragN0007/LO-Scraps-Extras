package com.dragn0007.dragnloextras.datagen;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.blocks.SEBlocks;
import com.dragn0007.dragnloextras.blocks.custom.FeedBowlBlock;
import com.dragn0007.dragnloextras.blocks.custom.FeedTroughBlock;
import com.dragn0007.dragnloextras.util.WoodType;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SEBlockstateProvider extends BlockStateProvider {
    public SEBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ScrapsExtras.MODID, exFileHelper);
    }
    @Override
    protected void registerStatesAndModels() {
        for (DyeColor color : DyeColor.values()) {
            RegistryObject<FeedBowlBlock> blockRegistryObject = SEBlocks.BOWLS.get(color);
            FeedBowlBlock block = blockRegistryObject.get();
            bowlBlock(block, color + "_feed_bowl");
        }
        for (WoodType color : WoodType.values()) {
            RegistryObject<FeedTroughBlock> blockRegistryObject = SEBlocks.TROUGHS.get(color);
            FeedTroughBlock block = blockRegistryObject.get();
            troughBlock(block, FeedTroughBlock.FEED, color.toString().toLowerCase() + "_feed_trough", "feed_trough", "feed_trough_full");
        }
    }

    private void bowlBlock(Block block, String getTextureName) {
        ResourceLocation texture = modLoc("block/" + getTextureName);
        var model = models().withExistingParent(block.builtInRegistryHolder().key().location().getPath(),
                        modLoc("block/feed_bowl"))
                .texture("particle", texture)
                .texture("texture", texture);
        simpleBlock(block, models().getBuilder(model.getLocation().toString()).renderType("cutout"));
        simpleBlockItem(block, model);
    }

    private void troughBlock(Block block, BooleanProperty booleanProperty, String getTextureName, String emptyModel, String fullModel) {
        ResourceLocation texture = modLoc("block/" + getTextureName);
        String blockPath = block.builtInRegistryHolder().key().location().getPath();

        ModelFile empty = models().withExistingParent(blockPath, modLoc("block/" + emptyModel))
                .texture("particle", texture)
                .texture("texture", texture)
                .renderType("cutout");

        ModelFile full = models().withExistingParent(blockPath + "_full", modLoc("block/" + fullModel))
                .texture("particle", texture)
                .texture("texture", texture)
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            boolean hasFood = state.getValue(booleanProperty);
            ModelFile modelFile = hasFood ? full : empty;
            int yRot = (int) facing.toYRot();
            return ConfiguredModel.builder()
                    .modelFile(modelFile)
                    .rotationY(yRot)
                    .build();
        });

        simpleBlockItem(block, empty);
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile(ScrapsExtras.MODID +
                ":block/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
