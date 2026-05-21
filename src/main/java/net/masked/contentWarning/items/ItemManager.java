package net.masked.contentWarning.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {
    public static ItemStack cubanCigar;
    public static ItemStack cubanCigarButt;
    public static ItemStack hempSeeds;
    public static ItemStack marijuana;
    public static ItemStack hemp;
    public static ItemStack cowFeces;
    public static ItemStack shrooms;
    public static ItemStack cigar;

    // Registers the items
    public static void init() {
        createCubanCigar();
        createCubanCigarButt();
        createHempSeeds();
        createMarijuana();
        createHemp();
        createCowFeces();
        createShrooms();
        createCigar();
    }

    // Adds data to the Cuban Cigar
    private static void createCubanCigar() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            NamespacedKey cigarKey = new NamespacedKey("content_warning", "cuban_cigar");
            meta.displayName(Component.text("Cuban Cigar", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(cigarKey);
            item.setItemMeta(meta);
        }

        NamespacedKey loopSoundKey = new NamespacedKey("content_warning", "custom.smoke_inhale");
        item.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
                .animation(ItemUseAnimation.TOOT_HORN)
                .consumeSeconds(5.0f)
                .hasConsumeParticles(false)
                .sound(loopSoundKey)
                .build());

        item.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        cubanCigar = item;
    }

    // Adds data to the Cuban Cigar Butt
    private static void createCubanCigarButt() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey buttKey = new NamespacedKey("content_warning", "cuban_cigar_butt");
            meta.displayName(Component.text("Cuban Cigar Butt", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(buttKey);
            item.setItemMeta(meta);
        }
        cubanCigarButt = item;
    }

    // Adds data to the Marijuana Seeds
    private static void createHempSeeds() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey hempSeedsKey = new NamespacedKey("content_warning", "hemp_seeds");
            meta.displayName(Component.text("Hemp Seeds").decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(hempSeedsKey);
            item.setItemMeta(meta);
        }
        hempSeeds = item;
    }

    // Adds data to the Marijuana
    private static void createMarijuana() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey marijuanaSeedsKey = new NamespacedKey("content_warning", "marijuana");
            meta.displayName(Component.text("Marijuana").decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(marijuanaSeedsKey);
            item.setItemMeta(meta);
        }
        marijuana = item;
    }

    // Adds data to the Hemp
    private static void createHemp() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey hempKey = new NamespacedKey("content_warning", "hemp");
            meta.displayName(Component.text("Hemp").decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(hempKey);
            item.setItemMeta(meta);
        }
        hemp = item;
    }

    // Adds data to the Cow Feces
    private static void createCowFeces() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey cowFecesKey = new NamespacedKey("content_warning", "cow_feces");
            meta.displayName(Component.text("Cow Feces").decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(cowFecesKey);
            item.setItemMeta(meta);
        }
        cowFeces = item;
    }

    // Adds data to the Shrooms
    private static void createShrooms() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            NamespacedKey shroomsKey = new NamespacedKey("content_warning", "shrooms");
            meta.displayName(Component.text("Shrooms").decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(shroomsKey);
            item.setItemMeta(meta);
        }
        shrooms = item;
    }

    // Adds data to the Cuban Cigar
    private static void createCigar() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            NamespacedKey cigarKey = new NamespacedKey("content_warning", "cigar");
            meta.displayName(Component.text("Cuban Cigar", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
            meta.setItemModel(cigarKey);
            item.setItemMeta(meta);
        }

        NamespacedKey loopSoundKey = new NamespacedKey("content_warning", "custom.smoke_inhale");
        item.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable()
                .animation(ItemUseAnimation.TOOT_HORN)
                .consumeSeconds(5.0f)
                .hasConsumeParticles(false)
                .sound(loopSoundKey)
                .build());

        item.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        cigar = item;
    }

}
