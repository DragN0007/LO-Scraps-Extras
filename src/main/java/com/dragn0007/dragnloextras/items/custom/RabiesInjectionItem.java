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

public class RabiesInjectionItem extends Item {

    public RabiesInjectionItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (ScrapsExtrasCommonConfig.TREATMENTS.get()) {
            pTooltipComponents.add(Component.translatable("Cures Rabies in animals, as long as they haven't become too sick.").withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.dragnloextras.medicals_disabled.tooltip").withStyle(ChatFormatting.GRAY));
        }
    }
}
