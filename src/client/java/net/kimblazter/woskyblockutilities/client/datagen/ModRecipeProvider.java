package net.kimblazter.woskyblockutilities.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.kimblazter.woskyblockutilities.item.ModItems;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(
            FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture
    ) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.PUTRID_SOUP)
                .input(Items.BOWL)
                .input(Items.LILY_PAD)
                .input(ItemTags.FISHES)
                .input(Items.ROTTEN_FLESH)
                .criterion(hasItem(Items.ROTTEN_FLESH), conditionsFromItem(Items.ROTTEN_FLESH))
                .offerTo(exporter);
        offerCompactingRecipe(exporter, RecipeCategory.DECORATIONS, ModItems.COMPRESSED_LILY_PAD, Items.LILY_PAD);
        offer2x2CompactingRecipe(exporter, RecipeCategory.DECORATIONS, Items.MOSS_BLOCK, ModItems.COMPRESSED_LILY_PAD);
    }
}
