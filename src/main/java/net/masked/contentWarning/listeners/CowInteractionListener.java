/*
package net.masked.contentWarning.listeners;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CowInteractionListener implements Listener {

    @EventHandler
    public void onCowInteract(PlayerInteractEntityEvent event) {
        // Prevent the event from firing twice (once for the main-hand, once for thr off-hand)
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Entity clickedEntity = event.getRightClicked();
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        // Check if the clicked entity is a Cow and the player is holding a Hay Block
        if (clickedEntity instanceof Cow cow && heldItem.getType() == Material.HAY_BLOCK) {

            // drop chance is just conversion between % to decimal
            double dropChance = 0.2;

            if (Math.random() < dropChance) {
                // Double check that the item actually exists before dropping
                if (ItemManager.cowFeces != null) {
                    cow.getWorld().dropItemNaturally(cow.getLocation(), ItemManager.cowFeces.clone());
                }
            }

            // Optional: Consume the hay block if the player isn't in creative mode.
            // Remove this block if you want them to be able to click infinitely without losing hay.
            if (player.getGameMode() != GameMode.CREATIVE) {
                heldItem.setAmount(heldItem.getAmount() - 1);
            }
        }
    }
}
 */