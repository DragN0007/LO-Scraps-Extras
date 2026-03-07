package com.dragn0007.dragnloextras;

import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.items.SEItemGroupModifier;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.ScrapsExtrasClientConfig;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;


@Mod(ScrapsExtras.MODID)
public class ScrapsExtras
{
    public static final String MODID = "dragnloextras";

    public ScrapsExtras()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SEItems.register(eventBus);
        SEItemGroupModifier.register(eventBus);
        SEEffects.register(eventBus);

        GeckoLib.initialize();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ScrapsExtrasClientConfig.SPEC, "livestock-overhaul-extras-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ScrapsExtrasCommonConfig.SPEC, "livestock-overhaul-extras-common.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final EntityDataSerializer<ResourceLocation> RESOURCE_LOCATION = new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, ResourceLocation resourceLocation) {
            buf.writeResourceLocation(resourceLocation);
        }

        @Override
        public ResourceLocation read(FriendlyByteBuf buf) {
            return buf.readResourceLocation();
        }

        @Override
        public ResourceLocation copy(ResourceLocation resourceLocation) {
            return resourceLocation;
        }
    };

    static {
        EntityDataSerializers.registerSerializer(RESOURCE_LOCATION);
    }
}