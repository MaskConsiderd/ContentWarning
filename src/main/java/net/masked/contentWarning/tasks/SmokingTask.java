package net.masked.contentWarning.tasks;

import net.masked.contentWarning.items.ItemManager;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class SmokingTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isHandRaised()) {
                ItemStack item = player.getActiveItem();

                // Check if the held item is the Cuban Cigar
                if (item != null && item.equals(ItemManager.cubanCigar)) {

                    // NEW: Check if the off-hand is holding Flint and Steel
                    ItemStack offHandItem = player.getInventory().getItemInOffHand();
                    if (offHandItem.getType() != Material.FLINT_AND_STEEL) {
                        continue; // Skip this player if they don't have a lighter
                    }

                    // Sound
                    NamespacedKey soundKey = new NamespacedKey("content_warning", "custom.smoke_inhale");
                    Sound smokeSound = Sound.sound(soundKey, Source.PLAYER, 0.7f, 1.0f);
                    player.playSound(smokeSound);

                    // Smoke particles
                    player.getWorld().spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE,
                            player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.4)),
                            4,
                            0.05, 0.05, 0.05,
                            0.01
                    );
                }
                // Check if the held item is the Cigar
                if (item != null && item.equals(ItemManager.cigar)) {

                    // NEW: Check if the off-hand is holding Flint and Steel
                    ItemStack offHandItem = player.getInventory().getItemInOffHand();
                    if (offHandItem.getType() != Material.FLINT_AND_STEEL) {
                        continue; // Skip this player if they don't have a lighter
                    }

                    // Sound
                    NamespacedKey soundKey = new NamespacedKey("content_warning", "custom.smoke_inhale");
                    Sound smokeSound = Sound.sound(soundKey, Source.PLAYER, 0.7f, 1.0f);
                    player.playSound(smokeSound);

                    // Smoke particles
                    player.getWorld().spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE,
                            player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.4)),
                            4,
                            0.05, 0.05, 0.05,
                            0.01
                    );
                }
            }
        }
    }
}