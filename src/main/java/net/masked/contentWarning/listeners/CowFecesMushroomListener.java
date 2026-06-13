/* package net.masked.contentWarning.listeners;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.UUID;

public class CowFecesMushroomListener implements Listener {

    private final NamespacedKey typeKey;
    private final NamespacedKey displayUuidKey;
    private final NamespacedKey stageKey;
    private final NamespacedKey stage3TimeKey;

    public CowFecesMushroomListener(JavaPlugin plugin) {
        this.typeKey = new NamespacedKey(plugin, "interaction_type");
        this.displayUuidKey = new NamespacedKey(plugin, "display_uuid");
        this.stageKey = new NamespacedKey(plugin, "feces_stage");
        this.stage3TimeKey = new NamespacedKey(plugin, "stage3_time");

        // Background task to check for Stage 3 -> Stage 4 progression
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (World world : Bukkit.getWorlds()) {
                for (Interaction interaction : world.getEntitiesByClass(Interaction.class)) {
                    PersistentDataContainer pdc = interaction.getPersistentDataContainer();

                    if (pdc.has(typeKey, PersistentDataType.STRING) && "feces_growth".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
                        int stage = pdc.getOrDefault(stageKey, PersistentDataType.INTEGER, 0);

                        if (stage == 3 && pdc.has(stage3TimeKey, PersistentDataType.LONG)) {
                            long stage3StartTime = pdc.get(stage3TimeKey, PersistentDataType.LONG);
                            long currentTime = world.getFullTime();

                            // If 24,000 ticks (1 in-game day) have passed since reaching Stage 3
                            if (currentTime - stage3StartTime >= 24000L) {
                                pdc.set(stageKey, PersistentDataType.INTEGER, 4);
                                updateDisplayModel(interaction, 4);
                            }
                        }
                    }
                }
            }
        }, 100L, 100L); // Checks every 5 seconds
    }

    @EventHandler
    public void onHaybaleRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        // Strict check: Only trigger if the top face is clicked
        if (event.getBlockFace() != BlockFace.UP) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (block != null && block.getType() == Material.HAY_BLOCK && item != null && item.isSimilar(ItemManager.cowFeces)) {
            event.setCancelled(true);
            player.swingMainHand();

            // Setup locations
            Location interactionLocation = block.getLocation().add(0.5, 1.0, 0.5);
            // 8 pixels is exactly 0.5 blocks. So block top (1.0) + 0.5 = 1.5
            Location displaySpawnLocation = block.getLocation().add(0.5, 1.5, 0.5);

            // Spawn the visual Item Display
            ItemDisplay itemDisplay = block.getWorld().spawn(displaySpawnLocation, ItemDisplay.class, display -> {
                ItemStack modelItem = new ItemStack(Material.PAPER);
                var meta = modelItem.getItemMeta();
                if (meta != null) {
                    meta.setItemModel(new NamespacedKey("content_warning", "cow_feces_block"));
                    modelItem.setItemMeta(meta);
                }
                display.setItemStack(modelItem);
            });

            // Spawn the physical Interaction Hitbox
            block.getWorld().spawn(interactionLocation, Interaction.class, interaction -> {
                interaction.setInteractionWidth(1.0f);
                interaction.setInteractionHeight(1.0f);

                PersistentDataContainer pdc = interaction.getPersistentDataContainer();
                pdc.set(typeKey, PersistentDataType.STRING, "feces_growth");
                pdc.set(displayUuidKey, PersistentDataType.STRING, itemDisplay.getUniqueId().toString());
                pdc.set(stageKey, PersistentDataType.INTEGER, 0);
            });

            if (player.getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onMushroomInteraction(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Interaction interaction)) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        PersistentDataContainer pdc = interaction.getPersistentDataContainer();

        if (pdc.has(typeKey, PersistentDataType.STRING) && "feces_growth".equals(pdc.get(typeKey, PersistentDataType.STRING))) {

            // Allow manual upgrades via Brown Mushroom
            if (item.getType() == Material.BROWN_MUSHROOM) {
                int currentStage = pdc.getOrDefault(stageKey, PersistentDataType.INTEGER, 0);

                // Stop accepting mushrooms once Stage 3 is reached
                if (currentStage < 3) {
                    event.setCancelled(true);
                    player.swingMainHand();

                    int newStage = currentStage + 1;
                    pdc.set(stageKey, PersistentDataType.INTEGER, newStage);

                    // If we just hit Stage 3, record the exact time so the 24000 tick timer can start
                    if (newStage == 3) {
                        pdc.set(stage3TimeKey, PersistentDataType.LONG, interaction.getWorld().getFullTime());
                    }

                    updateDisplayModel(interaction, newStage);

                    if (player.getGameMode() != GameMode.CREATIVE) {
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteractionAttack(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Interaction interaction)) return;
        if (!(event.getDamager() instanceof Player)) return;

        PersistentDataContainer pdc = interaction.getPersistentDataContainer();

        if (pdc.has(typeKey, PersistentDataType.STRING) && "feces_growth".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
            event.setCancelled(true);
            handleDropAndRemoval(interaction);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        checkAndRemoveInteraction(event.getBlock());
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (event.getBlock().getType() == Material.HAY_BLOCK) {
            checkAndRemoveInteraction(event.getBlock());
        }
    }

    private void checkAndRemoveInteraction(Block block) {
        Location checkLoc = block.getLocation().add(0.5, 1.0, 0.5);
        Collection<Entity> nearbyEntities = block.getWorld().getNearbyEntities(checkLoc, 0.1, 1.0, 0.1);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Interaction interaction) {
                PersistentDataContainer pdc = interaction.getPersistentDataContainer();
                if (pdc.has(typeKey, PersistentDataType.STRING) && "feces_growth".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
                    handleDropAndRemoval(interaction);
                    break;
                }
            }
        }
    }

    private void updateDisplayModel(Interaction interaction, int stage) {
        PersistentDataContainer pdc = interaction.getPersistentDataContainer();
        if (!pdc.has(displayUuidKey, PersistentDataType.STRING)) return;

        try {
            UUID displayUuid = UUID.fromString(pdc.get(displayUuidKey, PersistentDataType.STRING));
            Entity target = interaction.getWorld().getEntity(displayUuid);

            if (target instanceof ItemDisplay display) {
                ItemStack updatedModel = new ItemStack(Material.PAPER);
                var meta = updatedModel.getItemMeta();

                if (meta != null) {
                    String modelName = switch (stage) {
                        case 1 -> "cow_feces_stage1_block";
                        case 2 -> "cow_feces_stage2_block";
                        case 3 -> "cow_feces_stage3_block";
                        case 4 -> "cow_feces_stage4_block";
                        default -> "cow_feces_block";
                    };
                    meta.setItemModel(new NamespacedKey("content_warning", modelName));
                    updatedModel.setItemMeta(meta);
                }

                // Set item stack rather than deleting and respawning the entity
                display.setItemStack(updatedModel);
            }
        } catch (IllegalArgumentException ignored) {}
    }

    private void handleDropAndRemoval(Interaction interaction) {
        PersistentDataContainer pdc = interaction.getPersistentDataContainer();
        Location dropLocation = interaction.getLocation();

        // 1. Clean up the Item Display entity
        if (pdc.has(displayUuidKey, PersistentDataType.STRING)) {
            try {
                UUID displayUuid = UUID.fromString(pdc.get(displayUuidKey, PersistentDataType.STRING));
                Entity displayEntity = interaction.getWorld().getEntity(displayUuid);
                if (displayEntity != null) {
                    displayEntity.remove();
                }
            } catch (IllegalArgumentException ignored) {}
        }

        int currentStage = pdc.getOrDefault(stageKey, PersistentDataType.INTEGER, 0);

        // 2. Drop the original feces block
        if (ItemManager.cowFeces != null) {
            dropLocation.getWorld().dropItemNaturally(dropLocation, ItemManager.cowFeces.clone());
        }

        // 3. Drop stage-dependent items
        if (currentStage >= 1 && currentStage <= 3) {
            ItemStack shrooms = new ItemStack(Material.BROWN_MUSHROOM);
            shrooms.setAmount(currentStage); // Stage 1 = 1, Stage 2 = 2, Stage 3 = 3
            dropLocation.getWorld().dropItemNaturally(dropLocation, shrooms);

        } else if (currentStage == 4) {
            if (ItemManager.shrooms != null) {
                ItemStack specialDrops = ItemManager.shrooms.clone();
                specialDrops.setAmount(3);
                dropLocation.getWorld().dropItemNaturally(dropLocation, specialDrops);
            }
        }

        // 4. Clean up the Interaction entity itself
        interaction.remove();
    }
}
 */