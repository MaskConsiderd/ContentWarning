package net.masked.contentWarning.commands;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class ContentWarningCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // 1. Ensure the sender is a player
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can execute this command.", NamedTextColor.RED));
            return true;
        }

        // 2. Check if the correct number of arguments are provided (e.g., /cw items all = 2 args)
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /contentwarning items all", NamedTextColor.RED));
            return true;
        }

        // 3. Verify that args[0] is "items" and args[1] is "all"
        if (args[0].equalsIgnoreCase("items") && args[1].equalsIgnoreCase("all")) {

            player.getInventory().addItem(ItemManager.cigar);
            player.getInventory().addItem(ItemManager.cubanCigar);
            player.getInventory().addItem(ItemManager.hemp);
            player.getInventory().addItem(ItemManager.hempSeeds);
            player.getInventory().addItem(ItemManager.marijuana);
            player.getInventory().addItem(ItemManager.shrooms);
            player.getInventory().addItem(ItemManager.cowFeces);

            player.sendMessage(Component.text("Items received!", NamedTextColor.GREEN));
            return true;
        }

        // If the arguments don't match "items all"
        player.sendMessage(Component.text("Unknown arguments. Usage: /contentwarning items all", NamedTextColor.RED));
        return true;
    }
}