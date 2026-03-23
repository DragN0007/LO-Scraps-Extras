package com.dragn0007.dragnloextras.datagen;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.items.SEItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class SEItemModelProvider extends ItemModelProvider {
    public SEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ScrapsExtras.MODID, existingFileHelper);
    }

    @Override
    public void registerModels() {
        simpleItem(SEItems.HORSE_MANNEQUIN);

        simpleItem(SEItems.STETHOSCOPE);
        simpleItem(SEItems.ANTIBIOTIC_INJECTION);
        simpleItem(SEItems.ANTIBIOTIC_OINTMENT);
        simpleItem(SEItems.ANTIBIOTIC_EARDROPS);
        simpleItem(SEItems.ANTIPARASITIC_OINTMENT);
        simpleItem(SEItems.HEARTWORM_MEDICINE);
        simpleItem(SEItems.RABIES_SHOT);
        simpleItem(SEItems.VETERINARY_BANDAGE);

        simpleItem(SEItems.BRUSH);
//        simpleItem(SEItems.COMB);
        simpleItem(SEItems.HOOF_PICK);

        simpleItem(SEItems.GRAIN_FEED);
        simpleItem(SEItems.HEARTY_GRAIN_FEED);
        simpleItem(SEItems.KIBBLE);
        simpleItem(SEItems.HEARTY_KIBBLE);

        simpleItem(SEItems.BLACK_HALTER);
        simpleItem(SEItems.BLUE_HALTER);
        simpleItem(SEItems.BROWN_HALTER);
        simpleItem(SEItems.CYAN_HALTER);
        simpleItem(SEItems.GREEN_HALTER);
        simpleItem(SEItems.GREY_HALTER);
        simpleItem(SEItems.LIGHT_BLUE_HALTER);
        simpleItem(SEItems.LIGHT_GREY_HALTER);
        simpleItem(SEItems.LIME_HALTER);
        simpleItem(SEItems.MAGENTA_HALTER);
        simpleItem(SEItems.ORANGE_HALTER);
        simpleItem(SEItems.PINK_HALTER);
        simpleItem(SEItems.PURPLE_HALTER);
        simpleItem(SEItems.RED_HALTER);
        simpleItem(SEItems.WHITE_HALTER);
        simpleItem(SEItems.YELLOW_HALTER);

        simpleItem(SEItems.BLACK_TURNOUT_BLANKET);
        simpleItem(SEItems.BLUE_TURNOUT_BLANKET);
        simpleItem(SEItems.BROWN_TURNOUT_BLANKET);
        simpleItem(SEItems.CYAN_TURNOUT_BLANKET);
        simpleItem(SEItems.GREEN_TURNOUT_BLANKET);
        simpleItem(SEItems.GREY_TURNOUT_BLANKET);
        simpleItem(SEItems.LIGHT_BLUE_TURNOUT_BLANKET);
        simpleItem(SEItems.LIGHT_GREY_TURNOUT_BLANKET);
        simpleItem(SEItems.LIME_TURNOUT_BLANKET);
        simpleItem(SEItems.MAGENTA_TURNOUT_BLANKET);
        simpleItem(SEItems.ORANGE_TURNOUT_BLANKET);
        simpleItem(SEItems.PINK_TURNOUT_BLANKET);
        simpleItem(SEItems.PURPLE_TURNOUT_BLANKET);
        simpleItem(SEItems.RED_TURNOUT_BLANKET);
        simpleItem(SEItems.WHITE_TURNOUT_BLANKET);
        simpleItem(SEItems.YELLOW_TURNOUT_BLANKET);

        simpleItem(SEItems.LOGO);
    }

    public ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ScrapsExtras.MODID,"item/" + item.getId().getPath()));
    }
}