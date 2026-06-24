package com.dragn0007.dragnloextras.compat.jade;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnloextras.blocks.custom.FeedTroughBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class TroughTooltipProvider implements IBlockComponentProvider {

    public static final IntegerProperty FOOD_AMOUNT = FeedTroughBlock.FOOD_AMOUNT;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockState().hasProperty(FOOD_AMOUNT)) {
            int food = accessor.getBlockState().getValue(FOOD_AMOUNT);
            tooltip.add(Component.literal(food + " Food Left"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(LivestockOverhaul.MODID, "o_tooltips");
    }
}
