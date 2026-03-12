package com.dragn0007.dragnloextras.items;

import com.dragn0007.dragnlivestock.LivestockOverhaul;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SEItemGroupModifier {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LivestockOverhaul.MODID);

    public static final RegistryObject<CreativeModeTab> PETS_OVERHAUL_GROUP = CREATIVE_MODE_TABS.register("scraps",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(SEItems.LOGO.get())).title(Component.translatable("itemGroup.scraps")).withSearchBar()
                    .displayItems((displayParameters, output) -> {

                        output.accept(SEItems.STETHOSCOPE.get());
                        output.accept(SEItems.ANTIBIOTIC_INJECTION.get());
                        output.accept(SEItems.ANTIBIOTIC_OINTMENT.get());
                        output.accept(SEItems.ANTIBIOTIC_EARDROPS.get());
                        output.accept(SEItems.ANTIPARASITIC_OINTMENT.get());
                        output.accept(SEItems.HEARTWORM_MEDICINE.get());
                        output.accept(SEItems.RABIES_SHOT.get());
                        output.accept(SEItems.VETERINARY_BANDAGE.get());

                        output.accept(SEItems.BRUSH.get());
                        output.accept(SEItems.COMB.get());
                        output.accept(SEItems.HOOF_PICK.get());

                        output.accept(SEItems.GRAIN_FEED.get());
                        output.accept(SEItems.HEARTY_GRAIN_FEED.get());
                        output.accept(SEItems.KIBBLE.get());
                        output.accept(SEItems.HEARTY_KIBBLE.get());

                        output.accept(SEItems.HORSE_MANNEQUIN.get());

                        output.accept(SEItems.BLACK_HALTER.get());
                        output.accept(SEItems.BLUE_HALTER.get());
                        output.accept(SEItems.BROWN_HALTER.get());
                        output.accept(SEItems.CYAN_HALTER.get());
                        output.accept(SEItems.GREEN_HALTER.get());
                        output.accept(SEItems.GREY_HALTER.get());
                        output.accept(SEItems.LIGHT_BLUE_HALTER.get());
                        output.accept(SEItems.LIGHT_GREY_HALTER.get());
                        output.accept(SEItems.LIME_HALTER.get());
                        output.accept(SEItems.MAGENTA_HALTER.get());
                        output.accept(SEItems.ORANGE_HALTER.get());
                        output.accept(SEItems.PINK_HALTER.get());
                        output.accept(SEItems.PURPLE_HALTER.get());
                        output.accept(SEItems.RED_HALTER.get());
                        output.accept(SEItems.WHITE_HALTER.get());
                        output.accept(SEItems.YELLOW_HALTER.get());

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}


