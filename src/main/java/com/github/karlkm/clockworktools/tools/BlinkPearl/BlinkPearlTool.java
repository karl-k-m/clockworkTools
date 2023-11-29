package com.github.karlkm.clockworktools.tools.BlinkPearl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class BlinkPearlTool {

    public static ItemStack createBlinkPearl() {
        ItemStack blinkPearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = blinkPearl.getItemMeta();
        final Component itemName = text("Blink Pearl").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
        meta.displayName(itemName);
        final Component itemLore = text("Ender pearl that doesn't get used up. Has a 5 second cooldown.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
        List<Component> lore = List.of(itemLore);
        meta.lore(lore);
        blinkPearl.setItemMeta(meta);

        return blinkPearl;
    }
}
