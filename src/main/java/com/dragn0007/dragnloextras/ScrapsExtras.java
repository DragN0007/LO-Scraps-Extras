package com.dragn0007.dragnloextras;

import com.dragn0007.dragnloextras.common.gui.SEMenuTypes;
import com.dragn0007.dragnloextras.datagen.conditions.TFCCondition;
import com.dragn0007.dragnloextras.effects.SEEffects;
import com.dragn0007.dragnloextras.entity.SEEntityTypes;
import com.dragn0007.dragnloextras.items.SEItemGroupModifier;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.ScrapsExtrasClientConfig;
import com.dragn0007.dragnloextras.util.ScrapsExtrasCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
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
        SEMenuTypes.register(eventBus);
        SEEntityTypes.ENTITY_TYPES.register(eventBus);

        GeckoLib.initialize();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ScrapsExtrasClientConfig.SPEC, "livestock-overhaul-extras-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ScrapsExtrasCommonConfig.SPEC, "livestock-overhaul-extras-common.toml");

        //if TFC is installed, the recipes for this mod switch into TFC-friendly ones
        CraftingHelper.register(new TFCCondition.Serializer(new ResourceLocation(MODID, "tfc_condition")));

        MinecraftForge.EVENT_BUS.register(this);

        System.out.println("[DragN's LO: Scraps and Extras] Registered Livestock Overhaul: Scraps and Extras.");
        System.out.println("[DragN's LO: Scraps and Extras] This mod may work strangely on worlds and O-Animals that already exist!");
        System.out.println("[DragN's LO: Scraps and Extras] Hello Livestock Overhaul and LO: Pets!");
    }
}