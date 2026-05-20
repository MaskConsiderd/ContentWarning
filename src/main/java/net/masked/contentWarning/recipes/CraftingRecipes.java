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
        if (ItemManager.marijuana == null || ItemManager.cubanCigar == null) {
            return;
        }

        // Unique identifier for the crafting recipe
        NamespacedKey recipeKey = new NamespacedKey(plugin, "magic_item_crafting");

        // Create the Shaped Recipe with the output item
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, ItemManager.cubanCigar);

        // Define the 3x3 crafting grid pattern (3 rows of strings)
        // 'P' represents Paper, 'M' represents marijuana, and ' ' is an empty slot
        recipe.shape(
                "GPG",
                "PMP",
                "GPG"
        );

        // Map the characters to their corresponding items
        recipe.setIngredient('P', Material.PAPER);

        recipe.setIngredient('G', Material.GOLD_INGOT);

        // Use ExactChoice for the custom ingredient to respect item components, models, and metadata
        recipe.setIngredient('M', new RecipeChoice.ExactChoice(ItemManager.marijuana));

        // Register the recipe with the server
        Bukkit.addRecipe(recipe);
    }
}