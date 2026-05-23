package net.masked.contentWarning;

import net.masked.contentWarning.items.ItemManager;
import net.masked.contentWarning.commands.ContentWarningCommand;
import net.masked.contentWarning.listeners.HempProcessInteractionListener;
import net.masked.contentWarning.tasks.SmokingTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContentWarning extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // managers
        ItemManager.init();

        // commands
        this.getCommand("contentwarning").setExecutor(new net.masked.contentWarning.commands.ContentWarningCommand());

        // tasks
        new SmokingTask().runTaskTimer(this, 0L, 15L);

        // listeners
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.BlockDropListener(), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.HempProcessInteractionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.SmokingListener(), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.CowInteractionListener(), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.CowFecesMushroomListener(this), this);


        // recipes
        net.masked.contentWarning.recipes.SmokerRecipes.register(this);
        net.masked.contentWarning.recipes.CraftingRecipes.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
