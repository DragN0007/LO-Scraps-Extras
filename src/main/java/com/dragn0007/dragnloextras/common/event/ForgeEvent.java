package com.dragn0007.dragnloextras.common.event;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.TraitDuck;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
    @SubscribeEvent
    public static void onTryFeedEntity(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof LivingEntity entity) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();

            //TODO: feeding system

            if (stack.is(SEItems.GRAIN_FEED.get())) {
                if (entity instanceof OHorse) {

                }
            }
        }
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

        if (event.getTarget() instanceof LivingEntity entity) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();

            if (stack.is(SEItems.STETHOSCOPE.get())) {
                if (entity instanceof OHorse animal) {
                    String traitText = getTraits(((TraitDuck)animal).livestockOverhaulScraps$getTrait());
                    String illnessText = "";
                    if (animal.hasEffect(SEEffects.DIRTY.get()))
                        illnessText = "This animal is Dirty and needs to be cleaned.\n";
                    if (animal.hasEffect(SEEffects.INFECTION.get()))
                        illnessText = "This animal has an Infection.\n";
                    if (animal.hasEffect(SEEffects.RABIES.get()))
                        illnessText = "This animal has Rabies.\n";
                    if (animal.hasEffect(SEEffects.HOOF_ABSCESS.get()))
                        illnessText = "This animal has a Hoof Abscess.\n";
                    player.sendSystemMessage(Component.translatable(traitText + "\n" + illnessText).withStyle(ChatFormatting.WHITE));
//                    player.displayClientMessage(Component.translatable(traitText + " | " + illnessText).withStyle(ChatFormatting.GOLD), true);
                }
            }
        }
    }

    private static String getTraits(int traits) {
        switch (traits) {
            case 0: return "Trait: Brave \nBrave animals regenerate health faster, even - especially - in the face of danger.";
            case 1: return "Trait: Immunocompetent \nImmunocompetent animals are far less likely to get sick from common illnesses, and are more likely to recover from them.";
            case 2: return "Trait: Swift \nSwift animals are faster on their feet.";
            case 3: return "Trait: Vaulter \nVaulters are better at jumping and leaping, vaulting over even the tallest of obstacles.";
            case 4: return "Trait: Climber \nClimbers are great at climbing tough terrain; perfect for mountainous regions.";
            case 5: return "Trait: Buster \nBusters deal more damage, making them great battle-buddies.";
            case 6: return "Trait: Sturdy \nSturdy animals have more overall health and are slightly less likely to get sick from common illnesses.";
            case 7: return "Trait: Cowardly \nCowardly animals are more likely to run away when low on health, and don't regenerate health at all when threatened.";
            case 8: return "Trait: Immunocompromised \nImmunocompromised animals are far more likely to get sick from common illnesses, and are less likely to recover from them.";
            case 9: return "Trait: Stubborn \nStubborn animals may stop randomly, stop following you, or refuse to perform a command.";
            case 10: return "Trait: Laggard \nLaggards are slow on their feet.";
            case 11: return "Trait: Frail \nFrail animals have less overall health and are slightly more likely to get sick from common illnesses.";
            case 12: return "Trait: Mean \nMean animals might attack you or other animals out of anger on occasion.";
            default: return "Trait: Unknown";
        }
    }
}