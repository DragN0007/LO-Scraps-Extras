package com.dragn0007.dragnloextras.datagen;

import com.dragn0007.dragnlivestock.util.LOTags;
import com.dragn0007.dragnloextras.items.SEItems;
import com.dragn0007.dragnloextras.util.SETags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SERecipeMaker extends RecipeProvider implements IConditionBuilder {
    public SERecipeMaker(PackOutput pOutput) {
        super(pOutput);
    }

    public void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        buildCommonRecipes(pFinishedRecipeConsumer);
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
                .unlockedBy("has_beef_brisket", has(SEItems.BEEF_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_BRISKET.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_BRISKET.get(), 0.35F, 200)
                .unlockedBy("has_beef_brisket", has(SEItems.BEEF_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_BRISKET.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_BRISKET.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_BRISKET.get(), 0.35F, 600)
                .unlockedBy("has_beef_brisket", has(SEItems.BEEF_BRISKET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_BRISKET.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_CHUCK.get(), 0.35F, 100)
                .unlockedBy("has_beef_chuck", has(SEItems.BEEF_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_CHUCK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_CHUCK.get(), 0.35F, 200)
                .unlockedBy("has_beef_chuck", has(SEItems.BEEF_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_CHUCK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_CHUCK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_CHUCK.get(), 0.35F, 600)
                .unlockedBy("has_beef_chuck", has(SEItems.BEEF_CHUCK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_CHUCK.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_RIB.get(), 0.35F, 100)
                .unlockedBy("has_beef_rib", has(SEItems.BEEF_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_RIB.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_RIB.get(), 0.35F, 200)
                .unlockedBy("has_beef_rib", has(SEItems.BEEF_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_RIB.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_RIB.get(), 0.35F, 600)
                .unlockedBy("has_beef_rib", has(SEItems.BEEF_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_RIB.get() + "_campfire_cooking"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.BEEF_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_SHANK.get(), 0.35F, 100)
                .unlockedBy("has_beef_shank", has(SEItems.BEEF_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_SHANK.get() + "_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.BEEF_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_SHANK.get(), 0.35F, 200)
                .unlockedBy("has_beef_shank", has(SEItems.BEEF_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_SHANK.get() + "_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.BEEF_SHANK.get()), RecipeCategory.MISC, SEItems.COOKED_BEEF_SHANK.get(), 0.35F, 600)
                .unlockedBy("has_beef_shank", has(SEItems.BEEF_SHANK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", SEItems.COOKED_BEEF_SHANK.get() + "_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CHICKEN_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_THIGH.get(), 0.35F, 100)
                .unlockedBy("has_chicken_thigh", has(SEItems.CHICKEN_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chicken_thigh_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CHICKEN_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_THIGH.get(), 0.35F, 200)
                .unlockedBy("has_chicken_thigh", has(SEItems.CHICKEN_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chicken_thigh_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CHICKEN_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_CHICKEN_THIGH.get(), 0.35F, 600)
                .unlockedBy("has_chicken_thigh", has(SEItems.CHICKEN_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chicken_thigh_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.HORSE_RIB_STEAK.get()),RecipeCategory.MISC,  SEItems.COOKED_HORSE_RIB_STEAK.get(), 0.35F, 100)
                .unlockedBy("has_horse_rib_steak", has(SEItems.HORSE_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_horse_rib_steak_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.HORSE_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_RIB_STEAK.get(), 0.35F, 200)
                .unlockedBy("has_horse_rib_steak", has(SEItems.HORSE_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_horse_rib_steak_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.HORSE_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_RIB_STEAK.get(), 0.35F, 600)
                .unlockedBy("has_horse_rib_steak", has(SEItems.HORSE_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_horse_rib_steak_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.HORSE_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_SIRLOIN_STEAK.get(), 0.35F, 100)
                .unlockedBy("has_horse_sirloin_steak", has(SEItems.HORSE_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_horse_sirloin_steak_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.HORSE_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_SIRLOIN_STEAK.get(), 0.35F, 200)
                .unlockedBy("has_horse_sirloin_steak", has(SEItems.HORSE_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_horse_sirloin_steak_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.HORSE_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_HORSE_SIRLOIN_STEAK.get(), 0.35F, 600)
                .unlockedBy("has_horse_sirloin_steak", has(SEItems.HORSE_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_horse_sirloin_steak_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.LLAMA_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_LLAMA_RIB.get(), 0.35F, 100)
                .unlockedBy("has_llama_rib", has(SEItems.LLAMA_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_llama_rib_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.LLAMA_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_LLAMA_RIB.get(), 0.35F, 200)
                .unlockedBy("has_llama_rib", has(SEItems.LLAMA_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_llama_rib_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.LLAMA_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_LLAMA_RIB.get(), 0.35F, 600)
                .unlockedBy("has_llama_rib", has(SEItems.LLAMA_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_llama_rib_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.LLAMA_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_LLAMA_LOIN.get(), 0.35F, 100)
                .unlockedBy("has_llama_loin", has(SEItems.LLAMA_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_llama_loin_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.LLAMA_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_LLAMA_LOIN.get(), 0.35F, 200)
                .unlockedBy("has_llama_loin", has(SEItems.LLAMA_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_llama_loin_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.LLAMA_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_LLAMA_LOIN.get(), 0.35F, 600)
                .unlockedBy("has_llama_loin", has(SEItems.LLAMA_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_llama_loin_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.UNICORN_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_RIB_STEAK.get(), 0.35F, 100)
                .unlockedBy("has_unicorn_rib_steak", has(SEItems.UNICORN_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_unicorn_rib_steak_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.UNICORN_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_RIB_STEAK.get(), 0.35F, 200)
                .unlockedBy("has_unicorn_rib_steak", has(SEItems.UNICORN_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_unicorn_rib_steak_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.UNICORN_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_RIB_STEAK.get(), 0.35F, 600)
                .unlockedBy("has_unicorn_rib_steak", has(SEItems.UNICORN_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_unicorn_rib_steak_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.UNICORN_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_SIRLOIN_STEAK.get(), 0.35F, 100)
                .unlockedBy("has_unicorn_sirloin_steak", has(SEItems.UNICORN_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_unicorn_sirloin_steak_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.UNICORN_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_SIRLOIN_STEAK.get(), 0.35F, 200)
                .unlockedBy("has_unicorn_sirloin_steak", has(SEItems.UNICORN_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_unicorn_sirloin_steak_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.UNICORN_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_UNICORN_SIRLOIN_STEAK.get(), 0.35F, 600)
                .unlockedBy("has_unicorn_sirloin_steak", has(SEItems.UNICORN_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_unicorn_sirloin_steak_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.MUTTON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_RIB.get(), 0.35F, 100)
                .unlockedBy("has_mutton_rib", has(SEItems.MUTTON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_mutton_rib_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.MUTTON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_RIB.get(), 0.35F, 200)
                .unlockedBy("has_mutton_rib", has(SEItems.MUTTON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_mutton_rib_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.MUTTON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_RIB.get(), 0.35F, 600)
                .unlockedBy("has_mutton_rib", has(SEItems.MUTTON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_mutton_rib_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.MUTTON_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_LOIN.get(), 0.35F, 100)
                .unlockedBy("has_mutton_loin", has(SEItems.MUTTON_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_mutton_loin_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.MUTTON_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_LOIN.get(), 0.35F, 200)
                .unlockedBy("has_mutton_loin", has(SEItems.MUTTON_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_mutton_loin_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.MUTTON_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_MUTTON_LOIN.get(), 0.35F, 600)
                .unlockedBy("has_mutton_loin", has(SEItems.MUTTON_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_mutton_loin_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CAMEL_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_RIB.get(), 0.35F, 100)
                .unlockedBy("has_camel_rib", has(SEItems.CAMEL_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_camel_rib_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CAMEL_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_RIB.get(), 0.35F, 200)
                .unlockedBy("has_camel_rib", has(SEItems.CAMEL_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_camel_rib_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CAMEL_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_RIB.get(), 0.35F, 600)
                .unlockedBy("has_camel_rib", has(SEItems.CAMEL_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_camel_rib_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CAMEL_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_LOIN.get(), 0.35F, 100)
                .unlockedBy("has_camel_loin", has(SEItems.CAMEL_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_camel_loin_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CAMEL_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_LOIN.get(), 0.35F, 200)
                .unlockedBy("has_camel_loin", has(SEItems.CAMEL_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_camel_loin_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CAMEL_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_CAMEL_LOIN.get(), 0.35F, 600)
                .unlockedBy("has_camel_loin", has(SEItems.CAMEL_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_camel_loin_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CHEVON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CHEVON_RIB.get(), 0.35F, 100)
                .unlockedBy("has_chevon_rib", has(SEItems.CHEVON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chevon_rib_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CHEVON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CHEVON_RIB.get(), 0.35F, 200)
                .unlockedBy("has_chevon_rib", has(SEItems.CHEVON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chevon_rib_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CHEVON_RIB.get()), RecipeCategory.MISC, SEItems.COOKED_CHEVON_RIB.get(), 0.35F, 600)
                .unlockedBy("has_chevon_rib", has(SEItems.CHEVON_RIB.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chevon_rib_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CHEVON_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_CHEVON_LOIN.get(), 0.35F, 100)
                .unlockedBy("has_chevon_loin", has(SEItems.CHEVON_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chevon_loin_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CHEVON_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_CHEVON_LOIN.get(), 0.35F, 200)
                .unlockedBy("has_chevon_loin", has(SEItems.CHEVON_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chevon_loin_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CHEVON_LOIN.get()), RecipeCategory.MISC, SEItems.COOKED_CHEVON_LOIN.get(), 0.35F, 600)
                .unlockedBy("has_chevon_loin", has(SEItems.CHEVON_LOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_chevon_loin_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.RABBIT_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_RABBIT_THIGH.get(), 0.35F, 100)
                .unlockedBy("has_rabbit_thigh", has(SEItems.RABBIT_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_rabbit_thigh_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.RABBIT_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_RABBIT_THIGH.get(), 0.35F, 200)
                .unlockedBy("has_rabbit_thigh", has(SEItems.RABBIT_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_rabbit_thigh_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.RABBIT_THIGH.get()), RecipeCategory.MISC, SEItems.COOKED_RABBIT_THIGH.get(), 0.35F, 600)
                .unlockedBy("has_rabbit_thigh", has(SEItems.RABBIT_THIGH.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_rabbit_thigh_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.PORK_RIB_CHOP.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_RIB_CHOP.get(), 0.35F, 100)
                .unlockedBy("has_pork_rib_chop", has(SEItems.PORK_RIB_CHOP.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_pork_rib_chop_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.PORK_RIB_CHOP.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_RIB_CHOP.get(), 0.35F, 200)
                .unlockedBy("has_pork_rib_chop", has(SEItems.PORK_RIB_CHOP.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_pork_rib_chop_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.PORK_RIB_CHOP.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_RIB_CHOP.get(), 0.35F, 600)
                .unlockedBy("has_pork_rib_chop", has(SEItems.PORK_RIB_CHOP.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_pork_rib_chop_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.PORK_TENDERLOIN.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_TENDERLOIN.get(), 0.35F, 100)
                .unlockedBy("has_pork_tenderloin", has(SEItems.PORK_TENDERLOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_pork_tenderloin_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.PORK_TENDERLOIN.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_TENDERLOIN.get(), 0.35F, 200)
                .unlockedBy("has_pork_tenderloin", has(SEItems.PORK_TENDERLOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_pork_tenderloin_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.PORK_TENDERLOIN.get()), RecipeCategory.MISC, SEItems.COOKED_PORK_TENDERLOIN.get(), 0.35F, 600)
                .unlockedBy("has_pork_tenderloin", has(SEItems.PORK_TENDERLOIN.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_pork_tenderloin_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CARIBOU_RIB_STEAK.get()),RecipeCategory.MISC,  SEItems.COOKED_CARIBOU_RIB_STEAK.get(), 0.35F, 100)
                .unlockedBy("has_caribou_rib_steak", has(SEItems.CARIBOU_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_caribou_rib_steak_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CARIBOU_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_RIB_STEAK.get(), 0.35F, 200)
                .unlockedBy("has_caribou_rib_steak", has(SEItems.CARIBOU_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_caribou_rib_steak_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CARIBOU_RIB_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_RIB_STEAK.get(), 0.35F, 600)
                .unlockedBy("has_caribou_rib_steak", has(SEItems.CARIBOU_RIB_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_caribou_rib_steak_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.CARIBOU_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_SIRLOIN_STEAK.get(), 0.35F, 100)
                .unlockedBy("has_caribou_sirloin_steak", has(SEItems.CARIBOU_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_caribou_sirloin_steak_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.CARIBOU_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_SIRLOIN_STEAK.get(), 0.35F, 200)
                .unlockedBy("has_caribou_sirloin_steak", has(SEItems.CARIBOU_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_caribou_sirloin_steak_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.CARIBOU_SIRLOIN_STEAK.get()), RecipeCategory.MISC, SEItems.COOKED_CARIBOU_SIRLOIN_STEAK.get(), 0.35F, 600)
                .unlockedBy("has_caribou_sirloin_steak", has(SEItems.CARIBOU_SIRLOIN_STEAK.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_caribou_sirloin_steak_campfire_cooking"));
        
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.SALMON_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_SALMON_FILLET.get(), 0.35F, 100)
                .unlockedBy("has_salmon_fillet", has(SEItems.SALMON_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_salmon_fillet_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.SALMON_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_SALMON_FILLET.get(), 0.35F, 200)
                .unlockedBy("has_salmon_fillet", has(SEItems.SALMON_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_salmon_fillet_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.SALMON_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_SALMON_FILLET.get(), 0.35F, 600)
                .unlockedBy("has_salmon_fillet", has(SEItems.SALMON_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_salmon_fillet_campfire_cooking"));

        SimpleCookingRecipeBuilder.smoking(Ingredient.of(SEItems.COD_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_COD_FILLET.get(), 0.35F, 100)
                .unlockedBy("has_cod_fillet", has(SEItems.COD_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_cod_fillet_smoking"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(SEItems.COD_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_COD_FILLET.get(), 0.35F, 200)
                .unlockedBy("has_cod_fillet", has(SEItems.COD_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_cod_fillet_smelting"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(SEItems.COD_FILLET.get()), RecipeCategory.MISC, SEItems.COOKED_COD_FILLET.get(), 0.35F, 600)
                .unlockedBy("has_cod_fillet", has(SEItems.COD_FILLET.get())).save(pFinishedRecipeConsumer, new ResourceLocation("dragnlivestock", "cooked_cod_fillet_campfire_cooking"));
    }

    public void buildTFCRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.COLLAR_SPIKES.get())
//                .define('A', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                .pattern("AA")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.COLLAR_SPIKES.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HOOF_PICK.get())
//                .define('B', TFCTags.Items.FIREPIT_LOGS)
//                .define('A', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                .pattern("  A")
//                .pattern(" B ")
//                .pattern("B  ")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HOOF_PICK.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.BRUSH.get())
//                .define('A', TFCTags.Items.FIREPIT_LOGS)
//                .define('B', TFCItems.WOOL_YARN.get())
//                .pattern("AA")
//                .pattern("BB")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.BRUSH.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.HEARTY_KIBBLE.get())
//                .requires(TFCTags.Items.YAK_FOOD)
//                .requires(TFCTags.Items.DOG_FOOD)
//                .requires(Items.BONE)
//                .requires(TFCTags.Items.COMPOST_GREENS_HIGH)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HEARTY_KIBBLE.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.HEARTY_GRAIN_FEED.get())
//                .requires(TFCTags.Items.YAK_FOOD)
//                .requires(TFCTags.Items.YAK_FOOD)
//                .requires(Items.SUGAR)
//                .requires(TFCTags.Items.COMPOST_GREENS_HIGH)
//                .requires(TFCTags.Items.COMPOST_GREENS_HIGH)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HEARTY_GRAIN_FEED.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.KIBBLE.get())
//                .requires(TFCTags.Items.YAK_FOOD)
//                .requires(TFCTags.Items.DOG_FOOD)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.KIBBLE.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SEItems.GRAIN_FEED.get())
//                .requires(TFCTags.Items.YAK_FOOD)
//                .requires(TFCTags.Items.YAK_FOOD)
//                .requires(Items.SUGAR)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.GRAIN_FEED.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.VETERINARY_BANDAGE.get())
//                .define('A', ItemTags.WOOL)
//                .define('B', TFCItems.WOOL_YARN.get())
//                .pattern("B ")
//                .pattern("AB")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.VETERINARY_BANDAGE.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HEARTWORM_MEDICINE.get())
//                .define('A', Items.ROTTEN_FLESH)
//                .define('B', TFCItems.FOOD.get(Food.BEEF).get())
//                .define('C', TFCItems.POWDERS.get(Powder.SALT).get())
//                .pattern("BA")
//                .pattern("AC")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HEARTWORM_MEDICINE.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_OINTMENT.get())
//                .define('C', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                .define('B', Items.CLAY)
//                .pattern(" B ")
//                .pattern("BCB")
//                .pattern("BBB")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIBIOTIC_OINTMENT.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_EARDROPS.get())
//                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
//                .define('B', Items.CLAY)
//                .pattern(" B ")
//                .pattern("BCB")
//                .pattern("BCB")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIBIOTIC_EARDROPS.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIPARASITIC_OINTMENT.get())
//                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
//                .define('B', Items.CLAY)
//                .pattern(" B ")
//                .pattern("BCB")
//                .pattern("BBB")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIPARASITIC_OINTMENT.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.RABIES_SHOT.get())
//                .define('A', Items.GLASS)
//                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
//                .pattern("C  ")
//                .pattern(" A ")
//                .pattern("  B")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.RABIES_SHOT.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIPARASITIC_INJECTION.get())
//                .define('A', Items.GLASS)
//                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                .define('C', Items.CLAY)
//                .pattern("C  ")
//                .pattern(" A ")
//                .pattern("  B")
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIPARASITIC_INJECTION.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.ANTIBIOTIC_INJECTION.get())
//                                .define('A', Items.GLASS)
//                                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                                .define('C', TFCItems.POWDERS.get(Powder.WOOD_ASH).get())
//                                .pattern("C  ")
//                                .pattern(" A ")
//                                .pattern("  B")
//                                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.ANTIBIOTIC_INJECTION.get() + "_tfc"));
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.STETHOSCOPE.get())
//                                .define('A', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.CAST_IRON).get(Metal.ItemType.INGOT).get())
//                                .pattern("A A")
//                                .pattern(" A ")
//                                .pattern("  B")
//                                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.STETHOSCOPE.get() + "_tfc"));
//
//
//        ConditionalRecipe.builder()
//                .addCondition(new TFCCondition(new ResourceLocation(ScrapsExtras.MODID, "tfc_condition")))
//                .addRecipe(
//                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HORSE_MANNEQUIN.get())
//                                .define('A', Items.LIGHT_GRAY_WOOL)
//                                .define('B', TFCItems.METAL_ITEMS.get(Metal.Default.COPPER).get(Metal.ItemType.INGOT).get())
//                                .pattern("  A")
//                                .pattern("AAA")
//                                .pattern("B B")
//                                .unlockedBy("has_wool", has(ItemTags.WOOL))
//                                ::save).build
//                        (pFinishedRecipeConsumer, new ResourceLocation(ScrapsExtras.MODID, SEItems.HORSE_MANNEQUIN.get() + "_tfc"));

    }
}