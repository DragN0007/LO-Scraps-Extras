package com.dragn0007.dragnloextras.datagen;

import com.dragn0007.dragnloextras.items.SEItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SERecipeMaker extends RecipeProvider implements IConditionBuilder {
    public SERecipeMaker(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SEItems.HORSE_MANNEQUIN.get())
                .define('A', Items.LIGHT_GRAY_WOOL)
                .define('B', Items.IRON_INGOT)
                .pattern("  A")
                .pattern("AAA")
                .pattern("B B")
                .unlockedBy("has_iron", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_INGOT).build()))
                .save(pFinishedRecipeConsumer);
        }
}