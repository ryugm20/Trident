package com.kyle.anywhereweather;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnywhereWeather extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChannelingListener(this), this);
        getServer().getPluginManager().registerEvents(new RiptideListener(this), this);
        getLogger().info("AnywhereWeather enabled — Channeling works in any weather, Riptide works anywhere.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AnywhereWeather disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§b[AnywhereWeather] §fv1.0.0 — Channeling triggers lightning in any weather, Riptide launches you anywhere.");
        return true;
    }
}
