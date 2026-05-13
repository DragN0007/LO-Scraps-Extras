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
        simpleItem(SEItems.ANTIPARASITIC_INJECTION);
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

        simpleItem(SEItems.COLLAR_SPIKES);

        simpleItem(SEItems.RAWHIDE_SADDLE);

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

        simpleItem(SEItems.BEEF_BRISKET);
        simpleItem(SEItems.BEEF_CHUCK);
        simpleItem(SEItems.BEEF_RIB);
        simpleItem(SEItems.BEEF_SHANK);
        simpleItem(SEItems.COOKED_BEEF_BRISKET);
        simpleItem(SEItems.COOKED_BEEF_CHUCK);
        simpleItem(SEItems.COOKED_BEEF_RIB);
        simpleItem(SEItems.COOKED_BEEF_SHANK);

        simpleItem(SEItems.HORSE_BRISKET);
        simpleItem(SEItems.HORSE_CHUCK);
        simpleItem(SEItems.HORSE_RIB);
        simpleItem(SEItems.HORSE_SHANK);
        simpleItem(SEItems.COOKED_HORSE_BRISKET);
        simpleItem(SEItems.COOKED_HORSE_CHUCK);
        simpleItem(SEItems.COOKED_HORSE_RIB);
        simpleItem(SEItems.COOKED_HORSE_SHANK);

        simpleItem(SEItems.CAMEL_BRISKET);
        simpleItem(SEItems.CAMEL_CHUCK);
        simpleItem(SEItems.CAMEL_RIB);
        simpleItem(SEItems.CAMEL_SHANK);
        simpleItem(SEItems.COOKED_CAMEL_BRISKET);
        simpleItem(SEItems.COOKED_CAMEL_CHUCK);
        simpleItem(SEItems.COOKED_CAMEL_RIB);
        simpleItem(SEItems.COOKED_CAMEL_SHANK);

        simpleItem(SEItems.PORK_HAM);
        simpleItem(SEItems.PORK_LOIN);
        simpleItem(SEItems.PORK_BELLY);
        simpleItem(SEItems.COOKED_PORK_HAM);
        simpleItem(SEItems.COOKED_PORK_LOIN);
        simpleItem(SEItems.COOKED_PORK_BELLY);

        simpleItem(SEItems.MUTTON_FLANK);
        simpleItem(SEItems.MUTTON_RIB);
        simpleItem(SEItems.MUTTON_LEG);
        simpleItem(SEItems.COOKED_MUTTON_FLANK);
        simpleItem(SEItems.COOKED_MUTTON_RIB);
        simpleItem(SEItems.COOKED_MUTTON_LEG);

        simpleItem(SEItems.CHICKEN_THIGH);
        simpleItem(SEItems.CHICKEN_WING);
        simpleItem(SEItems.COOKED_CHICKEN_THIGH);
        simpleItem(SEItems.COOKED_CHICKEN_WING);

        simpleItem(SEItems.RABBIT_THIGH);
        simpleItem(SEItems.COOKED_RABBIT_THIGH);

        simpleItem(SEItems.SALMON_FILLET);
        simpleItem(SEItems.COD_FILLET);
        simpleItem(SEItems.COOKED_SALMON_FILLET);
        simpleItem(SEItems.COOKED_COD_FILLET);

        advancedItem(SEItems.CARIBOU_BRISKET, SEItems.HORSE_BRISKET.get().toString());
        advancedItem(SEItems.CARIBOU_CHUCK, SEItems.HORSE_CHUCK.get().toString());
        advancedItem(SEItems.CARIBOU_RIB, SEItems.HORSE_RIB.get().toString());
        advancedItem(SEItems.CARIBOU_SHANK, SEItems.HORSE_SHANK.get().toString());
        advancedItem(SEItems.COOKED_CARIBOU_BRISKET, SEItems.COOKED_HORSE_BRISKET.get().toString());
        advancedItem(SEItems.COOKED_CARIBOU_CHUCK, SEItems.COOKED_HORSE_CHUCK.get().toString());
        advancedItem(SEItems.COOKED_CARIBOU_RIB, SEItems.COOKED_HORSE_RIB.get().toString());
        advancedItem(SEItems.COOKED_CARIBOU_SHANK, SEItems.COOKED_HORSE_SHANK.get().toString());

        simpleItem(SEItems.UNICORN_BRISKET);
        simpleItem(SEItems.UNICORN_CHUCK);
        simpleItem(SEItems.UNICORN_RIB);
        simpleItem(SEItems.UNICORN_SHANK);
        simpleItem(SEItems.COOKED_UNICORN_BRISKET);
        simpleItem(SEItems.COOKED_UNICORN_CHUCK);
        simpleItem(SEItems.COOKED_UNICORN_RIB);
        simpleItem(SEItems.COOKED_UNICORN_SHANK);

        simpleItem(SEItems.LOGO);
    }

    public ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ScrapsExtras.MODID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder advancedItem(RegistryObject<Item> item, String getTextureName) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(ScrapsExtras.MODID,"item/" + getTextureName));
    }
}