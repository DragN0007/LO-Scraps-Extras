package com.dragn0007.dragnloextras.client.gui;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnloextras.common.gui.MannequinMenu;
import com.dragn0007.dragnloextras.entity.mannequin.HorseMannequin;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MannequinScreen extends AbstractContainerScreen<MannequinMenu> {

    public static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation(LivestockOverhaul.MODID, "textures/gui/o_horse.png");

    public final HorseMannequin oHorse;

    public MannequinScreen(MannequinMenu oHorseMenu, Inventory inventory, Component component) {
        super(oHorseMenu, inventory, component);
        this.oHorse = oHorseMenu.oHorse;
    }

    @Override
    public void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    public void renderBg(GuiGraphics graphics, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, HORSE_INVENTORY_LOCATION);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(HORSE_INVENTORY_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);

        if (this.oHorse.isSaddleable()) {
            graphics.blit(HORSE_INVENTORY_LOCATION, x + 7, y + 17, 18, this.imageHeight + 54, 18, 18);
        }

        if (this.oHorse.canWearArmor()) {
            graphics.blit(HORSE_INVENTORY_LOCATION, x + 7, y + 35, 0, this.imageHeight + 54, 18, 18);
            graphics.blit(HORSE_INVENTORY_LOCATION, x + 7, y + 53, 36, this.imageHeight + 54, 18, 18);
        }

        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x + 51, y + 60, 17, (float)(x + 51), (float)(y + 75 - 50), this.oHorse);
    }
}


