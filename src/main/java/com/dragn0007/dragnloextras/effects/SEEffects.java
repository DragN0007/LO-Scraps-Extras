package com.dragn0007.dragnloextras.effects;

import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.effects.traits.BraveEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SEEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ScrapsExtras.MODID);

    //TODO: ailment classes

    //Ailments
    public static final RegistryObject<MobEffect> IMMUNOCOMPROMISED = MOB_EFFECTS.register("immunocompromised",
            () -> new ImmunocomprimisedEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> DIRTY = MOB_EFFECTS.register("dirty",
            () -> new DirtyEffect(MobEffectCategory.NEUTRAL, 0x5c0d12));

    public static final RegistryObject<MobEffect> INFECTION = MOB_EFFECTS.register("infection",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> RABIES = MOB_EFFECTS.register("rabies",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> HOOF_ABSCESS = MOB_EFFECTS.register("hoof_abscess",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> ABRASION = MOB_EFFECTS.register("abrasion",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> RAIN_ROT = MOB_EFFECTS.register("rain_rot",
            () -> new InfectionEffect(MobEffectCategory.NEUTRAL, 0x5c0d12));

    public static final RegistryObject<MobEffect> EAR_INFECTION = MOB_EFFECTS.register("ear_infection",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> FLEA_INFESTATION = MOB_EFFECTS.register("flea_infestation",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> HEARTWORMS = MOB_EFFECTS.register("heartworms",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> RINGWORM = MOB_EFFECTS.register("ringworm",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> BOTFLY_INFESTATION = MOB_EFFECTS.register("botfly_infestation",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> MANGE = MOB_EFFECTS.register("mange",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static final RegistryObject<MobEffect> HUNGER = MOB_EFFECTS.register("hunger",
            () -> new InfectionEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    //Traits
    public static final RegistryObject<MobEffect> BRAVE = MOB_EFFECTS.register("brave",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));
    public static final RegistryObject<MobEffect> IMMUNOCOMPETENT = MOB_EFFECTS.register("immunocompetent",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));
    public static final RegistryObject<MobEffect> SWIFT = MOB_EFFECTS.register("swift",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));
    public static final RegistryObject<MobEffect> VAULTER = MOB_EFFECTS.register("vaulter",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));
    public static final RegistryObject<MobEffect> CLIMBER = MOB_EFFECTS.register("climber",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));
    public static final RegistryObject<MobEffect> BUSTER = MOB_EFFECTS.register("buster",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));
    public static final RegistryObject<MobEffect> STURDY = MOB_EFFECTS.register("sturdy",
            () -> new BraveEffect(MobEffectCategory.BENEFICIAL, 0x5c0d12));

    public static final RegistryObject<MobEffect> COWARDLY = MOB_EFFECTS.register("cowardly",
            () -> new BraveEffect(MobEffectCategory.HARMFUL, 0x5c0d12));
    public static final RegistryObject<MobEffect> IMMUNOSUPPRESSED = MOB_EFFECTS.register("immunosuppressed",
            () -> new BraveEffect(MobEffectCategory.HARMFUL, 0x5c0d12));
    public static final RegistryObject<MobEffect> STUBBORN = MOB_EFFECTS.register("stubborn",
            () -> new BraveEffect(MobEffectCategory.HARMFUL, 0x5c0d12));
    public static final RegistryObject<MobEffect> LAGGARD = MOB_EFFECTS.register("laggard",
            () -> new BraveEffect(MobEffectCategory.HARMFUL, 0x5c0d12));
    public static final RegistryObject<MobEffect> FRAIL = MOB_EFFECTS.register("frail",
            () -> new BraveEffect(MobEffectCategory.HARMFUL, 0x5c0d12));
    public static final RegistryObject<MobEffect> MEAN = MOB_EFFECTS.register("mean",
            () -> new BraveEffect(MobEffectCategory.HARMFUL, 0x5c0d12));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}