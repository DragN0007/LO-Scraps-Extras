package com.dragn0007.dragnloextras.items.custom;

import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HoofPickItem extends Item {

    public HoofPickItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (ScrapsExtrasCommonConfig.HYGIENE_SYSTEM.get()) {
            pTooltipComponents.add(Component.translatable("tooltip.dragnloextras.hoof_pick.tooltip").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(Component.translatable("Prevents Hoof Abscesses.").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.dragnloextras.hygiene_disabled.tooltip").withStyle(ChatFormatting.GRAY));
        }
    }
}
