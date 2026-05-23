package net.masked.contentWarning.listeners;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockDropListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Optional: Prevent drops if the player is in Creative mode
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Check if the broken block is a normal fern or a tall fern
        if (block.getType() == Material.FERN || block.getType() == Material.LARGE_FERN) {

            // Define the drop chance (e.g., 0.05 = 5% chance)
            double dropChance = 0.05;

            if (Math.random() < dropChance) {
                // Ensure the item exists before dropping
                if (ItemManager.hempSeeds != null) {
                    block.getWorld().dropItemNaturally(block.getLocation(), ItemManager.hempSeeds.clone());

                }
            }
        }
    }
}