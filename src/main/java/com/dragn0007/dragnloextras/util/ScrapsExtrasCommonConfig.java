package com.dragn0007.dragnloextras.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class ScrapsExtrasCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue HYGIENE_SYSTEM;
    public static final ForgeConfigSpec.ConfigValue<Integer> DIRTY_TICK;
    public static final ForgeConfigSpec.BooleanValue DIRTY_IMMUNITY_DAMPER;
    public static final ForgeConfigSpec.ConfigValue<Integer> DIRTY_IMMUNITY_DAMPER_TICK;
    public static final ForgeConfigSpec.BooleanValue GESTATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> GESTATION_TICK;
    public static final ForgeConfigSpec.BooleanValue FEEDING_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue HUNGRY_IMMUNITY_DAMPER;
    public static final ForgeConfigSpec.ConfigValue<Integer> HUNGRY_IMMUNITY_DAMPER_TICK;
    public static final ForgeConfigSpec.BooleanValue HORSE_FOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> HORSE_FEED_TICK;
    public static final ForgeConfigSpec.BooleanValue DOG_FOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> DOG_FEED_TICK;
    public static final ForgeConfigSpec.BooleanValue WOLF_FOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> WOLF_FEED_TICK;
    public static final ForgeConfigSpec.BooleanValue CAT_FOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> CAT_FEED_TICK;
    public static final ForgeConfigSpec.BooleanValue OCELOT_FOOD;
    public static final ForgeConfigSpec.ConfigValue<Integer> OCELOT_FEED_TICK;
    public static final ForgeConfigSpec.BooleanValue AILMENT_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue TREATMENTS;
    public static final ForgeConfigSpec.BooleanValue TREATMENT_RECIPES;
    public static final ForgeConfigSpec.BooleanValue LETHAL_INFECTIONS;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFECTION_TICK;
    public static final ForgeConfigSpec.ConfigValue<Integer> INFECTION_DEATH_TICK;
    public static final ForgeConfigSpec.BooleanValue LETHAL_RABIES;
    public static final ForgeConfigSpec.ConfigValue<Integer> RABIES_TREATABLE_TICK;
    public static final ForgeConfigSpec.ConfigValue<Integer> RABIES_DEATH_TICK;
    public static final ForgeConfigSpec.BooleanValue INFECTIONS;
    public static final ForgeConfigSpec.BooleanValue RABIES;
    public static final ForgeConfigSpec.BooleanValue HOOF_ABSCESS;
    public static final ForgeConfigSpec.BooleanValue ABRASION;
    public static final ForgeConfigSpec.BooleanValue RAIN_ROT;
    public static final ForgeConfigSpec.BooleanValue EAR_INFECTION;
    public static final ForgeConfigSpec.BooleanValue FLEA_INFESTATION;
    public static final ForgeConfigSpec.BooleanValue HEARTWORMS;
    public static final ForgeConfigSpec.BooleanValue RINGWORM;
    public static final ForgeConfigSpec.BooleanValue BOTFLY_INFESTATION;
    public static final ForgeConfigSpec.BooleanValue MANGE;
    public static final ForgeConfigSpec.BooleanValue SADDLE_SORE;
    public static final ForgeConfigSpec.BooleanValue TRAITS_SYSTEM;
    public static final ForgeConfigSpec.BooleanValue TRAITS_BY_BREED;
    public static final ForgeConfigSpec.BooleanValue GOOD_TRAITS_ONLY;
    public static final ForgeConfigSpec.BooleanValue HALTER;
    public static final ForgeConfigSpec.BooleanValue SPIKE_COLLAR;
    public static final ForgeConfigSpec.BooleanValue SLEEPING;

    static {
        BUILDER.push("Hygiene & Welfare");
        HYGIENE_SYSTEM = BUILDER.define("Hygiene System Enabled", true);
        DIRTY_TICK = BUILDER.comment("The amount of time at which an animal becomes dirty. Default is 72000 ticks.")
                .define("Dirty Tick", 72000);
        DIRTY_IMMUNITY_DAMPER = BUILDER.comment("Should being dirty negatively affect an animal's immune system (making it easier for them to become sick)?")
                .define("Dirty Immunity Damper", true);
        DIRTY_IMMUNITY_DAMPER_TICK = BUILDER.comment("The amount of time at which an animal's immune system becomes dampened from being dirty. Default is 72000 ticks.")
                .define("Dirty Immunity Damper Tick", 72000);
        GESTATION = BUILDER.comment("Should female animals become pregnant and have a gestation period after breeding? This requires the (LO) Genders Affect Breeding config to be on.")
                .define("Gestation", true);
        GESTATION_TICK = BUILDER.comment("The amount of time at which an animal gestates. Default is 72000 ticks.")
                .define("Gestation Tick", 72000);
        BUILDER.pop();

        BUILDER.push("Food");
        FEEDING_SYSTEM = BUILDER.define("Feeding System Enabled", true);
        HUNGRY_IMMUNITY_DAMPER = BUILDER.comment("Should starving negatively affect an animal's immune system (making it easier for them to become sick)?")
                .define("Starving Immunity Damper", true);
        HUNGRY_IMMUNITY_DAMPER_TICK = BUILDER.comment("The amount of time at which an animal's immune system becomes dampened from starving. Default is 72000 ticks.")
                .define("Starving Immunity Damper Tick", 72000);
        HORSE_FOOD = BUILDER.define("Horses Need Food", true);
        HORSE_FEED_TICK = BUILDER.comment("The amount of time at which a horse needs to be fed. Default is 48000 ticks.")
                .define("Horse Feed Tick", 48000);
        DOG_FOOD = BUILDER.define("Dogs Need Food", true);
        DOG_FEED_TICK = BUILDER.comment("The amount of time at which a dog needs to be fed. Default is 48000 ticks.")
                .define("Dog Feed Tick", 48000);
        WOLF_FOOD = BUILDER.define("Wolves Need Food", true);
        WOLF_FEED_TICK = BUILDER.comment("The amount of time at which a wolf needs to be fed. Default is 72000 ticks.")
                .define("Wolf Feed Tick", 72000);
        CAT_FOOD = BUILDER.define("Cats Need Food", true);
        CAT_FEED_TICK = BUILDER.comment("The amount of time at which a cat needs to be fed. Default is 48000 ticks.")
                .define("Cat Feed Tick", 48000);
        OCELOT_FOOD = BUILDER.define("Ocelots Need Food", true);
        OCELOT_FEED_TICK = BUILDER.comment("The amount of time at which an ocelot needs to be fed. Default is 72000 ticks.")
                .define("Ocelot Feed Tick", 72000);
        BUILDER.pop();

        BUILDER.push("Ailments");
        AILMENT_SYSTEM = BUILDER.define("Ailment System Enabled", true);
        TREATMENTS = BUILDER.comment("Should treatments/ medical items work?")
                .define("Treatments Enabled", true);
        TREATMENT_RECIPES = BUILDER.comment("Should players be able to craft treatments/ medical items?")
                .define("Treatment Crafting Enabled", true);
        LETHAL_INFECTIONS = BUILDER.comment("Should infections eventually become lethal if left untreated?")
                .define("Lethal Infections", true);
        INFECTION_TICK = BUILDER.comment("The amount of time at which a wound becomes infected. Default is 72000 ticks.")
                .define("Infection Tick", 72000);
        INFECTION_DEATH_TICK = BUILDER.comment("The amount of time at which an infection becomes deadly. Default is 168,000 ticks.")
                .define("Infection Death Tick", 168000);
        LETHAL_RABIES = BUILDER.comment("Should rabies eventually become lethal if left untreated?")
                .define("Lethal Rabies", true);
        RABIES_TREATABLE_TICK = BUILDER.comment("The amount of time at which rabies becomes untreatable. Set higher to give more time for treatment. Default is 72000 ticks.")
                .define("Rabies Treatable Tick", 72000);
        RABIES_DEATH_TICK = BUILDER.comment("The amount of time at which rabies becomes deadly and begins killing an animal. Default is 168,000 ticks.")
                .define("Rabies Death Tick", 168000);
        INFECTIONS = BUILDER.define("Infection Enabled", true);
        RABIES = BUILDER.define("Rabies Enabled", true);
        HOOF_ABSCESS = BUILDER.define("Hoof Abscess Enabled", true);
        ABRASION = BUILDER.define("Abrasion Enabled", true);
        RAIN_ROT = BUILDER.define("Rain Rot Enabled", true);
        EAR_INFECTION = BUILDER.define("Ear Infection Enabled", true);
        FLEA_INFESTATION = BUILDER.define("Flea Infestation Enabled", true);
        HEARTWORMS = BUILDER.define("Heartworms Enabled", true);
        RINGWORM = BUILDER.define("Ringworm Enabled", true);
        BOTFLY_INFESTATION = BUILDER.define("Botfly Infestation", true);
        MANGE = BUILDER.define("Mange Enabled", true);
        SADDLE_SORE = BUILDER.define("Saddle Sore Enabled", true);
        BUILDER.pop();

        BUILDER.push("Traits");
        TRAITS_SYSTEM = BUILDER.define("Traits System Enabled", true);
        TRAITS_BY_BREED = BUILDER.comment("Should traits depend on the breed of the animal? (For example, Draft horses are more likely to get a \"strong\"-type trait)")
                .define("Traits Chance By Breed", true);
        GOOD_TRAITS_ONLY = BUILDER.comment("Should animals only be allowed to spawn/ be born with beneficial traits? This does not change already-existing animals' traits.")
                .define("Good Traits Only", false);
        BUILDER.pop();

        BUILDER.push("Misc");
        HALTER = BUILDER.define("Halter Owner Following Enabled", true);
        SPIKE_COLLAR = BUILDER.define("Spike Collar Thorns Enabled", true);
        SLEEPING = BUILDER.define("Animals Sleep at Night", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
