package com.github.karlkm.clockworktools.tools.MobCompactor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MobCompactorListener implements Listener {

    private final JavaPlugin plugin;

    public MobCompactorListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player right-clicking with a Mob Compactor (bottle) in their hand and prevents them from using it.
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    public void onPlayerInteractWater(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if  (isMobCompactor(heldItem) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);
        }
    }

    /**
     * Listens for a player right-clicking on a mob with a Mob Compactor (bottle) in their hand and stores the mob.
     * @param event The PlayerInteractEntityEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (isMobCompactor(heldItem) && event.getHand() == EquipmentSlot.HAND) {
            event.setCancelled(true);

            if (event.getRightClicked() instanceof LivingEntity entity && !(entity instanceof Player) && entity.getType() != EntityType.ENDER_DRAGON && entity.getType() != EntityType.WITHER && isNotAmbient(entity)) {
                ItemStack bottledMob = new BottledMobTool(plugin).createBottledMob(entity);
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                player.getInventory().setItemInMainHand(bottledMob);

                entity.setInvulnerable(true);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false, false));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 255, false, false, false));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255, false, false, false));

                // Add a custom name to the entity, so it doesn't despawn.
                if (entity.customName() == null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> entity.customName(Component.text("PersistentEntity")), 20L);
                }
                entity.setRemoveWhenFarAway(false);

                Location tpLocation = new Location(player.getWorld(), 0, player.getWorld().getMaxHeight(), 0);
                entity.teleport(tpLocation);

                player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5F, 0.5F);

                // Set metadata to prevent the player from immediately releasing the mob.
                player.setMetadata("justInteractedWithEntity", new FixedMetadataValue(plugin, true));
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.removeMetadata("justInteractedWithEntity", plugin), 10L);
            }
        }
    }

    /**
     * Checks if the item is a Mob Compactor.
     * @param item The item to check.
     * @return True if the item is a Mob Compactor, false otherwise.
     */
    private boolean isMobCompactor(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            Component displayName = item.getItemMeta().displayName();
            if (displayName != null) {
                return displayName.equals(Component.text("Mob Compactor").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            }
        }
        return false;
    }

    /**
     * Checks if the entity is not an ambient mob.
     * @param entity The entity to check.
     * @return True if the entity is not an ambient mob, false otherwise.
     */
    private boolean isNotAmbient(LivingEntity entity) {
        EntityType type = entity.getType();
        return (!(entity instanceof Ambient) && type != EntityType.SQUID && type != EntityType.BAT && type != EntityType.PHANTOM && type != EntityType.GLOW_SQUID && type != EntityType.SALMON && type != EntityType.COD && type != EntityType.TROPICAL_FISH && type != EntityType.PUFFERFISH && type != EntityType.TURTLE && type != EntityType.DOLPHIN);
    }
}


