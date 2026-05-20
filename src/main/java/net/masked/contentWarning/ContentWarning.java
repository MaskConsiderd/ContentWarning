package net.masked.contentWarning;

import net.masked.contentWarning.items.ItemManager;
import net.masked.contentWarning.commands.GetContentWarningItems;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContentWarning extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        ItemManager.init();
        this.getCommand("get_content_warning_items").setExecutor(new GetContentWarningItems());
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.CigarListener(), this);
        new net.masked.contentWarning.tasks.CigarSmokeTask().runTaskTimer(this, 0L, 15L);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.BlockDropListener(), this);
        this.getServer().getPluginManager().registerEvents(new net.masked.contentWarning.listeners.InteractionItemListener(this), this);
        net.masked.contentWarning.recipes.SmokerRecipes.register(this);
        net.masked.contentWarning.recipes.CraftingRecipes.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
