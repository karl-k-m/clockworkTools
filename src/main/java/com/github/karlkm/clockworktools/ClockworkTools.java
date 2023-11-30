package com.github.karlkm.clockworktools;

import com.github.karlkm.clockworktools.commands.Ctool;
import com.github.karlkm.clockworktools.tools.BlinkPearl.BlinkPearlListener;
import com.github.karlkm.clockworktools.tools.BlinkPearl.BlinkPearlTool;
import com.github.karlkm.clockworktools.tools.MobCompactor.BottledMobListener;
import com.github.karlkm.clockworktools.tools.MobCompactor.MobCompactorListener;
import com.github.karlkm.clockworktools.tools.MobCompactor.MobCompactorTool;
import com.github.karlkm.clockworktools.tools.TunnelersPickaxe.TunnelersPickaxeListener;
import com.github.karlkm.clockworktools.tools.TunnelersPickaxe.TunnelersPickaxeTool;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ClockworkTools extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCustomRecipes();
        Objects.requireNonNull(this.getCommand("ctool")).setExecutor(new Ctool(this));
    }

    /**
     * Registers custom recipes for the tools if they are enabled in the config.
     */
    private void registerCustomRecipes() {

        if (getConfig().getBoolean("settings.enableTunnelersPickaxe")) {
            // Tunneler's Pickaxe
            getServer().getPluginManager().registerEvents(new TunnelersPickaxeListener(), this);
            ItemStack tunnelersPickaxe = TunnelersPickaxeTool.createTunnelersPickaxe();
            NamespacedKey key = new NamespacedKey(this, "tunnelers_pickaxe");
            ShapelessRecipe recipe = new ShapelessRecipe(key, tunnelersPickaxe);
            recipe.addIngredient(Material.DIAMOND_PICKAXE);
            recipe.addIngredient(Material.GOLDEN_APPLE);
            getServer().addRecipe(recipe);
        }

        if (getConfig().getBoolean("settings.enableBlinkPearl")) {
            // Blink Pearl
            getServer().getPluginManager().registerEvents(new BlinkPearlListener(this), this);
            ItemStack blinkPearl = BlinkPearlTool.createBlinkPearl();
            NamespacedKey key = new NamespacedKey(this, "blink_pearl");
            ShapelessRecipe recipe = new ShapelessRecipe(key, blinkPearl);
            recipe.addIngredient(Material.ENDER_PEARL);
            recipe.addIngredient(Material.GOLDEN_APPLE);
            getServer().addRecipe(recipe);
        }

        if (getConfig().getBoolean("settings.enableMobCompactor")) {
            // Mob Compactor and Bottled Mob
            getServer().getPluginManager().registerEvents(new MobCompactorListener(this), this);
            ItemStack mobCompactor = MobCompactorTool.createMobCompactor();
            NamespacedKey key = new NamespacedKey(this, "mob_compactor");
            ShapelessRecipe recipe = new ShapelessRecipe(key, mobCompactor);
            recipe.addIngredient(Material.GLASS_BOTTLE);
            recipe.addIngredient(Material.GOLDEN_APPLE);
            getServer().addRecipe(recipe);

            getServer().getPluginManager().registerEvents(new BottledMobListener(this), this);
        }

    }
}
