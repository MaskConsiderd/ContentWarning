package net.masked.contentWarning;

import net.masked.contentWarning.items.ItemManager;
import net.masked.contentWarning.commands.GetContentWarningItems;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContentWarning extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // managers
        ItemManager.init();

        // commands
        this.getCommand("get_content_warning_items").setExecutor(new GetContentWarningItems());

        // tasks
        new net.masked.contentWarning.tasks.CigarSmokeTask().runTaskTimer(this, 0L, 15L);

        // listeners
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.BlockDropListener(), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.InteractionItemListener(this), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.CigarListener(), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.CowInteractionListener(), this);

        // recipes
        net.masked.contentWarning.recipes.SmokerRecipes.register(this);
        net.masked.contentWarning.recipes.CraftingRecipes.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
