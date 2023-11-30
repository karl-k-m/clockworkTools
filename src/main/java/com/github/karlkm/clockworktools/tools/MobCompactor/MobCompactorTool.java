package com.github.karlkm.clockworktools.tools.MobCompactor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class MobCompactorTool {

    private final JavaPlugin plugin;

    public MobCompactorTool(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public static ItemStack createMobCompactor() {
        ItemStack mobCompactor = new ItemStack(Material.GLASS_BOTTLE);
        ItemMeta meta = mobCompactor.getItemMeta();
        final Component itemName = text("Mob Compactor").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
        meta.displayName(itemName);
        final Component itemLore = text("Right-click on a mob with this to store them.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
        List<Component> lore = List.of(itemLore);
        meta.lore(lore);
        mobCompactor.setItemMeta(meta);
        return mobCompactor;
    }
}
