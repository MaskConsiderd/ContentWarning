package net.masked.contentWarning.listeners;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SmokingListener implements Listener {

    // NEW: Prevents the player from even starting the right-click animation
    @EventHandler
    public void onCubanCigarUse(PlayerInteractEvent event) {
        // Check if the action is a right-click on a block or air
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Ensure we are only evaluating the main hand interaction to prevent double-firing
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the item being right-clicked is the Cuban Cigar
        if (item != null && item.isSimilar(ItemManager.cubanCigar)) {
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            // Cancel the event immediately if the off-hand is not holding flint and steel
            if (offHandItem.getType() != Material.FLINT_AND_STEEL) {
                event.setCancelled(true);
                player.sendMessage("§cYou need something to light this");
            }
        }
    }

    @EventHandler
    public void onCubanCigarFinished(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.isSimilar(ItemManager.cubanCigar)) {

            // Double-check condition upon completion (in case they swap items mid-animation)
            ItemStack offHandItem = player.getInventory().getItemInOffHand();
            if (offHandItem.getType() != Material.FLINT_AND_STEEL) {
                event.setCancelled(true);
                player.sendMessage("§cYou need something to light this");
                return;
            }

            event.setCancelled(true);

            // Gets rid of the Cuban Cigar
            int currentAmount = item.getAmount();
            if (currentAmount > 1) {
                item.setAmount(currentAmount - 1);
            } else {
                if (player.getInventory().getItemInMainHand().isSimilar(ItemManager.cubanCigar)) {
                    player.getInventory().setItemInMainHand(null);
                } else if (player.getInventory().getItemInOffHand().isSimilar(ItemManager.cubanCigar)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }

            // Gives player status effects
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 3 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 1, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 2));

            // Gives the Cigar Butt
            if (ItemManager.cubanCigarButt != null) {
                player.getInventory().addItem(ItemManager.cubanCigarButt.clone()).values().forEach(droppedItem ->
                        player.getWorld().dropItemNaturally(player.getLocation(), droppedItem)
                );
            }

            player.sendMessage("§7Your cigar has burned out down to a butt.");
        }
    }

    // NEW: Prevents the player from even starting the right-click animation
    @EventHandler
    public void onCigarUse(PlayerInteractEvent event) {
        // Check if the action is a right-click on a block or air
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Ensure we are only evaluating the main hand interaction to prevent double-firing
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the item being right-clicked is the Cuban Cigar
        if (item != null && item.isSimilar(ItemManager.cigar)) {
            ItemStack offHandItem = player.getInventory().getItemInOffHand();

            // Cancel the event immediately if the off-hand is not holding flint and steel
            if (offHandItem.getType() != Material.FLINT_AND_STEEL) {
                event.setCancelled(true);
                player.sendMessage("§cYou need something to light this");
            }
        }
    }

    @EventHandler
    public void onCigarFinished(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item.isSimilar(ItemManager.cigar)) {

            // Double-check condition upon completion (in case they swap items mid-animation)
            ItemStack offHandItem = player.getInventory().getItemInOffHand();
            if (offHandItem.getType() != Material.FLINT_AND_STEEL) {
                event.setCancelled(true);
                player.sendMessage("§cYou need something to light this");
                return;
            }

            event.setCancelled(true);

            // Gets rid of the Cuban Cigar
            int currentAmount = item.getAmount();
            if (currentAmount > 1) {
                item.setAmount(currentAmount - 1);
            } else {
                if (player.getInventory().getItemInMainHand().isSimilar(ItemManager.cigar)) {
                    player.getInventory().setItemInMainHand(null);
                } else if (player.getInventory().getItemInOffHand().isSimilar(ItemManager.cigar)) {
                    player.getInventory().setItemInOffHand(null);
                }
            }

            // Gives player status effects
            player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 3 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 1, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 2));

            // Gives the Cigar Butt
            if (ItemManager.cigarButt != null) {
                player.getInventory().addItem(ItemManager.cigarButt.clone()).values().forEach(droppedItem ->
                        player.getWorld().dropItemNaturally(player.getLocation(), droppedItem)
                );
            }

            player.sendMessage("§7Your cigar has burned out down to a butt.");
        }
    }
}