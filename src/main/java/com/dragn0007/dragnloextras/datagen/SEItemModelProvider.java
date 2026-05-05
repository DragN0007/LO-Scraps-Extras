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
        simpleItem(SEItems.MUTTON_FLANK);
        simpleItem(SEItems.MUTTON_RIB);
        simpleItem(SEItems.MUTTON_LEG);
        simpleItem(SEItems.COOKED_MUTTON_FLANK);
        simpleItem(SEItems.COOKED_MUTTON_RIB);
        simpleItem(SEItems.COOKED_MUTTON_LEG);

        simpleItem(SEItems.LLAMA_RIB);
        simpleItem(SEItems.LLAMA_LOIN);
        simpleItem(SEItems.COOKED_LLAMA_RIB);
        simpleItem(SEItems.COOKED_LLAMA_LOIN);
        simpleItem(SEItems.CHEVON_RIB);
        simpleItem(SEItems.CHEVON_LOIN);
        simpleItem(SEItems.COOKED_CHEVON_LOIN);
        simpleItem(SEItems.COOKED_CHEVON_RIB);
        simpleItem(SEItems.PORK_TENDERLOIN);
        simpleItem(SEItems.PORK_RIB_CHOP);
        simpleItem(SEItems.COOKED_PORK_TENDERLOIN);
        simpleItem(SEItems.COOKED_PORK_RIB_CHOP);
        simpleItem(SEItems.CAMEL_RIB);
        simpleItem(SEItems.CAMEL_LOIN);
        simpleItem(SEItems.COOKED_CAMEL_RIB);
        simpleItem(SEItems.COOKED_CAMEL_LOIN);
        simpleItem(SEItems.CHICKEN_THIGH);
        simpleItem(SEItems.COOKED_CHICKEN_THIGH);
        simpleItem(SEItems.RABBIT_THIGH);
        simpleItem(SEItems.COOKED_RABBIT_THIGH);
        simpleItem(SEItems.SALMON_FILLET);
        simpleItem(SEItems.COD_FILLET);
        simpleItem(SEItems.COOKED_SALMON_FILLET);
        simpleItem(SEItems.COOKED_COD_FILLET);
        advancedItem(SEItems.CARIBOU_RIB_STEAK, "horse_rib_steak");
        advancedItem(SEItems.CARIBOU_SIRLOIN_STEAK, "horse_sirloin_steak");
        advancedItem(SEItems.COOKED_CARIBOU_RIB_STEAK, "cooked_horse_rib_steak");
        advancedItem(SEItems.COOKED_CARIBOU_SIRLOIN_STEAK, "cooked_horse_sirloin_steak");
        simpleItem(SEItems.UNICORN_RIB_STEAK);
        simpleItem(SEItems.UNICORN_SIRLOIN_STEAK);
        simpleItem(SEItems.COOKED_UNICORN_RIB_STEAK);
        simpleItem(SEItems.COOKED_UNICORN_SIRLOIN_STEAK);

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