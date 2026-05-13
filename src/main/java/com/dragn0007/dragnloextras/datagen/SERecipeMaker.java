package com.dragn0007.dragnloextras.datagen;

import com.dragn0007.dragnlivestock.util.LOTags;
import com.dragn0007.dragnloextras.ScrapsExtras;
import com.dragn0007.dragnloextras.datagen.conditions.TFCCondition;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.SETags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.Food;
import net.dries007.tfc.common.items.HideItemType;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SERecipeMaker extends RecipeProvider implements IConditionBuilder {
    public SERecipeMaker(PackOutput pOutput) {
        super(pOutput);
    }

    public void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        buildCommonRecipes(pFinishedRecipeConsumer);
        buildFoodRecipes(pFinishedRecipeConsumer);
        buildTFCRecipes(pFinishedRecipeConsumer);
    }

    public void buildCommonRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.COLLAR_SPIKES.get())
                .define('A', Items.IRON_NUGGET)
                .pattern(" AA")
                .pattern("AA ")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_NUGGET).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HOOF_PICK.get())
                .define('B', ItemTags.PLANKS)
                .define('A', Items.IRON_NUGGET)
                .pattern("  A")
                .pattern(" A ")
                .pattern("B  ")
                .unlockedBy("has_planks", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.PLANKS).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BRUSH.get())
                .define('A', ItemTags.PLANKS)
                .define('B', Items.STRING)
                .pattern("AA")
                .pattern("BB")
                .unlockedBy("has_string", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.STRING).build()))
                .save(pFinishedRecipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.HEARTY_KIBBLE.get())
                .requires(SETags.Items.GRAIN)
                .requires(LOTags.Items.RAW_MEATS)
                .requires(Items.BONE)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_meat", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(LOTags.Items.RAW_MEATS).build()))
                .save(pFinishedRecipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.HEARTY_GRAIN_FEED.get())
                .requires(SETags.Items.GRAIN)
                .requires(SETags.Items.GRAIN)
                .requires(Items.SUGAR)
                .requires(Items.BEETROOT)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_grain", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(SETags.Items.GRAIN).build()))
                .save(pFinishedRecipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.KIBBLE.get())
                .requires(SETags.Items.GRAIN)
                .requires(LOTags.Items.RAW_MEATS)
                .unlockedBy("has_meat", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(LOTags.Items.RAW_MEATS).build()))
                .save(pFinishedRecipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.GRAIN_FEED.get())
                .requires(SETags.Items.GRAIN)
                .requires(SETags.Items.GRAIN)
                .requires(Items.SUGAR)
                .unlockedBy("has_grain", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(SETags.Items.GRAIN).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.VETERINARY_BANDAGE.get())
                .define('A', ItemTags.WOOL)
                .define('B', Items.STRING)
                .pattern("B ")
                .pattern("AB")
                .unlockedBy("has_string", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.STRING).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HEARTWORM_MEDICINE.get())
                .define('A', Items.ROTTEN_FLESH)
                .define('B', Items.BEEF)
                .define('C', Items.IRON_NUGGET)
                .pattern("BA")
                .pattern("AC")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_NUGGET).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_OINTMENT.get())
                .define('B', Items.IRON_NUGGET)
                .define('C', Items.REDSTONE)
                .pattern("BCB")
                .pattern("BCB")
                .pattern("BBB")
                .unlockedBy("has_redstone", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.REDSTONE).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIPARASITIC_OINTMENT.get())
                .define('B', Items.IRON_NUGGET)
                .define('C', Items.SLIME_BALL)
                .pattern("BCB")
                .pattern("BCB")
                .pattern("BBB")
                .unlockedBy("has_slime", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.SLIME_BALL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_EARDROPS.get())
                .define('A', Items.REDSTONE)
                .define('B', Items.IRON_NUGGET)
                .pattern(" B ")
                .pattern("BAB")
                .pattern("BBB")
                .unlockedBy("has_redstone", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.REDSTONE).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.RABIES_SHOT.get())
                .define('A', Items.GLASS)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.GOLDEN_APPLE)
                .pattern("C  ")
                .pattern(" A ")
                .pattern("  B")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_INGOT).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIPARASITIC_INJECTION.get())
                .define('A', Items.GLASS)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.SLIME_BALL)
                .pattern("C  ")
                .pattern(" A ")
                .pattern("  B")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_INGOT).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_INJECTION.get())
                .define('A', Items.GLASS)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.REDSTONE)
                .pattern("C  ")
                .pattern(" A ")
                .pattern("  B")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_INGOT).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.STETHOSCOPE.get())
                .define('A', Items.IRON_NUGGET)
                .define('B', Items.IRON_INGOT)
                .pattern("A A")
                .pattern(" A ")
                .pattern("  B")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_INGOT).build()))
                .save(pFinishedRecipeConsumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HORSE_MANNEQUIN.get())
                .define('A', Items.LIGHT_GRAY_WOOL)
                .define('B', Items.IRON_INGOT)
                .pattern("  A")
                .pattern("AAA")
                .pattern("B B")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_INGOT).build()))
                .save(pFinishedRecipeConsumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BLACK_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.BLACK_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BLUE_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.BLUE_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BROWN_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.BROWN_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.CYAN_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.CYAN_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.GREEN_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.GREEN_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.GREY_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.GRAY_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.LIGHT_BLUE_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.LIGHT_BLUE_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.LIGHT_GREY_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.LIGHT_GRAY_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.LIME_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.LIME_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.MAGENTA_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.MAGENTA_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ORANGE_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.ORANGE_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.PINK_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.PINK_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.PURPLE_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.PURPLE_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.RED_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.RED_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.WHITE_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.WHITE_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.YELLOW_HALTER.get())
                .define('A', Items.LEATHER)
                .define('B', Items.YELLOW_WOOL)
                .pattern("ABA")
                .pattern("AA ")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BLACK_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.BLACK_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BLUE_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.BLUE_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BROWN_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.BROWN_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.CYAN_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.CYAN_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.GREEN_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.GREEN_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.GREY_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.GRAY_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.LIGHT_BLUE_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.LIGHT_BLUE_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.LIGHT_GREY_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.LIGHT_GRAY_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.LIME_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.LIME_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.MAGENTA_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.MAGENTA_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ORANGE_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.ORANGE_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.PINK_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.PINK_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.PURPLE_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.PURPLE_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.RED_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.RED_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.WHITE_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.WHITE_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.YELLOW_TURNOUT_BLANKET.get())
                .define('A', Items.LEATHER)
                .define('B', Items.YELLOW_WOOL)
                .pattern("  B")
                .pattern("BBB")
                .pattern("A A")
                .unlockedBy("has_wool", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ItemTags.WOOL).build()))
                .save(pFinishedRecipeConsumer);
    }

    public void buildFoodRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.SALMON_FILLET.get(), 2)
                .requires(Items.SALMON)
                .unlockedBy("has_fish", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.SALMON)
                        .build()))
                .save(pFinishedRecipeConsumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.COD_FILLET.get(), 2)
                .requires(Items.COD)
                .unlockedBy("has_fish", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.COD)
                        .build()))
                .save(pFinishedRecipeConsumer);

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_BRISKET.get(), 0.35F, 100)
                .unlockedBy("has_beef_brisket", has(SEItems.BEEF_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_BRISKET.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_BRISKET.get(), 0.35F, 200)
                .unlockedBy("has_beef_brisket", has(SEItems.BEEF_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_BRISKET.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_BRISKET.get(), 0.35F, 600)
                .unlockedBy("has_beef_brisket", has(SEItems.BEEF_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_BRISKET.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_CHUCK.get(), 0.35F, 100)
                .unlockedBy("has_beef_chuck", has(SEItems.BEEF_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_CHUCK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_CHUCK.get(), 0.35F, 200)
                .unlockedBy("has_beef_chuck", has(SEItems.BEEF_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_CHUCK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_CHUCK.get(), 0.35F, 600)
                .unlockedBy("has_beef_chuck", has(SEItems.BEEF_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_CHUCK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_RIB.get(), 0.35F, 100)
                .unlockedBy("has_beef_rib", has(SEItems.BEEF_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_RIB.get(), 0.35F, 200)
                .unlockedBy("has_beef_rib", has(SEItems.BEEF_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_RIB.get(), 0.35F, 600)
                .unlockedBy("has_beef_rib", has(SEItems.BEEF_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_SHANK.get(), 0.35F, 100)
                .unlockedBy("has_beef_shank", has(SEItems.BEEF_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_SHANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_SHANK.get(), 0.35F, 200)
                .unlockedBy("has_beef_shank", has(SEItems.BEEF_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_SHANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_SHANK.get(), 0.35F, 600)
                .unlockedBy("has_beef_shank", has(SEItems.BEEF_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_BEEF_SHANK.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.HORSE_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_BRISKET.get(), 0.35F, 100)
                .unlockedBy("has_horse_brisket", has(SEItems.HORSE_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_BRISKET.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.HORSE_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_BRISKET.get(), 0.35F, 200)
                .unlockedBy("has_horse_brisket", has(SEItems.HORSE_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_BRISKET.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.HORSE_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_BRISKET.get(), 0.35F, 600)
                .unlockedBy("has_horse_brisket", has(SEItems.HORSE_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_BRISKET.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.HORSE_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_CHUCK.get(), 0.35F, 100)
                .unlockedBy("has_horse_chuck", has(SEItems.HORSE_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_CHUCK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.HORSE_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_CHUCK.get(), 0.35F, 200)
                .unlockedBy("has_horse_chuck", has(SEItems.HORSE_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_CHUCK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.HORSE_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_CHUCK.get(), 0.35F, 600)
                .unlockedBy("has_horse_chuck", has(SEItems.HORSE_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_CHUCK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.HORSE_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_RIB.get(), 0.35F, 100)
                .unlockedBy("has_horse_rib", has(SEItems.HORSE_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.HORSE_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_RIB.get(), 0.35F, 200)
                .unlockedBy("has_horse_rib", has(SEItems.HORSE_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.HORSE_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_RIB.get(), 0.35F, 600)
                .unlockedBy("has_horse_rib", has(SEItems.HORSE_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.HORSE_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_SHANK.get(), 0.35F, 100)
                .unlockedBy("has_horse_shank", has(SEItems.HORSE_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_SHANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.HORSE_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_SHANK.get(), 0.35F, 200)
                .unlockedBy("has_horse_shank", has(SEItems.HORSE_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_SHANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.HORSE_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_SHANK.get(), 0.35F, 600)
                .unlockedBy("has_horse_shank", has(SEItems.HORSE_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_HORSE_SHANK.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CAMEL_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_BRISKET.get(), 0.35F, 100)
                .unlockedBy("has_camel_brisket", has(SEItems.CAMEL_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_BRISKET.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CAMEL_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_BRISKET.get(), 0.35F, 200)
                .unlockedBy("has_camel_brisket", has(SEItems.CAMEL_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_BRISKET.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CAMEL_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_BRISKET.get(), 0.35F, 600)
                .unlockedBy("has_camel_brisket", has(SEItems.CAMEL_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_BRISKET.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CAMEL_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_CHUCK.get(), 0.35F, 100)
                .unlockedBy("has_camel_chuck", has(SEItems.CAMEL_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_CHUCK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CAMEL_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_CHUCK.get(), 0.35F, 200)
                .unlockedBy("has_camel_chuck", has(SEItems.CAMEL_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_CHUCK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CAMEL_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_CHUCK.get(), 0.35F, 600)
                .unlockedBy("has_camel_chuck", has(SEItems.CAMEL_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_CHUCK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CAMEL_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_RIB.get(), 0.35F, 100)
                .unlockedBy("has_camel_rib", has(SEItems.CAMEL_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CAMEL_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_RIB.get(), 0.35F, 200)
                .unlockedBy("has_camel_rib", has(SEItems.CAMEL_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CAMEL_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_RIB.get(), 0.35F, 600)
                .unlockedBy("has_camel_rib", has(SEItems.CAMEL_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CAMEL_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_SHANK.get(), 0.35F, 100)
                .unlockedBy("has_camel_shank", has(SEItems.CAMEL_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_SHANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CAMEL_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_SHANK.get(), 0.35F, 200)
                .unlockedBy("has_camel_shank", has(SEItems.CAMEL_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_SHANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CAMEL_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_SHANK.get(), 0.35F, 600)
                .unlockedBy("has_camel_shank", has(SEItems.CAMEL_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CAMEL_SHANK.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.MUTTON_FLANK.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_FLANK.get(), 0.35F, 100)
                .unlockedBy("has_mutton_flank", has(SEItems.MUTTON_FLANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_FLANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.MUTTON_FLANK.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_FLANK.get(), 0.35F, 200)
                .unlockedBy("has_mutton_flank", has(SEItems.MUTTON_FLANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_FLANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.MUTTON_FLANK.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_FLANK.get(), 0.35F, 600)
                .unlockedBy("has_mutton_flank", has(SEItems.MUTTON_FLANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_FLANK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.MUTTON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_RIB.get(), 0.35F, 100)
                .unlockedBy("has_mutton_rib", has(SEItems.MUTTON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.MUTTON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_RIB.get(), 0.35F, 200)
                .unlockedBy("has_mutton_rib", has(SEItems.MUTTON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.MUTTON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_RIB.get(), 0.35F, 600)
                .unlockedBy("has_mutton_rib", has(SEItems.MUTTON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.MUTTON_LEG.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_LEG.get(), 0.35F, 100)
                .unlockedBy("has_mutton_leg", has(SEItems.MUTTON_LEG.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_LEG.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.MUTTON_LEG.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_LEG.get(), 0.35F, 200)
                .unlockedBy("has_mutton_leg", has(SEItems.MUTTON_LEG.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_LEG.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.MUTTON_LEG.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_LEG.get(), 0.35F, 600)
                .unlockedBy("has_mutton_leg", has(SEItems.MUTTON_LEG.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_MUTTON_LEG.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.PORK_HAM.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_HAM.get(), 0.35F, 100)
                .unlockedBy("has_pork_ham", has(SEItems.PORK_HAM.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_HAM.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.PORK_HAM.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_HAM.get(), 0.35F, 200)
                .unlockedBy("has_pork_ham", has(SEItems.PORK_HAM.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_HAM.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.PORK_HAM.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_HAM.get(), 0.35F, 600)
                .unlockedBy("has_pork_ham", has(SEItems.PORK_HAM.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_HAM.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.PORK_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_LOIN.get(), 0.35F, 100)
                .unlockedBy("has_pork_loin", has(SEItems.PORK_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_LOIN.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.PORK_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_LOIN.get(), 0.35F, 200)
                .unlockedBy("has_pork_loin", has(SEItems.PORK_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_LOIN.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.PORK_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_LOIN.get(), 0.35F, 600)
                .unlockedBy("has_pork_loin", has(SEItems.PORK_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_LOIN.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.PORK_BELLY.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_BELLY.get(), 0.35F, 100)
                .unlockedBy("has_pork_belly", has(SEItems.PORK_BELLY.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_BELLY.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.PORK_BELLY.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_BELLY.get(), 0.35F, 200)
                .unlockedBy("has_pork_belly", has(SEItems.PORK_BELLY.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_BELLY.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.PORK_BELLY.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_BELLY.get(), 0.35F, 600)
                .unlockedBy("has_pork_belly", has(SEItems.PORK_BELLY.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_PORK_BELLY.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CARIBOU_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_BRISKET.get(), 0.35F, 100)
                .unlockedBy("has_caribou_brisket", has(SEItems.CARIBOU_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_BRISKET.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CARIBOU_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_BRISKET.get(), 0.35F, 200)
                .unlockedBy("has_caribou_brisket", has(SEItems.CARIBOU_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_BRISKET.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CARIBOU_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_BRISKET.get(), 0.35F, 600)
                .unlockedBy("has_caribou_brisket", has(SEItems.CARIBOU_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_BRISKET.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CARIBOU_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_CHUCK.get(), 0.35F, 100)
                .unlockedBy("has_caribou_chuck", has(SEItems.CARIBOU_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_CHUCK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CARIBOU_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_CHUCK.get(), 0.35F, 200)
                .unlockedBy("has_caribou_chuck", has(SEItems.CARIBOU_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_CHUCK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CARIBOU_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_CHUCK.get(), 0.35F, 600)
                .unlockedBy("has_caribou_chuck", has(SEItems.CARIBOU_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_CHUCK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CARIBOU_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_RIB.get(), 0.35F, 100)
                .unlockedBy("has_caribou_rib", has(SEItems.CARIBOU_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CARIBOU_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_RIB.get(), 0.35F, 200)
                .unlockedBy("has_caribou_rib", has(SEItems.CARIBOU_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CARIBOU_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_RIB.get(), 0.35F, 600)
                .unlockedBy("has_caribou_rib", has(SEItems.CARIBOU_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CARIBOU_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_SHANK.get(), 0.35F, 100)
                .unlockedBy("has_caribou_shank", has(SEItems.CARIBOU_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_SHANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CARIBOU_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_SHANK.get(), 0.35F, 200)
                .unlockedBy("has_caribou_shank", has(SEItems.CARIBOU_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_SHANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CARIBOU_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_SHANK.get(), 0.35F, 600)
                .unlockedBy("has_caribou_shank", has(SEItems.CARIBOU_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_CARIBOU_SHANK.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.UNICORN_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_BRISKET.get(), 0.35F, 100)
                .unlockedBy("has_unicorn_brisket", has(SEItems.UNICORN_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_BRISKET.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.UNICORN_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_BRISKET.get(), 0.35F, 200)
                .unlockedBy("has_unicorn_brisket", has(SEItems.UNICORN_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_BRISKET.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.UNICORN_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_BRISKET.get(), 0.35F, 600)
                .unlockedBy("has_unicorn_brisket", has(SEItems.UNICORN_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_BRISKET.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.UNICORN_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_CHUCK.get(), 0.35F, 100)
                .unlockedBy("has_unicorn_chuck", has(SEItems.UNICORN_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_CHUCK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.UNICORN_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_CHUCK.get(), 0.35F, 200)
                .unlockedBy("has_unicorn_chuck", has(SEItems.UNICORN_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_CHUCK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.UNICORN_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_CHUCK.get(), 0.35F, 600)
                .unlockedBy("has_unicorn_chuck", has(SEItems.UNICORN_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_CHUCK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.UNICORN_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_RIB.get(), 0.35F, 100)
                .unlockedBy("has_unicorn_rib", has(SEItems.UNICORN_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.UNICORN_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_RIB.get(), 0.35F, 200)
                .unlockedBy("has_unicorn_rib", has(SEItems.UNICORN_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.UNICORN_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_RIB.get(), 0.35F, 600)
                .unlockedBy("has_unicorn_rib", has(SEItems.UNICORN_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.UNICORN_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_SHANK.get(), 0.35F, 100)
                .unlockedBy("has_unicorn_shank", has(SEItems.UNICORN_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_SHANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.UNICORN_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_SHANK.get(), 0.35F, 200)
                .unlockedBy("has_unicorn_shank", has(SEItems.UNICORN_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_SHANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.UNICORN_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_SHANK.get(), 0.35F, 600)
                .unlockedBy("has_unicorn_shank", has(SEItems.UNICORN_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COOKED_UNICORN_SHANK.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CHICKEN_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_THIGH.get(), 0.35F, 100)
                .unlockedBy("has_chicken_thigh", has(SEItems.CHICKEN_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_chicken_thigh_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CHICKEN_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_THIGH.get(), 0.35F, 200)
                .unlockedBy("has_chicken_thigh", has(SEItems.CHICKEN_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_chicken_thigh_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CHICKEN_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_THIGH.get(), 0.35F, 600)
                .unlockedBy("has_chicken_thigh", has(SEItems.CHICKEN_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_chicken_thigh_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CHICKEN_WING.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_WING.get(), 0.35F, 100)
                .unlockedBy("has_chicken_wing", has(SEItems.CHICKEN_WING.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_chicken_wing_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CHICKEN_WING.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_WING.get(), 0.35F, 200)
                .unlockedBy("has_chicken_wing", has(SEItems.CHICKEN_WING.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_chicken_wing_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CHICKEN_WING.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_WING.get(), 0.35F, 600)
                .unlockedBy("has_chicken_wing", has(SEItems.CHICKEN_WING.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_chicken_wing_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.SALMON_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_SALMON_FILLET.get(), 0.35F, 100)
                .unlockedBy("has_salmon_fillet", has(SEItems.SALMON_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_salmon_fillet_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.SALMON_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_SALMON_FILLET.get(), 0.35F, 200)
                .unlockedBy("has_salmon_fillet", has(SEItems.SALMON_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_salmon_fillet_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.SALMON_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_SALMON_FILLET.get(), 0.35F, 600)
                .unlockedBy("has_salmon_fillet", has(SEItems.SALMON_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_salmon_fillet_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.COD_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_COD_FILLET.get(), 0.35F, 100)
                .unlockedBy("has_cod_fillet", has(SEItems.COD_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_cod_fillet_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.COD_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_COD_FILLET.get(), 0.35F, 200)
                .unlockedBy("has_cod_fillet", has(SEItems.COD_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_cod_fillet_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.COD_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_COD_FILLET.get(), 0.35F, 600)
                .unlockedBy("has_cod_fillet", has(SEItems.COD_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, "cooked_cod_fillet_campfire_cooking"));
    }

    public void buildTFCRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.COLLAR_SPIKES.get())
                .define('A', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                .pattern("AA")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COLLAR_SPIKES.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HOOF_PICK.get())
                .define('B', TFCTags.Items.FIREPIT_LOGS)
                .define('A', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                .pattern("  A")
                .pattern(" B ")
                .pattern("B  ")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HOOF_PICK.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BRUSH.get())
                .define('A', TFCTags.Items.FIREPIT_LOGS)
                .define('B', TFCItems.WOOL_YARN.get())
                .pattern("AA")
                .pattern("BB")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.BRUSH.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.HEARTY_KIBBLE.get())
                .requires(TFCTags.Items.YAK_FOOD)
                .requires(TFCTags.Items.DOG_FOOD)
                .requires(Items.BONE)
                .requires(TFCTags.Items.COMPOST_GREENS_HIGH)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HEARTY_KIBBLE.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.HEARTY_GRAIN_FEED.get())
                .requires(TFCTags.Items.YAK_FOOD)
                .requires(TFCTags.Items.YAK_FOOD)
                .requires(Items.SUGAR)
                .requires(TFCTags.Items.COMPOST_GREENS_HIGH)
                .requires(TFCTags.Items.COMPOST_GREENS_HIGH)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HEARTY_GRAIN_FEED.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.KIBBLE.get())
                .requires(TFCTags.Items.YAK_FOOD)
                .requires(TFCTags.Items.DOG_FOOD)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.KIBBLE.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.GRAIN_FEED.get())
                .requires(TFCTags.Items.YAK_FOOD)
                .requires(TFCTags.Items.YAK_FOOD)
                .requires(Items.SUGAR)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.GRAIN_FEED.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.VETERINARY_BANDAGE.get())
                .define('A', ItemTags.WOOL)
                .define('B', TFCItems.WOOL_YARN.get())
                .pattern("B ")
                .pattern("AB")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.VETERINARY_BANDAGE.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HEARTWORM_MEDICINE.get())
                .define('A', Items.ROTTEN_FLESH)
                .define('B', TFCItems.FOOD.get(Food.BEEF).get())
                .define('C', TFCItems.POWDERS.get(Powder.SALT).get())
                .pattern("BA")
                .pattern("AC")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HEARTWORM_MEDICINE.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_OINTMENT.get())
                .define('C', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                .define('B', Items.CLAY)
                .pattern(" B ")
                .pattern("BCB")
                .pattern("BBB")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIBIOTIC_OINTMENT.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_EARDROPS.get())
                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
                .define('B', Items.CLAY)
                .pattern(" B ")
                .pattern("BCB")
                .pattern("BCB")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIBIOTIC_EARDROPS.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIPARASITIC_OINTMENT.get())
                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
                .define('B', Items.CLAY)
                .pattern(" B ")
                .pattern("BCB")
                .pattern("BBB")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIPARASITIC_OINTMENT.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.RABIES_SHOT.get())
                .define('A', Items.GLASS)
                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
                .pattern("C  ")
                .pattern(" A ")
                .pattern("  B")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.RABIES_SHOT.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIPARASITIC_INJECTION.get())
                .define('A', Items.GLASS)
                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                .define('C', Items.CLAY)
                .pattern("C  ")
                .pattern(" A ")
                .pattern("  B")
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIPARASITIC_INJECTION.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_INJECTION.get())
                                .define('A', Items.GLASS)
                                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
                                .pattern("C  ")
                                .pattern(" A ")
                                .pattern("  B")
                                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIBIOTIC_INJECTION.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.STETHOSCOPE.get())
                                .define('A', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                                .define('B', TFCItems.JUTE_FIBER.get())
                                .pattern("B B")
                                .pattern(" B ")
                                .pattern("  A")
                                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.STETHOSCOPE.get() + "_tfc"));


        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HORSE_MANNEQUIN.get())
                                .define('A', Items.LIGHT_GRAY_WOOL)
                                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
                                .pattern("  A")
                                .pattern("AAA")
                                .pattern("B B")
                                .unlockedBy("has_wool", has(ItemTags.WOOL))
                                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HORSE_MANNEQUIN.get() + "_tfc"));

        ConditionalRecipe.builder()
                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
                .addRecipe(
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.RAWHIDE_SADDLE.get())
                                .define('A', TFCItems.HIDES.get(HideItemType.RAW).get(HideItemType.Size.LARGE).get())
                                .define('B', TFCItems.JUTE_FIBER.get())
                                .pattern("BAB")
                                .pattern("B B")
                                .unlockedBy("has_jute_fiber", has(ItemTags.WOOL))
                                ::save).build
                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.RAWHIDE_SADDLE.get() + "_tfc"));
    }

}