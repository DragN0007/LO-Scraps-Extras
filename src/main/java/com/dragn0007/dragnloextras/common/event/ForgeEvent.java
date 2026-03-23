package com.dragn0007.dragnloextras.common.event;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnlivestock.util.LivestockOverhaulCommonConfig;
import com.dragn0007.dragnloextras.capabilities.*;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.network.*;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {

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

        }
    }

    @SubscribeEvent
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        //horses
        if (event.getObject() instanceof OHorse) {
            if (!event.getObject().getCapability(SECapabilities.DIRTY_CAPABILITY).isPresent()) {
                DirtyCapabilityAttacher.attach(event);
                if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                    System.out.println("Attached the Dirty Capability NBT to " + event.getObject().getName());
                }
            }
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
            if (!event.getObject().getCapability(SECapabilities.TRAIT_CAPABILITY).isPresent()) {
                TraitCapabilityAttacher.attach(event);
                if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                    System.out.println("Attached the Trait Capability NBT to " + event.getObject().getName());
                }
            }
            if (!event.getObject().getCapability(SECapabilities.IMMUNITY_CAPABILITY).isPresent()) {
                ImmunityCapabilityAttacher.attach(event);
                if (LivestockOverhaulCommonConfig.DEBUG_LOGS.get()) {
                    System.out.println("Attached the Immunity Capability NBT to " + event.getObject().getName());
                }
            }
        }

        //camels
        //caribou
        //donkeys
        //mules
        //dogs
        //wolves
        //cats
        //ocelots
    }

    @SubscribeEvent
    public static void onTryCureEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity entity) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();

            //TODO: all ailment cures

            if (stack.is(SEItems.ANTIBIOTIC_INJECTION.get())) {
                if (entity.hasEffect(SEEffects.INFECTION.get())) {
                    int duration = entity.getEffect(SEEffects.INFECTION.get()).getDuration();
                    entity.removeEffect(SEEffects.INFECTION.get());

                    if (entity.hasEffect(MobEffects.WEAKNESS) && entity.getEffect(MobEffects.WEAKNESS).getDuration() == duration) {
                        entity.removeEffect(MobEffects.WEAKNESS);
                    }
                    if (entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && entity.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getDuration() == duration) {
                        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    }

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    player.displayClientMessage(Component.translatable("tooltip.dragnloextras.cured.tooltip").withStyle(ChatFormatting.GOLD), true);
                } else {
                    player.displayClientMessage(Component.translatable("tooltip.dragnloextras.no_ailment.tooltip").withStyle(ChatFormatting.YELLOW), true);
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
                    player.sendSystemMessage(Component.translatable("The Scraps & Extras traits system is disabled on this server.").withStyle(ChatFormatting.RED));
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
                        player.sendSystemMessage(Component.translatable("Immunity: " + immunityText).withStyle(ChatFormatting.YELLOW));
                    } else {
                        player.sendSystemMessage(Component.translatable("The Scraps & Extras ailment system is disabled on this server.").withStyle(ChatFormatting.RED));
                    }
                }

                //illness (if applicable)
                if (ScrapsExtrasCommonConfig.AILMENT_SYSTEM.get()) {
                    String illnessText = "";
                    if (animal.hasEffect(SEEffects.HUNGER.get()))
                        illnessText = "This animal is Hungry.\n";
                    if (animal.hasEffect(SEEffects.DIRTY.get()))
                        illnessText = "This animal is Dirty.\n";
                    if (animal.hasEffect(SEEffects.INFECTION.get()))
                        illnessText = "This animal has an Infection.\n";
                    if (animal.hasEffect(SEEffects.RABIES.get()))
                        illnessText = "This animal has Rabies.\n";
                    if (animal.hasEffect(SEEffects.HOOF_ABSCESS.get()))
                        illnessText = "This animal has a Hoof Abscess.\n";
                    if (animal.hasEffect(SEEffects.ABRASION.get()))
                        illnessText = "This animal has an Abrasion.\n";
                    if (animal.hasEffect(SEEffects.SADDLE_SORE.get()))
                        illnessText = "This animal has a Saddle Sore.\n";
                    if (animal.hasEffect(SEEffects.EAR_INFECTION.get()))
                        illnessText = "This animal has an Ear Infection.\n";
                    if (animal.hasEffect(SEEffects.BOTFLY_INFESTATION.get()))
                        illnessText = "This animal has a Botfly Infestation.\n";
                    if (animal.hasEffect(SEEffects.FLEA_INFESTATION.get()))
                        illnessText = "This animal has a Flea Infestation.\n";
                    if (animal.hasEffect(SEEffects.HEARTWORMS.get()))
                        illnessText = "This animal has Heartworms.\n";
                    if (animal.hasEffect(SEEffects.RINGWORM.get()))
                        illnessText = "This animal has Ringworm.\n";
                    if (animal.hasEffect(SEEffects.MANGE.get()))
                        illnessText = "This animal has Mange.\n";
                    if (animal.hasEffect(SEEffects.RAIN_ROT.get()))
                        illnessText = "This animal has Rain Rot.\n";
                    player.sendSystemMessage(Component.translatable(illnessText).withStyle(ChatFormatting.RED));
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
            case 8 -> "Trait: Immunocompromised \nImmunocompromised animals are far more likely to get sick from common illnesses, and are less likely to recover from them.\n";
            case 9 -> "Trait: Stubborn \nStubborn animals may stop randomly, stop following you, or refuse to perform a command.\n";
            case 10 -> "Trait: Laggard \nLaggards are slow on their feet.\n";
            case 11 -> "Trait: Frail \nFrail animals have less overall health and are slightly more likely to get sick from common illnesses.\n";
            case 12 -> "Trait: Mean \nMean animals might attack you or other animals out of anger on occasion.\n";
            default -> "Trait: Unknown\n";
        };
    }
}