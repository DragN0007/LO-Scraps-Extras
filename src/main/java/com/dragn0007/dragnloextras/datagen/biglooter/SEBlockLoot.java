package com.dragn0007.dragnloextras.datagen.biglooter;

import com.dragn0007.dragnloextras.blocks.SEBlocks;
import com.dragn0007.dragnloextras.util.WoodType;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class SEBlockLoot extends BlockLootSubProvider {
    public SEBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        for (DyeColor color : DyeColor.values()) {dropSelf(SEBlocks.BOWLS.get(color).get());}
        for (WoodType color : WoodType.values()) {dropSelf(SEBlocks.TROUGHS.get(color).get());}
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return SEBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
