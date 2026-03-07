package com.dragn0007.dragnloextras.items;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.items.custom.*;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SEItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ScrapsExtras.MODID);

    //TODO: item classes
    public static final RegistryObject<Item> STETHOSCOPE = ITEMS.register("stethoscope",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ANTIBIOTIC_INJECTION = ITEMS.register("antibiotic_injection",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIBIOTIC_OINTMENT = ITEMS.register("antibiotic_ointment",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIBIOTIC_EARDROPS = ITEMS.register("antibiotic_eardrops",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIPARASITIC_OINTMENT = ITEMS.register("antiparasitic_ointment",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> HEARTWORM_MEDICINE = ITEMS.register("heartworm_medicine",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> RABIES_SHOT = ITEMS.register("rabies_shot",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> VETERINARY_BANDAGE = ITEMS.register("veterinary_bandage",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> BRUSH = ITEMS.register("brush",
            () -> new BrushItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> COMB = ITEMS.register("comb",
            () -> new CombItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HOOF_PICK = ITEMS.register("hoof_pick",
            () -> new HoofPickItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GRAIN_FEED = ITEMS.register("grain_feed",
            () -> new GrainItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> HEARTY_GRAIN_FEED = ITEMS.register("hearty_grain_feed",
            () -> new HeartyGrainItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> KIBBLE = ITEMS.register("kibble",
            () -> new KibbleItem(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> HEARTY_KIBBLE = ITEMS.register("hearty_kibble",
            () -> new HeartyKibbleItem(new Item.Properties().stacksTo(64)));
    
    public static final RegistryObject<Item> BLACK_HALTER = ITEMS.register("black_halter",
            () -> new HalterItem(DyeColor.BLACK, new Item.Properties()));
    public static final RegistryObject<Item> BLUE_HALTER = ITEMS.register("blue_halter",
            () -> new HalterItem(DyeColor.BLUE, new Item.Properties()));
    public static final RegistryObject<Item> BROWN_HALTER = ITEMS.register("brown_halter",
            () -> new HalterItem(DyeColor.BROWN, new Item.Properties()));
    public static final RegistryObject<Item> CYAN_HALTER = ITEMS.register("cyan_halter",
            () -> new HalterItem(DyeColor.CYAN, new Item.Properties()));
    public static final RegistryObject<Item> GREEN_HALTER = ITEMS.register("green_halter",
            () -> new HalterItem(DyeColor.GREEN, new Item.Properties()));
    public static final RegistryObject<Item> GREY_HALTER = ITEMS.register("grey_halter",
            () -> new HalterItem(DyeColor.GRAY, new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_BLUE_HALTER = ITEMS.register("light_blue_halter",
            () -> new HalterItem(DyeColor.LIGHT_BLUE, new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_GREY_HALTER = ITEMS.register("light_grey_halter",
            () -> new HalterItem(DyeColor.LIGHT_GRAY, new Item.Properties()));
    public static final RegistryObject<Item> LIME_HALTER = ITEMS.register("lime_halter",
            () -> new HalterItem(DyeColor.LIME, new Item.Properties()));
    public static final RegistryObject<Item> MAGENTA_HALTER = ITEMS.register("magenta_halter",
            () -> new HalterItem(DyeColor.MAGENTA, new Item.Properties()));
    public static final RegistryObject<Item> ORANGE_HALTER = ITEMS.register("orange_halter",
            () -> new HalterItem(DyeColor.ORANGE, new Item.Properties()));
    public static final RegistryObject<Item> PINK_HALTER = ITEMS.register("pink_halter",
            () -> new HalterItem(DyeColor.PINK, new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_HALTER = ITEMS.register("purple_halter",
            () -> new HalterItem(DyeColor.PURPLE, new Item.Properties()));
    public static final RegistryObject<Item> RED_HALTER = ITEMS.register("red_halter",
            () -> new HalterItem(DyeColor.RED, new Item.Properties()));
    public static final RegistryObject<Item> WHITE_HALTER = ITEMS.register("white_halter",
            () -> new HalterItem(DyeColor.WHITE, new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_HALTER = ITEMS.register("yellow_halter",
            () -> new HalterItem(DyeColor.YELLOW, new Item.Properties()));


    public static final RegistryObject<Item> LOGO = ITEMS.register("logo",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}