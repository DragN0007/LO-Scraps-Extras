package com.dragn0007.dragnloextras.datagen;

import com.dragn0007.dragnlivestock.util.LOTags;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.SETags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SERecipeMaker extends RecipeProvider implements IConditionBuilder {
    public SERecipeMaker(PackOutput pOutput) {
        super(pOutput);
    }

    //TODO: stethoscope, treatments, halter, turnout blanket, food recipes

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
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
}