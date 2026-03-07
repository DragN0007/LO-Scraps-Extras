package com.dragn0007.dragnloextras.common.event;

import com.dragn0007.dragnlivestock.entities.horse.OHorse;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.items.SEItems;
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
}