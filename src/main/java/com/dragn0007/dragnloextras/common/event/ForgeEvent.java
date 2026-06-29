package com.dragn0007.dragnloextras.common.event;

import com.dragn0007.dragnlivestock.entities.camel.OCamel;
import com.dragn0007.dragnlivestock.entities.caribou.Caribou;
import com.dragn0007.dragnlivestock.entities.chicken.OChicken;
import com.dragn0007.dragnlivestock.entities.cow.OCow;
import com.dragn0007.dragnlivestock.entities.donkey.ODonkey;
import com.dragn0007.dragnlivestock.entities.farm_goat.FarmGoat;
import com.dragn0007.dragnlivestock.entities.goat.OGoat;
import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.entities.llama.OLlama;
import com.dragn0007.dragnlivestock.entities.mule.OMule;
import com.dragn0007.dragnlivestock.entities.pig.OPig;
import com.dragn0007.dragnlivestock.entities.rabbit.ORabbit;
import com.dragn0007.dragnlivestock.entities.sheep.OSheep;
import com.dragn0007.dragnlivestock.entities.unicorn.Unicorn;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.entity.butchering.*;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.*;
import com.dragn0007.dragnloextras.util.ISleepAsLeaderHolder;
import com.dragn0007.dragnloextras.util.SETags;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {

    @SubscribeEvent
    public void setBabyNBT(BabyEntitySpawnEvent event) {
        LivingEntity baby = event.getChild();
        if (baby.getType().is(SETags.Entity_Types.TRAITABLE)) {
            CompoundTag nbt = baby.getPersistentData();
            if (!nbt.getBoolean("loextras_initialized")) {
                nbt.putBoolean("loextras_initialized", true);
            }
        }
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        SECapabilities.register(event);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();

        //syncs the player client back up when they rejoin so they render the correct layers
        if (!target.level().isClientSide) {

            target.getCapability(SECapabilities.HALTER_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncHalterLayerPacket(target.getId(), cap.hasHalter())
                );
            });

            target.getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncHalterColorPacket(target.getId(), cap.getHalterColor())
                );
            });

            target.getCapability(SECapabilities.DIRTY_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncDirtyLayerPacket(target.getId(), cap.isDirty())
                );
            });

            target.getCapability(SECapabilities.TRAIT_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncTraitPacket(target.getId(), cap.getTrait())
                );
            });

            target.getCapability(SECapabilities.IMMUNITY_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncImmunityPacket(target.getId(), cap.getImmunity())
                );
            });

            target.getCapability(SECapabilities.SLEEPING_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncSleepingPacket(target.getId(), cap.isSleeping())
                );
            });

            if (target instanceof OHorse horse) {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncSleepingAsLeaderPacket(target.getId(), ((ISleepAsLeaderHolder) horse).isSleepingAsLeader())
                );
            }

            target.getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).ifPresent(cap -> {
                SENetwork.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SyncSpikeCollarLayerPacket(target.getId(), cap.hasSpikeCollar())
                );
            });

        }
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        //attach general caps
        if (SETags.CAPABLE.contains(event.getObject().getType())) {

            if (SETags.SLEEPABLE.contains(event.getObject().getType())) {
                if (!event.getObject().getCapability(SECapabilities.SLEEPING_CAPABILITY).isPresent()) {
                    SleepingCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Sleeping Capability NBT to " + event.getObject().getName());
                    }
                }
            }

            if (SETags.DIRTYABLE.contains(event.getObject().getType())) {
                if (!event.getObject().getCapability(SECapabilities.DIRTY_CAPABILITY).isPresent()) {
                    DirtyCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Dirty Capability NBT to " + event.getObject().getName());
                    }
                }
            }

            if (SETags.TRAITABLE.contains(event.getObject().getType())) {
                if (!event.getObject().getCapability(SECapabilities.TRAIT_CAPABILITY).isPresent()) {
                    TraitCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Trait Capability NBT to " + event.getObject().getName());
                    }
                }
            }

            if (SETags.IMMUNITYABLE.contains(event.getObject().getType())) {
                if (!event.getObject().getCapability(SECapabilities.IMMUNITY_CAPABILITY).isPresent()) {
                    ImmunityCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Immunity Capability NBT to " + event.getObject().getName());
                    }
                }
            }

            if (SETags.HALTERABLE.contains(event.getObject().getType())) {
                if (!event.getObject().getCapability(SECapabilities.HALTER_CAPABILITY).isPresent()) {
                    HalterCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Halter Capability NBT to " + event.getObject().getName());
                    }
                }
                if (!event.getObject().getCapability(SECapabilities.HALTER_COLOR_CAPABILITY).isPresent()) {
                    HalterColorCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Halter Color Capability NBT to " + event.getObject().getName());
                    }
                }
            }

            if (SETags.SPIKE_COLLARABLE.contains(event.getObject().getType())) {
                if (!event.getObject().getCapability(SECapabilities.SPIKE_COLLAR_CAPABILITY).isPresent()) {
                    SpikeCollarCapabilityAttacher.attach(event);
                    if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                        System.out.println("Attached the Spike Collar Capability NBT to " + event.getObject().getName());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTryCureEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity entity) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();

            if (player.isSecondaryUseActive()) {
                if (stack.is(SEItems.ANTIBIOTIC_INJECTION.get())) {
                    if (entity.hasEffect(SEEffects.INFECTION.get())) {
                        entity.removeEffect(SEEffects.INFECTION.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else if (entity.hasEffect(SEEffects.HOOF_ABSCESS.get())) {
                        entity.removeEffect(SEEffects.HOOF_ABSCESS.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }

                if (stack.is(SEItems.ANTIBIOTIC_OINTMENT.get())) {
                    if (entity.hasEffect(SEEffects.RAIN_ROT.get())) {
                        entity.removeEffect(SEEffects.RAIN_ROT.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }

                if (stack.is(SEItems.ANTIPARASITIC_OINTMENT.get())) {
                    if (entity.hasEffect(SEEffects.MANGE.get())) {
                        entity.removeEffect(SEEffects.MANGE.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else if (entity.hasEffect(SEEffects.FLEA_INFESTATION.get())) {
                        entity.removeEffect(SEEffects.FLEA_INFESTATION.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }

                if (stack.is(SEItems.ANTIPARASITIC_INJECTION.get())) {
                    if (entity.hasEffect(SEEffects.HEARTWORMS.get())) {
                        entity.removeEffect(SEEffects.HEARTWORMS.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else if (entity.hasEffect(SEEffects.BOTFLY_INFESTATION.get())) {
                        entity.removeEffect(SEEffects.BOTFLY_INFESTATION.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else if (entity.hasEffect(SEEffects.RINGWORM.get())) {
                        entity.removeEffect(SEEffects.RINGWORM.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }

                if (stack.is(SEItems.ANTIBIOTIC_EARDROPS.get())) {
                    if (entity.hasEffect(SEEffects.EAR_INFECTION.get())) {
                        entity.removeEffect(SEEffects.EAR_INFECTION.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }

                if (stack.is(SEItems.VETERINARY_BANDAGE.get())) {
                    if (entity.hasEffect(SEEffects.SADDLE_SORE.get())) {
                        entity.removeEffect(SEEffects.SADDLE_SORE.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else if (entity.hasEffect(SEEffects.ABRASION.get())) {
                        entity.removeEffect(SEEffects.ABRASION.get());
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }

                if (stack.is(SEItems.RABIES_SHOT.get())) {
                    if (entity.hasEffect(SEEffects.RABIES.get())) {
                        int duration = entity.getEffect(SEEffects.RABIES.get()).getDuration();
                        if (duration <= ScrapsExtrasCommonConfig.RABIES_TREATABLE_TICK.get()) {
                            entity.removeEffect(SEEffects.RABIES.get());
                            if (!player.getAbilities().instabuild) {
                                stack.shrink(1);
                            }
                            player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                        } else {
                            player.displayClientMessage(Component.translatable("tooltip.dragnloextras.untreatable.tooltip").withStyle(ChatFormatting.GOLD), true);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public static void onUseStethoscope(PlayerInteractEvent.EntityInteract event) {

        if (event.getTarget() instanceof LivingEntity animal) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();
            if (event.getLevel().isClientSide()) return;

            if (stack.is(SEItems.STETHOSCOPE.get())) {
                //trait
                if (ScrapsExtrasCommonConfig.TRAITS_SYSTEM.get()) {
                    if (animal.getCapability(SECapabilities.TRAIT_CAPABILITY).isPresent()) {
                        TraitCapabilityInterface traitCap = animal.getCapability(SECapabilities.TRAIT_CAPABILITY).orElse(null);
                        String traitText = getTraitText(traitCap.getTrait());
                        player.sendSystemMessage(Component.translatable(traitText).withStyle(ChatFormatting.WHITE));
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("The Scraps & Extras ailment system doesn't apply to this animal or is disabled on this server.").withStyle(ChatFormatting.RED));
                }

//                //hunger
                if (ScrapsExtrasCommonConfig.FEEDING_SYSTEM.get()) {
                    String feedingText;
                    if (animal.hasEffect(SEEffects.HUNGER.get())) {
                        feedingText = "This animal is currently hungry and should be fed.";
                        player.sendSystemMessage(Component.translatable("Feeding: " + feedingText).withStyle(ChatFormatting.YELLOW));
//                    } else {
//                        CompoundTag tag = animal.getPersistentData();
//                        if (tag.contains("HungryTick")) {
//                            int ticks = tag.getInt("HungryTick");
//                            int tickToMinutes = ticks / 1200;
//
//                            if (animal instanceof OHorse || animal instanceof OMule || animal instanceof ODonkey || animal instanceof OCamel || animal instanceof Caribou) {
//                                feedingText = "This animal has " + (ScrapsExtrasCommonConfig.HORSE_FEED_TICK.get() - tickToMinutes) + " minute(s) left until it becomes hungry.\n";
//                                player.sendSystemMessage(Component.translatable("Feeding: " + feedingText).withStyle(ChatFormatting.WHITE));
//                            }
//                            if (animal instanceof ODog) {
//                                feedingText = "This animal has " + (ScrapsExtrasCommonConfig.DOG_FEED_TICK.get() - tickToMinutes) + " minute(s) left until it becomes hungry.\n";
//                                player.sendSystemMessage(Component.translatable("Feeding: " + feedingText).withStyle(ChatFormatting.WHITE));
//                            }
//                            if (animal instanceof OWolf) {
//                                feedingText = "This animal has " + (ScrapsExtrasCommonConfig.WOLF_FEED_TICK.get() - tickToMinutes) + " minute(s) left until it becomes hungry.\n";
//                                player.sendSystemMessage(Component.translatable("Feeding: " + feedingText).withStyle(ChatFormatting.WHITE));
//                            }
//                            if (animal instanceof OCat) {
//                                feedingText = "This animal has " + (ScrapsExtrasCommonConfig.CAT_FEED_TICK.get() - tickToMinutes) + " minute(s) left until it becomes hungry.\n";
//                                player.sendSystemMessage(Component.translatable("Feeding: " + feedingText).withStyle(ChatFormatting.WHITE));
//                            }
//                            if (animal instanceof OOcelot) {
//                                feedingText = "This animal has " + (ScrapsExtrasCommonConfig.OCELOT_FEED_TICK.get() - tickToMinutes) + " minute(s) left until it becomes hungry.\n";
//                                player.sendSystemMessage(Component.translatable("Feeding: " + feedingText).withStyle(ChatFormatting.WHITE));
//                            }
//                        }
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("The Scraps & Extras feeding system doesn't apply to this animal or is disabled on this server.").withStyle(ChatFormatting.RED));
                }

                //immunity
                if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
                    if (animal.getCapability(SECapabilities.IMMUNITY_CAPABILITY).isPresent()) {
                        ImmunityCapabilityInterface immunityCap = animal.getCapability(SECapabilities.IMMUNITY_CAPABILITY).orElse(null);
                        String immunityText = "";
                        if (immunityCap.getImmunity() <= 25) {
                            immunityText = immunityCap.getImmunity() + "%" +  "\nThis animal has a poor immune system and may get sick more often.";
                        } else if (immunityCap.getImmunity() > 25 && immunityCap.getImmunity() <= 50) {
                            immunityText = immunityCap.getImmunity() + "%" +  "\nThis animal has a fine immune system and may get sick occasionally.";
                        } else if (immunityCap.getImmunity() > 50 && immunityCap.getImmunity() <= 75) {
                            immunityText = immunityCap.getImmunity() + "%" + "\nThis animal has a good immune system and may not get sick often!";
                        } else if (immunityCap.getImmunity() > 75 && immunityCap.getImmunity() <= 100) {
                            immunityText = immunityCap.getImmunity() + "%" +  "\nThis animal has a great immune system and may hardly ever get sick, if at all!";
                        }
                        player.sendSystemMessage(Component.translatable("Immunity: " + immunityText).withStyle(ChatFormatting.WHITE));
                    } else {
                        player.sendSystemMessage(Component.translatable("The Scraps & Extras ailment system doesn't apply to this animal or is disabled on this server.").withStyle(ChatFormatting.RED));
                    }
                }

                //illness (if applicable)
                if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
                    String illnessText = "";
                    if (animal.hasEffect(SEEffects.IMMUNOCOMPROMISED.get())) {
                        illnessText = "This animal is Immunocompromised - likely from extensive dirtiness or starvation.\n";
                    } else if (animal.hasEffect(SEEffects.INFECTION.get())) {
                        illnessText = "This animal has an Infection.\n";
                    } else if (animal.hasEffect(SEEffects.RABIES.get())) {
                        illnessText = "This animal has Rabies.\n";
                    } else if (animal.hasEffect(SEEffects.HOOF_ABSCESS.get())) {
                        illnessText = "This animal has a Hoof Abscess.\n";
                    } else if (animal.hasEffect(SEEffects.ABRASION.get())) {
                        illnessText = "This animal has an Abrasion.\n";
                    } else if (animal.hasEffect(SEEffects.SADDLE_SORE.get())) {
                        illnessText = "This animal has a Saddle Sore.\n";
                    } else if (animal.hasEffect(SEEffects.EAR_INFECTION.get())) {
                        illnessText = "This animal has an Ear Infection.\n";
                    } else if (animal.hasEffect(SEEffects.BOTFLY_INFESTATION.get())) {
                        illnessText = "This animal has a Botfly Infestation.\n";
                    } else if (animal.hasEffect(SEEffects.FLEA_INFESTATION.get())) {
                        illnessText = "This animal has a Flea Infestation.\n";
                    } else if (animal.hasEffect(SEEffects.HEARTWORMS.get())) {
                        illnessText = "This animal has Heartworms.\n";
                    } else if (animal.hasEffect(SEEffects.RINGWORM.get())) {
                        illnessText = "This animal has Ringworm.\n";
                    } else if (animal.hasEffect(SEEffects.MANGE.get())) {
                        illnessText = "This animal has Mange.\n";
                    } else if (animal.hasEffect(SEEffects.RAIN_ROT.get())) {
                        illnessText = "This animal has Rain Rot.\n";
                    } else {
                        illnessText = "\nThis animal is healthy and has no current ailments!\n";
                    }
                    player.sendSystemMessage(Component.translatable(illnessText).withStyle(ChatFormatting.YELLOW));
                } else {
                    player.sendSystemMessage(Component.translatable("The Scraps & Extras ailment system is disabled on this server.").withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    private static String getTraitText(int traits) {
        return switch (traits) {
            case 0 -> "Trait: Brave \nBrave animals regenerate health faster, even - especially - in the face of danger.\n";
            case 1 -> "Trait: Immunocompetent \nImmunocompetent animals are far less likely to get sick from common illnesses, and are more likely to recover from them.\n";
            case 2 -> "Trait: Swift \nSwift animals are faster on their feet.\n";
            case 3 -> "Trait: Vaulter \nVaulters are better at jumping and leaping, vaulting over even the tallest of obstacles.\n";
            case 4 -> "Trait: Climber \nClimbers are great at climbing tough terrain; perfect for mountainous regions.\n";
            case 5 -> "Trait: Buster \nBusters deal more damage, making them great battle-buddies.\n";
            case 6 -> "Trait: Sturdy \nSturdy animals have more overall health and are slightly less likely to get sick from common illnesses.\n";
            case 7 -> "Trait: Cowardly \nCowardly animals are more likely to run away when low on health, and don't regenerate health at all when threatened.\n";
            case 8 -> "Trait: Immunosuppressed \nImmunosuppressed animals are far more likely to get sick from common illnesses, and are less likely to recover from them.\n";
            case 9 -> "Trait: Stubborn \nStubborn animals may stop randomly, stop following you, or refuse to perform a command.\n";
            case 10 -> "Trait: Laggard \nLaggards are slow on their feet.\n";
            case 11 -> "Trait: Frail \nFrail animals have less overall health and are slightly more likely to get sick from common illnesses.\n";
            case 12 -> "Trait: Hot-Headed \nHot-Headed animals might attack you or other animals out of anger on occasion.\n";
            case 13 -> "Trait: None \nThis animal seems to have no notable personality or physical trait.\n";
            case 14 -> "Trait: Swimmer \nSwimmer animals are faster at swimming.\n";
            case 15 -> "Trait: Sinker \nSinker animals are slower while swimming.\n";
            default -> "Trait: Unknown\n";
        };
    }

    @SubscribeEvent
    public static void corpseOnEntityDeath(LivingDeathEvent event) {
        LivingEntity deceased = event.getEntity();
        Level level = deceased.level();

        if (!deceased.isBaby() && ScrapsExtrasCommonConfig.BUTCHERING.get()) {

            //Base LO
            if (deceased instanceof OCow animal) {
                if (!level.isClientSide()) {
                    CowCorpse corpse = new CowCorpse(SEEntityTypes.COW_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.setQuality(animal.getQuality());
                    if (animal.isMeatBreed()) {
                        corpse.setMeatBreed(true);
                    } else if (animal.isMiniBreed()) {
                        corpse.setMiniBreed(true);
                    } else {
                        corpse.setNormalBreed(true);
                    }
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OHorse animal && animal.getClass() == OHorse.class) {
                if (!level.isClientSide()) {
                    HorseCorpse corpse = new HorseCorpse(SEEntityTypes.HORSE_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OSheep animal) {
                if (!level.isClientSide()) {
                    SheepCorpse corpse = new SheepCorpse(SEEntityTypes.SHEEP_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OMule animal) {
                if (!level.isClientSide()) {
                    MuleCorpse corpse = new MuleCorpse(SEEntityTypes.MULE_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof ODonkey animal) {
                if (!level.isClientSide()) {
                    DonkeyCorpse corpse = new DonkeyCorpse(SEEntityTypes.DONKEY_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OChicken animal) {
                if (!level.isClientSide()) {
                    ChickenCorpse corpse = new ChickenCorpse(SEEntityTypes.CHICKEN_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.setQuality(animal.getQuality());
                    if (animal.isMeatBreed()) {
                        corpse.setMeatBreed(true);
                    }
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof ORabbit animal) {
                if (!level.isClientSide()) {
                    RabbitCorpse corpse = new RabbitCorpse(SEEntityTypes.RABBIT_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.setQuality(animal.getQuality());
                    if (animal.isMeatBreed()) {
                        corpse.setMeatBreed(true);
                    }
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OPig animal) {
                if (!level.isClientSide()) {
                    PigCorpse corpse = new PigCorpse(SEEntityTypes.PIG_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.setQuality(animal.getQuality());
                    if (animal.isMeatBreed()) {
                        corpse.setMeatBreed(true);
                    }
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OCamel animal) {
                if (!level.isClientSide()) {
                    CamelCorpse corpse = new CamelCorpse(SEEntityTypes.CAMEL_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OLlama animal) {
                if (!level.isClientSide()) {
                    LlamaCorpse corpse = new LlamaCorpse(SEEntityTypes.LLAMA_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof FarmGoat animal) {
                if (!level.isClientSide()) {
                    FarmGoatCorpse corpse = new FarmGoatCorpse(SEEntityTypes.FARM_GOAT_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.setQuality(animal.getQuality());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof OGoat animal) {
                if (!level.isClientSide()) {
                    GoatCorpse corpse = new GoatCorpse(SEEntityTypes.GOAT_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.setQuality(animal.getQuality());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof Caribou animal) {
                if (!level.isClientSide()) {
                    CaribouCorpse corpse = new CaribouCorpse(SEEntityTypes.CARIBOU_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
            if (deceased instanceof Unicorn animal) {
                if (!level.isClientSide()) {
                    UnicornCorpse corpse = new UnicornCorpse(SEEntityTypes.UNICORN_CORPSE.get(), level);
                    corpse.setVariant(animal.getVariant());
                    corpse.moveTo(deceased.getX(), deceased.getY(), deceased.getZ(), deceased.getYRot(), deceased.getXRot());
                    level.addFreshEntity(corpse);
                }
            }
        }
    }
}