package net.masked.contentWarning.listeners;

import net.masked.contentWarning.items.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace; // Added import
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

public class InteractionItemListener implements Listener {

    private final NamespacedKey timeKey;
    private final NamespacedKey typeKey;
    private final NamespacedKey displayUuidKey;
    private final NamespacedKey stageKey;

    public InteractionItemListener(JavaPlugin plugin) {
        this.timeKey = new NamespacedKey(plugin, "creation_time");
        this.typeKey = new NamespacedKey(plugin, "interaction_type");
        this.displayUuidKey = new NamespacedKey(plugin, "display_uuid");
        this.stageKey = new NamespacedKey(plugin, "plant_stage");

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (World world : Bukkit.getWorlds()) {
                for (Interaction interaction : world.getEntitiesByClass(Interaction.class)) {
                    PersistentDataContainer pdc = interaction.getPersistentDataContainer();
                    if (pdc.has(typeKey, PersistentDataType.STRING) && "hemp_plant".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
                        updatePlantStage(interaction);
                    }
                }
            }
        }, 100L, 100L);
    }

    @EventHandler
    public void onPodzolRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        // MODIFIED: Only allow interactions on the top face of the block
        if (event.getBlockFace() != BlockFace.UP) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (block != null && block.getType() == Material.PODZOL && item != null && item.isSimilar(ItemManager.hempSeeds)) {
            event.setCancelled(true);

            player.swingMainHand();

            long currentTime = block.getWorld().getFullTime();

            // ADJUSTED: Interaction bounding box base location remains at y + 1.0
            Location interactionLocation = block.getLocation().add(0.5, 1.0, 0.5);
            // ADJUSTED: Raised by 0.5 blocks (8 pixels) for the visual element
            Location displaySpawnLocation = block.getLocation().add(0.5, 1.5, 0.5);

            // Stage 0 Initial Display
            ItemDisplay itemDisplay = block.getWorld().spawn(displaySpawnLocation, ItemDisplay.class, display -> {
                ItemStack modelItem = new ItemStack(Material.PAPER);
                var meta = modelItem.getItemMeta();
                if (meta != null) {
                    meta.setItemModel(new NamespacedKey("content_warning", "hemp_small_stage0_block"));
                    modelItem.setItemMeta(meta);
                }
                display.setItemStack(modelItem);
            });

            // Stage 0 Initial Interaction Box (1 Block Tall)
            block.getWorld().spawn(interactionLocation, Interaction.class, interaction -> {
                interaction.setInteractionWidth(1.0f);
                interaction.setInteractionHeight(1.0f);

                PersistentDataContainer pdc = interaction.getPersistentDataContainer();
                pdc.set(typeKey, PersistentDataType.STRING, "hemp_plant");
                pdc.set(timeKey, PersistentDataType.LONG, currentTime);
                pdc.set(displayUuidKey, PersistentDataType.STRING, itemDisplay.getUniqueId().toString());
                pdc.set(stageKey, PersistentDataType.INTEGER, 0);
            });

            if (player.getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onBonemealInteraction(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Interaction interaction)) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.BONE_MEAL) {
            PersistentDataContainer pdc = interaction.getPersistentDataContainer();

            if (pdc.has(typeKey, PersistentDataType.STRING) && "hemp_plant".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
                long currentTime = interaction.getWorld().getFullTime();
                long creationTime = pdc.getOrDefault(timeKey, PersistentDataType.LONG, currentTime);

                if (currentTime - creationTime < 24000L) {
                    event.setCancelled(true);
                    player.swingMainHand();

                    pdc.set(timeKey, PersistentDataType.LONG, creationTime - 8000L);

                    updatePlantStage(interaction);

                    Location particleLoc = interaction.getLocation().add(0, 0.5, 0);
                    int stage = pdc.getOrDefault(stageKey, PersistentDataType.INTEGER, 0);

                    if (stage >= 3) {
                        interaction.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, particleLoc, 20, 0.25, 0.5, 0.25, 0.05);
                    } else {
                        interaction.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, particleLoc, 8, 0.2, 0.4, 0.2, 0.05);
                    }

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

        if (pdc.has(typeKey, PersistentDataType.STRING) && "hemp_plant".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
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
        if (event.getBlock().getType() == Material.PODZOL) {
            checkAndRemoveInteraction(event.getBlock());
        }
    }

    private void checkAndRemoveInteraction(Block block) {
        Location checkLoc = block.getLocation().add(0.5, 1.0, 0.5);
        Collection<Entity> nearbyEntities = block.getWorld().getNearbyEntities(checkLoc, 0.1, 1.0, 0.1);

        for (Entity entity : nearbyEntities) {
            if (entity instanceof Interaction interaction) {
                PersistentDataContainer pdc = interaction.getPersistentDataContainer();
                if (pdc.has(typeKey, PersistentDataType.STRING) && "hemp_plant".equals(pdc.get(typeKey, PersistentDataType.STRING))) {
                    handleDropAndRemoval(interaction);
                    break;
                }
            }
        }
    }

    private void updatePlantStage(Interaction interaction) {
        PersistentDataContainer pdc = interaction.getPersistentDataContainer();
        if (!pdc.has(displayUuidKey, PersistentDataType.STRING)) return;

        long creationTime = pdc.getOrDefault(timeKey, PersistentDataType.LONG, interaction.getWorld().getFullTime());
        long elapsedTicks = interaction.getWorld().getFullTime() - creationTime;

        int targetStage = Math.min(3, (int) (elapsedTicks / 8000L));
        int currentStage = pdc.getOrDefault(stageKey, PersistentDataType.INTEGER, 0);

        if (targetStage > currentStage) {
            pdc.set(stageKey, PersistentDataType.INTEGER, targetStage);

            if (targetStage >= 2) {
                interaction.setInteractionHeight(2.0f);
            }

            try {
                UUID oldDisplayUuid = UUID.fromString(pdc.get(displayUuidKey, PersistentDataType.STRING));
                Entity oldTarget = interaction.getWorld().getEntity(oldDisplayUuid);

                if (oldTarget instanceof ItemDisplay oldDisplay) {
                    Location spawnLoc = oldDisplay.getLocation();
                    oldDisplay.remove();

                    ItemDisplay newDisplay = interaction.getWorld().spawn(spawnLoc, ItemDisplay.class, display -> {
                        ItemStack updatedModel = new ItemStack(Material.PAPER);
                        var meta = updatedModel.getItemMeta();
                        if (meta != null) {
                            String modelName = switch (targetStage) {
                                case 1 -> "hemp_small_stage1_block";
                                case 2 -> "hemp_block";
                                case 3 -> "hemp_flowering_block";
                                default -> "hemp_small_stage0_block";
                            };
                            meta.setItemModel(new NamespacedKey("content_warning", modelName));
                            updatedModel.setItemMeta(meta);
                        }
                        display.setItemStack(updatedModel);
                    });

                    pdc.set(displayUuidKey, PersistentDataType.STRING, newDisplay.getUniqueId().toString());
                }
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void handleDropAndRemoval(Interaction interaction) {
        PersistentDataContainer pdc = interaction.getPersistentDataContainer();
        Location dropLocation = interaction.getLocation();

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

        if (currentStage >= 3) {
            if (ItemManager.hemp != null) {
                dropLocation.getWorld().dropItemNaturally(dropLocation, ItemManager.hemp.clone());
            }
            if (ItemManager.hempSeeds != null) {
                ItemStack extraDrops = ItemManager.hempSeeds.clone();
                extraDrops.setAmount(2);
                dropLocation.getWorld().dropItemNaturally(dropLocation, extraDrops);
            }
        } else {
            if (ItemManager.hempSeeds != null) {
                dropLocation.getWorld().dropItemNaturally(dropLocation, ItemManager.hempSeeds.clone());
            }
        }

        interaction.remove();
    }
}