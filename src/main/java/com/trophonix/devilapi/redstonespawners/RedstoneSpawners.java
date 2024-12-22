package com.trophonix.devilapi.redstonespawners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RedstoneSpawners extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Ensures the config is saved if it doesn't exist
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info( "RedstoneSpawners enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload":
                case "rl":
                    reloadConfig();
                    sender.sendMessage(ChatColor.RED + "[RedstoneSpawners] " + ChatColor.WHITE + "Config reloaded!");
                    return true;
            }
        }
        sender.sendMessage(new String[]{
                " ",
                ChatColor.RED + "[RedstoneSpawners by Trophonix, updated by devilAPI]",
                ChatColor.WHITE + "/rss reload " + ChatColor.GRAY + "Reload config file.",
                " "
        });
        return true;
    }

    @EventHandler
    public void onSpawnerActivate(SpawnerSpawnEvent event) {
        Block spawner = event.getSpawner().getBlock();
        FileConfiguration config = getConfig();

        boolean powerEnables = config.getBoolean("power-enables", true);
        boolean isPowered = spawner.getBlockPower() > 0; // Check for any Redstone power source
        boolean cancel = (isPowered != powerEnables);

        if (cancel) {
            event.setCancelled(true);
        }

        if (config.getBoolean("debug", false)) {
            String loc = "[" + spawner.getX() + ", " + spawner.getY() + ", " + spawner.getZ() + "]";
            getLogger().info((cancel ? "Cancelled" : "Allowed") + " spawner at " + loc);
        }
    }
}
