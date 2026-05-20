package net.masked.contentWarning.recipes;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class SmokerRecipes {

    public static void register(JavaPlugin plugin) {
        // Validation safeguard to ensure items exist before building the recipe
        if (ItemManager.hemp == null || ItemManager.marijuana == null) {
            return;
        }

        // Unique identifier for the recipe
        NamespacedKey recipeKey = new NamespacedKey(plugin, "magic_item_smoking");

        // Uses ExactChoice so it checks custom display names, components, and item models
        RecipeChoice.ExactChoice inputChoice = new RecipeChoice.ExactChoice(ItemManager.hemp);

        // Parameters: Key, Output ItemStack, Input Choice, Experience (float), Cooking Time in Ticks (int)
        SmokingRecipe smokingRecipe = new SmokingRecipe(
                recipeKey,
                ItemManager.marijuana,
                inputChoice,
                0.35f, // Experience given upon collection
                10 * 20    // 20 Ticks = 1 second
        );

        Bukkit.addRecipe(smokingRecipe);
    }
}