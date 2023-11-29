package com.github.karlkm.clockworktools.tools.TunnelersPickaxe;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;

public class TunnelersPickaxeListener implements Listener {

    /**
     * Set of natural blocks breakable by a pickaxe.
     */
    private static final EnumSet<Material> PICKAXE_BREAKABLE_NATURAL_BLOCKS = EnumSet.of(Material.ANDESITE,
            Material.STONE, Material.DIORITE, Material.DEEPSLATE, Material.GRANITE, Material.TUFF,
            Material.CALCITE, Material.NETHERRACK, Material.END_STONE, Material.SANDSTONE);

    /**
     * Listens for a player breaking a block with a Tunnelers Pickaxe in their hand and breaks the surrounding blocks.
     * @param event The BlockBreakEvent
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (isTunnelersPickaxe(heldItem)) {


            if (PICKAXE_BREAKABLE_NATURAL_BLOCKS.contains(event.getBlock().getType())) {
                Location brokenBlockLocation = event.getBlock().getLocation();
                Block[] surroundingBlocks = getSurroundingBlocks(event.getBlock(), player);

                for (Block block : surroundingBlocks) {
                    if (PICKAXE_BREAKABLE_NATURAL_BLOCKS.contains(block.getType())) {

                        Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);
                        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, blockLocation, 10, block.getBlockData());

                        // Don't drop items if player is in creative.
                        if (player.getGameMode() == GameMode.CREATIVE) {
                            block.setType(Material.AIR);
                        }

                        else {
                            block.breakNaturally();
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if an ItemStack is a Tunnelers Pickaxe.
     * @param item The ItemStack to check
     * @return True if the ItemStack is a Tunnelers Pickaxe, false otherwise
     */
    private boolean isTunnelersPickaxe(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            Component displayName = item.getItemMeta().displayName();
            if (displayName != null) {
                return displayName.equals(Component.text("Tunneler's Pickaxe").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            }
        }
        return false;
    }

    /**
     * Gets the blocks surrounding the broken block with respect to the direction the player is facing.
     * @param brokenBlock The block that was broken
     * @param player The player that broke the block
     * @return An array of the blocks surrounding the broken block
     */
    private Block[] getSurroundingBlocks(Block brokenBlock, Player player) {
        Block[] surroundingBlocks = new Block[8];

        String playerDirection = getPrimaryDirection(player);

        // Get the blocks surrounding the broken block in the direction the player is facing
        if (playerDirection.equals("south") || playerDirection.equals("north")) {
            surroundingBlocks[0] = brokenBlock.getRelative(0, 1, 0);
            surroundingBlocks[1] = brokenBlock.getRelative(0, -1, 0);
            surroundingBlocks[2] = brokenBlock.getRelative(1, 0, 0);
            surroundingBlocks[3] = brokenBlock.getRelative(-1, 0, 0);
            surroundingBlocks[4] = brokenBlock.getRelative(-1, 1, 0);
            surroundingBlocks[5] = brokenBlock.getRelative(-1, -1, 0);
            surroundingBlocks[6] = brokenBlock.getRelative(1, 1, 0);
            surroundingBlocks[7] = brokenBlock.getRelative(1, -1, 0);
        }

        else if (playerDirection.equals("east") || playerDirection.equals("west")) {
            surroundingBlocks[0] = brokenBlock.getRelative(0, 1, 0);
            surroundingBlocks[1] = brokenBlock.getRelative(0, -1, 0);
            surroundingBlocks[2] = brokenBlock.getRelative(0, 0, 1);
            surroundingBlocks[3] = brokenBlock.getRelative(0, 0, -1);
            surroundingBlocks[4] = brokenBlock.getRelative(0, 1, -1);
            surroundingBlocks[5] = brokenBlock.getRelative(0, -1, -1);
            surroundingBlocks[6] = brokenBlock.getRelative(0, 1, 1);
            surroundingBlocks[7] = brokenBlock.getRelative(0, -1, 1);
        }

        else if (playerDirection.equals("up") || playerDirection.equals("down")) {
            surroundingBlocks[0] = brokenBlock.getRelative(1, 0, 0);
            surroundingBlocks[1] = brokenBlock.getRelative(-1, 0, 0);
            surroundingBlocks[2] = brokenBlock.getRelative(0, 0, 1);
            surroundingBlocks[3] = brokenBlock.getRelative(0, 0, -1);
            surroundingBlocks[4] = brokenBlock.getRelative(1, 0, -1);
            surroundingBlocks[5] = brokenBlock.getRelative(-1, 0, -1);
            surroundingBlocks[6] = brokenBlock.getRelative(1, 0, 1);
            surroundingBlocks[7] = brokenBlock.getRelative(-1, 0, 1);
        }

        return surroundingBlocks;
    }

    /**
     * Gets the primary direction the player is facing.
     * @param player The player
     * @return The primary direction the player is facing
     */
    public static String getPrimaryDirection(Player player) {
        // Get the yaw and pitch from the player's location
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();

        // Normalize the yaw
        yaw = yaw % 360;
        if (yaw < 0) {
            yaw += 360;
        }

        // Check if the player is looking predominantly up or down
        if (pitch <= -45) {
            return "up";
        } else if (pitch >= 45) {
            return "down";
        }

        // Otherwise, determine the cardinal direction
        if (yaw >= 315 || yaw < 45) {
            return "south";
        } else if (yaw < 135) {
            return "west";
        } else if (yaw < 225) {
            return "north";
        } else {
            return "east";
        }
    }
}
