package com.github.karlkm.clockworktools.tools.MobCompactor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

public class BottledMobListener implements Listener {

    JavaPlugin plugin;

    public BottledMobListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata("justInteractedWithEntity")) {
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (isBottledMob(heldItem) && event.getHand() == EquipmentSlot.HAND) {
            ItemMeta meta = heldItem.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "entity_uuid");

            UUID uuid = UUID.fromString(Objects.requireNonNull(container.get(key, PersistentDataType.STRING)));
            for (World world : Bukkit.getWorlds()) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (entity.getUniqueId().toString().equals(uuid.toString())) {
                        event.setCancelled(true);

                        if (Objects.equals(Objects.requireNonNull(entity.customName()) , Component.text("PersistentEntity"))) {
                            entity.customName(null);
                        }

                        entity.teleport(player.getLocation());
                        entity.removePotionEffect(PotionEffectType.SLOW);
                        entity.removePotionEffect(PotionEffectType.INVISIBILITY);
                        entity.removePotionEffect(PotionEffectType.SLOW_FALLING);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> entity.setInvulnerable(false), 20L);

                        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5F, 0.8F);

                        ItemStack mobCompactor = MobCompactorTool.createMobCompactor();
                        player.getInventory().setItemInMainHand(mobCompactor);
                    }
                }
            }
        }
    }

    /**
     * Checks if the item is a bottled mob.
     * @param item The item to check.
     * @return True if the item is a bottled mob, false otherwise.
     */
    private boolean isBottledMob(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            Component displayName = item.getItemMeta().displayName();
            if (displayName != null) {
                return displayName.equals(Component.text("Bottled Mob").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            }
        }
        return false;
    }
}
