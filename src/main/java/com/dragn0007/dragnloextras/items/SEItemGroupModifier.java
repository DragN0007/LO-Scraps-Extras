package com.dragn0007.dragnloextras.items;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import com.dragn0007.dragnlivestock.compat.medievalembroidery.MECompatItems;
import com.dragn0007.dragnloextras.ScrapsExtras;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SEItemGroupModifier {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ScrapsExtras.MODID);

    public static final RegistryObject<CreativeModeTab> SCRAPS_GROUP = CREATIVE_MODE_TABS.register("scraps",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(SEItems.LOGO.get())).title(Component.translatable("itemGroup.scraps")).withSearchBar()
                    .displayItems((displayParameters, output) -> {

                        output.accept(SEItems.STETHOSCOPE.get());
                        output.accept(SEItems.ANTIBIOTIC_INJECTION.get());
                        output.accept(SEItems.ANTIBIOTIC_OINTMENT.get());
                        output.accept(SEItems.ANTIBIOTIC_EARDROPS.get());
                        output.accept(SEItems.ANTIPARASITIC_INJECTION.get());
                        output.accept(SEItems.ANTIPARASITIC_OINTMENT.get());
                        output.accept(SEItems.HEARTWORM_MEDICINE.get());
                        output.accept(SEItems.RABIES_SHOT.get());
                        output.accept(SEItems.VETERINARY_BANDAGE.get());

                        output.accept(SEItems.BRUSH.get());
                        output.accept(SEItems.HOOF_PICK.get());

                        output.accept(SEItems.GRAIN_FEED.get());
                        output.accept(SEItems.HEARTY_GRAIN_FEED.get());
                        output.accept(SEItems.KIBBLE.get());
                        output.accept(SEItems.HEARTY_KIBBLE.get());

                        output.accept(SEItems.HORSE_MANNEQUIN.get());
                        output.accept(SEItems.COLLAR_SPIKES.get());

                        if (ModList.get().isLoaded("tfc"))
                        output.accept(SEItems.RAWHIDE_SADDLE.get());

                        for (DyeColor color : DyeColor.values()) {
                            output.accept(SEItems.HALTERS.get(color).get());
                        }

                        for (DyeColor color : DyeColor.values()) {
                            output.accept(SEItems.TURNOUT_BLANKETS.get(color).get());
                        }

                        output.accept(SEItems.BEEF_BRISKET.get());
                        output.accept(SEItems.BEEF_CHUCK.get());
                        output.accept(SEItems.BEEF_RIB.get());
                        output.accept(SEItems.BEEF_SHANK.get());
                        output.accept(SEItems.COOKED_BEEF_BRISKET.get());
                        output.accept(SEItems.COOKED_BEEF_CHUCK.get());
                        output.accept(SEItems.COOKED_BEEF_RIB.get());
                        output.accept(SEItems.COOKED_BEEF_SHANK.get());

                        output.accept(SEItems.HORSE_BRISKET.get());
                        output.accept(SEItems.HORSE_CHUCK.get());
                        output.accept(SEItems.HORSE_RIB.get());
                        output.accept(SEItems.HORSE_SHANK.get());
                        output.accept(SEItems.COOKED_HORSE_BRISKET.get());
                        output.accept(SEItems.COOKED_HORSE_CHUCK.get());
                        output.accept(SEItems.COOKED_HORSE_RIB.get());
                        output.accept(SEItems.COOKED_HORSE_SHANK.get());

                        output.accept(SEItems.LLAMA_FLANK.get());
                        output.accept(SEItems.LLAMA_RIB.get());
                        output.accept(SEItems.LLAMA_SHANK.get());
                        output.accept(SEItems.COOKED_LLAMA_FLANK.get());
                        output.accept(SEItems.COOKED_LLAMA_RIB.get());
                        output.accept(SEItems.COOKED_LLAMA_SHANK.get());

                        output.accept(SEItems.CAMEL_BRISKET.get());
                        output.accept(SEItems.CAMEL_CHUCK.get());
                        output.accept(SEItems.CAMEL_RIB.get());
                        output.accept(SEItems.CAMEL_SHANK.get());
                        output.accept(SEItems.COOKED_CAMEL_BRISKET.get());
                        output.accept(SEItems.COOKED_CAMEL_CHUCK.get());
                        output.accept(SEItems.COOKED_CAMEL_RIB.get());
                        output.accept(SEItems.COOKED_CAMEL_SHANK.get());

                        output.accept(SEItems.CHEVON_FLANK.get());
                        output.accept(SEItems.CHEVON_RIB.get());
                        output.accept(SEItems.CHEVON_LEG.get());
                        output.accept(SEItems.COOKED_CHEVON_FLANK.get());
                        output.accept(SEItems.COOKED_CHEVON_RIB.get());
                        output.accept(SEItems.COOKED_CHEVON_LEG.get());

                        output.accept(SEItems.PORK_HAM.get());
                        output.accept(SEItems.PORK_LOIN.get());
                        output.accept(SEItems.PORK_BELLY.get());
                        output.accept(SEItems.COOKED_PORK_HAM.get());
                        output.accept(SEItems.COOKED_PORK_LOIN.get());
                        output.accept(SEItems.COOKED_PORK_BELLY.get());

                        output.accept(SEItems.MUTTON_FLANK.get());
                        output.accept(SEItems.MUTTON_RIB.get());
                        output.accept(SEItems.MUTTON_LEG.get());
                        output.accept(SEItems.COOKED_MUTTON_FLANK.get());
                        output.accept(SEItems.COOKED_MUTTON_RIB.get());
                        output.accept(SEItems.COOKED_MUTTON_LEG.get());

                        output.accept(SEItems.CHICKEN_THIGH.get());
                        output.accept(SEItems.CHICKEN_WING.get());
                        output.accept(SEItems.COOKED_CHICKEN_THIGH.get());
                        output.accept(SEItems.COOKED_CHICKEN_WING.get());

                        output.accept(SEItems.RABBIT_THIGH.get());
                        output.accept(SEItems.COOKED_RABBIT_THIGH.get());

                        output.accept(SEItems.SALMON_FILLET.get());
                        output.accept(SEItems.COD_FILLET.get());
                        output.accept(SEItems.COOKED_SALMON_FILLET.get());
                        output.accept(SEItems.COOKED_COD_FILLET.get());

                        output.accept(SEItems.CARIBOU_BRISKET.get());
                        output.accept(SEItems.CARIBOU_CHUCK.get());
                        output.accept(SEItems.CARIBOU_RIB.get());
                        output.accept(SEItems.CARIBOU_SHANK.get());
                        output.accept(SEItems.COOKED_CARIBOU_BRISKET.get());
                        output.accept(SEItems.COOKED_CARIBOU_CHUCK.get());
                        output.accept(SEItems.COOKED_CARIBOU_RIB.get());
                        output.accept(SEItems.COOKED_CARIBOU_SHANK.get());

                        output.accept(SEItems.UNICORN_BRISKET.get());
                        output.accept(SEItems.UNICORN_CHUCK.get());
                        output.accept(SEItems.UNICORN_RIB.get());
                        output.accept(SEItems.UNICORN_SHANK.get());
                        output.accept(SEItems.COOKED_UNICORN_BRISKET.get());
                        output.accept(SEItems.COOKED_UNICORN_CHUCK.get());
                        output.accept(SEItems.COOKED_UNICORN_RIB.get());
                        output.accept(SEItems.COOKED_UNICORN_SHANK.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}


