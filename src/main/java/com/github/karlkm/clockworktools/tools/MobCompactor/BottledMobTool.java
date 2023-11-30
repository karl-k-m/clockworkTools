package com.github.karlkm.clockworktools.tools.MobCompactor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class BottledMobTool {

    private final JavaPlugin plugin;

    public BottledMobTool(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public ItemStack createBottledMob(Entity entity) {
        ItemStack bottledMob = new ItemStack(Material.DRAGON_BREATH);
        ItemMeta meta = bottledMob.getItemMeta();
        final Component itemName = text("Bottled Mob").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
        meta.displayName(itemName);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "entity_uuid");
        container.set(key, PersistentDataType.STRING, entity.getUniqueId().toString());

        final Component itemLore = text("Contains a " + entity.getType().toString() +". Right click to release.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false);
        List<Component> lore = List.of(itemLore);
        meta.lore(lore);
        bottledMob.setItemMeta(meta);

        return bottledMob;
    }
}
