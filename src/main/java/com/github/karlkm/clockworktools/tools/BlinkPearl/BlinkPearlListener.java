package com.github.karlkm.clockworktools.tools.BlinkPearl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class BlinkPearlListener implements Listener {

    private final JavaPlugin plugin;

    public BlinkPearlListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player right-clicking with a Blink Pearl in their hand and launches an Ender Pearl.
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isBlinkPearl(heldItem)) {
                if (player.getCooldown(Material.ENDER_PEARL) > 0) {
                    return;
                }
                // Cancel the event so the player doesn't throw the pearl twice.
                event.setCancelled(true);

                EnderPearl pearl = player.launchProjectile(EnderPearl.class);
                pearl.setMetadata("BlinkPearl", new FixedMetadataValue(plugin, true));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 0.5F, 0.5F);

                player.setCooldown(Material.ENDER_PEARL, 100);
            }
        }
    }

    /**
     * Checks if an ItemStack is a Blink Pearl.
     * @param item The ItemStack to check
     * @return True if the ItemStack is a Blink Pearl, false otherwise
     */
    private boolean isBlinkPearl(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            Component displayName = item.getItemMeta().displayName();
            if (displayName != null) {
                return displayName.equals(Component.text("Blink Pearl").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            }
        }
        return false;
    }
}
