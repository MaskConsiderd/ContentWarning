package net.masked.contentWarning.commands;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetContentWarningItems implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        player.getInventory().addItem(ItemManager.cubanCigar);
        player.getInventory().addItem(ItemManager.cubanCigarButt);
        player.getInventory().addItem(ItemManager.hempSeeds);
        player.getInventory().addItem(ItemManager.marijuana);
        player.getInventory().addItem(ItemManager.hemp);
        player.getInventory().addItem(ItemManager.cowFeces);
        return true;
    }
}
