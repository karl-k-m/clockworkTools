package com.github.karlkm.clockworktools.commands;

import com.github.karlkm.clockworktools.tools.BlinkPearl.BlinkPearlTool;
import com.github.karlkm.clockworktools.tools.TunnelersPickaxe.TunnelersPickaxeTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Ctool implements CommandExecutor, TabCompleter {

    JavaPlugin plugin;

    public Ctool(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return false;
        }

        if (!sender.hasPermission("clockworktools.ctool")) {
            sender.sendMessage("You do not have permission to use this command!");
            return false;
        }

        if (command.getLabel().equals("ctool")) {
            if (args.length == 0) {
                sender.sendMessage("You need to specify a tool!");
                return false;
            }

            if (args[0].equals("blinkpearl")) {
                if (!plugin.getConfig().getBoolean("settings.enableBlinkPearl")) {
                    sender.sendMessage("Blink Pearl is disabled! (see config.yml)");
                    return true;
                }
                Player p = (Player) sender;
                ItemStack blinkPearl = BlinkPearlTool.createBlinkPearl();
                p.getInventory().addItem(blinkPearl);
                return true;
            }

            if (args[0].equals("tunnelerspickaxe")) {
                if (!plugin.getConfig().getBoolean("settings.enableTunnelersPickaxe")) {
                    sender.sendMessage("Tunneler's Pickaxe is disabled! (see config.yml)");
                    return true;
                }
                Player p = (Player) sender;
                ItemStack tunnelersPickaxe = TunnelersPickaxeTool.createTunnelersPickaxe();
                p.getInventory().addItem(tunnelersPickaxe);
                return true;
            }

            sender.sendMessage("Invalid tool!");

            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = List.of("blinkpearl", "tunnelerspickaxe");

        if (args.length == 1) {
            return completions;
        }
        return null;
    }
}
