package com.trophonix.redstonespawners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;

public class RedstoneSpawners extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    getConfig().setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getResource("config.yml"))));
    getConfig().options().copyDefaults(true).copyHeader(true);
    saveConfig();
    getServer().getPluginManager().registerEvents(this, this);
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
            ChatColor.RED + "[RedstoneSpawners by Trophonix]",
            ChatColor.GRAY + "/rss reload " + ChatColor.WHITE + "Reload config file.",
            " "
    });
    return true;
  }

  @EventHandler
  public void onSpawnerActivate(SpawnerSpawnEvent event) {
    Block spawner = event.getSpawner().getBlock();
    boolean cancel = (spawner.isBlockPowered() != getConfig().getBoolean("power-enables", true));
    if (cancel) event.setCancelled(true);
    if (getConfig().getBoolean("debug", false)) {
      String loc = "["
              + spawner.getX() + ", " + spawner.getY() + ", " + spawner.getZ()
              + "]";
      Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RedstoneSpawners] "
              + ChatColor.WHITE + (cancel ? "Cancelled" : "Allowed") + " spawner " + loc);
    }
  }

}
