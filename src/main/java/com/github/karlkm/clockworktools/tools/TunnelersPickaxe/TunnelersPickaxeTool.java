package com.github.karlkm.clockworktools.tools.TunnelersPickaxe;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class TunnelersPickaxeTool {

    public static ItemStack createTunnelersPickaxe() {
        ItemStack tunnelersPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = tunnelersPickaxe.getItemMeta();
        final Component itemName = text("Tunneler's Pickaxe").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
        meta.displayName(itemName);
        final Component itemLore = text("Mines 3x3 through natural stone-type blocks.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
        List<Component> lore = List.of(itemLore);
        meta.lore(lore);
        tunnelersPickaxe.setItemMeta(meta);
        return tunnelersPickaxe;
    }
}
