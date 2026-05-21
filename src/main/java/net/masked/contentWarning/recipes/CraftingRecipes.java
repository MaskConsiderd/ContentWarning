package net.masked.contentWarning.recipes;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class CraftingRecipes {

    public static void register(JavaPlugin plugin) {
        // Validation safeguard to ensure items exist before building the recipe
        if (ItemManager.cigar == null || ItemManager.cubanCigar == null) {
            return;
        }

        // Unique identifier for the crafting recipe
        NamespacedKey cubanCigarKey = new NamespacedKey(plugin, "cuban_cigar_crafting");

        // Create the Shaped Recipe with the output item
        ShapedRecipe recipe = new ShapedRecipe(cubanCigarKey, ItemManager.cubanCigar);

        recipe.shape(
                "GGG",
                "GCG",
                "GGG"
        );

        // Map the characters to their corresponding items
        recipe.setIngredient('C', new RecipeChoice.ExactChoice(ItemManager.cigar));

        recipe.setIngredient('G', Material.GOLD_INGOT);

        // Register the recipe with the server
        Bukkit.addRecipe(recipe);
    }
}