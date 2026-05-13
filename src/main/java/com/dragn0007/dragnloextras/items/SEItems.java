package com.dragn0007.dragnloextras.items;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.items.custom.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SaddleItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SEItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ScrapsExtras.MODID);

    public static final RegistryObject<Item> HORSE_MANNEQUIN = ITEMS.register("horse_mannequin",
            () -> new ForgeSpawnEggItem(SEEntityTypes.HORSE_MANNEQUIN_ENTITY, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> STETHOSCOPE = ITEMS.register("stethoscope",
            () -> new StethoscopeItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ANTIBIOTIC_INJECTION = ITEMS.register("antibiotic_injection",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIBIOTIC_OINTMENT = ITEMS.register("antibiotic_ointment",
            () -> new AntibioticOintmentItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIBIOTIC_EARDROPS = ITEMS.register("antibiotic_eardrops",
            () -> new EardropsItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIPARASITIC_OINTMENT = ITEMS.register("antiparasitic_ointment",
            () -> new ParasiticOintmentItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ANTIPARASITIC_INJECTION = ITEMS.register("antiparasitic_injection",
            () -> new AntibioticInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> HEARTWORM_MEDICINE = ITEMS.register("heartworm_medicine",
            () -> new HeartwormMedicineItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> RABIES_SHOT = ITEMS.register("rabies_shot",
            () -> new RabiesInjectionItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> VETERINARY_BANDAGE = ITEMS.register("veterinary_bandage",
            () -> new BandageItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> BRUSH = ITEMS.register("brush",
            () -> new BrushItem(new Item.Properties().stacksTo(1)));
//    public static final RegistryObject<Item> COMB = ITEMS.register("comb",
//            () -> new CombItem(new Item.Properties().stacksTo(1)));
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

    public static final RegistryObject<Item> COLLAR_SPIKES = ITEMS.register("collar_spikes",
            () -> new CollarSpikesItem(new Item.Properties()));

    public static final RegistryObject<Item> RAWHIDE_SADDLE = ITEMS.register("rawhide_saddle",
            () -> new SaddleItem(new Item.Properties()));
    
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

    public static final RegistryObject<Item> BLACK_TURNOUT_BLANKET = ITEMS.register("black_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.BLACK, new Item.Properties()));
    public static final RegistryObject<Item> BLUE_TURNOUT_BLANKET = ITEMS.register("blue_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.BLUE, new Item.Properties()));
    public static final RegistryObject<Item> BROWN_TURNOUT_BLANKET = ITEMS.register("brown_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.BROWN, new Item.Properties()));
    public static final RegistryObject<Item> CYAN_TURNOUT_BLANKET = ITEMS.register("cyan_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.CYAN, new Item.Properties()));
    public static final RegistryObject<Item> GREEN_TURNOUT_BLANKET = ITEMS.register("green_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.GREEN, new Item.Properties()));
    public static final RegistryObject<Item> GREY_TURNOUT_BLANKET = ITEMS.register("grey_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.GRAY, new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_BLUE_TURNOUT_BLANKET = ITEMS.register("light_blue_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.LIGHT_BLUE, new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_GREY_TURNOUT_BLANKET = ITEMS.register("light_grey_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.LIGHT_GRAY, new Item.Properties()));
    public static final RegistryObject<Item> LIME_TURNOUT_BLANKET = ITEMS.register("lime_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.LIME, new Item.Properties()));
    public static final RegistryObject<Item> MAGENTA_TURNOUT_BLANKET = ITEMS.register("magenta_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.MAGENTA, new Item.Properties()));
    public static final RegistryObject<Item> ORANGE_TURNOUT_BLANKET = ITEMS.register("orange_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.ORANGE, new Item.Properties()));
    public static final RegistryObject<Item> PINK_TURNOUT_BLANKET = ITEMS.register("pink_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.PINK, new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_TURNOUT_BLANKET = ITEMS.register("purple_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.PURPLE, new Item.Properties()));
    public static final RegistryObject<Item> RED_TURNOUT_BLANKET = ITEMS.register("red_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.RED, new Item.Properties()));
    public static final RegistryObject<Item> WHITE_TURNOUT_BLANKET = ITEMS.register("white_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.WHITE, new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_TURNOUT_BLANKET = ITEMS.register("yellow_turnout_blanket",
            () -> new TurnoutBlanketItem(DyeColor.YELLOW, new Item.Properties()));

    //Meat Cuts (Transferred from LO: Base)
    public static final RegistryObject<Item> BEEF_BRISKET = ITEMS.register("beef_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> BEEF_CHUCK = ITEMS.register("beef_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> BEEF_RIB = ITEMS.register("beef_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> BEEF_SHANK = ITEMS.register("beef_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_BEEF_BRISKET = ITEMS.register("cooked_beef_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.8F).build())));
    public static final RegistryObject<Item> COOKED_BEEF_CHUCK = ITEMS.register("cooked_beef_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.4F).build())));
    public static final RegistryObject<Item> COOKED_BEEF_RIB = ITEMS.register("cooked_beef_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).meat().saturationMod(0.6F).build())));
    public static final RegistryObject<Item> COOKED_BEEF_SHANK = ITEMS.register("cooked_beef_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.4F).build())));

    public static final RegistryObject<Item> HORSE_BRISKET = ITEMS.register("horse_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> HORSE_CHUCK = ITEMS.register("horse_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> HORSE_RIB = ITEMS.register("horse_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> HORSE_SHANK = ITEMS.register("horse_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_HORSE_BRISKET = ITEMS.register("cooked_horse_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.8F).build())));
    public static final RegistryObject<Item> COOKED_HORSE_CHUCK = ITEMS.register("cooked_horse_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.4F).build())));
    public static final RegistryObject<Item> COOKED_HORSE_RIB = ITEMS.register("cooked_horse_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.6F).build())));
    public static final RegistryObject<Item> COOKED_HORSE_SHANK = ITEMS.register("cooked_horse_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).meat().saturationMod(0.4F).build())));

    public static final RegistryObject<Item> CAMEL_BRISKET = ITEMS.register("camel_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> CAMEL_CHUCK = ITEMS.register("camel_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> CAMEL_RIB = ITEMS.register("camel_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> CAMEL_SHANK = ITEMS.register("camel_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_CAMEL_BRISKET = ITEMS.register("cooked_camel_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.8F).build())));
    public static final RegistryObject<Item> COOKED_CAMEL_CHUCK = ITEMS.register("cooked_camel_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.4F).build())));
    public static final RegistryObject<Item> COOKED_CAMEL_RIB = ITEMS.register("cooked_camel_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.6F).build())));
    public static final RegistryObject<Item> COOKED_CAMEL_SHANK = ITEMS.register("cooked_camel_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).meat().saturationMod(0.4F).build())));

    public static final RegistryObject<Item> MUTTON_FLANK = ITEMS.register("mutton_flank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> MUTTON_RIB = ITEMS.register("mutton_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> MUTTON_LEG = ITEMS.register("mutton_leg",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_MUTTON_FLANK = ITEMS.register("cooked_mutton_flank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.8F).build())));
    public static final RegistryObject<Item> COOKED_MUTTON_RIB = ITEMS.register("cooked_mutton_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.6F).build())));
    public static final RegistryObject<Item> COOKED_MUTTON_LEG = ITEMS.register("cooked_mutton_leg",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).meat().saturationMod(0.4F).build())));

    public static final RegistryObject<Item> CARIBOU_BRISKET = ITEMS.register("caribou_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> CARIBOU_CHUCK = ITEMS.register("caribou_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> CARIBOU_RIB = ITEMS.register("caribou_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> CARIBOU_SHANK = ITEMS.register("caribou_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_CARIBOU_BRISKET = ITEMS.register("cooked_caribou_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.8F).build())));
    public static final RegistryObject<Item> COOKED_CARIBOU_CHUCK = ITEMS.register("cooked_caribou_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.4F).build())));
    public static final RegistryObject<Item> COOKED_CARIBOU_RIB = ITEMS.register("cooked_caribou_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.6F).build())));
    public static final RegistryObject<Item> COOKED_CARIBOU_SHANK = ITEMS.register("cooked_caribou_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).meat().saturationMod(0.4F).build())));

    public static final RegistryObject<Item> CHICKEN_THIGH = ITEMS.register("chicken_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> CHICKEN_WING = ITEMS.register("chicken_wing",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.4F).build())));
    public static final RegistryObject<Item> COOKED_CHICKEN_THIGH = ITEMS.register("cooked_chicken_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.6F).build())));
    public static final RegistryObject<Item> COOKED_CHICKEN_WING = ITEMS.register("cooked_chicken_wing",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).build())));

    public static final RegistryObject<Item> RABBIT_THIGH = ITEMS.register("rabbit_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_RABBIT_THIGH = ITEMS.register("cooked_rabbit_thigh",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(1).build())));

    public static final RegistryObject<Item> SALMON_FILLET = ITEMS.register("salmon_fillet",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).build())));
    public static final RegistryObject<Item> COD_FILLET = ITEMS.register("cod_fillet",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).build())));
    public static final RegistryObject<Item> COOKED_SALMON_FILLET = ITEMS.register("cooked_salmon_fillet",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).build())));
    public static final RegistryObject<Item> COOKED_COD_FILLET = ITEMS.register("cooked_cod_fillet",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).build())));

    public static final RegistryObject<Item> UNICORN_BRISKET = ITEMS.register("unicorn_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> UNICORN_CHUCK = ITEMS.register("unicorn_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> UNICORN_RIB = ITEMS.register("unicorn_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> UNICORN_SHANK = ITEMS.register("unicorn_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).meat().saturationMod(1).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> COOKED_UNICORN_BRISKET = ITEMS.register("cooked_unicorn_brisket",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.8F).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> COOKED_UNICORN_CHUCK = ITEMS.register("cooked_unicorn_chuck",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).meat().saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> COOKED_UNICORN_RIB = ITEMS.register("cooked_unicorn_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).meat().saturationMod(0.6F).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));
    public static final RegistryObject<Item> COOKED_UNICORN_SHANK = ITEMS.register("cooked_unicorn_shank",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).meat().saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.REGENERATION, 600, 0), 0.8F).effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 0.8F).build())));

    public static final RegistryObject<Item> LLAMA_RIB = ITEMS.register("llama_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build())));
    public static final RegistryObject<Item> LLAMA_LOIN = ITEMS.register("llama_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_LLAMA_RIB = ITEMS.register("cooked_llama_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_LLAMA_LOIN = ITEMS.register("cooked_llama_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(9).saturationMod(1).build())));
    public static final RegistryObject<Item> PORK_RIB_CHOP = ITEMS.register("pork_rib_chop",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build())));
    public static final RegistryObject<Item> PORK_TENDERLOIN = ITEMS.register("pork_tenderloin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_PORK_RIB_CHOP = ITEMS.register("cooked_pork_rib_chop",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_PORK_TENDERLOIN = ITEMS.register("cooked_pork_tenderloin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1).build())));
    public static final RegistryObject<Item> CHEVON_RIB = ITEMS.register("chevon_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build())));
    public static final RegistryObject<Item> CHEVON_LOIN = ITEMS.register("chevon_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_CHEVON_RIB = ITEMS.register("cooked_chevon_rib",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1).build())));
    public static final RegistryObject<Item> COOKED_CHEVON_LOIN = ITEMS.register("cooked_chevon_loin",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(1).build())));

    public static final RegistryObject<Item> LOGO = ITEMS.register("logo",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}